# StartFromZero-Shiro
- Authentication：完成身份验证
- Authorization：完成权限认证

## Authentication
Authentication是一个验证身份的完整过程，包括：  
- 收集用户验证信息 --> `AuthenticationToken`
- 进入Shiro身份验证 --> `Authenticator`
- 执行真正的验证工作 --> `Realm`
- 验证失败，直接抛出`AuthenticationException`;验证通过，返回用户信息 --> `AuthenticationInfo`

### AuthenticationToken接口
*`AuthenticationToken`* 用户登录凭证，包含两个方法:  
```java
// 首要的用户凭证，一般能够唯一标识一个用户，例如用户名、工号等
Object getPrincipal();
// 密码等凭证
Object getCredentials();
```
**1 <extends>** *`HostAuthenticationToken`*获取用户主机信息(IP)，同`ServletRequest.getRemoteHost()`  
```java
String getHost();
```
**2 <extends>** *`RememberMeAuthenticationToken`*是否跨session保存用户标识信息
```java
boolean isRememberMe();
```
**1.1/2.1 <implements>** `UsernamePasswordToken`实现了上面两者，提供了一般基于用户名/密码形式的验证机制  

### AuthenticationInfo接口
*`AuthenticationInfo`*保存通过验证流程的用户的数据，而`AuthenticationToken`保存的是验证请求中、未经验证的数据  
```java
// 保存用户标识性信息，可能不仅仅是用户名，可能邮箱、手机号等也可以唯一标识一个用户
PrincipalCollection getPrincipals();
// 密码等凭证
Object getCredentials();
```
**1 <extends>** *`SaltedAuthenticationInfo`* 为了加强密码凭证的安全性，提供salt接口
```java
ByteSource getCredentialsSalt();
```
**2 <extends>** *`MergableAuthenticationInfo`* 在多Realm条件下(下面会解释)，提供合并`AuthenticationInfo`的接口
```java
void merge(AuthenticationInfo info);
```

**1.1/2.1 <implements>** `SimpleAuthenticationInfo` 实现了上面两者  

**3 <implements>** `SimpleAccount` 融合了`AuthenticationInfo`和`AuthorizationInfo`, 静态代理了`SimpleAuthenticationInfo`的功能，后面详解
```java
public class SimpleAccount implements Account, MergableAuthenticationInfo, SaltedAuthenticationInfo, Serializable {
    private SimpleAuthenticationInfo authcInfo;
	// 静态代理方法
	public PrincipalCollection getPrincipals() {
        return authcInfo.getPrincipals();
    }
	public Object getCredentials() {
        return authcInfo.getCredentials();
    }
	public ByteSource getCredentialsSalt() {
        return this.authcInfo.getCredentialsSalt();
    }
	public void merge(AuthenticationInfo info) {
        authcInfo.merge(info);
        // Merge SimpleAccount specific info
        if (info instanceof SimpleAccount) {
            SimpleAccount otherAccount = (SimpleAccount) info;
            if (otherAccount.isLocked()) {
                setLocked(true);
            }
            if (otherAccount.isCredentialsExpired()) {
                setCredentialsExpired(true);
            }
        }
    }
}
```

### Realm接口
*`Realm`*是一个系统安全组件，可以访问特定系统的安全部件，包括用户、角色、权限等。*`Realm`*可以在Authentication和Ahthorization流程中工作。  
```java
// 全局唯一的名称
String getName();
//验证前先判断是否支持该AuthenticationToken，否则不进行验证
boolean supports(AuthenticationToken token);
// 执行真正的验证工作
AuthenticationInfo getAuthenticationInfo(AuthenticationToken token) throws AuthenticationException;
```
**1 <implements>** `CachingRealm` 基本的Realm抽象实现，提供cache功能  
**1.1 <extends>** `AuthenticatingRealm` 在登录流程中提供用户身份验证功能，一般登录实现继承该抽象类即可  
```java
public final AuthenticationInfo getAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
    AuthenticationInfo info = getCachedAuthenticationInfo(token);
    if (info == null) {
        // 抽象出真正实现验证工作的接口，需子类实现
        info = doGetAuthenticationInfo(token);
        if (token != null && info != null) {
            cacheAuthenticationInfoIfPossible(token, info);
        }
    }
    if (info != null) {
        assertCredentialsMatch(token, info);
    }
    return info;
}
```
**1.1.1 <extends>** `AuthorizingRealm`  
**1.1.1.1 <extends>** `SimpleAccountRealm`  
**1.1.1.1.1 <extends>** `IniRealm`  

### Authenticator接口
*`Authenticator`*接口用于验证用户身份，是Shiro API的入口。
```java
public AuthenticationInfo authenticate(AuthenticationToken authenticationToken) throws AuthenticationException;
```
**1 <implements>** `AbstractAuthenticator` 所有Authenticator的抽象父类，细化了身份验证的流程，并提供了登录成功/失败以及退出的事件通知
```java
// 通知监听器
private Collection<AuthenticationListener> listeners;
// 实现接口方法
public final AuthenticationInfo authenticate(AuthenticationToken token) throws AuthenticationException {
    try {
		// 抽象出实际验证身份操作的方法
        info = doAuthenticate(token);
    } catch (Throwable t) {
		// 当验证失败时，调用监听器通知
        notifyFailure(token, ae);
        throw ae;
    }
	// 验证成功时，调用监听器，通知
    notifySuccess(token, info);
    return info;
}

// 新增退出通知
public void onLogout(PrincipalCollection principals) {
    notifyLogout(principals);
}
```
**1.1 <extends>** *`ModularRealmAuthenticator`* 调用配置的Realm(s)完成验证操作
```java
// 实现父类抽象方法
protected AuthenticationInfo doAuthenticate(AuthenticationToken authenticationToken) throws AuthenticationException {
    Collection<Realm> realms = getRealms();
    if (realms.size() == 1) {
		// 只配置了一个Realm
        return doSingleRealmAuthentication(realms.iterator().next(), authenticationToken);
    } else {
		// 配置了多个Realm
        return doMultiRealmAuthentication(realms, authenticationToken);
    }
}
// 多Realm的操作
protected AuthenticationInfo doMultiRealmAuthentication(Collection<Realm> realms, AuthenticationToken token) {
	// 验证策略，AllSuccessfulStrategy、AtLeastOneSuccessfulStrategy、FirstSuccessfulStrategy可供使用
	// 不同的策略，最终表现在返回的AuthenticationInfo对象中，是合并上一步的结果还是代替等等
	AuthenticationStrategy strategy = getAuthenticationStrategy();
	AuthenticationInfo aggregate = strategy.beforeAllAttempts(realms, token)
	//　遍历Realm，执行其
	for (Realm realm : realms) {
	    aggregate = strategy.beforeAttempt(realm, token, aggregate);
	    if (realm.supports(token)) {
	        AuthenticationInfo info = null;
	        Throwable t = null;
	        try {
				// 验证操作
	            info = realm.getAuthenticationInfo(token);
	        } catch (Throwable throwable) {
	            
	        }
	        aggregate = strategy.afterAttempt(realm, token, info, aggregate, t);
	    } else {
	        
	    }
	}
	aggregate = strategy.afterAllAttempts(token, aggregate);
	return aggregate;
}
```

## Authorization
