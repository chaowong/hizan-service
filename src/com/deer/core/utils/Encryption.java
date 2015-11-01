package com.deer.core.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64OutputStream;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;

import com.deer.core.utils.trace.Trace;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * 类说明：密码的加密与解密
 * 
 */
public class Encryption {
	public final static String MD5 = "MD5";
	public final static String NONE = "NONE";
	public final static String SHA_256 = "SHA-256";
	public final static String SHA_512 = "SHA-512";
	public final static String SHA_384 = "SHA-384";
	//KeyGenerator 提供对称密钥生成器的功能，支持各种算法
	private  KeyGenerator keygen;
	//SecretKey 负责保存对称密钥
	private SecretKey deskey;
	//Cipher负责完成加密或解密工作
	private Cipher c;
	//该字节数组负责保存加密的结果
	private byte[] cipherByte;
	
	/** 
     * 密钥算法 
     * java6支持56位密钥，bouncycastle支持64位 
     * */ 
    public static final String KEY_ALGORITHM="AES";  
    /** 
     * 加密/解密算法/工作模式/填充方式 
     *  
     * JAVA6 支持PKCS5PADDING填充方式 
     * Bouncy castle支持PKCS7Padding填充方式 
     * */ 
    public static final String CIPHER_ALGORITHM="AES/ECB/PKCS7Padding"; 
	
	public Encryption() throws NoSuchAlgorithmException, NoSuchPaddingException{
		//实例化支持DES算法的密钥生成器(算法名称命名需按规定，否则抛出异常)
		keygen = KeyGenerator.getInstance("AES");
		//生成密钥
		deskey = keygen.generateKey();
		//生成Cipher对象,指定其支持的DES算法
		c = Cipher.getInstance("AES");
	}
	
	/** 
     *  
     * 生成密钥，java6只支持56位密钥，bouncycastle支持64位密钥 
     * @return byte[] 二进制密钥 
     * */ 
    public  byte[] initkey() throws Exception{  
           
//      //实例化密钥生成器  
//      Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
//      KeyGenerator kg=KeyGenerator.getInstance(KEY_ALGORITHM, "BC");  
//      //初始化密钥生成器，AES要求密钥长度为128位、192位、256位  
////        kg.init(256);  
//      kg.init(128); 
//      //生成密钥  
//      SecretKey secretKey=kg.generateKey();  
//      //获取二进制密钥编码形式  
//      return secretKey.getEncoded();  
        //为了便于测试，这里我把key写死了，如果大家需要自动生成，可用上面注释掉的代码
        return new byte[] { 0x08, 0x08, 0x04, 0x0b, 0x02, 0x0f, 0x0b, 0x0c,
                0x01, 0x03, 0x09, 0x07, 0x0c, 0x03, 0x07, 0x0a, 0x04, 0x0f,
                0x06, 0x0f, 0x0e, 0x09, 0x05, 0x01, 0x0a, 0x0a, 0x01, 0x09,
                0x06, 0x07, 0x09, 0x0d };
    }
	 /** 
     * 转换密钥 
     * @param key 二进制密钥 
     * @return Key 密钥 
     * */ 
    public  SecretKey toKey(byte[] key) throws Exception{  
        //实例化DES密钥  
        //生成密钥  
        SecretKey secretKey=new SecretKeySpec(key,KEY_ALGORITHM);  
        return secretKey;  
    }  
	/**
	 * aes对字符串加密
	 * 
	 * @param str
	 * @return
	 * @throws Exception 
	 */
	public byte[] Encrytor(String str) throws Exception {
		// 根据密钥，对Cipher对象进行初始化，ENCRYPT_MODE表示加密模式
		SecretKey key = toKey(initkey());
		byte[] src = str.getBytes();
		//生成Cipher对象,指定其支持的DES算法
		Cipher cipher=Cipher.getInstance(CIPHER_ALGORITHM); 
		cipher.init(Cipher.ENCRYPT_MODE, key);
		// 加密，结果保存进cipherByte
		cipherByte = c.doFinal(src);
		return cipherByte;
	}

	/**
	 * aes对字符串解密
	 * 
	 * @param buff
	 * @return
	 * @throws Exception 
	 */
	public byte[] Decryptor(byte[] buff) throws Exception {
		// 根据密钥，对Cipher对象进行初始化，DECRYPT_MODE表示加密模式
		SecretKey key = toKey(initkey());
		/** 
         * 实例化 
         * 使用 PKCS7PADDING 填充方式，按如下方式实现,就是调用bouncycastle组件实现 
         * Cipher.getInstance(CIPHER_ALGORITHM,"BC") 
         */ 
		Cipher cipher=Cipher.getInstance(CIPHER_ALGORITHM) ;  
        //初始化，设置为解密模式
        cipher.init(Cipher.DECRYPT_MODE, key);  
		cipherByte = c.doFinal(buff);
		return cipherByte;
	}

	/**
	 * 加密文件算法
	 * 
	 * @param filename
	 *            需要加密的文件名
	 * @param algorithm
	 *            加密算法名
	 */
	public static void digestFile(String filename, String algorithm) {
		byte[] b = new byte[1024 * 4];
		int len = 0;
		FileInputStream fis = null;
		FileOutputStream fos = null;
		try {
			MessageDigest md = MessageDigest.getInstance(algorithm);
			fis = new FileInputStream(filename);
			while ((len = fis.read(b)) != -1) {
				md.update(b, 0, len);
			}
			byte[] digest = md.digest();
			StringBuffer fileNameBuffer = new StringBuffer(128).append(filename).append(".").append(algorithm);
			fos = new FileOutputStream(fileNameBuffer.toString());
			OutputStream encodedStream = new Base64OutputStream(fos);
			encodedStream.write(digest);
			encodedStream.flush();
			encodedStream.close();
		} catch (Exception e) {
			System.out.println("Error computing Digest: " + e);
		} finally {
			try {
				if (fis != null)
					fis.close();
			} catch (Exception ignored) {
			}
			try {
				if (fos != null)
					fos.close();
			} catch (Exception ignored) {
			}
		}
	}

	/**
	 * 加密密码算法
	 * 
	 * @param pass
	 *            需要加密的原始密码
	 * @param algorithm
	 *            加密算法名称
	 * @return 加密后的密码
	 * @throws NoSuchAlgorithmException
	 *             当加密算法不可用时抛出此异常
	 */
	public static String digestString(String password, String alg) throws NoSuchAlgorithmException {
		String newPass;
		if (alg == null || MD5.equals(alg)) {
			newPass = DigestUtils.md5Hex(password);
		} else if (NONE.equals(alg)) {
			newPass = password;
		} else if (SHA_256.equals(alg)) {
			newPass = DigestUtils.sha256Hex(password);
		} else if (SHA_384.equals(alg)) {
			newPass = DigestUtils.sha384Hex(password);
		} else if (SHA_512.equals(alg)) {
			newPass = DigestUtils.sha512Hex(password);
		} else {
			newPass = DigestUtils.shaHex(password);
		}
		return newPass;
	}

	/**
	 * 加密密码算法，默认的加密算法是MD5
	 * 
	 * @param password
	 *   未加密的密码
	 * @return String 加密后的密码
	 */
	public static String digestPassword(String password) {
		try {
			if (password != null && !"".equals(password)) {
				return digestString(password, MD5);
			} else
				return null;
		} catch (NoSuchAlgorithmException nsae) {
			throw new RuntimeException("Security error: " + nsae);
		}
	}
	
	@Test
	public void ecode() throws IOException{
		String a = "MTkyODg3ODU1OTQxNzI5OTdlOQ==";
		String encode = new BASE64Encoder().encode(a.getBytes());
		byte[] decode = new BASE64Decoder().decodeBuffer("MTkyODg3ODU1OTQxNzI5OTdlOQ==");
		String decodeString = new String(decode);
		System.out.println("加密前："+a+"加密后："+encode+"解密后:"+decodeString+"time:"+System.currentTimeMillis());
	}

	/**
	 * 判断密码是不是相等，默认的加密算法是MD5
	 * 
	 * @param beforePwd
	 *            要判断的密码
	 * @param afterPwd
	 *            加密后的数据库密码
	 * @return Boolean true 密码相等
	 */
	public static boolean isPasswordEnable(String beforePwd, String afterPwd) {
		if (beforePwd != null && !"".equals(beforePwd)) {
			String password = digestPassword(beforePwd);
			return afterPwd.equals(password);
		} else
			return false;
	}

	/**
	 * base64加密
	 * @param content  加密内容
	 * @return
	 */
	public static String base64encode(String content){
		String encode = new BASE64Encoder().encode(content.getBytes());
		return encode;
	}
	
	/**
	 * base64解密
	 * @param content    解密内容
	 * @return
	 */
	public static String base64decode(String content){
		byte[] bytes;
		String decode=null;
		try {
			bytes = new BASE64Decoder().decodeBuffer(content);
			decode = new String(bytes);
		} catch (IOException e) {
			Trace.printStackTrace(e);
		}
		return decode;
	}
	public static void main(String[] args) throws Exception {
		System.out.println(Encryption.digestPassword("1427879169248"));
		System.out.println((Encryption.digestString("123456", Encryption.SHA_512)));
		//SHA512.digestFile("C:\\Users\\user\\Desktop\\PasswordEncode.java", SHA512.SHA_512);
		System.out.println(Encryption.isPasswordEnable("123456", Encryption.digestPassword("123456")));
		
		Encryption de1 = new Encryption();
		String msg ="1427879169248";
		byte[] encontent = de1.Encrytor(msg);
		byte[] decontent = de1.Decryptor(encontent);
		System.out.println("明文是:" + msg);
		String encode = new BASE64Encoder().encode(encontent);
		System.out.println(encode);
		byte[] decodeBuffer = new BASE64Decoder().decodeBuffer(encode);
		byte[] aesdecode = de1.Decryptor(decodeBuffer);
		System.out.println(new String(aesdecode));
		System.out.println("加密后:" + new String(encontent));
		System.out.println(base64decode(new String(encontent)));
		System.out.println("解密后:" + new String(decontent));
	}
}
