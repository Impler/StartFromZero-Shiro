package com.study.shiro.realm;

import junit.framework.Assert;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.junit.Test;

/**
 * A Realm is essentially a security-specific DAO
 * Realm 可以处理Authentication过程和Authorization过程。分别对应Realm接口和Authorizer接口,
 * AuthorizingRealm抽象类结合了这两者，一般自定义Realm继承AuthorizingRealm即可
 * @author impler
 */
public class TestRealm {

	@Test
	public void testRightAuth() {
		Factory<org.apache.shiro.mgt.SecurityManager> factory = new IniSecurityManagerFactory("classpath:iniconfig/shiro-realm.ini");
		
		SecurityManager sm = factory.getInstance();
	
		SecurityUtils.setSecurityManager(sm);
		
		Subject user = SecurityUtils.getSubject();
		
		UsernamePasswordToken token = new UsernamePasswordToken("zhangsan", "abcd");
		
		user.login(token );
		
		Assert.assertTrue(user.isAuthenticated());
		
		user.logout();
	}
	
	@Test(expected=AuthenticationException.class)
	public void testInvalidAuth() {
		Factory<org.apache.shiro.mgt.SecurityManager> factory = new IniSecurityManagerFactory("classpath:iniconfig/shiro-realm.ini");
		
		SecurityManager sm = factory.getInstance();
		
		SecurityUtils.setSecurityManager(sm);
		
		Subject user = SecurityUtils.getSubject();
		
		UsernamePasswordToken token = new UsernamePasswordToken("lisi", "abcd");
		
		user.login(token );
		
		Assert.assertTrue(user.isAuthenticated());
		
		user.logout();
	}
	
}
