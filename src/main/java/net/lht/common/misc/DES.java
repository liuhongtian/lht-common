package net.lht.common.misc;

import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.InvalidKeyException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

/**
 * 通过DES加密解密实现一个String字符串的加密和解密.
 * 
 * @author liuhongtian
 * 
 */
public class DES {

	/**
	 * 自动生成一个密钥
	 * 
	 * @return 以十六进制字符串的形式表示的密钥，长度为16位
	 * @throws NoSuchAlgorithmException
	 */
	public static String genKeyHexString() throws NoSuchAlgorithmException {

		SecureRandom sr = new SecureRandom();
		KeyGenerator kg = KeyGenerator.getInstance("DES");
		kg.init(sr);
		SecretKey key = kg.generateKey();
		byte rawKeyData[] = key.getEncoded();
		String keyHexString = DES.BytesTohexString(rawKeyData);

		return keyHexString;

	}

	/**
	 * 加密方法
	 * 
	 * @param keyHexString
	 * @param str
	 * @return 加密后的十六进制字符串，长度为32位
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 * @throws NoSuchPaddingException
	 * @throws InvalidKeySpecException
	 */
	public static String encrypt(String keyHexString, String str) throws InvalidKeyException, NoSuchAlgorithmException,
			InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {

		SecureRandom sr = new SecureRandom();
		DESKeySpec dks = new DESKeySpec(DES.hexStringToBytes(keyHexString));
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey key = keyFactory.generateSecret(dks);
		Cipher cipher = Cipher.getInstance("DES");
		cipher.init(Cipher.ENCRYPT_MODE, key, sr);
		byte data[] = str.getBytes();
		byte[] encryptedData = cipher.doFinal(data);
		String encrypedHexString = DES.BytesTohexString(encryptedData);

		return encrypedHexString;

	}

	/**
	 * 解密方法
	 * 
	 * @param keyHexString
	 * @param encrypedHexString
	 *            加密后的十六进制字符串，长度为32位
	 * @return 解密后的字符串
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws InvalidKeySpecException
	 */
	public static String decrypt(String keyHexString, String encrypedHexString)
			throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidKeySpecException {

		SecureRandom sr = new SecureRandom();
		DESKeySpec dks = new DESKeySpec(DES.hexStringToBytes(keyHexString));
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey key = keyFactory.generateSecret(dks);
		Cipher cipher = Cipher.getInstance("DES");
		cipher.init(Cipher.DECRYPT_MODE, key, sr);
		byte decryptedData[] = cipher.doFinal(DES.hexStringToBytes(encrypedHexString));

		return new String(decryptedData);

	}

	/**
	 * byte数组转换为十六进制字符串
	 * 
	 * @param b
	 * @return
	 */
	public static String BytesTohexString(byte[] b) {

		String result = "";
		for (int i = 0; i < b.length; i++) {
			String hex = Integer.toHexString(b[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			result = result + hex.toUpperCase();
		}

		return result;

	}

	/**
	 * 十六进制字符串转换为byte数组
	 * 
	 * @param hexString
	 * @return
	 */
	public static byte[] hexStringToBytes(String hexString) {

		if (hexString == null || hexString.equals("")) {
			return null;
		}
		hexString = hexString.toUpperCase();
		int length = hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		byte[] d = new byte[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
		}

		return d;

	}

	/**
	 * 字节数据转换成byte
	 * 
	 * @param c
	 * @return
	 */
	private static byte charToByte(char c) {
		return (byte) "0123456789ABCDEF".indexOf(c);
	}

	public final static String MD5(String s) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

		try {
			byte[] btInput = s.getBytes();
			// 获得MD5摘要算法的 MessageDigest 对象
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			// 使用指定的字节更新摘要
			mdInst.update(btInput);
			// 获得密文
			byte[] md = mdInst.digest();
			// 把密文转换成十六进制的字符串形式
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void main(String[] args) {
		String str = "liuhongtian";
		System.out.println(MD5(str));
		System.out.println(BytesTohexString(MD5(str).getBytes()));
	}

}