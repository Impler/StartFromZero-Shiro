package com.study.shiro.helloworld;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.junit.Test;

public class TestHelloWorld {

	@Test
	public void testValid() {
		
		// 此处为org.apache.shiro.mgt.SecurityManager，而非java.lang.SecurityManager
		Factory<org.apache.shiro.mgt.SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro-helloworld.ini");
		
		SecurityManager sm = factory.getInstance();
		
		SecurityUtils.setSecurityManager(sm);
		
		Subject user = SecurityUtils.getSubject();
		
		UsernamePasswordToken token = new UsernamePasswordToken("zhangsan", "abc");
		
		user.login(token);
		
	}

	@Test(expected=AuthenticationException.class)
	public void testInValid() {
		
		// 此处为org.apache.shiro.mgt.SecurityManager，而非java.lang.SecurityManager
		Factory<org.apache.shiro.mgt.SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro-helloworld.ini");
		
		SecurityManager sm = factory.getInstance();
		
		SecurityUtils.setSecurityManager(sm);
		
		Subject user = SecurityUtils.getSubject();
		
		UsernamePasswordToken token = new UsernamePasswordToken("lisi", "abc");
		
		user.login(token);
		
	}
}
