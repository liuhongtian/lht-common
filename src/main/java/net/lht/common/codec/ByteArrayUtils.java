package net.lht.common.codec;

import java.math.BigInteger;

/**
 * 字节数组操作工具
 * 
 * @author liuhongtian
 *
 */
public class ByteArrayUtils {

	private static final String B0 = "0";
	private static final String B1 = "1";

	public static void main(String... args) {
		// System.out.println(binStringToByte("10000000.00000000-00000000_00000001|00000000|00000000_10000000
		// 10011101"));
		System.out.println(ByteArrayUtils.getHexString(ByteArrayUtils.intToByteArray4(12)));
		System.out.println(ByteArrayUtils.getHexString(ByteArrayUtils.int2Byte(12)));
		System.out.println(StringToAsciiString("aabvd"));
		System.out.println(AsciiStringToString("6161627664"));
		System.out.println(algorismToHexString(1125));
		System.out.println(algorismToHexString(12, 8));

		byte[] bytes = parseHexBinary("11000000");
		System.out.println(byteArrayToString(bytes));
	}

	/**
	 * 获取最短限定长度的BCD编码
	 * 
	 * @param i   源数字
	 * @param len BCD最小长度（字节长度，BCD码长度为此长度X2）
	 * @return BCD码，最小长度为len字节，不足时前面补0，超过时按实际输出。
	 */
	public static String getBCD(int i, int len) {
		String src = Integer.toString(i);
		StringBuffer preBuff = new StringBuffer();
		int bcdLen = len + len;
		int currLen = src.length();
		while (currLen < bcdLen) {
			preBuff.append("0");
			currLen++;
		}
		return preBuff.append(src).toString();
	}

	/**
	 * 生成一个字节数组的完整克隆
	 * 
	 * @param src 源
	 * @return 新字节数组
	 */
	public byte[] clone(byte[] src) {
		if (src == null) {
			return null;
		}

		if (src.length == 0) {
			return new byte[0];
		}

		byte[] dest = new byte[src.length];
		for (int i = 0; i < src.length; i++) {
			dest[i] = src[i];
		}
		return dest;
	}

	/**
	 * 将一个字节数组的指定字节复制到另一个字节数组的指定字节
	 * 
	 * @param src       源
	 * @param dest      目标
	 * @param srcStart  源起始地址
	 * @param destStart 目标起始地址
	 * @param length    复制长度
	 */
	public static void copy(byte[] src, byte[] dest, int srcStart, int destStart, int length) {
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
		copy(src, dest, 0, countOf0Byte, src.length);
		return dest;
	}

	/**
	 * 清除数字字符串中的连字符和空白，包括： . 、 - 、 _ 、 | 、 + 以及空白
	 * 
	 * @param str
	 * @return
	 */
	private static String purgeNumberString(String str) {
		return str.replaceAll("\\+", "").replaceAll("\\.", "").replaceAll("\\-", "").replaceAll("_", "")
				.replaceAll("\\|", "")
				.replaceAll("\\s", "");
	}

	/**
	 * 二进制字符串转换为对应的long数值
	 * 
	 * @param binStr 二进制字符串，例如：0011101。最长64位（8字节，int类型的长度）。<br>
	 *               binStr超长时，超出的部分从最高位（binStr最小索引）起丢弃，截取后，最高位（binStr[0]）为符号位；<br>
	 *               binStr长度不足时，高位（binStr最小索引）补零。<br>
	 *               binStr可以用特定连字符进行分隔，这些字符不占长度，并被忽略。
	 * @return long数值，对应于0011101，则为29
	 */
	public static long binStringToLong(String binStr) {
		binStr = purgeNumberString(binStr);
		if (binStr.length() > 64) {
			binStr = binStr.substring(binStr.length() - 64);
		}
		long tmp = 0; // 初始值
		int j = 0;
		for (int i = binStr.length(); i > 0; i--) {
			long s = Long.parseLong(binStr.substring(i - 1, i)) > 0 ? 1 : 0;
			tmp += (s << j);
			j++;
		}
		return tmp;
	}

	/**
	 * 二进制字符串转换为对应的int数值
	 * 
	 * @param binStr 二进制字符串，例如：0011101。最长32位（4字节，int类型的长度）。<br>
	 *               binStr超长时，超出的部分从最高位（binStr最小索引）起丢弃，截取后，最高位（binStr[0]）为符号位；<br>
	 *               binStr长度不足时，高位（binStr最小索引）补零。<br>
	 *               binStr可以用特定连字符进行分隔，这些字符不占长度，并被忽略。
	 * @return int数值，对应于0011101，则为29
	 */
	public static int binStringToInt(String binStr) {
		binStr = purgeNumberString(binStr);
		if (binStr.length() > 32) {
			binStr = binStr.substring(binStr.length() - 32);
		}
		int tmp = 0; // 初始值
		int j = 0;
		for (int i = binStr.length(); i > 0; i--) {
			int s = Integer.parseInt(binStr.substring(i - 1, i)) > 0 ? 1 : 0;
			tmp += (s << j);
			j++;
		}
		return tmp;
	}

	/**
	 * 二进制字符串转换为对应的short数值
	 * 
	 * @param binStr 二进制字符串，例如：0011101。最长16位（2字节，short类型的长度）。<br>
	 *               binStr超长时，超出的部分从最高位（binStr最小索引）起丢弃，截取后，最高位（binStr[0]）为符号位；<br>
	 *               binStr长度不足时，高位（binStr最小索引）补零。<br>
	 *               binStr可以用特定连字符进行分隔，这些字符不占长度，并被忽略。
	 * @return int数值，对应于0011101，则为29
	 */
	public static short binStringToShort(String binStr) {
		binStr = purgeNumberString(binStr);
		if (binStr.length() > 16) {
			binStr = binStr.substring(binStr.length() - 16);
		}
		short tmp = 0; // 初始值
		int j = 0;
		for (int i = binStr.length(); i > 0; i--) {
			short s = Short.parseShort(binStr.substring(i - 1, i)) > 0 ? (short) 1 : (short) 0;
			tmp += (s << j);
			j++;
		}
		return tmp;
	}

	/**
	 * 二进制字符串转换为对应的byte数值
	 * 
	 * @param binStr 二进制字符串，例如：0011101。最长8位（1字节，byte类型的长度）。<br>
	 *               binStr超长时，超出的部分从最高位（binStr最小索引）起丢弃，截取后，最高位（binStr[0]）为符号位；<br>
	 *               binStr长度不足时，高位（binStr最小索引）补零。<br>
	 *               binStr可以用特定连字符进行分隔，这些字符不占长度，并被忽略。
	 * @return int数值，对应于0011101，则为29
	 */
	public static byte binStringToByte(String binStr) {
		binStr = purgeNumberString(binStr);
		if (binStr.length() > 8) {
			binStr = binStr.substring(binStr.length() - 8);
		}
		byte tmp = 0; // 初始值
		int j = 0;
		for (int i = binStr.length(); i > 0; i--) {
			byte s = Byte.parseByte(binStr.substring(i - 1, i)) > 0 ? (byte) 1 : (byte) 0;
			tmp += (s << j);
			j++;
		}
		return tmp;
	}

	/**
	 * 网络传输的字节流（字节数组）型转换成int数据
	 * 
	 * @param bytes 长度为4的字节数组
	 * @return 一个int数据
	 */
	public static int byteArray4OverNetworkToInt(byte[] bytes) {
		int num = 0;
		int temp;
		temp = (0x000000ff & (bytes[0])) << 0;
		num = num | temp;
		temp = (0x000000ff & (bytes[1])) << 8;
		num = num | temp;
		temp = (0x000000ff & (bytes[2])) << 16;
		num = num | temp;
		temp = (0x000000ff & (bytes[3])) << 24;
		num = num | temp;
		return num;
	}

	/**
	 * int转换成网络传输的字节流（字节数组）型数据
	 * 
	 * @param num 一个整型数据
	 * @return 长度为4的字节数组
	 */
	public static byte[] intToByteArray4OverNetwork(int num) {
		byte[] bytes = new byte[4];
		bytes[0] = (byte) (0xff & (num >> 0));
		bytes[1] = (byte) (0xff & (num >> 8));
		bytes[2] = (byte) (0xff & (num >> 16));
		bytes[3] = (byte) (0xff & (num >> 24));
		return bytes;
	}

	/**
	 * 长整形转换成网络传输的字节流（字节数组）型数据
	 * 
	 * @param num 一个长整型数据
	 * @return 长度为8的字节数组
	 */
	public static byte[] longToByteArray8OverNetwork(long num) {
		byte[] bytes = new byte[8];
		for (int i = 0; i < 8; i++) {
			bytes[i] = (byte) (0xff & (num >> (i * 8)));
		}

		return bytes;
	}

	/**
	 * 将长度为4的byte数组转换为4字节int
	 * 
	 * @param arr
	 * @return
	 */
	public static int byteArray4ToInt(byte[] arr) {
		if (arr == null || arr.length != 4) {
			throw new IllegalArgumentException("byte must not null, and length is 4.");
		}
		return (int) (((arr[0] & 0xff) << 24) | ((arr[1] & 0xff) << 16) | ((arr[2] & 0xff) << 8) | ((arr[3] & 0xff)));
	}

	/**
	 * 将4字节int转换为长度为4的byte数组.
	 * 
	 * @param num
	 * @return
	 */
	public static byte[] intToByteArray4(int num) {
		byte[] arr = new byte[4];
		arr[0] = (byte) (num >> 24);
		arr[1] = (byte) (num >> 16);
		arr[2] = (byte) (num >> 8);
		arr[3] = (byte) (num & 0xff);
		return arr;
	}

	/**
	 * 将4个byte数字组成的数组合并为一个float数.
	 * 
	 * @param arr
	 * @return
	 */
	public static float byteArray4ToFloat(byte[] arr) {
		return Float.intBitsToFloat(byteArray4ToInt(arr));
	}

	/**
	 * 将一个float数字转换为长度为4的字节数组.
	 * 
	 * @param f
	 * @return
	 */
	public static byte[] floatToByteArray4(float f) {
		return intToByteArray4(Float.floatToIntBits(f));
	}

	/**
	 * 将长度为8的byte数组转换为64位long.
	 * 
	 * 0xff对应16进制,f代表1111,0xff刚好是8位 byte[] arr,byte[i]&0xff刚好满足一位byte计算,不会导致数据丢失.
	 * 如果是int计算. int[] arr,arr[i]&0xffff
	 * 
	 * @param arr
	 * @return
	 */
	public static long byteArray8ToLong(byte[] arr) {
		if (arr == null || arr.length != 8) {
			throw new IllegalArgumentException("byte must not null, and length is 8.");
		}
		// ArrayUtils.reverse(arr);
		return (long) (((long) (arr[0] & 0xff) << 56) | ((long) (arr[1] & 0xff) << 48) | ((long) (arr[2] & 0xff) << 40)
				| ((long) (arr[3] & 0xff) << 32) | ((long) (arr[4] & 0xff) << 24) | ((long) (arr[5] & 0xff) << 16)
				| ((long) (arr[6] & 0xff) << 8) | ((long) (arr[7] & 0xff)));
	}

	/**
	 * 将一个long数字转换为长度为8的byte数组.
	 * 
	 * @param num
	 * @return
	 */
	public static byte[] longToByteArray8(long num) {
		byte[] arr = new byte[8];
		arr[0] = (byte) (num >> 56);
		arr[1] = (byte) (num >> 48);
		arr[2] = (byte) (num >> 40);
		arr[3] = (byte) (num >> 32);
		arr[4] = (byte) (num >> 24);
		arr[5] = (byte) (num >> 16);
		arr[6] = (byte) (num >> 8);
		arr[7] = (byte) (num & 0xff);
		return arr;
	}

	/**
	 * 将长度为8的byte数组转换为一个double数字.
	 * 
	 * @param arr
	 * @return
	 */
	public static double byteArray8ToDouble(byte[] arr) {
		return Double.longBitsToDouble(byteArray8ToLong(arr));
	}

	/**
	 * 将一个double数字转换为长度为8的byte数组.
	 * 
	 * @param d
	 * @return
	 */
	public static byte[] doubleToByteArray8(double d) {
		return longToByteArray8(Double.doubleToLongBits(d));
	}

	/**
	 * 长度为2的byte数组转换为一个16位short数字.
	 * 
	 * @param arr
	 * @return
	 */
	public static short byteArray2ToShort(byte[] arr) {
		if (arr != null && arr.length != 2) {
			throw new IllegalArgumentException("byte must not null, and length is 2.");
		}
		// ArrayUtils.reverse(arr);
		short res = (short) (((short) arr[0] << 8) | ((short) arr[1] & 0xff));
		return res;
	}

	/**
	 * 将一个16位的short转换为长度为2的8位byte数组.
	 * 
	 * @param s
	 * @return
	 */
	public static byte[] shortToByteArray2(short s) {
		byte[] arr = new byte[2];
		arr[0] = (byte) (s >> 8);
		arr[1] = (byte) (s & 0xff);
		return arr;
	}

	/**
	 * 将长度为2的byte数组转换为一个char字符.
	 * 
	 * @param arr
	 * @return
	 */
	public static char byteArray2ToChar(byte[] arr) {
		if (arr == null || arr.length != 2) {
			throw new IllegalArgumentException("byte must not null, and length is 2.");
		}
		// ArrayUtils.reverse(arr);
		return (char) (((char) (arr[0] << 8)) | ((char) arr[1]));
	}

	/**
	 * 将一个char字符转换为长度为2的byte数组.
	 * 
	 * @param c
	 * @return
	 */
	public static byte[] charToByteArray2(char c) {
		byte[] arr = new byte[2];
		arr[0] = (byte) (c >> 8);
		arr[1] = (byte) (c & 0xff);
		return arr;
	}

	/**
	 * 将一个字节转换为无符号整数（0 ~ 255）
	 * 
	 * @param b
	 * @return
	 */
	public static int byteToUnsignedInt(byte b) {
		int t = b & 0x7f; // 去掉最高位
		if ((b & 0x80) == 0x80) { // 最高位为1
			t += 128;
		}
		return t;
	}

	/**
	 * 将无符号整数（0 ~ 255）转换为一个字节
	 * 
	 * @param t
	 * @return
	 */
	public static byte unsignedIntToByte(int t) {
		boolean overRange = false;
		if (t > 127) {// 最高位是1
			t = t & 0x7f; // 最高位置0（t = t - 128）
			overRange = true;
		}

		byte b = Byte.parseByte(Integer.toString(t));

		// 最高位是1
		if (overRange) {
			b = intToByteArray4(b | 0x80)[3];
		}

		return b;
	}

	/**
	 * 字节数组转大数字
	 * 
	 * @param b
	 * @return
	 */
	public static BigInteger byteArrayToBigInteger(byte[] b) {
		if (b[0] < 0) {
			byte[] temp = new byte[b.length + 1];
			temp[0] = 0;
			System.arraycopy(b, 0, temp, 1, b.length);
			return new BigInteger(temp);
		}
		return new BigInteger(b);
	}

	/**
	 * 大数字转长度为32的字节数组
	 * 
	 * @param n
	 * @return
	 */
	public static byte[] bigIntegerToByteArray32(BigInteger n) {
		byte tmpd[] = (byte[]) null;
		if (n == null) {
			return null;
		}

		if (n.toByteArray().length == 33) {
			tmpd = new byte[32];
			System.arraycopy(n.toByteArray(), 1, tmpd, 0, 32);
		} else if (n.toByteArray().length == 32) {
			tmpd = n.toByteArray();
		} else {
			tmpd = new byte[32];
			for (int i = 0; i < 32 - n.toByteArray().length; i++) {
				tmpd[i] = 0;
			}
			System.arraycopy(n.toByteArray(), 0, tmpd, 32 - n.toByteArray().length, n.toByteArray().length);
		}
		return tmpd;
	}

	/**
	 * 将字节数组以二进制字符串表示
	 * 
	 * @param bytes
	 * @return
	 */
	public static String getBinString(byte[] bytes) {
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

	/**
	 * 根据字节数组获得值(十六进制数字，字母大写)
	 * 
	 * @param bytes
	 * @return
	 */
	public static String getHexString(byte[] bytes) {
		return bytesToHexString(bytes, true);
	}

	/**
	 * 根据字节数组获得值(十六进制数字)
	 * 
	 * @param bytes
	 * @param upperCase
	 * @return
	 */
	public static String getHexString(byte[] bytes, boolean upperCase) {
		return bytesToHexString(bytes, upperCase);
	}

	/**
	 * 根据字节数组获得值(十六进制数字，字母大写)
	 * 
	 * @param bytes
	 * @return
	 */
	public static String bytesToHexString(byte[] bytes) {
		return bytesToHexString(bytes, true);
	}

	/**
	 * 根据字节数组获得值(十六进制数字)
	 * 
	 * @param bytes
	 * @param upperCase
	 * @return
	 */
	public static String bytesToHexString(byte[] bytes, boolean upperCase) {
		String ret = "";
		for (int i = 0; i < bytes.length; i++) {
			ret += Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1);
		}
		return upperCase ? ret.toUpperCase() : ret;
	}

	/**
	 * 将十六进制的字符串转换为字节数组
	 * 
	 * @param hexString the hex string
	 * @return byte[]
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
			d[i] = (byte) (hexCharToByte(hexChars[pos]) << 4 | hexCharToByte(hexChars[pos + 1]));
		}
		return d;
	}

	/**
	 * 将十六进制数字涉及的字符转换为byte
	 * 
	 * @param c char
	 * @return byte
	 */
	private static byte hexCharToByte(char c) {
		return (byte) "0123456789ABCDEF".indexOf(c);
	}

	/**
	 * 用于建立十六进制字符的输出的小写字符数组
	 */
	private static final char[] DIGITS_LOWER = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
			'e', 'f' };

	/**
	 * 用于建立十六进制字符的输出的大写字符数组
	 */
	private static final char[] DIGITS_UPPER = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D',
			'E', 'F' };

	/**
	 * 将字节数组转换为十六进制字符数组
	 *
	 * @param data byte[]
	 * @return 十六进制char[]
	 */
	public static char[] byteArrayToHexCharArray(byte[] data) {
		return byteArrayToHexCharArray(data, true);
	}

	/**
	 * 将字节数组转换为十六进制字符数组
	 *
	 * @param data        byte[]
	 * @param toLowerCase <code>true</code> 传换成小写格式 ， <code>false</code> 传换成大写格式
	 * @return 十六进制char[]
	 */
	public static char[] byteArrayToHexCharArray(byte[] data, boolean toLowerCase) {
		return byteArrayToHexCharArray(data, toLowerCase ? DIGITS_LOWER : DIGITS_UPPER);
	}

	/**
	 * 将字节数组转换为十六进制字符数组
	 *
	 * @param data     byte[]
	 * @param toDigits 用于控制输出的char[]
	 * @return 十六进制char[]
	 */
	private static char[] byteArrayToHexCharArray(byte[] data, char[] toDigits) {
		int l = data.length;
		char[] out = new char[l << 1];
		// two characters form the hex value.
		for (int i = 0, j = 0; i < l; i++) {
			out[j++] = toDigits[(0xF0 & data[i]) >>> 4];
			out[j++] = toDigits[0x0F & data[i]];
		}
		return out;
	}

	/**
	 * 将十六进制字符数组转换为字节数组
	 *
	 * @param data 十六进制char[]
	 * @return byte[]
	 * @throws RuntimeException 如果源十六进制字符数组是一个奇怪的长度，将抛出运行时异常
	 */
	public static byte[] hexCharArrayToByteArray(char[] data) {
		int len = data.length;

		if ((len & 0x01) != 0) {
			throw new RuntimeException("Odd number of characters.");
		}

		byte[] out = new byte[len >> 1];

		// two characters form the hex value.
		for (int i = 0, j = 0; j < len; i++) {
			int f = toDigit(data[j], j) << 4;
			j++;
			f = f | toDigit(data[j], j);
			j++;
			out[i] = (byte) (f & 0xFF);
		}

		return out;
	}

	/**
	 * 将十六进制字符转换成一个整数
	 *
	 * @param ch    十六进制char
	 * @param index 十六进制字符在字符数组中的位置
	 * @return 一个整数
	 * @throws RuntimeException 当ch不是一个合法的十六进制字符时，抛出运行时异常
	 */
	private static int toDigit(char ch, int index) {
		int digit = Character.digit(ch, 16);
		if (digit == -1) {
			throw new RuntimeException("Illegal hexadecimal character " + ch + " at index " + index);
		}
		return digit;
	}

	/**
	 * 字符串转ASCII码字符串
	 * 
	 * @param String 字符串
	 * @return ASCII字符串
	 */
	public static String StringToAsciiString(String content) {
		String result = "";
		int max = content.length();
		for (int i = 0; i < max; i++) {
			char c = content.charAt(i);
			String b = Integer.toHexString(c);
			result = result + b;
		}
		return result;
	}

	/**
	 * ASCII码字符串转字符串
	 * 
	 * @param String ASCII字符串
	 * @return 字符串
	 */
	public static String AsciiStringToString(String content) {
		String result = "";
		int length = content.length() / 2;
		for (int i = 0; i < length; i++) {
			String c = content.substring(i * 2, i * 2 + 2);
			int a = hexStringToAlgorism(c);
			char b = (char) a;
			String d = String.valueOf(b);
			result += d;
		}
		return result;
	}

	/**
	 * 十六进制转字符串
	 * 
	 * @param hexString  十六进制字符串
	 * @param encodeType 编码类型4：Unicode，2：普通编码
	 * @return 字符串
	 */
	public static String hexStringToString(String hexString, int encodeType) {
		String result = "";
		int max = hexString.length() / encodeType;
		for (int i = 0; i < max; i++) {
			char c = (char) hexStringToAlgorism(hexString.substring(i * encodeType, (i + 1) * encodeType));
			result += c;
		}
		return result;
	}

	/**
	 * 十六进制字符串转十进制数值
	 * 
	 * @param hex 十六进制字符串
	 * @return 十进制数值
	 */
	public static int hexStringToAlgorism(String hex) {
		hex = hex.toUpperCase();
		int max = hex.length();
		int result = 0;
		for (int i = max; i > 0; i--) {
			char c = hex.charAt(i - 1);
			int algorism = 0;
			if (c >= '0' && c <= '9') {
				algorism = c - '0';
			} else {
				algorism = c - 55;
			}
			result += Math.pow(16, max - i) * algorism;
		}
		return result;
	}

	/**
	 * 将十进制转换为指定长度的十六进制字符串
	 * 
	 * @param algorism  int 十进制数字
	 * @param strLength int 转换后的十六进制字符串长度
	 * @return String 转换后的十六进制字符串
	 */
	public static String algorismToHexString(int algorism, int strLength) {
		String result = "";
		result = Integer.toHexString(algorism);

		if (result.length() % 2 == 1) {
			result = "0" + result;
		}
		return patchHexString(result.toUpperCase(), strLength);
	}

	/**
	 * 十进制转换为十六进制字符串
	 * 
	 * @param algorism int 十进制的数字
	 * @return String 对应的十六进制字符串
	 */
	public static String algorismToHexString(int algorism) {
		String result = "";
		result = Integer.toHexString(algorism);

		if (result.length() % 2 == 1) {
			result = "0" + result;

		}
		result = result.toUpperCase();

		return result;
	}

	/**
	 * 十六进制字符串转二进制字符串
	 * 
	 * @param hex 十六进制字符串
	 * @return 二进制字符串
	 */
	public static String hexStringToBinString(String hex) {
		hex = hex.toUpperCase();
		String result = "";
		int max = hex.length();
		for (int i = 0; i < max; i++) {
			char c = hex.charAt(i);
			switch (c) {
				case '0':
					result += "0000";
					break;
				case '1':
					result += "0001";
					break;
				case '2':
					result += "0010";
					break;
				case '3':
					result += "0011";
					break;
				case '4':
					result += "0100";
					break;
				case '5':
					result += "0101";
					break;
				case '6':
					result += "0110";
					break;
				case '7':
					result += "0111";
					break;
				case '8':
					result += "1000";
					break;
				case '9':
					result += "1001";
					break;
				case 'A':
					result += "1010";
					break;
				case 'B':
					result += "1011";
					break;
				case 'C':
					result += "1100";
					break;
				case 'D':
					result += "1101";
					break;
				case 'E':
					result += "1110";
					break;
				case 'F':
					result += "1111";
					break;
			}
		}
		return result;
	}

	/**
	 * 字节数组转为普通字符串（ASCII对应的字符）
	 * 
	 * @param bytearray byte[]
	 * @return String
	 */
	public static String byteArrayToString(byte[] bytearray) {
		String result = "";
		char temp;

		int length = bytearray.length;
		for (int i = 0; i < length; i++) {
			temp = (char) bytearray[i];
			result += temp;
		}
		return result;
	}

	/**
	 * 二进制字符串转十进制
	 * 
	 * @param binary 二进制字符串
	 * @return 十进制数值
	 */
	public static int binStringToAlgorism(String binary) {
		int max = binary.length();
		int result = 0;
		for (int i = max; i > 0; i--) {
			char c = binary.charAt(i - 1);
			int algorism = c - '0';
			result += Math.pow(2, max - i) * algorism;
		}
		return result;
	}

	/**
	 * HEX字符串前补0，主要用于长度位数不足。
	 * 
	 * @param str       String 需要补充长度的十六进制字符串
	 * @param maxLength int 补充后十六进制字符串的长度
	 * @return 补充结果
	 */
	static public String patchHexString(String str, int maxLength) {
		String temp = "";
		for (int i = 0; i < maxLength - str.length(); i++) {
			temp = "0" + temp;
		}
		str = (temp + str).substring(0, maxLength);
		return str;
	}

	/**
	 * 将一个字符串转换为int
	 * 
	 * @param s          String 要转换的字符串
	 * @param defaultInt int 如果出现异常,默认返回的数字
	 * @param radix      int 要转换的字符串是什么进制的,如16 8 10.
	 * @return int 转换后的数字
	 */
	public static int parseStringToInt(String s, int defaultInt, int radix) {
		int i = 0;
		try {
			i = Integer.parseInt(s, radix);
		} catch (NumberFormatException ex) {
			i = defaultInt;
		}
		return i;
	}

	/**
	 * 取指定字节数组的指定子集
	 * 
	 * @param input
	 * @param startIndex
	 * @param length
	 * @return
	 */
	public static byte[] subByte(byte[] input, int startIndex, int length) {
		byte[] bt = new byte[length];
		for (int i = 0; i < length; i++) {
			bt[i] = input[i + startIndex];
		}
		return bt;
	}

	/**
	 * 字节内高低位转换
	 * 
	 * @param b String 需要转换的字符串
	 * @return collection 字符串
	 */
	public static String byteInventer(String b) {
		String collection = "";
		for (int i = 0; i < b.length(); i += 2) {
			String s1 = b.substring(i, i + 1);
			String s2 = b.substring(i + 1, i + 2);
			collection += s2 + s1;
		}
		return collection;
	}

	/**
	 * ASCII码hex字符串转String明文 每两个字符表示的16进制ASCII码解析成一个明文字符
	 * 
	 * @param hex
	 * @return
	 */
	public static String asciiHex2Str(String hex) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < hex.length() - 1; i += 2) {
			String h = hex.substring(i, (i + 2));
			int decimal = Integer.parseInt(h, 16);
			sb.append((char) decimal);
		}
		return sb.toString();
	}

	/**
	 * String明文转ASCII码hex字符串 一个明文字符生成两个字符表示的16进制ASCII码
	 * 
	 * @param str
	 * @return
	 */
	public static String str2AsciiHex(String str) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			// 这里的第二个参数16表示十六进制
			sb.append(Integer.toString(c, 16));
		}
		return sb.toString();

	}

	public static int getUINT8(byte data) { // 将data字节型数据转换为0~255 (0xFF 即BYTE)
		return data & 0x0FF;
	}

	public static int getUINT16(int i) { // 将data字节型数据转换为0~65535 (0xFFFF 即 WORD)。
		return i & 0x0FFFF;
	}

	public static long getUINT32(int data) { // 将int数据转换为0~4294967295 (0xFFFFFFFF即DWORD)。
		return data & 0x0FFFFFFFFl;
	}

	/**
	 * 小端，即低字节在前 eg.byte[]={0x01,0x02,0x03,0x04} 结果是：0x04030201
	 * 
	 * @param bytes
	 * @return
	 */
	public static int byte2int_big(byte[] bytes) {
		int result = 0;
		for (int i = 0; i < bytes.length; i++) {
			int shift = (bytes.length - 1 - i) * 8;
			result += (getUINT8(bytes[i]) & 0x000000FF) << shift;
		}
		return result;
	}

	/**
	 * 大端，即高字节在前 eg.byte[]={0x01,0x02,0x03,0x04} 结果是：0x01020304
	 * 
	 * @param bytes
	 * @return
	 */
	public static int byte2int_little(byte[] bytes) {
		int result = 0;
		for (int i = 0; i < bytes.length; i++) {
			int shift = i * 8;
			result += (getUINT8(bytes[i]) & 0x000000FF) << shift;
		}
		return result;
	}

	// TODO 下面需要确认！！！

	// TODO what?
	public static byte[] parseHexBinary(String s) {
		final int len = s.length();

		// "111" is not a valid hex encoding.
		if (len % 2 != 0) {
			throw new IllegalArgumentException("hexBinary needs to be even-length: " + s);
		}

		byte[] out = new byte[len / 2];

		for (int i = 0; i < len; i += 2) {
			int h = hexToBin(s.charAt(i));
			int l = hexToBin(s.charAt(i + 1));
			if (h == -1 || l == -1) {
				throw new IllegalArgumentException("contains illegal character for hexBinary: " + s);
			}

			out[i / 2] = (byte) (h * 16 + l);
		}

		return out;
	}

	// TODO only used by parseHexBinary
	private static int hexToBin(char ch) {
		if ('0' <= ch && ch <= '9') {
			return ch - '0';
		}
		if ('A' <= ch && ch <= 'F') {
			return ch - 'A' + 10;
		}
		if ('a' <= ch && ch <= 'f') {
			return ch - 'a' + 10;
		}
		return -1;
	}

	/**
	 * TODO ?
	 * 
	 * @param hexString
	 * @return byte[]
	 */
	public byte[] hexStringHigh2Low(String hexString) {
		if (hexString.length() % 2 != 0) {
			hexString = "0" + hexString;
		}
		byte[] tem = hexString.getBytes();
		// byte[] tem = TypeConver.hexStringToBytes(hexString);
		byte[] ret = new byte[tem.length];
		for (int i = 0; i < tem.length; i++) {
			if (i % 2 == 0 && i < tem.length - 1) {
				ret[i] = tem[tem.length - i - 2];
				ret[i + 1] = tem[tem.length - i - 1];
			}
		}
		String t = new String(ret);
		byte[] m = hexStringToBytes(t);

		return m;
	}

	/**
	 * 
	 * @param b
	 * @return
	 */
	public static int byte2Int(byte[] bytes) {
		int value = 0;
		for (int i = 0; i < bytes.length; i++) {
			int shift = (bytes.length - 1 - i) * 8;
			value += (bytes[i] & 0x000000FF) << shift;
		}
		return value;
	}

	/**
	 * 
	 * @param intValue
	 * @return
	 */
	public static byte[] int2Byte(int intValue) {
		byte[] b = new byte[2];
		for (int i = 0; i < 2; i++) {
			b[i] = (byte) (intValue >> 8 * (1 - i) & 0xFF);
		}
		return b;
	}

	/**
	 * 
	 * @param intValue
	 * @return
	 */
	public static String int2HexHigh2Low(int intValue) {
		String intStr = Integer.toHexString(intValue);
		int len = intStr.length();
		while (len < 4) {
			intStr = "0" + intStr;
			len++;
		}
		String strHigh2Low = intStr.substring(2, 4) + intStr.substring(0, 2);
		return strHigh2Low;
	}

	/**
	 * ByteתBit
	 */
	public static String byteToBit(byte b) {
		return "" + (byte) ((b >> 7) & 0x1) + (byte) ((b >> 6) & 0x1) + (byte) ((b >> 5) & 0x1)
				+ (byte) ((b >> 4) & 0x1) + (byte) ((b >> 3) & 0x1) + (byte) ((b >> 2) & 0x1) + (byte) ((b >> 1) & 0x1)
				+ (byte) ((b >> 0) & 0x1);
	}

	public String bytesToBit(byte[] b) {
		String bitStr = "";
		for (int i = 0; i < b.length; i++) {
			bitStr = bitStr + (byte) ((b[i] >> 7) & 0x1) + (byte) ((b[i] >> 6) & 0x1) + (byte) ((b[i] >> 5) & 0x1)
					+ (byte) ((b[i] >> 4) & 0x1) + (byte) ((b[i] >> 3) & 0x1) + (byte) ((b[i] >> 2) & 0x1)
					+ (byte) ((b[i] >> 1) & 0x1) + (byte) ((b[i] >> 0) & 0x1);
		}
		return bitStr;
	}

	/**
	 * Bit to Byte
	 */
	public byte BitToByte(String byteStr) {
		int re, len;
		if (null == byteStr) {
			return 0;
		}
		len = byteStr.length();
		if (len != 4 && len != 8) {
			return 0;
		}
		if (len == 8) {
			if (byteStr.charAt(0) == '0') {
				re = Integer.parseInt(byteStr, 2);
			} else {
				re = Integer.parseInt(byteStr, 2) - 256;
			}
		} else {
			re = Integer.parseInt(byteStr, 2);
		}
		return (byte) re;
	}

	/**
	 * int转化为4字节即8字符的hexstring，且低字节在前，高字节在后 其中int范围为：0-65535
	 * 
	 * @param intValue
	 * @return
	 */
	public static String int4HexHigh2Low(long intValue) {
		String intStr = Long.toHexString(intValue);
		int len = intStr.length();
		while (len < 8) {
			intStr = "0" + intStr;
			len++;
		}
		String strHigh2Low = intStr.substring(6, 8) + intStr.substring(4, 6) + intStr.substring(2, 4)
				+ intStr.substring(0, 2);
		return strHigh2Low;
	}

	public static String convertStringToHex(String str) {

		char[] chars = str.toCharArray();

		StringBuffer hex = new StringBuffer();
		for (int i = 0; i < chars.length; i++) {
			String myhex = Integer.toHexString((int) chars[i]);
			if (myhex.length() == 1) {
				myhex = "0" + myhex;
			}
			hex.append(myhex);
		}

		return hex.toString();
	}

}
