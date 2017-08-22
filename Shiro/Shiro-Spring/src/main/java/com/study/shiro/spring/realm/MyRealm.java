package com.study.shiro.spring.realm;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

public class MyRealm extends AuthorizingRealm{

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection arg0) {
		
		return null;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken token) throws AuthenticationException {
		
		// 根据token中的用户名查询用户的密码，放到info中去，密码匹配交由shiro处理
		Object credentials = ""; // e.g queryPassowrd();
		byte[] salts = new byte[]{}; // e.g querySalts();
		return new SimpleAuthenticationInfo(token.getPrincipal(), credentials, ByteSource.Util.bytes(salts), super.getName());
	}

}
