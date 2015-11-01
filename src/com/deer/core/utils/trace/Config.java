package com.deer.core.utils.trace;

import com.deer.core.utils.Lib;

public class Config {

	private static boolean consoleMode = true;
	private static boolean fileMode = false;
	private static String filePath;
	private static boolean enableCache = false;
	private static boolean appendTimeInfo = true;
	private static boolean appendPosition = true;
	private TraceOutMode modes[] = { TraceOutMode.CONSOLE_OUT_MODE(), null };
	private static Config config = null;

	private Config() {
	}

	public static Config getInstance() {
		if (config == null) {
			config = new Config();
			Trace.getFilter().setLevelConfig(Trace.INFO, true);
			Trace.getFilter().setLevelConfig(Trace.WARNING, true);
			Trace.getFilter().setLevelConfig(Trace.ERROR, true);
			Trace.getFilter().setLevelConfig(Trace.DEBUG, true);
			consoleMode = true;
			fileMode = false;
			filePath = "";
			enableCache = false;
			appendTimeInfo = true;
			appendPosition = true;
			if (consoleMode)
				config.modes[0] = TraceOutMode.CONSOLE_OUT_MODE();
			else
				config.modes[0] = null;
			if (fileMode)
				config.modes[1] = TraceOutMode.FILE_OUT_MODE();
			else
				config.modes[1] = null;
		}
		return config;
	}

	public boolean isAppendPosition() {
		return appendPosition;
	}

	public void setAppendPosition(boolean appendPosition) {
		Config.appendPosition = appendPosition;
	}

	public boolean isAppendTimeInfo() {
		return appendTimeInfo;
	}

	public void setAppendTimeInfo(boolean appendTimeInfo) {
		Config.appendTimeInfo = appendTimeInfo;
	}

	public boolean isConsoleMode() {
		return consoleMode;
	}

	public void setConsoleMode(boolean consoleMode) {
		if (Config.consoleMode == consoleMode)
			return;
		if (consoleMode)
			modes[0] = TraceOutMode.CONSOLE_OUT_MODE();
		else
			modes[0] = null;
		Config.consoleMode = consoleMode;
	}

	public boolean isEnableCache() {
		return enableCache;
	}

	public void setEnableCache(boolean enableCache) {
		Config.enableCache = enableCache;
	}

	public boolean isFileMode() {
		return fileMode;
	}

	public void setFileMode(boolean fileMode) {
		if (Config.fileMode == fileMode)
			return;
		if (fileMode) {
			if (Lib.isEmpty(filePath)) {
				Trace.println("可能设置了文件路径为空，如果已经设置了文件路径，可忽略此信息。");
			} else {
				TraceOutMode.FILE_OUT_MODE().setFilePath(filePath);
				modes[1] = TraceOutMode.FILE_OUT_MODE();
			}
		} else {
			modes[1] = null;
		}
		Config.fileMode = fileMode;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		if (fileMode)
			if (Lib.isEmpty(filePath))
				Trace.println("控制台信息输出文件路径不能设置为空...");
			else
				TraceOutMode.FILE_OUT_MODE().setFilePath(filePath);
		Config.filePath = filePath;
	}

	TraceOutMode[] getTraceOutMode() {
		return modes;
	}

	public void setLevelConfig(int levelCode, boolean isOut) {
		Trace.getFilter().setLevelConfig(levelCode, isOut);
	}

}
