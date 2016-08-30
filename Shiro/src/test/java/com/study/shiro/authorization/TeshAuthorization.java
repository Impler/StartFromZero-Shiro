package com.study.shiro.authorization;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.permission.WildcardPermission;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 三种方式完成Authorization操作
 * 1 硬编码
 * 2 JDK 注解
 * 3 JSP 标签
 * @author impler
 *
 */
public class TeshAuthorization {

	private Logger log = LoggerFactory.getLogger(getClass());
	
	@Before
	public void before(){
		IniSecurityManagerFactory factory = new IniSecurityManagerFactory("classpath:iniconfig/shiro-authorization.ini");
		SecurityManager sm = factory.getInstance();
		SecurityUtils.setSecurityManager(sm);
	}
	
	/**
	 * *************************************
	 * 硬编码
	 * *************************************
	 */
	@Test
	public void testRoleByProgram() {
		Subject user = SecurityUtils.getSubject();
		// 一般用户
		AuthenticationToken userToken = new UsernamePasswordToken("lisi", "abcd");
		user.login(userToken);
		// 判断是否拥有adminRole角色
		Assert.assertFalse(user.hasRole("adminRole"));
		// 判断是否拥有role1角色
		Assert.assertTrue(user.hasRole("role1"));
		
		List<String> roles = new ArrayList<String>();
		
		roles.add("role1");
		roles.add("role2");
		
		// 判断是否拥有所有角色，用户需拥有角色集合的超集
		Assert.assertTrue(user.hasAllRoles(roles));
		
		roles.add("adminRole");
		// 用户需拥有角色集合的子集，该方法返回boolean数组，对应是否拥有各个位置上的角色
		boolean[] rs = user.hasRoles(roles);
		log.debug("role list:{}", roles);
		log.debug("user.hasRoles:{}", rs);
		
		try {
			// checkXXX相较于hasXXX唯一的区别在于，当不包含待检查的角色时，将会抛出AuthorizationException异常
			user.checkRole("adminRole");
		} catch (AuthorizationException e) {
			log.error(e.getMessage());
		}
	}
	
	@Test
	public void testPermissionByProgram(){
		Subject user = SecurityUtils.getSubject();
		// admin用户
		AuthenticationToken userToken = new UsernamePasswordToken("admin", "admin");
		user.login(userToken);
		// admin用户拥有所有操作权限，包括ini中没配置的
		Assert.assertTrue(user.isPermitted("xxx"));
		user.logout();
		
		// 换成普通用户
		userToken = new UsernamePasswordToken("zhangsan", "abc");
		user.login(userToken);
		
		// 权限字符串
		Assert.assertTrue(user.isPermitted("user:update"));
		// 权限对象
		WildcardPermission pm = new WildcardPermission("user:delete");
		Assert.assertFalse(user.isPermitted(pm));
	}
	
	
	/**
	 * *************************************
	 * JDK 注解，TODO 需要AOP支持
	 * *************************************
	 */

}
