package com.htstar.ovms.common.core.util;

import cn.hutool.core.util.StrUtil;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 设备解码工具类
 */
public class ByteDataUtil {

	/**
	* @Description:    获取字符串
	* @Author:         范利瑞
	* @CreateDate:     2020/3/20 15:59
	*/
	public static String parseStr(byte[] bDeviceSn) {
		int endFlag = 0;
		while (endFlag < bDeviceSn.length) {
			if (bDeviceSn[endFlag] == 0) {
				break;
			}
			endFlag++;
		}

		String deviceSn = ByteDataUtil.bytesToString(ByteDataUtil.bytesFromBytes(bDeviceSn, 0, endFlag));

		return deviceSn;
	}

	public static int stringToInt(String val, int defval) {
		try {
			return Integer.parseInt(val);
		} catch (Exception e) {
			// LogManager.exception(e.getMessage(), e);
		}

		return defval;
	}

	public static float stringToFloat(String val, float defval) {
		try {
			return Float.parseFloat(val);
		} catch (Exception e) {
			// LogManager.exception(e.getMessage(), e);
		}

		return defval;
	}

	public static double stringToDouble(String val, double defval) {
		try {
			return Double.parseDouble(val);
		} catch (Exception e) {
			// LogManager.exception(e.getMessage(), e);
		}

		return defval;
	}



	/**
	 * bytes 转字符串
	 * @param bytes
	 * @return
	 */
	public static String bytesToString(byte[] bytes) {
		try {
			return new String(bytes, "utf-8");
		} catch (Exception e) {
			// e.printStackTrace();
		}
		return "";
	}

	/**
	 * bytes 转16进制字符串
	 * @param bytes
	 * @return
	 */
	public static String bytesToHexString(byte[] bytes) {
		return bytesToHexString(bytes, null, null, null, false);
	}

	public static String bytesToHexString(byte[] bytes, String headStr,
										  String tailStr, String splitStr, boolean include0x) {
		if (bytes == null || bytes.length == 0)
			return "[]";

		// headStr0x00splitStr0x01tailStr 格式
		StringBuilder sb = new StringBuilder();
		if (headStr != null)
			sb.append(headStr);
		if (bytes != null && bytes.length > 0) {
			sb.append(byteToHexString(bytes[0], include0x));
			for (int i = 1; i < bytes.length; i++) {
				if (splitStr != null)
					sb.append(splitStr);
				sb.append(byteToHexString(bytes[i], include0x));
			}
		}

		if (tailStr != null)
			sb.append(tailStr);

		return sb.toString();
	}

	public static String byteToHexString(byte b) {
		return byteToHexString(b, false);
	}

	public static String byteToHexString(byte b, boolean include0x) {
		String s = Integer.toHexString(b & 0xFF);

		if ((b & 0xFF) < 16)
			s = "0" + s;

		if (include0x)
			s = "0x" + s;

		return s;
	}

	public static byte[] hexStringToBytes(String hexStr, String headStr,
										  String tailStr, String splitStr) {
		if (headStr != null && hexStr.startsWith(headStr)) {
			hexStr = hexStr.substring(headStr.length());
		}

		if (tailStr != null && hexStr.endsWith(tailStr)) {
			hexStr = hexStr.substring(0, hexStr.length() - tailStr.length());
		}

		if (splitStr != null) {
			hexStr = hexStr.replace(splitStr, "");
		}

		hexStr = hexStr.replace("0x", "");
		hexStr = hexStr.replace("0X", "");

		try {
			byte[] b = new byte[hexStr.length() / 2];
			for (int i = 0; i < b.length; i++) {
				int start = i * 2;
				b[i] = (byte) Integer.parseInt(
						hexStr.substring(start, start + 2), 16);
			}

			return b;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static byte hexStringToByte(String hexStr) {
		try {
			return (byte) Integer.parseInt(hexStr.substring(0, 2), 16);
		} catch (Exception e) {
		}

		return 0;
	}
	/**
	 * 把16进制字符串转换成字节数组
	 * @param hex
	 * @return
	 */
	public static byte[] hexStringToBytes(String hex) {
		int len = (hex.length() / 2);
		byte[] result = new byte[len];
		char[] achar = hex.toCharArray();
		for (int i = 0; i < len; i++) {
			int pos = i * 2;
			result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
		}
		return result;
	}
	private static byte toByte(char c) {
		byte b = (byte) "0123456789ABCDEF".indexOf(c);
		return b;
	}

	public static String getStringFromMap(Map<String, String[]> params,
										  String key) {
		String[] strings = params.get(key);
		if (null != strings && strings.length > 0) {
			return strings[0];
		}
		return null;
	}

	public static int stringToHash(String key) {
		// 数组大小一般取质数
		int arraySize = 11113;
		int hashCode = 0;
		for (int i = 0; i < key.length(); i++) {
			int letterValue = key.charAt(i) - 96;
			// 防止编码溢出，对每步结果都进行取模运算
			hashCode = ((hashCode << 5) + letterValue) % arraySize;
		}
		return hashCode;
	}
	/**
	 * 根据标识符找到开始位置
	 * @param bytes
	 * @param start 索引
	 * @param b  标识符
	 * @return
	 */
	public static int indexOfByteArray(byte[] bytes, int start, byte b) {
		assert (start >= 0 && start < bytes.length);
		for (int i = start; i < bytes.length; i++) {
			if (bytes[i] == b)
				return i;
		}

		return -1;
	}


	/**
	 * 根据标识符找到开始位置
	 * @param bytes
	 * @param start 索引
	 * @param searchBytes  标识符
	 * @return
	 */
	public static int indexOfByteArray(byte[] bytes, int start, byte[] searchBytes) {
		assert (start >= 0 && start < bytes.length);
		for (int i = start; i < bytes.length; i++)
		{
			if (bytes[i] == searchBytes[0])
			{
				if (searchBytes.length == 1) { return i; }
				boolean flag = true;
				for (int j = 1; j < searchBytes.length; j++)
				{
					if (bytes[i + j] != searchBytes[j])
					{
						flag = false;
						break;
					}
				}
				if (flag) { return i; }
			}
		}

		return -1;
	}

	public static byte[] bytesFromBytes(byte[] bytes, int start, int len) {
		byte[] b = new byte[len];
		System.arraycopy(bytes, start, b, 0, len);
		return b;
	}
	/**
	 *
	 * @param bytes
	 * @param start 索引
	 * @param startTAG 开始标识符
	 * @param endTAG 结束标识符
	 * @param includeTAG 是否包含标识符
	 * @return
	 */
	public static byte[] bytesFromBytes(byte[] bytes, int start, byte startTAG,
										byte endTAG, boolean includeTAG) {
		int startIndex = start;
		int endIndex = -1;

		// 查找数据起始位置
		if (startTAG == 0) {
			startIndex = start;
		} else {
			for (; startIndex < bytes.length; startIndex++) {
				if (bytes[startIndex] == startTAG) {
					if (!includeTAG)
						startIndex++;
					break;
				}
			}
		}

		if (startIndex >= bytes.length)
			return null;

		// 查找数据结束位置
		if (endTAG == 0) {
			endIndex = bytes.length - 1;
		} else {
			for (endIndex = startIndex; endIndex < bytes.length; endIndex++) {
				if (bytes[endIndex] == endTAG) {
					if (!includeTAG)
						endIndex--;
					break;
				}
			}
		}

		if (endIndex >= bytes.length)
			return null;

		int len = endIndex - startIndex + 1;
		return bytesFromBytes(bytes, startIndex, len);
	}

	public static double doubleNDots(double value, int ndot) {
		BigDecimal bigDecimal = new BigDecimal(value);
		return bigDecimal.setScale(ndot, BigDecimal.ROUND_HALF_UP)
				.doubleValue();
	}

	public static float floatNDots(float value, int ndot) {
		BigDecimal bigDecimal = new BigDecimal(value);
		return bigDecimal.setScale(ndot, BigDecimal.ROUND_HALF_UP).floatValue();
	}


	public static short bytesToShortLittle(byte[] src) {
		short value;
		value = (short) ((src[0] & 0xFF)
				| ((src[1] & 0xFF)<<8));
		return value;
	}


	public static byte[] shortToBytesLittle(short src) {
		return new byte[]{
				(byte) (src & 0xFF),
				(byte) ((src >> 8) & 0xFF)
		};
	}

	/**
	 * int转byte数组对象 ,小端
	 * @param src
	 * @return
	 */
	public static Byte[] intToBytesLittle(int src) {
		return new Byte[]{
				(byte) (src & 0xFF),
				(byte) ((src >> 8) & 0xFF),
				(byte) ((src >> 16) & 0xFF),
				(byte) ((src >> 24) & 0xFF)
		};
	}

	/**
	 * BytesTobytes
	 * @param oBytes
	 * @return
	 */
	public static byte[] toPrimitives(Byte[] oBytes) {
		byte[] bytes = new byte[oBytes.length];
		for (int i = 0; i < oBytes.length; i++) {
			bytes[i] = oBytes[i];
		}
		return bytes;

	}
	/**
	 *
	 * @param bytesPrim
	 * @return
	 */
	public static Byte[] toObjects(byte[] bytesPrim) {
		Byte[] bytes = new Byte[bytesPrim.length];
		int i = 0;
		for (byte b : bytesPrim)
			bytes[i++] = b;

		return bytes;
	}

	/**
	 * 向将bytes添加到另一个bytes结尾，并返回位置
	 * @param buff 目标数组
	 * @param pos 目标数组放置的起始位置
	 * @param lens 添加的长度
	 * @param dx 要添加的数组
	 * @return lens添加的长度
	 */
	public static int addToBuff(byte[] buff, int pos, int lens, byte[] dx) {
		System.arraycopy(dx, 0, buff, pos, lens);
		return lens;
	}

	/**
	 * 获得bytes的一段数据
	 * @param buff 原byte数组
	 * @param startPos 起始位置
	 * @param lenth 获取的长度
	 * @return 返回获得的byte数组
	 */
	public static byte[] getFromBuff(byte[] buff,int startPos,int lenth) {
		byte[] bytes = new byte[lenth];
		System.arraycopy(buff, startPos, bytes, 0, lenth);
		return bytes;
	}

	/**
	 * double转byte数组，小端模式
	 * @param d
	 * @return
	 */
	public static byte[] doubleToBytesLittle(double d){
		long l = Double.doubleToLongBits(d);
		byte b[] = new byte[8];
		b[7] = (byte)  (0xff & (l >> 56));
		b[6] = (byte)  (0xff & (l >> 48));
		b[5] = (byte)  (0xff & (l >> 40));
		b[4] = (byte)  (0xff & (l >> 32));
		b[3] = (byte)  (0xff & (l >> 24));
		b[2] = (byte)  (0xff & (l >> 16));
		b[1] = (byte)  (0xff & (l >> 8));
		b[0] = (byte)  (0xff & l);
		return b;
	}

	/**
	 * double转byte数组，大端模式
	 * @param d
	 * @return
	 */
	public static byte[] doubleToBytesBig(double d){
		long l = Double.doubleToLongBits(d);
		byte b[] = new byte[8];
		b[0] = (byte)  (0xff & (l >> 56));
		b[1] = (byte)  (0xff & (l >> 48));
		b[2] = (byte)  (0xff & (l >> 40));
		b[3] = (byte)  (0xff & (l >> 32));
		b[4] = (byte)  (0xff & (l >> 24));
		b[5] = (byte)  (0xff & (l >> 16));
		b[6] = (byte)  (0xff & (l >> 8));
		b[7] = (byte)  (0xff & l);
		return b;
	}

	/**
	 * byte数组转double
	 * @param bytes 8位byte数组
	 * @param littleEndian 是否是小端模式
	 * @return
	 */
	public static double bytesToDouble(byte[] bytes, boolean littleEndian) {
		ByteBuffer buffer = ByteBuffer.wrap(bytes,0,8);
		if(littleEndian){
			// ByteBuffer.order(ByteOrder) 方法指定字节序,即大小端模式(BIG_ENDIAN/LITTLE_ENDIAN)
			// ByteBuffer 默认为大端(BIG_ENDIAN)模式
			buffer.order(ByteOrder.LITTLE_ENDIAN);
		}
		long l = buffer.getLong();
		return Double.longBitsToDouble(l);
	}

	/**
	 * long转byte数组，小端模式
	 * @param l
	 * @return
	 */
	public static byte[] longToBytesLittle(long l) {
		byte b[] = new byte[8];
		b[7] = (byte)  (0xff & (l >> 56));
		b[6] = (byte)  (0xff & (l >> 48));
		b[5] = (byte)  (0xff & (l >> 40));
		b[4] = (byte)  (0xff & (l >> 32));
		b[3] = (byte)  (0xff & (l >> 24));
		b[2] = (byte)  (0xff & (l >> 16));
		b[1] = (byte)  (0xff & (l >> 8));
		b[0] = (byte)  (0xff & l);
		return b;
	}

	/**
	 * long转byte数组，大端模式
	 * @param l
	 * @return
	 */
	public static byte[] longToBytesBig(long l) {
		byte b[] = new byte[8];
		b[0] = (byte)  (0xff & (l >> 56));
		b[1] = (byte)  (0xff & (l >> 48));
		b[2] = (byte)  (0xff & (l >> 40));
		b[3] = (byte)  (0xff & (l >> 32));
		b[4] = (byte)  (0xff & (l >> 24));
		b[5] = (byte)  (0xff & (l >> 16));
		b[6] = (byte)  (0xff & (l >> 8));
		b[7] = (byte)  (0xff & l);
		return b;
	}

	/**
	 * byte数组转long
	 * @param bytes 8位的byte数组
	 * @param littleEndian 是否是小端模式
	 * @return
	 * @throws Exception
	 */
	public static long bytesToLong(byte[] bytes, boolean littleEndian) throws Exception {
		if(bytes.length != 8) {
			throw new Exception("参数错误，无法解析。");
		}
		ByteBuffer buffer = ByteBuffer.wrap(bytes,0,8);
		if(littleEndian){
			// ByteBuffer.order(ByteOrder) 方法指定字节序,即大小端模式(BIG_ENDIAN/LITTLE_ENDIAN)
			// ByteBuffer 默认为大端(BIG_ENDIAN)模式
			buffer.order(ByteOrder.LITTLE_ENDIAN);
		}
		return buffer.getLong();
	}



	/**
	 * int转byte数组 ,大端
	 * @param num
	 * @return
	 */
	public static byte[] intToBytesBig(int num){
		byte[] result = new byte[4];
		result[0] = (byte)((num >>> 24) & 0xff);
		result[1] = (byte)((num >>> 16)& 0xff );
		result[2] = (byte)((num >>> 8) & 0xff );
		result[3] = (byte)((num >>> 0) & 0xff );
		return result;
	}

	/**
	 * byte数组转int,小端
	 * @param bytes
	 * @return
	 */
	public static int bytesToIntLittle(byte[] bytes){
		int result = 0;
		if(bytes.length == 4){
			int a = (bytes[0] & 0xff) << 0;
			int b = (bytes[1] & 0xff) << 8;
			int c = (bytes[2] & 0xff) << 16;
			int d = (bytes[3] & 0xff) << 24;
			result = a | b | c | d;
		}
		return result;
	}

	/**
	 * byte数组转int,大端
	 * @param bytes
	 * @return
	 */
	public static int bytesToIntBig(byte[] bytes){
		int result = 0;
		if(bytes.length == 4){
			int a = (bytes[0] & 0xff) << 24;
			int b = (bytes[1] & 0xff) << 16;
			int c = (bytes[2] & 0xff) << 8;
			int d = (bytes[3] & 0xff) << 0;
			result = a | b | c | d;
		}
		return result;
	}


	/**
	 * byte数组转十六进制
	 * @param bytes
	 * @return
	 */
	public static String bytesToHex(byte[] bytes) {
		StringBuilder buf = new StringBuilder(bytes.length * 2);
		for(byte b : bytes) { // 使用String的format方法进行转换
			buf.append(String.format("%02x", new Integer(b & 0xff)));
		}

		return buf.toString();
	}

	/**
	 * 十六进制转byte数组
	 * @param str
	 * @return
	 */
	public static byte[] hexToBytes(String str) {
		if(str == null || str.trim().equals("")) {
			return new byte[0];
		}
		byte[] bytes = new byte[str.length() / 2];
		for(int i = 0; i < str.length() / 2; i++) {
			String subStr = str.substring(i * 2, i * 2 + 2);
			bytes[i] = (byte) Integer.parseInt(subStr, 16);
		}
		return bytes;
	}



	/**
	 * 把二个字节数组组合成一个字节数组
	 * @param byte_1
	 * @param byte_2
	 * @return
	 */
	public static byte[] byteMerger(byte[] byte_1, byte[] byte_2) {
		byte[] byte_3 = new byte[byte_1.length + byte_2.length];
		System.arraycopy(byte_1, 0, byte_3, 0, byte_1.length);
		System.arraycopy(byte_2, 0, byte_3, byte_1.length, byte_2.length);
		return byte_3;
	}


	/**
	 * 不足20个字节的数组补全为20个字节数组
	 *
	 * @return
	 */
	public static byte[] completionByte(int size, String data) {
		if (StrUtil.isEmpty(data)) // 判断是否有数据
			return null;

		byte[] totalByte = data.getBytes();// 获取字节数组

		int length = totalByte.length;
		if (length < size) {// 不足20字节后面补0x00到二十位
			byte[] completion = {0x00};
			for (int i = 0; i < size - length; i++) {
				totalByte = byteMerger(totalByte, completion);
			}
		}
		return totalByte;
	}


	public static byte[] short2byte(int n) {
		byte[] b = new byte[2];
		b[1] = ((byte) (n >> 8));
		b[0] = ((byte) n);
		return b;
	}


	/**
	 * 获取协议长度的字节数组
	 *
	 * @param byte_1
	 * @param data
	 * @return
	 */
	public static byte[] getCombinationByte(byte[] byte_1, String data) {
		try {
			byte[] dataByte = data.getBytes();
			byte[] dataLengthByte = short2byte(dataByte.length);
			byte[] byteMerger = byteMerger(byte_1, dataLengthByte);
			return byteMerger(byteMerger, dataByte);
		} catch (Exception e) {

			e.printStackTrace();
		}
		return null;

	}

	// Generic function to merge multiple arrays of same type in Java
	public static <T> T[] mergeArrays(T[]... arrays)
	{
		int finalLength = 0;
		for (T[] array : arrays) {
			finalLength += array.length;
		}

		T[] dest = null;
		int destPos = 0;

		for (T[] array : arrays)
		{
			if (dest == null) {
				dest = Arrays.copyOf(array, finalLength);
				destPos = array.length;
			} else {
				System.arraycopy(array, 0, dest, destPos, array.length);
				destPos += array.length;
			}
		}
		return dest;
	}

	public static int bitToInt(int bit0 ,int bit1){
		int result = 0;

		if (bit0 == 0) {
			result = result^0;
		}else{
			result = result^1;
		}
		if (bit1 == 0) {
			result = result^0;
		}else{
			result = result^2;
		}
		return result;
	}


	public static long DWORDBytesToLong(byte[] res) {
//		return (data[0] << 8 * 3) | (data[1] << 8 * 2)  | (data[2] << 8)| data[3];
		int firstByte = 0;
		int secondByte = 0;
		int thirdByte = 0;
		int fourthByte = 0;
		int index = 0;
		firstByte = (0x000000FF & ((int) res[index]));
		secondByte = (0x000000FF & ((int) res[index + 1]));
		thirdByte = (0x000000FF & ((int) res[index + 2]));
		fourthByte = (0x000000FF & ((int) res[index + 3]));
		index = index + 4;
		return ((long) (firstByte << 24 | secondByte << 16 | thirdByte << 8 | fourthByte)) & 0xFFFFFFFFL;
	}

	public static byte[] longToDWORD(long value) {
		byte[] data = new byte[4];

		for (int i = 0; i < data.length; i++) {
			data[data.length - i] = (byte) (value >> (8 * i));
		}
		return data;
	}


	public static int WORDBytesToInt(byte[] data) {
		DataInputStream dataInputStream = new DataInputStream(
				new ByteArrayInputStream(data));
		int a = 0;
		try {
			a = dataInputStream.readUnsignedShort();

		} catch (IOException e) {
			e.printStackTrace();
		}

		return a;
	}

	/**
	 * 将byte转换为一个长度为8的byte数组，数组每个值代表bit
	 */
	public static byte[] getBooleanArray(byte b) {
		byte[] array = new byte[8];
		for (int i = 7; i >= 0; i--) {
			array[i] = (byte)(b & 1);
			b = (byte) (b >> 1);
		}
		return array;
	}
	/**
	 * 把byte转为字符串的bit
	 */
	public static String byteToBit(byte b) {
		return ""
				+ (byte) ((b >> 7) & 0x1) + (byte) ((b >> 6) & 0x1)
				+ (byte) ((b >> 5) & 0x1) + (byte) ((b >> 4) & 0x1)
				+ (byte) ((b >> 3) & 0x1) + (byte) ((b >> 2) & 0x1)
				+ (byte) ((b >> 1) & 0x1) + (byte) ((b >> 0) & 0x1);
	}

	/**
	 * 字符串转化成为16进制字符串
	 * @param s
	 * @return
	 */
	public static String strTo16(String s) {
		String str = "";
		for (int i = 0; i < s.length(); i++) {
			int ch = (int) s.charAt(i);
			String s4 = Integer.toHexString(ch);
			str = str + s4;
		}
		return str;
	}


	/**
	 * 把多个字节数组组合成一个字节数组
	 * @param param
	 * @return
	 */
	public static byte[] byteMergerMore(byte[]... param) {
		int length = param.length;
		byte[] resultbytes = {};
		if(length < 2){
			resultbytes = param[0];
		}else {
			for (int i = 0; i < length; i++) {
				resultbytes = byteMerger(resultbytes,param[i]);
			}
		}
		return resultbytes;
	}


	public static byte[] byteMergerList(List<byte[]> list) {
		int length = list.size();
		byte[] resultbytes = {};
		if(length < 2){
			resultbytes = list.get(0);
		}else {
			for (int i = 0; i < length; i++) {
				resultbytes = byteMerger(resultbytes,list.get(i));
			}
		}
		return resultbytes;
	}

	/**
	 * int转WORD ,大端
	 * @param num
	 * @return
	 */
	public static byte[] intToWORDBig(int num){
		byte[] result = new byte[2];
		result[0] = (byte)((num >>> 8) & 0xff );
		result[1] = (byte)((num >>> 0) & 0xff );
		return result;
	}

	/**
	 * int转BYTE ,大端
	 * @param num
	 * @return
	 */
	public static byte[] intToBYTEBig(int num){
		byte[] result = new byte[1];
		result[0] = (byte)((num >>> 0) & 0xff );
		return result;
	}

}
