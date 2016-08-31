package com.study.shiro.authentication.credentialsMatcher;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.junit.Before;
import org.junit.Test;

public class TestCredentialsMatcher {

	@Before
	public void before() {
		Factory<SecurityManager> factory = new IniSecurityManagerFactory(
				"classpath:iniconfig/shiro-authentication-credentialsMatcher.ini");
		SecurityManager sm = factory.getInstance();
		SecurityUtils.setSecurityManager(sm);
	}

	@Test
	public void testCredentialsMatcher() {
		// 配置了Realm的CredentialsMatcher为MD5
		Subject user = SecurityUtils.getSubject();
		AuthenticationToken token = new UsernamePasswordToken("zhangsan", "abc");
		user.login(token);
	}

	/**
	 * 使用salt
	 */
	@Test
	public void testCredentialsMatcherWithSalt() {
		// 配置了Realm的CredentialsMatcher为MD5
		Subject user = SecurityUtils.getSubject();
		AuthenticationToken token = new UsernamePasswordToken("lisi", "abc");
		user.login(token);
	}

}
