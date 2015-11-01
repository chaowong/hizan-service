package com.deer.core.utils.trace;

public class Trace {

	private static int ALLOUT;
	private static TraceFilter filter;
	public static int INFO;
	public static int WARNING;
	public static int ERROR = 4;
	public static int DEBUG = 8;
	static int TRACE_COUNT = 4;
	private static int DEFAULT_EXP;
	private static int DEFAULT_MSG;

	public Trace() {
	}

	private static void write(String x, int level) {
		if (filter.isLoggable(level))
			WriteTraceInfo.write(x, level);
	}

	private static void write(char buf[], int level) {
		if (filter.isLoggable(level))
			WriteTraceInfo.write(buf, level);
	}

	private static void write(Exception e, int level) {
		if (filter.isLoggable(level))
			WriteTraceInfo.write(e, level);
	}

	private static void write(Throwable e, int level) {
		if (filter.isLoggable(level))
			WriteTraceInfo.write(e, level);
	}

	private static void writeln(String x, int level) {
		if (filter.isLoggable(level))
			WriteTraceInfo.writeln(x, level);
	}

	private static void writeln(char buf[], int level) {
		if (filter.isLoggable(level))
			WriteTraceInfo.writeln(buf, level);
	}

	public static void print(boolean x) {
		write(x ? "true" : "false", DEFAULT_MSG);
	}

	public static void print(char x) {
		write(String.valueOf(x), DEFAULT_MSG);
	}

	public static void print(int x) {
		write(String.valueOf(x), DEFAULT_MSG);
	}

	public static void print(long x) {
		write(String.valueOf(x), DEFAULT_MSG);
	}

	public static void print(float x) {
		write(String.valueOf(x), DEFAULT_MSG);
	}

	public static void print(double x) {
		write(String.valueOf(x), DEFAULT_MSG);
	}

	public static void print(char x[]) {
		write(x, DEFAULT_MSG);
	}

	public static void print(String x) {
		write(x, DEFAULT_MSG);
	}

	public static void print(Object x) {
		write(String.valueOf(x), DEFAULT_MSG);
	}

	public static void println(boolean x) {
		writeln(x ? "true" : "false", DEFAULT_MSG);
	}

	public static void println(char x) {
		writeln(String.valueOf(x), DEFAULT_MSG);
	}

	public static void println(int x) {
		writeln(String.valueOf(x), DEFAULT_MSG);
	}

	public static void println(long x) {
		writeln(String.valueOf(x), DEFAULT_MSG);
	}

	public static void println(float x) {
		writeln(String.valueOf(x), DEFAULT_MSG);
	}

	public static void println(double x) {
		writeln(String.valueOf(x), DEFAULT_MSG);
	}

	public static void println(char x[]) {
		writeln(x, DEFAULT_MSG);
	}

	public static void println(String x) {
		writeln(x, DEFAULT_MSG);
	}

	public static void println(Object x) {
		writeln(String.valueOf(x), DEFAULT_MSG);
	}

	public static void print(boolean x, int level) {
		write(x ? "true" : "false", level);
	}

	public static void print(char x, int level) {
		write(String.valueOf(x), level);
	}

	public static void print(int x, int level) {
		write(String.valueOf(x), level);
	}

	public static void print(long x, int level) {
		write(String.valueOf(x), level);
	}

	public static void print(float x, int level) {
		write(String.valueOf(x), level);
	}

	public static void print(double x, int level) {
		write(String.valueOf(x), level);
	}

	public static void print(char x[], int level) {
		write(x, level);
	}

	public static void print(String x, int level) {
		write(x, level);
	}

	public static void print(Object x, int level) {
		write(String.valueOf(x), level);
	}

	public static void println() {
		writeln("", INFO);
	}

	public static void println(boolean x, int level) {
		writeln(x ? "true" : "false", level);
	}

	public static void println(char x, int level) {
		writeln(String.valueOf(x), level);
	}

	public static void println(int x, int level) {
		writeln(String.valueOf(x), level);
	}

	public static void println(long x, int level) {
		writeln(String.valueOf(x), level);
	}

	public static void println(float x, int level) {
		writeln(String.valueOf(x), level);
	}

	public static void println(double x, int level) {
		writeln(String.valueOf(x), level);
	}

	public static void println(char x[], int level) {
		writeln(x, level);
	}

	public static void println(String x, int level) {
		writeln(x, level);
	}

	public static void println(Object x, int level) {
		writeln(String.valueOf(x), level);
	}

	public static void printStackTrace(Exception e) {
		write(e, DEFAULT_EXP);
	}

	public static void printStackTrace(Exception e, int level) {
		write(e, level);
	}

	public static void printStackTrace(Throwable e) {
		write(e, DEFAULT_EXP);
	}

	public static void printStackTrace(Throwable e, int level) {
		write(e, level);
	}

	public static void setFilter(TraceFilter filter) {
		Trace.filter = filter;
	}

	public static TraceFilter getFilter() {
		return filter;
	}

	static {
		ALLOUT = 15;
		filter = new TraceFilter(ALLOUT);
		INFO = 1;
		WARNING = 2;
		DEFAULT_EXP = WARNING;
		DEFAULT_MSG = INFO;
	}
}
