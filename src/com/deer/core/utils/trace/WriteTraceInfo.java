package com.deer.core.utils.trace;

import java.io.BufferedWriter;
import java.io.PrintStream;
import java.security.AccessController;
import java.text.DateFormat;
import java.util.Date;
import sun.security.action.*;

class WriteTraceInfo implements Runnable {

	private static StringBuffer buffer;
	private static int CACHE_LENGTH;
	private static String lineSeparator = (String) AccessController.doPrivileged(new GetPropertyAction("line.separator"));
	private static Thread thread = null;
	private static String info[] = { "INFO", "WARNING!", "ERROR!", "DEBUG" };
	private static BufferedWriter fileOut = null;
	private static Config config;
	private static TraceOutMode modes[];
	private static final int CLASS_DEPTH = 5;

	private WriteTraceInfo() {
		
	}

	private static String generateMsg(String x, int level) {
		StringBuffer buf = new StringBuffer(100);
		if (config.isAppendTimeInfo())
			appendTimeInfo(buf);
		appendInfoName(buf, level);
		if (config.isAppendPosition())
			appendClassInfo(buf);
		buf.append(x);
		String rtMsg = buf.toString();
		buf = null;
		return rtMsg;
	}

	private static String generateMsg(char chs[], int level) {
		String x = String.valueOf(chs);
		StringBuffer buf = new StringBuffer(100);
		if (config.isAppendTimeInfo())
			appendTimeInfo(buf);
		appendInfoName(buf, level);
		if (config.isAppendPosition())
			appendClassInfo(buf);
		buf.append(x);
		String rtMsg = buf.toString();
		buf = null;
		return rtMsg;
	}

	private static String generateMsg(Exception exp, int level) {
		StringBuffer buf = new StringBuffer(100);
		if (config.isAppendTimeInfo())
			appendTimeInfo(buf);
		appendInfoName(buf, level);
		if (config.isAppendPosition())
			appendClassInfo(buf);
		buf.append("\n异常信息：\n");
		String x = exp.getMessage();
		String x2 = exp.getClass().getName();
		buf.append(lineSeparator).append(x2 != null ? x2 : "").append(" :").append(x != null ? x : "");
		StackTraceElement elem[] = exp.getStackTrace();
		for (int i = 0; i < elem.length; i++) {
			buf.append(lineSeparator + "\tat ");
			buf.append(elem[i]);
		}

		buf.append(lineSeparator);
		String rtMsg = buf.toString();
		buf = null;
		return rtMsg;
	}

	private static String generateMsg(Throwable exp, int level) {
		StringBuffer buf = new StringBuffer(100);
		if (config.isAppendTimeInfo())
			appendTimeInfo(buf);
		appendInfoName(buf, level);
		if (config.isAppendPosition())
			appendClassInfo(buf);
		buf.append("\n异常信息：\n");
		String x = exp.getMessage();
		String x2 = exp.getClass().getName();
		buf.append(lineSeparator).append(x2 != null ? x2 : "").append(" :").append(x != null ? x : "");
		StackTraceElement elem[] = exp.getStackTrace();
		for (int i = 0; i < elem.length; i++) {
			buf.append(lineSeparator + "\tat ");
			buf.append(elem[i]);
		}

		buf.append(lineSeparator);
		String rtMsg = buf.toString();
		buf = null;
		return rtMsg;
	}

	private static void writeInfo(String msg) {
		for (int i = 0; i < modes.length; i++) {
			TraceOutMode mode = modes[i];
			if (mode == null)
				continue;
			PrintStream ps = mode.getPrintStream();
			if (ps != null)
				ps.print(msg);
		}

	}

	private static void appendClassInfo(StringBuffer buf) {
		StackTraceElement traces[] = (new Throwable()).getStackTrace();
		buf.append("[");
		buf.append(traces[5].getClassName());
		buf.append(":");
		buf.append(traces[5].getMethodName());
		buf.append("]  ");
	}

	/**
	 * 添加 [时间信息]
	 * 
	 * @param buf
	 */
	private static void appendTimeInfo(StringBuffer buf) {
		buf.append("[");

		// 时间格式一:例 08-9-12 下午8:00
		// buf.append(DateFormat.getInstance().format(Calendar.getInstance().getTime()));

		// 时间格式二:
		// df.format(new Date()
		buf.append(DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM).format(new Date()).toString());
		buf.append("]:");
	}

	private static void appendInfoName(StringBuffer buf, int level) {
		int pos = convertLevelToPos(level);
		buf.append("[").append(info[pos]).append("]");
		buf.append("  ");
	}

	private static int convertLevelToPos(int levelCode) {
		int i = 0;
		int level = levelCode;
		if (level <= 0)
			return 1;
		while (level != 1) {
			level >>= 1;
			i++;
		}
		if (i > Trace.TRACE_COUNT)
			i = Trace.TRACE_COUNT;
		return i;
	}

	static void write(String x, int level) {
		if (modes == null) {
			return;
		} else {
			String msg = generateMsg(x, level);
			processMsg(msg);
			return;
		}
	}

	static void write(char buf[], int level) {
		if (modes == null) {
			return;
		} else {
			String msg = generateMsg(buf, level);
			processMsg(msg);
			return;
		}
	}

	static void writeln(String x, int level) {
		x = x + lineSeparator;
		if (modes == null) {
			return;
		} else {
			String msg = generateMsg(x, level);
			processMsg(msg);
			return;
		}
	}

	static void writeln(char buf[], int level) {
		String x = String.valueOf(buf);
		x = x + lineSeparator;
		if (modes == null) {
			return;
		} else {
			String msg = generateMsg(x, level);
			processMsg(msg);
			return;
		}
	}

	static void write(Exception x, int level) {
		if (modes == null) {
			return;
		} else {
			String msg = generateMsg(x, level);
			processMsg(msg);
			return;
		}
	}

	static void write(Throwable x, int level) {
		if (modes == null) {
			return;
		} else {
			String msg = generateMsg(x, level);
			processMsg(msg);
			return;
		}
	}

	private static void processMsg(String msg) {
		if (config.isEnableCache()) {
			if (thread == null) {
				thread = new Thread(new WriteTraceInfo());
				thread.start();
			}
			synchronized (buffer) {
				if (msg.length() >= CACHE_LENGTH - buffer.length())
					flush();
				buffer.append(msg);
			}
		} else {
			writeInfo(msg);
		}
	}

	static void flush() {
		synchronized (buffer) {
			writeInfo(buffer.toString());
			buffer.delete(0, buffer.length());
		}
	}

	public void run() {
		do {
			if (config.isEnableCache()) {
				flush();
			} else {
				flush();
				thread = null;
				return;
			}
			try {
				Thread.sleep(60000L);
			} catch (InterruptedException e) {
				Trace.printStackTrace(e, Trace.DEBUG);
			}
		} while (true);
	}

	static {
		buffer = new StringBuffer(CACHE_LENGTH);
		CACHE_LENGTH = 8192;
		config = Config.getInstance();
		modes = config.getTraceOutMode();
	}
}
