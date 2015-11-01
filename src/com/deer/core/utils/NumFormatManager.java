package com.deer.core.utils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Stack;

public class NumFormatManager {

	public NumFormatManager() {
	}

	public static NumberFormat createIntegerFormat(int i) {
		NumberFormat numberformat = null;
		Integer integer = getInt(i);
		synchronized (integerFormats) {
			Stack stack = (Stack) integerFormats.get(integer);
			if (stack == null) {
				stack = new Stack();
				integerFormats.put(integer, stack);
			}
			if (!stack.isEmpty()) {
				numberformat = (NumberFormat) stack.pop();
			} else {
				numberformat = NumberFormat.getInstance();
				numberformat.setMinimumFractionDigits(0);
				numberformat.setMaximumFractionDigits(0);
				numberformat.setMinimumIntegerDigits(i);
				numberformat.setGroupingUsed(false);
			}
		}
		return numberformat;
	}

	public static void recycleIntegerFormat(int i, NumberFormat numberformat) {
		Integer integer = getInt(i);
		synchronized (integerFormats) {
			Stack stack = (Stack) integerFormats.get(integer);
			if (stack != null)
				stack.push(numberformat);
		}
	}

	public static NumberFormat createNumberFormat(int i, boolean flag) {
		return createNumberFormat(i, i, flag);
	}

	public static NumberFormat createNumberFormat(int i, int j, boolean flag) {
		int k = flag ? 1 : 0;
		k = j << 16 | i << 8 | k;
		Integer integer = new Integer(k);
		NumberFormat numberformat = null;
		synchronized (numberFormats) {
			Stack stack = (Stack) numberFormats.get(integer);
			if (stack == null) {
				stack = new Stack();
				numberFormats.put(integer, stack);
			}
			if (!stack.isEmpty()) {
				numberformat = (NumberFormat) stack.pop();
			} else {
				numberformat = NumberFormat.getInstance();
				numberformat.setMaximumFractionDigits(j);
				numberformat.setMinimumFractionDigits(i);
				numberformat.setMinimumIntegerDigits(1);
				numberformat.setGroupingUsed(flag);
			}
		}
		return numberformat;
	}

	public static void recycleNumberFormat(int i, boolean flag, NumberFormat numberformat) {
		recycleNumberFormat(i, i, flag, numberformat);
	}

	public static void recycleNumberFormat(int i, int j, boolean flag, NumberFormat numberformat) {
		int k = flag ? 1 : 0;
		k = j << 16 | i << 8 | k;
		Integer integer = new Integer(k);
		synchronized (numberFormats) {
			Stack stack = (Stack) numberFormats.get(integer);
			if (stack != null)
				stack.push(numberformat);
		}
	}

	public static NumberFormat createNumberFormat(String s) {
		Object obj = null;
		synchronized (patternFormats) {
			Stack stack = (Stack) patternFormats.get(s);
			if (stack == null) {
				stack = new Stack();
				patternFormats.put(s, stack);
			}
			if (!stack.isEmpty())
				obj = (NumberFormat) stack.pop();
			else
				obj = new DecimalFormat(s);
		}
		return ((NumberFormat) (obj));
	}

	public static void recycleNumberFormat(String s, NumberFormat numberformat) {
		synchronized (patternFormats) {
			Stack stack = (Stack) patternFormats.get(s);
			if (stack != null)
				stack.push(numberformat);
		}
	}

	public static final Integer getInt(int i) {
		return i >= INT_VALUES.length ? new Integer(i) : INT_VALUES[i];
	}

	public static void main(String args[]) {
		// for(int i = 0; i < 2000; i++)
		// {
		// NumberFormatManagerTest1 numberformatmanagertest1 = new
		// NumberFormatManagerTest1();
		// numberformatmanagertest1.start();
		// }

	}

	private static HashMap integerFormats = new HashMap();
	private static HashMap numberFormats = new HashMap();
	private static HashMap patternFormats = new HashMap();
	private static final Integer INT_VALUES[] = { new Integer(0), new Integer(1), new Integer(2), new Integer(3), new Integer(4), new Integer(5), new Integer(6), new Integer(7), new Integer(8), new Integer(9), new Integer(10), new Integer(11), new Integer(12), new Integer(13), new Integer(14),
			new Integer(15), new Integer(16), new Integer(17), new Integer(18), new Integer(19), new Integer(20), new Integer(21), new Integer(22), new Integer(23), new Integer(24), new Integer(25), new Integer(26), new Integer(27), new Integer(28), new Integer(29), new Integer(30),
			new Integer(31), new Integer(32), new Integer(33), new Integer(34), new Integer(35), new Integer(36), new Integer(37), new Integer(38), new Integer(39) };

}