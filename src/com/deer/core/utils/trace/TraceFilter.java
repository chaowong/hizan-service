package com.deer.core.utils.trace;

public class TraceFilter {

	private int conf;

	public TraceFilter(int conf) {
		this.conf = conf;
	}

	public boolean isLoggable(int level) {
		int equal = level & conf;
		return equal == level;
	}

	public void setLevelConfig(int levelCode, boolean isOut) {
		if (isOut)
			conf = conf | levelCode;
		else
			conf = conf & ~levelCode;
	}
}
