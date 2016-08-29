package com.study.shiro.authentication;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.junit.Test;

/**
 * Authentication是验证用户身份的过程
 * 用户需要提供principals（用户名，唯一） 和 credentials（密码）给Shiro
 * 验证的过程可分为：
 * 1 收集principals和credentials
 * 2 提交principals和credentials
 * 3 验证通过，放行；否则，禁行
 * 
 * org.apache.shiro.authc.AuthenticationToken 是代表用户principals和credentials的接口。其实现类 UsernamePasswordToken用于一般基于用户名/密码的验证方式
 * 
 * @author impler
 *
 */
public class TestAuthentication {

	@Test()
	public void test() {
		
		Factory<org.apache.shiro.mgt.SecurityManager> factory = new IniSecurityManagerFactory("classpath:iniconfig/shiro-authentication.ini");
		
		SecurityManager sm = factory.getInstance();
		
		SecurityUtils.setSecurityManager(sm);
		
		Subject user = SecurityUtils.getSubject();
		
		// 1  收集用户principals/credentials，此过程一般从Request中接收用户请求或其他方式传入，此处hardcode

//		// 未知账户
//		String username = "user1";
//		String password = "1234";
		
		// 合法用户
		String username = "zhangsan";
		String password = "abc";
		UsernamePasswordToken token = new UsernamePasswordToken(username, password);
		// 记住我，remembered状态的用户来自于之前交互过的session，仅仅保留了subject的principals，并没有通过authentication验证，不能访问用户其他敏感信息，除非重新认证
		token.setRememberMe(true);
		
		// 2 提交验证信息, 可能抛出AuthenticationException异常
		try {
		    user.login(token);
		} catch ( UnknownAccountException uae ) {
			// 未知账户异常，username不存在
			throw uae;
		} catch ( IncorrectCredentialsException ice ) {
			// credentials不正确，密码不对
			throw ice;
		} catch ( LockedAccountException lae ) { 
			// 账户被锁异常
			throw lae;
		} catch ( ExcessiveAttemptsException eae ) {
			// 验证尝试次数过多异常
			throw eae;
		} catch ( AuthenticationException ae ) {
		    // 未明确的其他AuthenticationException异常
			// 也可以自定义AuthenticationException异常
			throw ae;
		}
		
		// 3 验证通过，继续其他正常操作
		// doOtherBusiness();

	}
	
}
