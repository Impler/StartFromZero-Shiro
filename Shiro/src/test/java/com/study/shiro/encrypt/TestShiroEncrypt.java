package com.study.shiro.encrypt;

import java.security.Key;

import junit.framework.Assert;

import org.apache.shiro.codec.Base64;
import org.apache.shiro.codec.Hex;
import org.apache.shiro.crypto.AesCipherService;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.DefaultHashService;
import org.apache.shiro.crypto.hash.HashRequest;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.util.ByteSource;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TestShiroEncrypt {

	private Logger logger = LoggerFactory.getLogger(TestShiroEncrypt.class);
	/**
	 * -------------------------------------
	 * Shiro提供了base64和16进制字符串编码/解码的API支持
	 * -------------------------------------
	 */
	
	/**
	 * base64编码
	 */
	@Test
	public void testBase64() {
		
		String msg = "hello world";
		logger.debug("msg:{}", msg);
		
		// 编码
		String encryStr = Base64.encodeToString(msg.getBytes());

		logger.debug("msg base64 encode:{}", encryStr);
		
		// 解码
		String decodeStr = Base64.decodeToString(encryStr.getBytes());
		
		logger.debug("msg base64 decode:{}", decodeStr);
		
		Assert.assertEquals(msg, decodeStr);
	}

	/**
	 * 16进制编码
	 */
	@Test
	public void testHex(){
		String msg = "hello world";
		logger.debug("msg:{}", msg);
		
		// 编码
		String encryStr = new String(Hex.encode(msg.getBytes()));

		logger.debug("msg hex encode:[{}]", encryStr);
		
		// 解码
		String decodeStr = new String(Hex.decode(encryStr));
		
		logger.debug("msg hex decode:[{}]", decodeStr);
		
		Assert.assertEquals(msg, decodeStr);
	}
	
	/**
	 * ----------------------------------
	 * 散列加密（不可逆）
	 * MD5, SHA等
	 * ----------------------------------
	 */
	
	/**
	 * Md5Hash, Sha256Hash extends SimpleHash
	 * SimpleHash内部实现是调用Java的MessageDigest
	 */
	@Test
	public void testMD5(){
		String msg = "hello world";
		logger.debug("msg:{}", msg);
		String encryStr = new Md5Hash(msg).toString();
		
		logger.debug("msg MD5 decode:[{}]", encryStr);
		
		String salt = "abc";
		
		String encryStrWithSalt = new Md5Hash(msg, salt).toString();
		
		logger.debug("msg MD5 with salt [{}]: [{}]", salt, encryStrWithSalt);
	}
	
	@Test
	public void testHashService(){
		DefaultHashService service = new DefaultHashService();
		// 是否生成公盐，默认false
		service.setGeneratePublicSalt(true);
		// 默认SHA-512
		service.setHashAlgorithmName("MD5");
		// 设置私盐
		service.setPrivateSalt(ByteSource.Util.bytes("abc"));
		// 默认SecureRandomNumberGenerator
		service.setRandomNumberGenerator(new SecureRandomNumberGenerator());
		
		HashRequest request = new HashRequest.Builder()
			.setAlgorithmName("SHA-256")
			.setSource("hello world")
			.setSalt("abc")
			.build();
		
		String hex = service.computeHash(request).toString();
		
		System.out.println(hex);
	}
	
	
	/**
	 * ---------------------------
	 * 对称加密
	 * 包括AES、Blowfish等
	 * ---------------------------
	 */
	@Test
	public void testSymmetrical(){
	    AesCipherService aes = new AesCipherService();  
	    aes.setKeySize(128); //设置key长度  
	   
	    String msg = "hello";  
	    logger.debug("msg:{}", msg);
	    //生成秘钥  
	    Key key = aes.generateNewKey();  
	    logger.debug("key:{}", key.getEncoded());
	    
	    //加密  
	    String encrptStr = aes.encrypt(msg.getBytes(), key.getEncoded()).toHex();  
	    logger.debug("msg aes encode:[{}]", encrptStr);
	    //解密  
	    String decodeStr =  new String(aes.decrypt(Hex.decode(encrptStr), key.getEncoded()).getBytes());  
	    logger.debug("msg aes decode:[{}]", decodeStr);
	    
	    Assert.assertEquals(msg, decodeStr);   
	}
}
