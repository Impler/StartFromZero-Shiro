# StartFromZero-Shiro
- Authentication：完成身份验证
- Authorization：完成权限认证

##Authentication
###Authenticator层级关系
*`Authenticator`*接口用于验证用户身份，是Shiro API的入口。
```java
public AuthenticationInfo authenticate(AuthenticationToken authenticationToken) throws AuthenticationException;
```
**==> implements <--** `AbstractAuthenticator` 所有Authenticator的抽象父类，细化了身份验证的流程，并提供了登录成功/失败以及退出的事件通知
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
**==> ==> extends<--** `ModularRealmAuthenticator` 调用配置的Realm(s)完成验证操作
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
