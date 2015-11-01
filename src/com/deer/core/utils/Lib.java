package com.deer.core.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

public final class Lib {

	public static final String EMPTY_STRING = "";
	public static final String SQL_FALSE = "0";
	public static final String SQL_TRUE = "1";
	public static final char HEX_CHARS[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
	public static final char BASE64_CHARS[] = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V',
			'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y',
			'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/', '=' };
	private static final int DAYS_IN_MONTH[] = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

	public static PrintWriter testLogger = null;

	public Lib() {
	}

	public static boolean isChinese(String s) {
		return s == null || s.equals("");
	}

	public static boolean isEmpty(String s) {
		return s == null || s.equals("");
	}

	public static boolean isBoolTrue(String s) {
		return s != null && (s.equals("1") || s.equalsIgnoreCase("T") || s.equalsIgnoreCase("TRUE"));
	}

	public static boolean isSymbol(String s) {
		if (s == null || s.length() == 0)
			return false;
		for (int i = 0; i < s.length(); i++)
			if (!Character.isLetterOrDigit(s.charAt(i)) && '_' != s.charAt(i))
				return false;

		return true;
	}

	public static boolean isIdentifier(String s) {
		if (s == null || s.length() == 0)
			return false;
		for (int i = 0; i < s.length(); i++)
			if (!Character.isLetterOrDigit(s.charAt(i)) && '_' != s.charAt(i))
				return false;

		return Character.isLetter(s.charAt(0)) || '_' == s.charAt(0);
	}

	public static boolean isNumberString(String s) {
		if (s == null || s.length() == 0)
			return false;
		for (int i = 0; i < s.length(); i++)
			if (!Character.isDigit(s.charAt(i)))
				return false;

		return true;
	}

	// 判断是否含有空格字符
	public static boolean hasSpaceChar(String s) {
		for (int i = 0; i < s.length(); i++)
			if (Character.isSpaceChar(s.charAt(i)))
				return true;

		return false;
	}

	// 将传入的字符转换为 iso8859_1
	public static String convInput(String s) {
		try {
			return new String(s.getBytes("ISO8859_1"));
		} catch (Exception exception) {

		}

		return s;
	}

	// 得到 双引号
	public static String quoteString(String s) {
		return '"' + s + '"';
	}

	// 得到单引号
	public static String quoteStringSingle(String s) {
		return "'" + s + "'";
	}

	public static String toFixedString(long l, int i) {
		NumberFormat numberformat = NumFormatManager.createIntegerFormat(i);
		return numberformat.format(l);
	}

	public static String toFixedString(long l, boolean flag) {
		NumberFormat numberformat = NumFormatManager.createNumberFormat(0, flag);
		return numberformat.format(l);
	}

	public static String replaceString(String s, String s1, String s2) {
		if (s2 == null)
			s2 = "";
		StringBuffer stringbuffer = new StringBuffer();
		int i = 0;
		int j = 0;
		do {
			j = s.indexOf(s1, i);
			if (j >= 0) {
				if (j > i)
					stringbuffer.append(s.substring(i, j));
				stringbuffer.append(s2);
				i = j + s1.length();
			}
		} while (j >= 0);
		if (i < s.length())
			stringbuffer.append(s.substring(i, s.length()));
		return stringbuffer.toString();
	}

	public static String toFixedString(double d, boolean flag) {
		DecimalFormat decimalformat = new DecimalFormat();
		decimalformat.setGroupingUsed(flag);
		return decimalformat.format(d);
	}

	public static String strExclude(String s, char c) {
		if (s == null)
			return null;
		char ac[] = new char[s.length()];
		int i = 0;
		for (int j = 0; j < s.length(); j++) {
			char c1 = s.charAt(j);
			if (c1 != c)
				ac[i++] = c1;
		}

		return new String(ac, 0, i);
	}

	public static boolean charIsInCharSet(char c, char ac[]) {
		for (int i = 0; i < ac.length; i++)
			if (c == ac[i])
				return true;

		return false;
	}

	
	public static String strExclude(String s, char ac[]) {
		if (s == null)
			return null;
		if (ac == null)
			return s;
		char ac1[] = new char[s.length()];
		int i = 0;
		for (int j = 0; j < s.length(); j++) {
			char c = s.charAt(j);
			if (!charIsInCharSet(c, ac))
				ac1[i++] = c;
		}

		return new String(ac1, 0, i);
	}

	public static String trimEx(String s) {
		StringBuffer stringbuffer = new StringBuffer(s);
		int i = stringbuffer.length() - 1;
		int j = s.length();
		if (j == 0)
			return "";
		for (; i >= 0; i--)
			if (stringbuffer.charAt(i) == '\n' || stringbuffer.charAt(i) == '\r')
				stringbuffer.deleteCharAt(i);

		return trim(stringbuffer.toString());
	}

	/**
	 * 去掉左右两侧所有空格
	 * 
	 * @param s
	 * @return
	 */
	public static String trim(String s) {
		if (s == null)
			return null;
		int i = -1;
		for (int j = 0; j < s.length() && (s.charAt(j) == ' ' || s.charAt(j) == '\u3000'); j++)
			i = j;

		i++;
		int k = s.length();
		for (int l = k - 1; l >= i && (s.charAt(l) == ' ' || s.charAt(l) == '\u3000'); l--)
			k = l;

		if (i == 0 && k == s.length())
			return s;
		if (i >= k)
			return "";
		else
			return s.substring(i, k);
	}

	/**
	 * 去掉左侧的空格
	 * 
	 * @param s
	 * @return
	 */
	public static String trimLeft(String s) {
		if (s == null)
			return null;
		int i = 0;
		for (int j = 0; j < s.length() && s.charAt(j) == ' '; j++)
			i = j;

		int k = s.length();
		if (i == 0)
			return s;
		if (i >= k)
			return "";
		else
			return s.substring(i, s.length());
	}

	/**
	 * 去掉右侧的空格
	 * 
	 * @param s
	 * @return
	 */
	public static String trimRight(String s) {
		if (s == null)
			return null;
		int i = s.length();
		for (int j = i - 1; j >= 0 && s.charAt(j) == ' '; j--)
			i = j;

		if (i == s.length())
			return s;
		if (i <= 0)
			return "";
		else
			return s.substring(0, i);
	}

	/**
	 * 比较两个字符串是否相等,如果相等返回0,如果s为null返回-1,如果s1为null,返回1;
	 * 
	 * @param s
	 * @param s1
	 * @return
	 */
	public static int compareString(String s, String s1) {
		if (s == s1)
			return 0;
		if (s == null)
			return -1;
		if (s1 == null)
			return 1;
		else
			return s.compareTo(s1);
	}

	/**
	 * 比较 两个数组,如果相等,返回0
	 * 
	 * @param ai
	 * @param ai1
	 * @return
	 */
	public static int compareArray(int ai[], int ai1[]) {
		if (ai == ai1)
			return 0;
		if (ai == null)
			return -1;
		if (ai1 == null)
			return 1;
		int i = ai.length - ai1.length;
		if (i != 0)
			return i;
		for (int j = 0; j < ai.length; j++) {
			i = ai[j] - ai1[j];
			if (i != 0)
				return i;
		}

		return i;
	}

	// ??????????反编译后出现问题,需要源码!!!
	public static String getLine(String s, int i) throws IOException {
		BufferedReader bufferedreader;
		if (s == null)
			return "";
		bufferedreader = new BufferedReader(new StringReader(s));
		String s1;
		for (s1 = null; i-- >= 0; s1 = bufferedreader.readLine())
			return s1;
		return s;
	}

	/**
	 * 
	 * @param s
	 *            : 要加密的字符串
	 * @param i
	 *            : 加密后的密码位数位 2i 位
	 * @return
	 */
	public static String encodePassword(String s, int i) {
		int j = 97;
		int k = 76;
		int l = s != null ? s.length() : 0;
		if (l > i)
			l = i;
		StringBuffer stringbuffer = new StringBuffer(2 * i);
		for (int i1 = 0; i1 < i; i1++) {
			int j1 = i1 < l && s != null ? ((int) (s.charAt(i1))) : 0;
			j1 = j++ + j1 ^ k++;
			int k1 = j1 / 26 + 73;
			int l1 = j1 % 26 + 65;
			if ((i1 & 1) == 1)
				k += k1 & 0xf;
			else
				k -= k1 & 0xf;
			stringbuffer.append((char) k1);
			stringbuffer.append((char) l1);
		}

		return new String(stringbuffer);
	}

	// 破译密码
	public static String decodePassword(String s) {
		if (s == null)
			return "";
		int i = 97;
		int j = 76;
		int k = s.length() / 2;
		StringBuffer stringbuffer = new StringBuffer();
		for (int l = 0; l < k; l++) {
			char c = s.charAt(2 * l);
			char c1 = s.charAt(2 * l + 1);
			int i1 = (c - 73) * 26 + (c1 - 65);
			i1 = (i1 ^ j++) - i++;
			if (i1 == 0)
				break;
			stringbuffer.append((char) i1);
			if ((l & 1) == 1)
				j += c & 0xf;
			else
				j -= c & 0xf;
		}

		return new String(stringbuffer);
	}

	public static int[] stringToColRow(String s) {
		int ai[] = new int[2];
		ai[0] = 0;
		ai[1] = 0;
		int i = 0;
		do {
			if (i >= s.length())
				break;
			char c = s.charAt(i);
			if (c >= 'A' && c <= 'Z') {
				ai[0] = ((ai[0] * 26 + (byte) c) - 65) + 1;
			} else {
				try {
					ai[1] = Integer.parseInt(s.substring(i));
				} catch (NumberFormatException numberformatexception) {
					ai[1] = 0;
				}
				break;
			}
			i++;
		} while (true);
		if (ai[0] > 0 && ai[1] > 0)
			return ai;
		else
			return null;
	}

	public static String colRowToString(int i, int j) {
		char ac[] = new char[64];
		int k;
		for (k = 0; i > 0; k++) {
			char c = (char) ((i % 26 + 65) - 1);
			i /= 26;
			if (c < 'A') {
				c = 'Z';
				i--;
			}
			ac[k] = c;
		}

		String s = "";
		for (int l = 0; l < k; l++)
			s = s + ac[k - l - 1];

		return s + Integer.toString(j);
	}

	public static String makeHexString(int i) {
		char ac[] = new char[8];
		int j = 7;
		for (; i != 0; i >>>= 4)
			ac[j--] = HEX_CHARS[i & 0xf];

		while (j >= 0)
			ac[j--] = HEX_CHARS[0];
		return new String(ac);
	}

	public static int parseHexValue(String s) {
		return (int) Long.parseLong(s, 16);
	}

	public static String bytesToHexString(byte abyte0[]) {
		if (abyte0 == null)
			return null;
		StringBuffer stringbuffer = new StringBuffer(abyte0.length * 2);
		for (int i = 0; i < abyte0.length; i++) {
			int j = abyte0[i];
			if (j < 0)
				j += 256;
			if (j < 16)
				stringbuffer.append('0');
			stringbuffer.append(Integer.toHexString(j));
		}

		return stringbuffer.toString().toUpperCase();
	}

	public static byte[] hexStringToBytes(String s) {
		if (s == null)
			return null;
		byte abyte0[] = new byte[s.length() / 2];
		for (int i = 0; i < abyte0.length; i++)
			abyte0[i] = Integer.decode("0x" + s.substring(2 * i, 2 * i + 2)).byteValue();

		return abyte0;
	}

	public static void listRevert(List list) {
		int i = list.size() - 1;
		for (int j = 0; j < list.size() / 2; j++) {
			Object obj = list.get(j);
			list.set(j, list.get(i - j));
			list.set(i - j, obj);
		}

	}

	public static void listMove(List list, int i, int j) {
		if (i > j) {
			Object obj = list.get(i);
			for (int k = i; k > j; k--)
				list.set(k, list.get(k - 1));

			list.set(j, obj);
		} else if (i < j) {
			Object obj1 = list.get(i);
			for (int l = i; l < j; l++)
				list.set(l, list.get(l + 1));

			list.set(j, obj1);
		}
	}

	public static int listFind(List list, Comparable comparable) {
		int i;
		int l;
		label0: {
			if (comparable == null)
				return list.size() <= 0 || list.get(0) != null ? -1 : 0;
			i = 0;
			int j = list.size() - 1;
			l = -1;
			int k;
			do {
				if (i > j)
					break label0;
				k = i + j >> 1;
				l = comparable.compareTo(list.get(k));
				if (l > 0) {
					i = k + 1;
					continue;
				}
				if (l >= 0)
					break;
				j = k - 1;
			} while (true);
			i = k;
		}
		return l != 0 ? -i - 1 : i;
	}

	public static int listSortAdd(List list, Object obj) {
		int i = listFind(list, (Comparable) obj);
		if (i < 0) {
			i = -i - 1;
			list.add(i, obj);
		}
		return i;
	}

	public static void listSort(List list) {
		if (list.size() > 1)
			listQuickSort(list, 0, list.size() - 1);
	}

	protected static void listQuickSort(List list, int i, int j) {
		int k;
		do {
			k = i;
			int l = j;
			do {
				Comparable comparable;
				for (comparable = (Comparable) list.get(i + j >> 1); comparable.compareTo(list.get(k)) > 0; k++)
					;
				for (; comparable.compareTo(list.get(l)) < 0; l--)
					;
				if (k <= l) {
					Object obj = list.get(k);
					list.set(k, list.get(l));
					list.set(l, obj);
					k++;
					l--;
				}
			} while (k <= l);
			if (i < l)
				listQuickSort(list, i, l);
			i = k;
		} while (k < j);
	}

	public static byte[] stringToBytes(String s) {
		return s != null ? s.getBytes() : null;
	}

	public static String bytesToString(byte abyte0[]) {
		return abyte0 != null ? new String(abyte0) : null;
	}

	// 判断 ：是否是闰年
	public static boolean isLeapYear(int i) {
		return i % 4 == 0 && (i % 100 != 0 || i % 400 == 0);
	}

	public static int daysPerMonth(int i, int j) {
		if (j == 2 && isLeapYear(i))
			return DAYS_IN_MONTH[j] + 1;
		else
			return DAYS_IN_MONTH[j];
	}

	public static Calendar dayTermToDate(int i, int j) {
		GregorianCalendar gregoriancalendar = new GregorianCalendar();
		gregoriancalendar.setTime(new Date(0L));
		gregoriancalendar.set(i, 1, 1);
		if (j <= 0)
			j = 1;
		int k = 1;
		do {
			if (k > 12)
				break;
			if (j <= daysPerMonth(i, k)) {
				gregoriancalendar.set(i, k, j);
				break;
			}
			j -= daysPerMonth(i, k);
			k++;
		} while (true);
		return gregoriancalendar;
	}

	// 获取文件名，且将原先文件名中的这些符号过滤掉
	public static String getFileName(String s) {
		String s1 = StringUtil.replace(s, "/", "");
		s1 = StringUtil.replace(s1, "\\", "");
		s1 = StringUtil.replace(s1, ":", "");
		s1 = StringUtil.replace(s1, "\"", "");
		s1 = StringUtil.replace(s1, ">", "");
		s1 = StringUtil.replace(s1, "<", "");
		s1 = StringUtil.replace(s1, "|", "");
		s1 = StringUtil.replace(s1, "?", "");
		s1 = StringUtil.replace(s1, "*", "");
		return s1;
	}

	// 将阿拉伯数字转化为大写数字
	public static String getChinaNumberTitle(int i) {
		switch (i) {
		case 0: // '\0'
			return "零";

		case 1: // '\001'
			return "一";

		case 2: // '\002'
			return "二";

		case 3: // '\003'
			return "三";

		case 4: // '\004'
			return "四";

		case 5: // '\005'
			return "五";

		case 6: // '\006'
			return "六";

		case 7: // '\007'
			return "七";

		case 8: // '\b'
			return "八";

		case 9: // '\t'
			return "九";

		case 10: // '\n'
			return "十";
		}
		return "一";
	}

	/**
	 * 获得繁体中文数字
	 * 
	 * @param i
	 * @return
	 */
	public static String getChinaTraditionalNumberTitle(int i) {
		switch (i) {
		case 0: // '\0'
			return "零";

		case 1: // '\001'
			return "壹";

		case 2: // '\002'
			return "贰";

		case 3: // '\003'
			return "叁";

		case 4: // '\004'
			return "肆";

		case 5: // '\005'
			return "伍";

		case 6: // '\006'
			return "陆";

		case 7: // '\007'
			return "柒";

		case 8: // '\b'
			return "捌";

		case 9: // '\t'
			return "玖";

		case 10: // '\n'
			return "拾";
		}
		return "壹";
	}

	/**
	 * 生成随机密码
	 * 
	 * @param pwd_len
	 *            生成的密码的总长度
	 * @return 密码的字符串
	 */
	public static String generateRandomNum(int pwd_len) {
		// 35是因为数组是从0开始的，26个字母+10个数字
		final int maxNum = 36;
		int i; // 生成的随机数
		int count = 0; // 生成的密码的长度
		char[] str = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
		StringBuffer pwd = new StringBuffer("");
		Random r = new Random();
		while (count < pwd_len) {
			// 生成随机数，取绝对值，防止生成负数，
			i = Math.abs(r.nextInt(maxNum)); // 生成的数最大为36-1
			if (i >= 0 && i < str.length) {
				pwd.append(str[i]);
				count++;
			}
		}
		return pwd.toString();
	}

	public static List removeDuplicateWithOrder(List list) {
		Set set = new HashSet();
		List newList = new ArrayList();
		for (Iterator iter = list.iterator(); iter.hasNext();) {
			Object element = iter.next();
			if (set.add(element))
				newList.add(element);
		}
		return newList;
	}


	//四舍五入
	public static BigDecimal round(float value ,int digits){
		return new BigDecimal(value).setScale(digits, BigDecimal.ROUND_HALF_UP) ;
	}
	//四舍五入 取整
	public static int round(float value){
		return  round(value,0).intValue();
	}
	//取整
	public static int floor(float value){
		return (int)Math.floor(value);
	}
	//凑整
	public static int ceil(float value){
		return (int)Math.ceil(value);
	}
	
	public static void main(String args[]) {
		System.out.println(round((float) 23223.447,0));
		System.out.println(ceil((float) 23225.74447));
	}
	
	/**从文件路径中获取文件名
	 * @return
	 */
	public static String getFilenameFromPath(String filePath){
		
		 //      举例：  
        //String fName =" G:\\Java_Source\\navigation_tigra_menu\\demo1\\img\\lev1_arrow.gif ";  
  
//      方法一：  
  
        File tempFile =new File( filePath.trim());  
  
        String fileName1 = tempFile.getName();  
          
        System.out.println("fileName = " + fileName1);  
  
//      方法二：  
  
        String fName = filePath.trim();  
  
        String fileName2 = fName.substring(fName.lastIndexOf("/")+1);  
        //或者  
        //String fileName2 = fName.substring(fName.lastIndexOf("\\")+1);  
          
        System.out.println("fileName = " + fileName2);  
  
//      方法三：  
  
        String fName3 = filePath.trim();  
  
        String temp[] = fName3.split("\\\\"); /**split里面必须是正则表达式，"\\"的作用是对字符串转义*/  
  
        String fileName3 = temp[temp.length-1];  
          
        System.out.println("fileName = " + fileName3);  
  
        return fileName1;//or fileName2 or fileName3
    }  

}