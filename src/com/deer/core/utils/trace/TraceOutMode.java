package com.deer.core.utils.trace;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

public class TraceOutMode {

	private static int TYPE_CONSOLE;
	private static int TYPE_FILE;
	private static TraceOutMode consoleOut;
	private static TraceOutMode fileOut;
	private PrintStream printStream;
	private String filePath;

	private TraceOutMode(int type) {
		printStream = null;
		if (TYPE_CONSOLE == type)
			printStream = System.out;
		else if (TYPE_FILE != type)
			;
	}

	public static TraceOutMode CONSOLE_OUT_MODE() {
		return consoleOut;
	}

	public static TraceOutMode FILE_OUT_MODE() {
		return fileOut;
	}

	public PrintStream getPrintStream() {
		return printStream;
	}

	public void setFilePath(String filePath) {
		if (this != FILE_OUT_MODE())
			return;
		fileOut.closePrintStream();
		try {
			fileOut.createPrintStream(filePath);
		} catch (Exception e) {
			Trace.println("创建控制台信息输出文件失败!请检查设置的文件路径。", Trace.ERROR);
		}
	}

	private boolean makeFile(String path) {
		File file = new File(path);
		if (file.isDirectory()) {
			Trace.println("设置的日志文件路径是文件夹路径，请重新设置文件路径。", Trace.ERROR);
			return false;
		}
		if (file.exists())
			if (!file.canWrite()) {
				Trace.println("没有对日志文件的写权限。", Trace.ERROR);
				return false;
			} else {
				return true;
			}
		String absPath = file.getAbsolutePath();
		if (absPath.lastIndexOf(File.separator) > 0) {
			String dirPath = absPath.substring(0, absPath.lastIndexOf(File.separator));
			File dir = new File(dirPath);
			if (!dir.exists() && !dir.mkdirs()) {
				Trace.println("创建日志文件目录失败。", Trace.ERROR);
				return false;
			}
		}
		try {
			file.createNewFile();
			Trace.println("成功创建日志文件：" + file.getAbsolutePath(), Trace.INFO);
		} catch (IOException e) {
			Trace.printStackTrace(e);
			Trace.println("创建日志文件失败，请检查日志文件路径", Trace.ERROR);
			return false;
		}
		return true;
	}

	private void createPrintStream(String path) throws Exception {
		filePath = path;
		if (filePath == null)
			return;
		if (makeFile(filePath)) {
			File file = new File(filePath);
			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream(file, true);
			} catch (FileNotFoundException e) {
				Trace.println("设置的文件不存在，或者是设置了文件夹路径。");
			}
			if (printStream != null)
				printStream.close();
			printStream = new PrintStream(fos);
		}
	}

	public void closePrintStream() {
		if (printStream != null)
			printStream.close();
	}

	static {
		TYPE_CONSOLE = 1;
		TYPE_FILE = 3;
		consoleOut = new TraceOutMode(TYPE_CONSOLE);
		fileOut = new TraceOutMode(TYPE_FILE);
	}
}
