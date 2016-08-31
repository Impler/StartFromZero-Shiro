package com.study.shiro.realm;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAccount;
import org.apache.shiro.realm.text.IniRealm;
import org.apache.shiro.util.ByteSource;

/**
 * 
 * @author impler
 *
 */
public class SaltRealm extends IniRealm{

	
	public SaltRealm() {
		super("classpath:/shiro-authentication-credentialsMatcher.ini");
	}
	
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken token) throws AuthenticationException {
		
		// SimpleAccount 实现了，SaltedAuthenticationInfo，可以设置salt信息
		SimpleAccount info = (SimpleAccount) super.doGetAuthenticationInfo(token);
		
		//正常情况下，用户的salt应该存放在数据库中动态获取，这里为了测试方便，仅针对用户lisi使用salt功能
		if("lisi".equals((String)info.getPrincipals().getPrimaryPrincipal())){
			info.setCredentialsSalt(ByteSource.Util.bytes("123"));
		}
		
		return info;
	}

	
	
}