package com.study.shiro.realm;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.realm.Realm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 验证用户名长度
 * @author impler
 *
 */
public class MyRealm1 implements Realm {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	public String getName() {
		
		return "myrealm1";
	}

	@Override
	public boolean supports(AuthenticationToken token) {
		return token instanceof UsernamePasswordToken;
	}

	@Override
	public AuthenticationInfo getAuthenticationInfo(AuthenticationToken token)
			throws AuthenticationException {
		logger.debug("******" + this.getName() + " 验证开始");
		UsernamePasswordToken tk = (UsernamePasswordToken) token;
		String username = tk.getUsername();
		
		if(username.length() <= 4){
			logger.debug("******用户名长度过短");
			throw new AuthenticationException("用户名长度过短(>4)");
		}
		logger.debug("******" + this.getName() + " 验证通过");
		return new SimpleAuthenticationInfo(tk.getUsername(), tk.getCredentials(), getName());
	}

}
