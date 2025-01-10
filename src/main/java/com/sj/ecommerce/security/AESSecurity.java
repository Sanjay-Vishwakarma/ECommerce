package com.sj.ecommerce.security;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class AESSecurity {
	private static final Logger LOGGER = LoggerFactory.getLogger(AESSecurity.class);
	private static final String AESCIPHER = "AES/ECB/PKCS5Padding";
	private static final String AES = "AES";
	private static final String KEY="9876543219876543";

	public static String encrypt(String input) {
		byte[] crypted = null;
		try {
			SecretKeySpec skey = new SecretKeySpec(KEY.getBytes(), AES);
			System.out.println(skey);
			Cipher cipher = Cipher.getInstance(AESCIPHER);
			cipher.init(Cipher.ENCRYPT_MODE, skey);
			crypted = cipher.doFinal(input.getBytes());
		} catch (Exception e) {
			LOGGER.error("AESSecurity_encrypt",e);
		}
		return new String(Base64.encodeBase64(crypted));
	}

	public static String decrypt(String input) {
		byte[] output = null;
		try {
			SecretKeySpec skey = new SecretKeySpec(KEY.getBytes(), AES);
			Cipher cipher = Cipher.getInstance(AESCIPHER);
			cipher.init(Cipher.DECRYPT_MODE, skey);
			output = cipher.doFinal(Base64.decodeBase64(input));
		} catch (Exception e) {
			LOGGER.error("AESSecurity_decrypt",e);
		}
		return new String(output);
	}

	public static String encryptWithKey(String input,String key) {
		byte[] crypted = null;
		try {
			SecretKeySpec skey = new SecretKeySpec(key.getBytes(), AES);
			Cipher cipher = Cipher.getInstance(AESCIPHER);
			cipher.init(Cipher.ENCRYPT_MODE, skey);
			crypted = cipher.doFinal(input.getBytes());
		} catch (Exception e) {
			LOGGER.error("AESSecurity_encrypt",e);
		}
		return new String(Base64.encodeBase64(crypted));
	}

	public static String decryptWithKey(String input,String key) {// Crypt
		byte[] output = null;
		try {
			SecretKeySpec skey = new SecretKeySpec(key.getBytes(), AES);
			Cipher cipher = Cipher.getInstance(AESCIPHER);
			cipher.init(Cipher.DECRYPT_MODE, skey);
			output = cipher.doFinal(Base64.decodeBase64(input));
		} catch (Exception e) {
			LOGGER.error("AESSecurity_decrypt",e);
		}
		return new String(output);//6
	}

}