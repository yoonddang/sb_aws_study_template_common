package com.template.common.util;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

public class Aes128Util {
	private static final Logger logger = LoggerFactory.getLogger(Aes128Util.class);

	private String ips;
	private Key keySpec;

	public Aes128Util(String key) {
		try {
			byte[] keyBytes = new byte[16];
			byte[] b = key.getBytes("UTF-8");
			System.arraycopy(b, 0, keyBytes, 0, keyBytes.length);
			SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
			this.ips = key.substring(0, 16);
			this.keySpec = keySpec;
		} catch (UnsupportedEncodingException e) {
			logger.error("ERROR Aes128Util", e);
		}
	}

	public String encrypt(String str) {
		Cipher cipher;
		try {
			cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, keySpec,
					new IvParameterSpec(ips.getBytes()));

			byte[] encrypted = cipher.doFinal(str.getBytes("UTF-8"));
			String Str = new String(Base64.encodeBase64(encrypted));

			return Str;
		} catch (NoSuchAlgorithmException | NoSuchPaddingException
				| InvalidKeyException | InvalidAlgorithmParameterException
				| IllegalBlockSizeException | BadPaddingException
				| UnsupportedEncodingException e) {
			logger.error("ERROR encrypt in Aes128Util", e);
		}
		return null;
	}

	public String decrypt(String str) {
		try {
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, keySpec,
					new IvParameterSpec(ips.getBytes("UTF-8")));

			byte[] byteStr = Base64.decodeBase64(str.getBytes());
			String Str = new String(cipher.doFinal(byteStr), "UTF-8");

			return Str;
		} catch (NoSuchAlgorithmException | NoSuchPaddingException
				| InvalidKeyException | InvalidAlgorithmParameterException
				| IllegalBlockSizeException | BadPaddingException
				| UnsupportedEncodingException e) {
			logger.error("ERROR decrypt in Aes128Util", e);
		}
		return null;
	}
}
