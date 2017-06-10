/*
 * @(#)EncryptionDecryptionTest.java
 *
 * Copyright (c) 2008-2009 LH Systems AS. All Rights Reserved.
 *
 * $LastChangedDate: 2012-04-11 12:35:13 +0530 (Tue, 11 Apr 2012) $
 * $LastChangedRevision:  $
 * $LastChangedBy:  $
 *
 * History: 11.04.2012 (Manoharan Thambi) Creation
 */
package com.websystique.springboot.model;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * Used to encrypt and decrypt the string
 * </p>
 * 
 * @author Manoharan Thambi
 * @version 1.0
 * @since 11.04.2012
 */
public class EncryptionDecryptionUtil {

	/**
	 * encoding format
	 */
	private static final String UNICODE_FORMAT = "UTF8";

	/**
	 * Encryption algorithm type
	 */
	public static final String DESEDE_ENCRYPTION_SCHEME = "DESede";

	/**
	 * Cipher reference
	 */
	private Cipher cipher;

	/**
	 * myEncryptionKey class level variables
	 */
	private final String myEncryptionKey;

	/**
	 * SecretKey reference
	 */
	private SecretKey key;

	/**
	 * APPLOG, Instance of log4j logger, is used for logging
	 */
	private static final Logger APPLOG = LoggerFactory.getLogger(EncryptionDecryptionUtil.class.getName());

	/**
	 * To load the properties for encryption and decryption.
	 */
	public EncryptionDecryptionUtil() {
		myEncryptionKey = "ThisIsSecretEncryptionKey";
		try {
			byte[] keyAsBytes = myEncryptionKey.getBytes(EncryptionDecryptionUtil.UNICODE_FORMAT);
			KeySpec myKeySpec = new DESedeKeySpec(keyAsBytes);
			SecretKeyFactory mySecretKeyFactory = SecretKeyFactory
					.getInstance(EncryptionDecryptionUtil.DESEDE_ENCRYPTION_SCHEME);
			cipher = Cipher.getInstance(EncryptionDecryptionUtil.DESEDE_ENCRYPTION_SCHEME);
			key = mySecretKeyFactory.generateSecret(myKeySpec);
		} catch (UnsupportedEncodingException uee) {
			cipher = null;
			key = null;
			APPLOG.error("UnsupportedEncodingException in encrypting string", uee);
		} catch (InvalidKeyException ike) {
			cipher = null;
			key = null;
			APPLOG.error("InvalidKeyException in encrypting string", ike);
		} catch (NoSuchAlgorithmException nsae) {
			cipher = null;
			key = null;
			APPLOG.error("NoSuchAlgorithmException in encrypting string", nsae);
		} catch (NoSuchPaddingException nspe) {
			cipher = null;
			key = null;
			APPLOG.error("NoSuchPaddingException in encrypting string", nspe);
		} catch (InvalidKeySpecException ikse) {
			cipher = null;
			key = null;
			APPLOG.error("InvalidKeySpecException in encrypting string", ikse);
		}
	}

	/**
	 * Method To Encrypt The String
	 * 
	 * @param unencryptedString
	 *            input string for encryption
	 * @return String
	 */
	public String encrypt(final String unencryptedString) {
		String encryptedString = null;

		try {
			cipher.init(Cipher.ENCRYPT_MODE, key);
			final byte[] plainText = unencryptedString.getBytes(EncryptionDecryptionUtil.UNICODE_FORMAT);
			final byte[] encryptedText = cipher.doFinal(plainText);
			final Base64 base64encoder = new Base64();
			encryptedString = base64encoder.encodeAsString(encryptedText);
		} catch (InvalidKeyException ike) {
			APPLOG.error("InvalidKeyException in encrypting string", ike);
		} catch (UnsupportedEncodingException uee) {
			APPLOG.error("UnsupportedEncodingException in encrypting string", uee);
		} catch (IllegalBlockSizeException ibse) {
			APPLOG.error("IllegalBlockSizeException in encrypting string", ibse);
		} catch (BadPaddingException bpe) {
			APPLOG.error("BadPaddingException in encrypting string", bpe);
		}

		return encryptedString;
	}

	/**
	 * Method To Decrypt An Ecrypted String
	 * 
	 * @param encryptedString
	 *            input encrypted string
	 * @return String
	 */
	public String decrypt(final String encryptedString) {
		String decryptedText = null;

		try {
			cipher.init(Cipher.DECRYPT_MODE, key);

			final Base64 base64decoder = new Base64();
			final byte[] encryptedText = base64decoder.decode(encryptedString);
			final byte[] plainText = cipher.doFinal(encryptedText);
			decryptedText = EncryptionDecryptionUtil.bytes2String(plainText);
		} catch (InvalidKeyException ike) {
			APPLOG.error("InvalidKeyException in encrypting string", ike);
		} catch (IllegalBlockSizeException ibse) {
			APPLOG.error("IllegalBlockSizeException in encrypting string", ibse);
		} catch (BadPaddingException bpe) {
			APPLOG.error("BadPaddingException in encrypting string", bpe);
		}
		return decryptedText;
	}

	/**
	 * Returns String From An Array Of Bytes
	 * 
	 * @param bytes
	 *            byte array
	 * @return String
	 */
	private static String bytes2String(final byte[] bytes) {
		final StringBuffer stringBuffer = new StringBuffer();
		for (int i = 0; i < bytes.length; i++) {
			stringBuffer.append((char) bytes[i]);
		}
		return stringBuffer.toString();
	}

	/*public static void main(String args[]) {
		System.out.println(new EncryptionDecryptionUtil().encrypt("welcome"));
	}*/
}
