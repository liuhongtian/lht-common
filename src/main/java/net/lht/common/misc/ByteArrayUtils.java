package net.lht.common.misc;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;

import io.netty.buffer.ByteBufUtil;

/**
 * 字节数组操作工具
 * 
 * @author liuhongtian
 *
 */
public class ByteArrayUtils {

	private static final String PREFIX = "00000000";
	private static final String B0 = "0";
	private static final String B1 = "1";

	/**
	 * 计算CRC16（Modbus格式）
	 * 
	 * @param bytes
	 *            待校验字节数组
	 * @return CRC字节数组
	 */
	public static byte[] getCRC16(byte[] bytes) {
		int CRC = 0x0000ffff;
		int POLYNOMIAL = 0x0000a001;
		int i, j;
		for (i = 0; i < bytes.length; i++) {
			CRC ^= ((int) bytes[i] & 0x000000ff);
			for (j = 0; j < 8; j++) {
				if ((CRC & 0x00000001) != 0) {
					CRC >>= 1;
					CRC ^= POLYNOMIAL;
				} else {
					CRC >>= 1;
				}
			}
		}
		byte[] crc = new byte[2];
		copyArray(intToByteArray(Integer.reverseBytes(CRC)), crc, 0, 0, 2);
		return crc;
	}

	/**
	 * 将一个字节数组完整的复制到另一个字节数组
	 * 
	 * @param src
	 *            源
	 * @param dest
	 *            目标
	 */
	public static void cloneArray(byte[] src, byte[] dest) {
		for (int i = 0; i < src.length; i++) {
			dest[i] = src[i];
		}
	}

	/**
	 * 将一个字节数组的指定字节复制到另一个字节数组的指定字节
	 * 
	 * @param src
	 *            源
	 * @param dest
	 *            目标
	 * @param srcStart
	 *            源起始地址
	 * @param destStart
	 *            目标起始地址
	 * @param length
	 *            复制长度
	 */
	public static void copyArray(byte[] src, byte[] dest, int srcStart, int destStart, int length) {
		for (int i = 0; i < length; i++) {
			dest[destStart + i] = src[srcStart + i];
		}
	}

	/**
	 * 向一个字节数组前添加指定数量的空字节
	 * 
	 * @param src
	 * @param countOf0Byte
	 * @return
	 */
	public static byte[] addPrefix0Bytes(byte[] src, int countOf0Byte) {
		byte[] dest = new byte[src.length + countOf0Byte];
		for (int i = 0; i < countOf0Byte; i++) {
			dest[i] = 0x00;
		}
		copyArray(src, dest, 0, countOf0Byte, src.length);
		return dest;
	}

	public static String binStringToDecString(String binStr) {
		int tmp = 0; // 初始值
		int j = 0;
		for (int i = binStr.length(); i > 0; i--) {
			tmp += (Integer.parseInt(binStr.substring(i - 1, i)) << j);
			j++;
		}
		return String.valueOf(tmp);
	}

	public static long binStringToLong(String binStr) {
		long tmp = 0; // 初始值
		int j = 0;
		for (int i = binStr.length(); i > 0; i--) {
			int s = Integer.parseInt(binStr.substring(i - 1, i)) > 0 ? 1 : 0;
			tmp += (s << j);
			j++;
		}
		return tmp;
	}

	/**
	 * int -> byte[]
	 * 
	 * @param integer
	 * @return
	 */
	public static byte[] intToByteArray(int integer) {
		int byteNum = (40 - Integer.numberOfLeadingZeros(integer < 0 ? ~integer : integer)) / 8;
		byte[] byteArray = new byte[4];

		for (int n = 0; n < byteNum; n++)
			byteArray[3 - n] = (byte) (integer >>> (n * 8));

		return (byteArray);
	}

	/**
	 * 字节数组转换为对应的Long，注意是有符号整数。
	 * 
	 * @param b
	 * @return
	 */
	public static Long byteArrayToLong(byte[] b) {
		return Long.decode("0x" + ByteBufUtil.hexDump(b));
	}

	/**
	 * 将byte[]转为二进制的字符串（整字节） 当二进制数字长度不足完整字节时，在数字前加前导0，以补全字节。
	 * 当字节数组本身有前导0时，每个前导0添加一个字节（8位）的前导0。
	 * 
	 * @param bytes
	 *            byte[]
	 * @return 转换后的字符串
	 */
	public static String binary(byte[] bytes) {
		String tmp = new BigInteger(1, bytes).toString(2);// 这里的1代表正数
		int t = tmp.length() % 8;
		if (t > 0) {
			tmp = PREFIX.substring(tmp.length() % 8, 8) + tmp;
		}

		int length = bytes.length * 8; // 转换后的二进制字符串期望长度
		for (int i = 0; i < ((length - tmp.length()) / 8); i++) {
			tmp = PREFIX + tmp;
		}
		return tmp;
	}

	/**
	 * 将字节数组以二进制字符串表示
	 * 
	 * @param bytes
	 * @return
	 */
	public static String binary2(byte[] bytes) {
		StringBuffer buff = new StringBuffer();
		for (byte b : bytes) {
			byte c = 0x01;
			StringBuffer tb = new StringBuffer();
			for (int i = 0; i < 8; i++) {
				if ((b & c) == c) {
					tb.append(B1);
				} else {
					tb.append(B0);
				}
				c *= 2;
			}
			tb.reverse();
			buff.append(tb);
		}
		return buff.toString();
	}
	
	public static void main(String[] args) throws UnsupportedEncodingException {
		String str = "DB6789EEappid=xdfde_aaad4&tocken=a687333==xeee56739*$==";
		String crc = ByteBufUtil.hexDump(getCRC16(str.getBytes("UTF-8")));
		System.out.println(crc);
	}

}
