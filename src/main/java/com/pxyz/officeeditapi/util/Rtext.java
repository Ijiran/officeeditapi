package com.pxyz.officeeditapi.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具类
 * 
 * @author admin
 * 
 */
public class Rtext {

	/**
	 * 判断字符串是否为空
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str) {
		if (null == str || "".equals(str)) {
			return true;
		}
		return false;
	}

	/**
	 * 数组取字符串函数 防止下标越界
	 * 
	 * @param args
	 * @param index
	 * @return
	 */
	public static String toStringForArray(Object[] args, int index) {
		if (null == args || args.length <= 0) {
			return "";
		}
		int length = args.length;
		if (index >= length) {
			return "";
		}
		String str = toString(args[index]);
		return str;
	}

	/**
	 * Object 转String类型
	 * 
	 * @param obj
	 * @return
	 */
	public static String toString(Object obj) {
		return toString(obj, "");
	}

	/**
	 *  Object数组 转String数组类型
	 * @param args
	 * @return
	 */
	public static String[] toString(Object[] args) {
		String[] strs = null;
		if (null != args && args.length > 0) {
			strs = new String[args.length];
			for (int i = 0; i < strs.length; i++) {
				strs[i] = toString(args[i]);
			}
		}
		return strs;

	}

	/**
	 * Object 转String类型
	 * 
	 * @param obj
	 * @param def
	 *            默认值
	 * @return
	 */
	public static String toString(Object obj, String def) {
		if (obj==null) {
			return def;
		}
		String str = def;
		try {
			str = String.valueOf(obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}

	/**
	 * 获取UUID
	 * 
	 * @return
	 */
	public static String getUUID() {
		UUID uuid = UUID.randomUUID();
		String uuidstr = uuid.toString();
		uuidstr = uuidstr.replaceAll("-", "").toUpperCase();
		return uuidstr;
	}

	/**
	 * 对中文字符编码，防止乱码
	 * 
	 * @param plainText
	 * @return
	 */
	public static String encodeURL(String plainText) {
		String value = null;
		try {
			value = new String(plainText.getBytes("iso-8859-1"), "gb2312");
		} catch (UnsupportedEncodingException e) {
			value = "";
		}
		return value;
	}

	/**
	 * 将iso8859-1转为utf-8
	 * 
	 * @param plainText
	 *            原始字符串
	 * @param t
	 *            t为true发生异常时返回原始字符串否则返回空串
	 * @return
	 */
	public static String encodeURLToUTF8KT(String plainText, boolean t) {
		String value = null;
		if (isEmpty(plainText)) {
			return "";
		}
		try {
			value = new String(plainText.getBytes("iso-8859-1"), "utf-8");
		} catch (UnsupportedEncodingException e) {
			if (t) {
				value = plainText;
			} else {
				value = "";
			}
		}
		return value;
	}

	/**
	 * 对中文字符编码，防止乱码
	 * 
	 * @param plainText
	 * @return
	 */
	public static String encodeURLToUTF8(String plainText) {
		if (isEmpty(plainText)) {
			return null;
		}
		String value = null;
		try {
			value = new String(plainText.getBytes("iso-8859-1"), "utf-8");
		} catch (UnsupportedEncodingException e) {
			value = "";
		}
		return value;
	}

	/**
	 * 对字符串MD5加密
	 * 
	 * @param plainText
	 * @return
	 */
	public static String Md5(String plainText) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(plainText.getBytes());
			byte b[] = md.digest();
			int i;
			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			return buf.toString();// 32位的加密
		} catch (NoSuchAlgorithmException e) {
			return "";
		}
	}

	/**
	 * 将全角字符转换成半角字符
	 * 
	 * @param input
	 * @return
	 */
	public static String ToDBC(String input) {
		char c[] = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == '\u3000') {
				c[i] = ' ';
			} else if (c[i] > '\uFF00' && c[i] < '\uFF5F') {
				c[i] = (char) (c[i] - 65248);
			}
		}
		String returnString = new String(c);
		return returnString;
	}

	/**
	 * 将半角字符转换成全角字符
	 * @param input
	 * @return
	 */
	public static String ToSBC(String input) {
		char c[] = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == ' ') {
				c[i] = '\u3000';
			} else if (c[i] < '\177') {
				c[i] = (char) (c[i] + 65248);
			}
		}
		return new String(c);
	}

	/**
	 * 根据文件名获得文件类型
	 * @param filename
	 * @return real media flash flv
	 */
	public static String FileType(String filename) {
		String fileType = "other";
		if (filename != null && !"".equals(filename)) {
			if (type(filename, ".swf")) {
				fileType = "flash";
			} else if (type(filename, ".flv")) {
				fileType = "flv";
			} else if (type(filename, ".txt")) {
				fileType = "txt";
			} else if (type(filename, ".doc")) {
				fileType = "doc";
			} else if (type(filename, ".xls")) {
				fileType = "xls";
			} else {
				String[] imageExt = { ".jpg", ".jpeg", ".gif", ".png", ".bmp" };
				for (int i = 0; i < imageExt.length; i++) {
					if (type(filename, imageExt[i])) {
						fileType = "image";
						break;
					}
				}
				String[] rmExt = { ".rm", ".ra", ".ram", ".rmvb" };
				for (int i = 0; i < rmExt.length; i++) {
					if (type(filename, rmExt[i])) {
						fileType = "real";
						break;
					}
				}
				String[] mediaExt = { ".mp3", ".wav", ".wma", ".wmv", ".avi", ".asf", ".asx", ".mid", ".midi", ".mpg", ".mpeg" };
				for (int i = 0; i < mediaExt.length; i++) {
					if (type(filename, mediaExt[i])) {
						fileType = "media";
						break;
					}
				}
			}
		}
		return fileType;
	}

	/**
	 * 根据文件名获得文件类型
	 * @param filename
	 * @return real media flash flv
	 */
	public static String ImageType(String filename) {
		String fileType = "other";
		if (filename != null && !"".equals(filename)) {
			if (type(filename, ".swf")) {
				fileType = "flash";
			} else if (type(filename, ".flv")) {
				fileType = "flv";
			} else if (type(filename, ".txt")) {
				fileType = "txt";
			} else if (type(filename, ".doc")) {
				fileType = "doc";
			} else if (type(filename, ".xls")) {
				fileType = "xls";
			} else if (type(filename, ".jpg") || type(filename, ".jpeg")) {
				fileType = "jpg";
			} else if (type(filename, ".gif")) {
				fileType = "gif";
			} else if (type(filename, ".png")) {
				fileType = "png";
			} else if (type(filename, ".bmp")) {
				fileType = "bmp";
			} else {

				String[] rmExt = { ".rm", ".ra", ".ram", ".rmvb" };
				for (int i = 0; i < rmExt.length; i++) {
					if (type(filename, rmExt[i])) {
						fileType = "real";
						break;
					}
				}

				String[] mediaExt = { ".mp3", ".wav", ".wma", ".wmv", ".avi", ".asf", ".asx", ".mid", ".midi", ".mpg", ".mpeg" };
				for (int i = 0; i < mediaExt.length; i++) {
					if (type(filename, mediaExt[i])) {
						fileType = "media";
						break;
					}
				}
			}
		}
		return fileType;
	}

	public static boolean type(String filename, String str) {
		if (filename != null && filename.length() != 0 && filename.contains(".") && str != null && str.length() != 0) {
			int lastDotIndex = filename.lastIndexOf(".");
			int length = filename.length();
			String type = filename.toLowerCase()
					.substring(lastDotIndex, length);
			if (str.equals(type)) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	// 特殊字符转换
	public static String toHtml(String str) {
		String html = str;
		html = replace(html, "&", "&amp;");
		html = replace(html, "<", "&lt;");
		html = replace(html, ">", "&gt;");
		html = replace(html, "\"", "&quot;");
		html = replace(html, " ", "&nbsp;");
		return html;
	}

	// 特殊字符转换
	public static String toHtml(String str, boolean newline) {
		String html = toHtml(str);
		html = replace(str, "\n", "<br />");
		return html;
	}

	public static String replace(String source, String oldString,
			String newString) {
		StringBuffer output = new StringBuffer();
		int lengthOfSource = source.length(); // 源字符串长度
		int lengthOfOld = oldString.length(); // 老字符串长度
		int posStart = 0; // 开始搜索位置
		int pos; // 搜索到老字符串的位置
		while ((pos = source.indexOf(oldString, posStart)) >= 0) {
			output.append(source.substring(posStart, pos));
			output.append(newString);
			posStart = pos + lengthOfOld;
		}
		if (posStart < lengthOfSource) {
			output.append(source.substring(posStart));
		}
		return output.toString();
	}

	/**
	 * 
	 * 把秒数转换成 小时：分钟：秒
	 * @param seconds
	 * @return
	 */
	public static String secondToHHMISS(Long seconds) {
		int h = 0, m = 0, s = 0;
		int theSecond = seconds.intValue();
		h = (int) Math.floor(theSecond / 3600);
		if (h > 0) {
			m = (int) Math.floor((theSecond - h * 3600) / 60);
			if (m > 0) {
				s = theSecond - h * 3600 - m * 60;
			} else {
				s = theSecond - h * 3600;
			}
		} else {
			m = (int) Math.floor(theSecond / 60);
			if (m > 0) {
				s = theSecond - m * 60;
			} else {
				s = theSecond;
			}
		}
		return h + " 时" + m + " 分" + s + " 秒";

	}

	// 获取文件扩展名
	public static String getFileExtendedName(String inFileName) {
		try {
			int intPos = inFileName.lastIndexOf('.');
			return inFileName.substring(intPos);
		} catch (Exception e) {
		}
		return "";
	}

	/**
	 * 获取一个随机字符串
	 * 
	 * @return
	 */
	public static String getRandomNumber() {
		String ran = toString((int) (Math.random() * 9999));
		int n = 4 - ran.length();
		for (int i = 0; i < n; i++) {
			ran = 0 + ran;
		}
		return ran;
	}

	/**
	 * 判断字符串是否包含中文
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isContainChinese(String str) {
		Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
		Matcher m = p.matcher(str);
		if (m.find()) {
			return true;
		}
		return false;
	}

	public static String URLEncoderUTF8(String str) {
		return URLEncoder(str, "UTF-8");
	}

	public static String URLDecoderUTF8(String str) {
		return URLDecoder(str, "UTF-8");
	}

	public static String URLEncoder(String str, String encoder) {
		if (isEmpty(str)) {
			return "";
		}
		try {
			return java.net.URLEncoder.encode(str, encoder);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return "";
		}
	}

	public static String URLDecoder(String str, String encoder) {
		if (isEmpty(str)) {
			return "";
		}
		try {
			return java.net.URLDecoder.decode(str, encoder);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return "";
		}
	}

	public static Integer ToInteger(Object inValue, Integer inDefaultValue) {
		try {
			Double _double = Double.parseDouble(inValue.toString());
			return _double.intValue();
		} catch (Exception e) {
		}
		return inDefaultValue;
	}

	public static Boolean ToBoolean(Object inValue, Boolean inDefaultValue) {
		try {
			return Boolean.parseBoolean(Rtext.toString(inValue));
		} catch (Exception e) {
			return inDefaultValue;
		}
	}

	public static Boolean ToBoolean(Object inValue) {
		try {
			return Boolean.parseBoolean(Rtext.toString(inValue));
		} catch (Exception e) {
			return false;
		}
	}

	// from javahelper.java
	public static Double ToDouble(Object inValue, Double inDefaultValue) {
		try {
			return Double.parseDouble(inValue.toString());
		} catch (Exception e) {
		}
		return inDefaultValue;
	}

	public static Long ToLong(Object inValue, Long inDefaultValue) {
		try {
			return Long.parseLong(inValue.toString());
		} catch (Exception e) {
		}
		return inDefaultValue;
	}

	// from javahelper.java
	public static float ToFloat(Object inValue, float inDefaultValue) {
		try {
			Float _float = Float.parseFloat(inValue.toString());
			return _float.floatValue();
		} catch (Exception e) {
		}
		return inDefaultValue;
	}

	/**
	 * 检查关键字在字符串中出现的次数
	 * 
	 * @param str
	 * @param keyword
	 * @return
	 */
	public static int counStrNum(String str, String keyword) {
		int num = 0;
		int index = 0;
		while ((index = str.indexOf(keyword, index)) != -1) {
			index += keyword.length();
			num++;
		}
		return num;
	}

	/**
	 * 获取包含特定key的Map在List中的下标
	 * @param list
	 * @param key
	 * @param value
	 * @return
	 */
	public static int ListMapContainKey(List<Map<String,Object>> list, String key, String value) {
		for (int i = 0; i < list.size(); i++) {
			Map<String,Object> map = list.get(i);
			if (Rtext.toString(map.get(key)).equals(value)) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * 删除List中包含特定Key的map
	 * @param list
	 * @param key
	 * @param value
	 */
	public static void removeListMapKey(List<Map<String,Object>> list, String key, String value) {
		int index = ListMapContainKey(list, key, value);
		if (-1 != index) {
			list.remove(index);
		}
	}

	/**
	 * 生成空格数据
	 * 
	 * @param length 生成空格数
	 * @return
	 */
	public static String generateSpace(int length) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++) {
			sb.append(" ");
		}
		return sb.toString();
	}

	/**
	 * 字符串转切割成list
	 * 
	 * @param str 要分隔的字符串
	 * @param splitStr 分隔符
	 * @return
	 */
	public static List<String> strSplitList(String str, String splitStr) {
		List<String> list = null;
		if (!isEmpty(str)) {
			String[] strs = str.split(splitStr);
			list = new ArrayList<String>();
			for (String tempStr : strs) {
				list.add(tempStr);
			}
			strs = null;
		}
		return list;
	}
	
	
	/**
	 * 获取文件名
	 * @param filename
	 * @return
	 */
	public static String getFileName(String filename){
		String fName=filename.substring(0,filename.lastIndexOf("."));
		return fName;
	}
	
	/**
	 * 根据类型获取当前时间字符串
	 * 
	 * @param type
	 *            1:yyyy-MM-dd HH:mm:ss 
	 *            2:yyyy-MM-dd HH:mm 
	 *            3.yyyy-MM-dd HH
	 *            4:yyyy-MM-dd 
	 *            5:HH:mm:ss 
	 *            6:HH时mm分
	 *            7:yyyy
	 * @return
	 */
	public static String getCurrentDateStr(int type){
		SimpleDateFormat sdf=null;
		switch (type) {
		case 1:
			sdf=new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
			return sdf.format(new Date());
		case 2:
			sdf=new SimpleDateFormat("yyyy:MM:dd HH:mm");
			return sdf.format(new Date());
		case 3:
			sdf=new SimpleDateFormat("yyyy:MM:dd HH");
			return sdf.format(new Date());
		case 4:
			sdf=new SimpleDateFormat("yyyy:MM:dd");
			return sdf.format(new Date());
		case 5:
			sdf=new SimpleDateFormat("HH:mm:ss");
			return sdf.format(new Date());
		case 6:
			sdf=new SimpleDateFormat("HH时mm分");
			return sdf.format(new Date());
		case 7:
			sdf=new SimpleDateFormat("YYYY");
			return sdf.format(new Date());
		default:
			return "";
		}
	}
	
	/**
	 * 获取当前年份
	 * @return
	 */
	public static String getCurrentYear(){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy");
		return sdf.format(new Date());
	}
}
