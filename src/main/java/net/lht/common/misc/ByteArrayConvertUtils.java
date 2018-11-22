package net.lht.common.misc;

//import org.apache.commons.lang3.ArrayUtils;

import io.netty.buffer.ByteBufUtil;

/**
 * 
 * <pre>
 * 基本数据类型转换(主要是byte和其它类型之间的互转).
 * </pre>
 *
 * @author F.Fang
 * @version $Id: ByteArrayConvertUtils.java,v 1.3 2017/08/25 02:42:22 baoxl Exp $
 */
public class ByteArrayConvertUtils {

	/**
	 * 
	 * <pre>
	 * 将4个byte数字组成的数组合并为一个float数.
	 * </pre>
	 * 
	 * @param arr
	 * @return
	 */
	public static float byte4ToFloat(byte[] arr) {
		if (arr == null || arr.length != 4) {
			throw new IllegalArgumentException("byte数组必须不为空,并且是4位!");
		}
		int i = byte4ToInt(arr);
		return Float.intBitsToFloat(i);
	}

	/**
	 * 
	 * <pre>
	 * 将一个float数字转换为4个byte数字组成的数组.
	 * </pre>
	 * 
	 * @param f
	 * @return
	 */
	public static byte[] floatToByte4(float f) {
		int i = Float.floatToIntBits(f);
		return intToByte4(i);
	}

	/**
	 * 
	 * <pre>
	 * 将八个byte数字组成的数组转换为一个double数字.
	 * </pre>
	 * 
	 * @param arr
	 * @return
	 */
	public static double byte8ToDouble(byte[] arr) {
		if (arr == null || arr.length != 8) {
			throw new IllegalArgumentException("byte数组必须不为空,并且是8位!");
		}
		long l = byte8ToLong(arr);
		return Double.longBitsToDouble(l);
	}

	/**
	 * 
	 * <pre>
	 * 将一个double数字转换为8个byte数字组成的数组.
	 * </pre>
	 * 
	 * @param i
	 * @return
	 */
	public static byte[] doubleToByte8(double i) {
		long j = Double.doubleToLongBits(i);
		return longToByte8(j);
	}

	/**
	 * 
	 * <pre>
	 * 将一个char字符转换为两个byte数字转换为的数组.
	 * </pre>
	 * 
	 * @param c
	 * @return
	 */
	public static byte[] charToByte2(char c) {
		byte[] arr = new byte[2];
		arr[0] = (byte) (c >> 8);
		arr[1] = (byte) (c & 0xff);
		return arr;
	}

	/**
	 * 
	 * <pre>
	 * 将2个byte数字组成的数组转换为一个char字符.
	 * </pre>
	 * 
	 * @param arr
	 * @return
	 */
	public static char byte2ToChar(byte[] arr) {
		if (arr == null || arr.length != 2) {
			throw new IllegalArgumentException("byte数组必须不为空,并且是2位!");
		}
		//ArrayUtils.reverse(arr);
		return (char) (((char) (arr[0] << 8)) | ((char) arr[1]));
	}

	/**
	 * 
	 * <pre>
	 * 将一个16位的short转换为长度为2的8位byte数组.
	 * </pre>
	 * 
	 * @param s
	 * @return
	 */
	public static byte[] shortToByte2(Short s) {
		byte[] arr = new byte[2];
		arr[0] = (byte) (s >> 8);
		arr[1] = (byte) (s & 0xff);
		return arr;
	}

	/**
	 * 
	 * <pre>
	 * 长度为2的8位byte数组转换为一个16位short数字.
	 * </pre>
	 * 
	 * @param arr
	 * @return
	 */
	public static short byte2ToShort(byte[] arr) {
		if (arr != null && arr.length != 2) {
			throw new IllegalArgumentException("byte数组必须不为空,并且是2位!");
		}
		//ArrayUtils.reverse(arr);
		short res = (short) (((short) arr[0] << 8) | ((short) arr[1] & 0xff));
		return res;
	}

	/**
	 * 
	 * <pre>
	 * 将32位int转换为由四个8位byte数字.
	 * </pre>
	 * 
	 * @param sum
	 * @return
	 */
	public static byte[] intToByte4(int sum) {
		byte[] arr = new byte[4];
		arr[0] = (byte) (sum >> 24);
		arr[1] = (byte) (sum >> 16);
		arr[2] = (byte) (sum >> 8);
		arr[3] = (byte) (sum & 0xff);
		return arr;
	}

	/**
	 * <pre>
	 * 将长度为4的8位byte数组转换为32位int.
	 * </pre>
	 * 
	 * @param arr
	 * @return
	 */
	public static int byte4ToInt(byte[] arr) {
		if (arr == null || arr.length != 4) {
			throw new IllegalArgumentException("byte数组必须不为空,并且是4位!");
		}
		//ArrayUtils.reverse(arr);
		return (int) (((arr[0] & 0xff) << 24) | ((arr[1] & 0xff) << 16) | ((arr[2] & 0xff) << 8) | ((arr[3] & 0xff)));
	}

	/**
	 * 
	 * <pre>
	 * 将长度为8的8位byte数组转换为64位long.
	 * </pre>
	 * 
	 * 0xff对应16进制,f代表1111,0xff刚好是8位 byte[]
	 * arr,byte[i]&0xff刚好满足一位byte计算,不会导致数据丢失. 如果是int计算. int[] arr,arr[i]&0xffff
	 * 
	 * @param arr
	 * @return
	 */
	public static long byte8ToLong(byte[] arr) {
		if (arr == null || arr.length != 8) {
			throw new IllegalArgumentException("byte数组必须不为空,并且是8位!");
		}
		//ArrayUtils.reverse(arr);
		return (long) (((long) (arr[0] & 0xff) << 56) | ((long) (arr[1] & 0xff) << 48) | ((long) (arr[2] & 0xff) << 40)
				| ((long) (arr[3] & 0xff) << 32) | ((long) (arr[4] & 0xff) << 24) | ((long) (arr[5] & 0xff) << 16)
				| ((long) (arr[6] & 0xff) << 8) | ((long) (arr[7] & 0xff)));
	}
	
	public static int byteToUnsignedInt(byte b) {
		int t = b & 0x7f;	// 去掉最高位
		if((b & 0x80) == 0x80) { // 最高位为1
			t += 128;
		}
		return t;
	}

	/**
	 * 将一个long数字转换为8个byte数组组成的数组.
	 */
	public static byte[] longToByte8(long sum) {
		byte[] arr = new byte[8];
		arr[0] = (byte) (sum >> 56);
		arr[1] = (byte) (sum >> 48);
		arr[2] = (byte) (sum >> 40);
		arr[3] = (byte) (sum >> 32);
		arr[4] = (byte) (sum >> 24);
		arr[5] = (byte) (sum >> 16);
		arr[6] = (byte) (sum >> 8);
		arr[7] = (byte) (sum & 0xff);
		return arr;
	}
	
	public static void main(String[] args) {
		String b = "080c";
		System.out.println(byte2ToShort(ByteBufUtil.decodeHexDump(b)));
		short s = 3661;
		System.out.println(ByteBufUtil.hexDump(shortToByte2(s)));
	}

}