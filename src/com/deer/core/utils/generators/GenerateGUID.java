package com.deer.core.utils.generators;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

import com.deer.core.utils.trace.Trace;

public class GenerateGUID extends Object {

	private String seedingString = "";
	private String rawGUID = "";
	private boolean bSecure = false;
	private static Random myRand;
	private static SecureRandom mySecureRand;

	private static String s_id;

	public static final int BeforeMD5 = 1;
	public static final int AfterMD5 = 2;
	public static final int FormatStringWithDash = 3;
	public static final int FormatStringWithoutDash = 4;

	static {
		mySecureRand = new SecureRandom();
		long secureInitializer = mySecureRand.nextLong();
		myRand = new Random(secureInitializer);
		try {
			s_id = InetAddress.getLocalHost().toString();
		} catch (UnknownHostException e) {
			Trace.printStackTrace(e);
		}
	}

	/*
	 * Default constructor. With no specification of security option, this
	 * constructor defaults to lower security, high performance.
	 */
	public GenerateGUID() {
	}

	/*
	 * Constructor with security option. Setting secure true enables each random
	 * number generated to be cryptographically strong. Secure false defaults to
	 * the standard Random function seeded with a single cryptographically
	 * strong random number.
	 */
	public GenerateGUID(boolean secure) {
		bSecure = secure;
	}

	/*
	 * Method to generate the random GUID
	 */
	private void getRandomGUID(boolean secure) {
		MessageDigest md5 = null;
		StringBuffer sbValueBeforeMD5 = new StringBuffer();

		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			System.out.println("Error: " + e);
		}

		try {
			long time = System.currentTimeMillis();
			long rand = 0;

			if (secure) {
				rand = mySecureRand.nextLong();
			} else {
				rand = myRand.nextLong();
			}

			// This StringBuffer can be a long as you need; the MD5
			// hash will always return 128 bits. You can change
			// the seed to include anything you want here.
			// You could even stream a file through the MD5 making
			// the odds of guessing it at least as great as that
			// of guessing the contents of the file!
			sbValueBeforeMD5.append(s_id);
			sbValueBeforeMD5.append(":");
			sbValueBeforeMD5.append(Long.toString(time));
			sbValueBeforeMD5.append(":");
			sbValueBeforeMD5.append(Long.toString(rand));

			seedingString = sbValueBeforeMD5.toString();
			md5.update(seedingString.getBytes());

			byte[] array = md5.digest();
			StringBuffer sb = new StringBuffer();
			for (int j = 0; j < array.length; ++j) {
				int b = array[j] & 0xFF;
				if (b < 0 * 10)
					sb.append('0');
				sb.append(Integer.toHexString(b));
			}

			rawGUID = sb.toString();

		} catch (Exception e) {
			System.out.println("Error:" + e);
		}
	}

	public String createNewGuid(int nFormatType, boolean secure) {
		getRandomGUID(secure);
		String sGuid = "";
		if (BeforeMD5 == nFormatType) {
			sGuid = this.seedingString;
		} else if (AfterMD5 == nFormatType) {
			sGuid = this.rawGUID;
		} else if (FormatStringWithoutDash == nFormatType) {
			sGuid = this.toString();
		} else {
			sGuid = this.toStringWithDash();
		}
		return sGuid;
	}

	public String createNewGuid(int nFormatType) {
		return this.createNewGuid(nFormatType, this.bSecure);
	}

	/*
	 * Convert to the standard format for GUID (Useful for SQL Server
	 * UniqueIdentifiers, etc.) Example: C2FEEEAC-CFCD-11D1-8B05-00600806D9B6
	 */
	public String toString() {
		String raw = rawGUID.toUpperCase();
		StringBuffer sb = new StringBuffer();
		sb.append(raw.substring(0, 8));
		// sb.append("-");
		sb.append(raw.substring(8, 12));
		// sb.append("-");
		sb.append(raw.substring(12, 16));
		// sb.append("-");
		sb.append(raw.substring(16, 20));
		// sb.append("-");
		sb.append(raw.substring(20));

		return sb.toString();
	}

	public String toStringWithDash() {
		String raw = rawGUID.toUpperCase();
		StringBuffer sb = new StringBuffer();
		sb.append(raw.substring(0, 8));
		sb.append("-");
		sb.append(raw.substring(8, 12));
		sb.append("-");
		sb.append(raw.substring(12, 16));
		sb.append("-");
		sb.append(raw.substring(16, 20));
		sb.append("-");
		sb.append(raw.substring(20));

		return sb.toString();
	}

	public String toStringWithoutDash() {
		String raw = rawGUID.toUpperCase();
		StringBuffer sb = new StringBuffer();
		sb.append(raw.substring(0, 8));
		// sb.append("-");
		sb.append(raw.substring(8, 12));
		// sb.append("-");
		sb.append(raw.substring(12, 16));
		// sb.append("-");
		sb.append(raw.substring(16, 20));
		// sb.append("-");
		sb.append(raw.substring(20));

		return sb.toString();
	}

	/**
	 * Use Exceple
	 * 
	 */
	public static void main(String args[]) {
		for (int i = 0; i < 100; i++) {
			GenerateGUID myGUID = new GenerateGUID();
			//System.out.println("Seeding String=" + myGUID.createNewGuid(GenerateGUID.BeforeMD5));// 编码元素
			//System.out.println("原生编码 rawGUID=" + myGUID.createNewGuid(GenerateGUID.AfterMD5));// 原生编码
			//System.out.println("RandomGUIDWithDash=" + myGUID.createNewGuid(GenerateGUID.FormatStringWithDash));
			System.out.println("RandomGUIDWithoutDash=" + myGUID.createNewGuid(GenerateGUID.FormatStringWithoutDash));
		}
	}
}