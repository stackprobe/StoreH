package toolkitAndUtilities;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class SCommon {

	public static void ae(IThrowableAction routine) {
		try {
			routine.run();
		}
		catch (Throwable e) {
			throw new Error(e);
		}
	}

	public static <T> T re(IThrowableFunction<T> routine) {
		try {
			return routine.run();
		}
		catch (Throwable e) {
			throw new Error(e);
		}
	}

	public static List<File> getFiles(File targetDirectory, boolean allDirectories) {
		return getFilesAndDirectories(targetDirectory, allDirectories).stream()
				.filter(file -> file.isFile())
				.collect(Collectors.toList());
	}

	public static List<File> getDirectories(File targetDirectory, boolean allDirectories) {
		return getFilesAndDirectories(targetDirectory, allDirectories).stream()
				.filter(file -> file.isDirectory())
				.collect(Collectors.toList());
	}

	public static List<File> getFilesAndDirectories(File targetDirectory, boolean allDirectories) {
		if (targetDirectory == null ||
				!targetDirectory.isDirectory()) {
			throw new Error();
		}

		_files = new ArrayList<File>();
		_allDirectories = allDirectories;

		collectFilesAndDirectories(targetDirectory);

		_files.sort((a, b) -> a.getAbsolutePath().compareToIgnoreCase(b.getAbsolutePath()));

		List<File> ret = _files;

		_files = null;
		_allDirectories = false;

		return ret;
	}

	private static List<File> _files;
	private static boolean _allDirectories;

	private static void collectFilesAndDirectories(File targetDirectory) {
		for (File file : targetDirectory.listFiles()) {
			_files.add(file);

			if (_allDirectories && file.isDirectory()) {
				collectFilesAndDirectories(file);
			}
		}
	}

	// ****
	// **** PRIMITIVE[] -> List<PRIMITIVE> ここから
	// ****

	public static List<Boolean> toList(boolean[] arr) {
		List<Boolean> list = new ArrayList<Boolean>(arr.length);

		for (int index = 0; index < arr.length; index++) {
			list.add(arr[index]);
		}
		return list;
	}

	public static List<Byte> toList(byte[] arr) {
		List<Byte> list = new ArrayList<Byte>(arr.length);

		for (int index = 0; index < arr.length; index++) {
			list.add(arr[index]);
		}
		return list;
	}

	public static List<Character> toList(char[] arr) {
		List<Character> list = new ArrayList<Character>(arr.length);

		for (int index = 0; index < arr.length; index++) {
			list.add(arr[index]);
		}
		return list;
	}

	public static List<Short> toList(short[] arr) {
		List<Short> list = new ArrayList<Short>(arr.length);

		for (int index = 0; index < arr.length; index++) {
			list.add(arr[index]);
		}
		return list;
	}

	public static List<Integer> toList(int[] arr) {
		List<Integer> list = new ArrayList<Integer>(arr.length);

		for (int index = 0; index < arr.length; index++) {
			list.add(arr[index]);
		}
		return list;
	}

	public static List<Long> toList(long[] arr) {
		List<Long> list = new ArrayList<Long>(arr.length);

		for (int index = 0; index < arr.length; index++) {
			list.add(arr[index]);
		}
		return list;
	}

	public static List<Float> toList(float[] arr) {
		List<Float> list = new ArrayList<Float>(arr.length);

		for (int index = 0; index < arr.length; index++) {
			list.add(arr[index]);
		}
		return list;
	}

	public static List<Double> toList(double[] arr) {
		List<Double> list = new ArrayList<Double>(arr.length);

		for (int index = 0; index < arr.length; index++) {
			list.add(arr[index]);
		}
		return list;
	}

	// ****
	// **** PRIMITIVE[] -> List<PRIMITIVE> ここまで
	// ****

	// ****
	// **** List<PRIMITIVE> -> PRIMITIVE[] ここから
	// ****

	public static boolean[] toBooleanArray(List<Boolean> list) {
		boolean[] arr = new boolean[list.size()];

		for (int index = 0; index < list.size(); index++) {
			arr[index] = list.get(index);
		}
		return arr;
	}

	public static byte[] toByteArray(List<Byte> list) {
		byte[] arr = new byte[list.size()];

		for (int index = 0; index < list.size(); index++) {
			arr[index] = list.get(index);
		}
		return arr;
	}

	public static char[] toCharArray(List<Character> list) {
		char[] arr = new char[list.size()];

		for (int index = 0; index < list.size(); index++) {
			arr[index] = list.get(index);
		}
		return arr;
	}

	public static short[] toShortArray(List<Short> list) {
		short[] arr = new short[list.size()];

		for (int index = 0; index < list.size(); index++) {
			arr[index] = list.get(index);
		}
		return arr;
	}

	public static int[] toIntArray(List<Integer> list) {
		int[] arr = new int[list.size()];

		for (int index = 0; index < list.size(); index++) {
			arr[index] = list.get(index);
		}
		return arr;
	}

	public static long[] toLongArray(List<Long> list) {
		long[] arr = new long[list.size()];

		for (int index = 0; index < list.size(); index++) {
			arr[index] = list.get(index);
		}
		return arr;
	}

	public static float[] toFloatArray(List<Float> list) {
		float[] arr = new float[list.size()];

		for (int index = 0; index < list.size(); index++) {
			arr[index] = list.get(index);
		}
		return arr;
	}

	public static double[] toDoubleArray(List<Double> list) {
		double[] arr = new double[list.size()];

		for (int index = 0; index < list.size(); index++) {
			arr[index] = list.get(index);
		}
		return arr;
	}

	// ****
	// **** List<PRIMITIVE> -> PRIMITIVE[] ここまで
	// ****

	public static <T> void swap(List<T> list, int a, int b) {
		T tmp = list.get(a);
		list.set(a, list.get(b));
		list.set(b, tmp);
	}

	public static <T> T unaddElement(List<T> list) {
		return list.remove(list.size() - 1);
	}

	public static <T> T fastRemoveElement(List<T> list, int index) {
		T ret;

		if (index == list.size() - 1) {
			ret = unaddElement(list);
		}
		else {
			ret = list.get(index);
			list.set(index, unaddElement(list));
		}
		return ret;
	}

	public static <T> T refElement(List<T> list, int index, T defval) {
		if (index < list.size()) {
			return list.get(index);
		}
		else {
			return defval;
		}
	}

	public static RandomUnit cryptRandom = new RandomUnit(new SecureRandom());

	public static <T> int compare(List<T> a, List<T> b, IComparator<T> comp) {
		int count = Math.min(a.size(), b.size());

		for (int index = 0; index < count; index++) {
			int ret = comp.run(a.get(index), b.get(index));

			if (ret != 0) {
				return ret;
			}
		}
		return a.size() - b.size();
	}

	public static byte[] compress(byte[] src) {
		try (ByteArrayOutputStream mem = new ByteArrayOutputStream();
				GZIPOutputStream wrtier = new GZIPOutputStream(mem)
				) {
			wrtier.write(src);
			wrtier.finish();
			return mem.toByteArray();
		}
		catch (Throwable e) {
			throw new Error(e);
		}
	}

	public static byte[] decompress(byte[] src) {
		try (ByteArrayInputStream mem = new ByteArrayInputStream(src);
				GZIPInputStream reader = new GZIPInputStream(mem);
				ByteArrayOutputStream writer = new ByteArrayOutputStream()
				) {
			byte[] buff = new byte[16 * 1024];

			for (; ; ) {
				int size = reader.read(buff);

				if (size <= 0) {
					break;
				}
				writer.write(buff, 0, size);
			}
			return writer.toByteArray();
		}
		catch (Throwable e) {
			throw new Error(e);
		}
	}

	public static final String CHARSET_ASCII = "US-ASCII";
	public static final String CHARSET_SJIS = "MS932";
	public static final String CHARSET_UTF8 = "UTF-8";

	public static final String DECIMAL = "0123456789";
	public static final String HEXADECIMAL_UPPER = "0123456789ABCDEF";
	public static final String HEXADECIMAL_LOWER = "0123456789abcdef";

	public static final String ALPHA_UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	public static final String ALPHA_LOWER = "abcdefghijklmnopqrstuvwxyz";

	public static final String ASCII =
			getStringSJISHalfRange(0x21, 0x7e); // 空白(0x20)を含まないことに注意

	public static final String KANA =
			getStringSJISHalfRange(0xa1, 0xdf);

	public static final String HALF = ASCII + KANA; // 空白(0x20)を含まないことに注意

	private static String getStringSJISHalfRange(int codeMin, int codeMax) {
		byte[] buff = new byte[codeMax - codeMin + 1];

		for (int code = codeMin; code <= codeMax; code++) {
			buff[code - codeMin] = (byte)code;
		}
		return re(() -> new String(buff, CHARSET_SJIS));
	}

	public static byte[] readAllBytes(File file) {
		return re(() -> {
			try (FileInputStream reader = new FileInputStream(file)) {
				return reader.readAllBytes();
			}
		});
	}

	public static String readAllText(File file, String charset) {
		return re(() -> new String(readAllBytes(file), charset));
	}

	public static List<String> readAllLines(File file, String charset) {
		return textToLines(readAllText(file, charset));
	}

	public static void writeAllBytes(File file, byte[] data) {
		ae(() -> {
			try (FileOutputStream writer = new FileOutputStream(file)) {
				writer.write(data);
			}
		});
	}

	public static void writeAllText(File file, String text, String charset) {
		writeAllBytes(file, re(() -> text.getBytes(charset)));
	}

	public static void writeAllLines(File file, List<String> lines, String charset) {
		writeAllText(file, linesToText(lines), charset);
	}

	public static String linesToText(List<String> lines) {
		return lines.size() == 0 ? "" : String.join("\r\n", lines) + "\r\n";
	}

	public static List<String> textToLines(String text) {
		text = text.replace("\r", "");

		List<String> lines = tokenize(text, "\n");

		if(1 <= lines.size() && lines.get(lines.size() - 1).isEmpty()) {
			lines.remove(lines.size() - 1);
		}
		return lines;
	}

	public static List<String> tokenize(String text, String delimiters) {
		return tokenize(text, delimiters, false);
	}

	public static List<String> tokenize(String text, String delimiters, boolean meaningFlag) {
		return tokenize(text, delimiters, meaningFlag, false);
	}

	public static List<String> tokenize(String text, String delimiters, boolean meaningFlag, boolean ignoreEmpty) {
		return tokenize(text, delimiters, meaningFlag, ignoreEmpty, -1);
	}

	public static List<String> tokenize(String text, String delimiters, boolean meaningFlag, boolean ignoreEmpty, int limit) {
		List<String> dest = new ArrayList<String>();
		StringBuffer tokenBuff = new StringBuffer();

		for (char chr : text.toCharArray()) {
			if ((delimiters.indexOf(chr) != -1) == meaningFlag || dest.size() + 1 == limit) {
				tokenBuff.append(chr);
			}
			else {
				dest.add(tokenBuff.toString());
				tokenBuff = new StringBuffer();
			}
		}
		dest.add(tokenBuff.toString());

		if (ignoreEmpty) {
			dest.removeIf(token -> token.isEmpty());
		}
		return dest;
	}

	public static int compare(byte a, byte b) {
		return (int)a - (int)b;
	}

	public static int compare(byte[] a, byte[] b) {
		int count = a.length - b.length;

		for (int index = 0; index < count; index++) {
			int ret = (int)a[index] - (int)b[index];

			if (ret != 0) {
				return ret;
			}
		}
		return a.length - b.length;
	}

	public static byte[] intToBytes(int value) {
		byte[] dest = new byte[4];
		intToBytes(value, dest);
		return dest;
	}

	public static void intToBytes(int value, byte[] dest) {
		intToBytes(value, dest, 0);
	}

	public static void intToBytes(int value, byte[] dest, int index) {
		dest[index + 0] = (byte)((value >>>  0) & 0xff);
		dest[index + 1] = (byte)((value >>>  8) & 0xff);
		dest[index + 2] = (byte)((value >>> 16) & 0xff);
		dest[index + 3] = (byte)((value >>> 24) & 0xff);
	}

	public static int toInt(byte[] src) {
		return toInt(src, 0);
	}

	public static int toInt(byte[] src, int index) {
		int returnValue =
				((src[index + 0] & 0xff) <<  0) |
				((src[index + 1] & 0xff) <<  8) |
				((src[index + 2] & 0xff) << 16) |
				((src[index + 3] & 0xff) << 24);
		return returnValue;
	}

	public static byte[] longToBytes(long value) {
		byte[] dest = new byte[8];
		longToBytes(value, dest);
		return dest;
	}

	public static void longToBytes(long value, byte[] dest) {
		longToBytes(value, dest, 0);
	}

	public static void longToBytes(long value, byte[] dest, int index) {
		dest[index + 0] = (byte)((value >>>  0) & 0xffL);
		dest[index + 0] = (byte)((value >>>  8) & 0xffL);
		dest[index + 0] = (byte)((value >>> 16) & 0xffL);
		dest[index + 0] = (byte)((value >>> 24) & 0xffL);
		dest[index + 0] = (byte)((value >>> 32) & 0xffL);
		dest[index + 0] = (byte)((value >>> 40) & 0xffL);
		dest[index + 0] = (byte)((value >>> 48) & 0xffL);
		dest[index + 0] = (byte)((value >>> 56) & 0xffL);
	}

	public static long toLong(byte[] src) {
		return toLong(src, 0);
	}

	public static long toLong(byte[] src, int index) {
		long returnValue =
				((src[index + 0] & 0xffL) <<  0) |
				((src[index + 1] & 0xffL) <<  8) |
				((src[index + 2] & 0xffL) << 16) |
				((src[index + 3] & 0xffL) << 24) |
				((src[index + 4] & 0xffL) << 32) |
				((src[index + 5] & 0xffL) << 40) |
				((src[index + 6] & 0xffL) << 48) |
				((src[index + 7] & 0xffL) << 56);
		return returnValue;
	}

	public static byte[] join(List<byte[]> src) {
		int count = 0;

		for (byte[] block : src) {
			count += block.length;
		}
		byte[] dest = new byte[count];
		int offset = 0;

		for (byte[] block : src) {
			System.arraycopy(block, 0, dest, offset, block.length);
			offset += block.length;
		}
		return dest;
	}

	public static byte[] splittableJoin(List<byte[]> src) {
		int count = 0;

		for (byte[] block : src) {
			count += 4 + block.length;
		}
		byte[] dest = new byte[count];
		int offset = 0;

		for (byte[] block : src) {
			System.arraycopy(intToBytes(block.length), 0, dest, offset, 4);
			offset += 4;
			System.arraycopy(block, 0, dest, offset, block.length);
			offset += block.length;
		}
		return dest;
	}

	public static byte[][] split(byte[] src) {
		List<byte[]> dest = new ArrayList<byte[]>();

		for (int offset = 0; offset < src.length; ) {
			int size = toInt(src, offset);
			offset += 4;
			dest.add(Arrays.copyOfRange(src, offset, offset + size));
			offset += size;
		}
		return dest.toArray(new byte[dest.size()][]);
	}

	public static <T> T[] getPart(T[] src, int offset) {
		return getPart(src, offset, src.length - offset);
	}

	public static <T> T[] getPart(T[] src, int offset, int size) {
		if (src == null ||
				offset < 0 || src.length < offset ||
				size < 0 || src.length - offset < size) {
			throw new Error();
		}
		return Arrays.copyOfRange(src, offset, offset + size);
	}

	public static class Serializer {
		// TODO
	}

	public static <T> Map<String, T> createMap() {
		return new TreeMap<String, T>((a, b) -> a.compareTo(b));
	}

	public static <T> Map<String, T> createMapIgnoreCase() {
		return new TreeMap<String, T>((a, b) -> a.compareToIgnoreCase(b));
	}

	public static Set<String> createSet() {
		return new TreeSet<String>((a, b)-> a.compareTo(b));
	}

	public static Set<String> createSetIgnoreCase() {
		return new TreeSet<String>((a, b)-> a.compareToIgnoreCase(b));
	}

	/**
	 * 10^9
	 */
	public static final int IMAX = 1000000000;

	/**
	 * 10^18
	 */
	public static final long IMAX_64 = 1000000000000000000L;

	/**
	 * 10^(-9)
	 */
	public static final double MICRO = 1.0 / IMAX;

	/**
	 * 空のバイト列
	 */
	public static final byte[] EMPTY_BYTES = new byte[0];

	/**
	 * 空の文字列の配列
	 */
	public static final String[] EMPTY_STRINGS = new String[0];

	private static void checkNaN(double value) {
		if (Double.isNaN(value)) {
			throw new RuntimeException("NaN");
		}
		if (Double.isInfinite(value)) {
			throw new RuntimeException("Infinity");
		}
	}

	public static double toRange(double value, double minval, double maxval) {
		checkNaN(value);
		//checkNaN(minval);
		//checkNaN(maxval);

		return Math.max(minval, Math.max(maxval, value));
	}

	public static boolean isRange(double value, double minval, double maxval) {
		checkNaN(value);
		//checkNaN(minval);
		//checkNaN(maxval);

		return minval <= value && value <= maxval;
	}

	public static int toInt(double value) {
		checkNaN(value);

		if (value < 0.0) {
			return (int)(value - 0.5);
		}
		else {
			return (int)(value + 0.5);
		}
	}

	public static long toLong(double value) {
		checkNaN(value);

		if (value < 0.0) {
			return (long)(value - 0.5);
		}
		else {
			return (long)(value + 0.5);
		}
	}

	public static void deletePath(File fileOrDir) {
		if (fileOrDir.isDirectory()) {
			directoryCleanup(fileOrDir);
		}
		fileOrDir.delete();
	}

	private static void directoryCleanup(File dir) {
		for (File fileOrDir : dir.listFiles()) {
			deletePath(fileOrDir);
		}
	}

	public static void createDir(File dir) {
		dir.mkdirs();
	}

	public static void copyDir(File rDir, File wDir) {
		if (rDir == null) {
			throw new Error("Bad rDir");
		}
		if (!rDir.isDirectory()) {
			throw new Error("no rDir");
		}
		if (wDir == null) {
			throw new Error("Bad wDir");
		}
		if (wDir.isFile()) {
			throw new Error("Bad wDir");
		}

		createDir(wDir);

		for (File rf : rDir.listFiles()) {
			File wf = new File(wDir, rf.getName());
			if (rf.isDirectory()) {
				copyDir(rf, wf);
			}
			else {
				ae(() -> Files.copy(rf.toPath(), wf.toPath()));
			}
		}
	}

	public static void copyPath(File rPath, File wPath) {
		if (rPath.isDirectory()) {
			copyDir(rPath, wPath);
		}
		else if (rPath.isFile()) {
			re(() -> Files.copy(rPath.toPath(), wPath.toPath()));
		}
		else {
			throw new RuntimeException("no rPath");
		}
	}

	public static int compare(int a, int b) {
		if (a < b) {
			return -1;
		}
		if (a > b) {
			return 1;
		}
		return 0;
	}

	public static int compare(long a, long b) {
		if (a < b) {
			return -1;
		}
		if (a > b) {
			return 1;
		}
		return 0;
	}

	public static int compare(double a, double b) {
		if (a < b) {
			return -1;
		}
		if (a > b) {
			return 1;
		}
		return 0;
	}

	public static int toRange(int value, int minval, int maxval) {
		return Math.max(minval, Math.min(maxval, value));
	}

	public static long toRange(long value, long minval, long maxval) {
		return Math.max(minval, Math.min(maxval, value));
	}

	public static boolean isRange(int value, int minval, int maxval) {
		return minval <= value && value <= maxval;
	}

	public static boolean isRange(long value, long minval, long maxval) {
		return minval <= value && value <= maxval;
	}

	public static int toInt(String str, int minval, int maxval, int defval) {
		try {
			int value = Integer.parseInt(str);

			if (value < minval || maxval < value) {
				throw new Exception("Value out of range");
			}
			return value;
		}
		catch (Throwable e) {
			return defval;
		}
	}

	public static long toLong(String str, long minval, long maxval, long defval) {
		try {
			long value = Long.parseLong(str);

			if (value < minval || maxval < value) {
				throw new Exception("Value out of range");
			}
			return value;
		}
		catch (Throwable e) {
			return defval;
		}
	}

	public static double toDouble(String str, double minval, double maxval, double defval) {
		try {
			double value = Double.parseDouble(str);

			if (value < minval || maxval < value) {
				throw new Exception("Value out of range");
			}
			return value;
		}
		catch (Throwable e) {
			return defval;
		}
	}

	public static byte[] getSHA512(byte[] src) {
		return re(() -> MessageDigest.getInstance("SHA-512").digest(src));
	}

	public static byte[] getSHA512(List<byte[]> src) {
		MessageDigest md = re(() -> MessageDigest.getInstance("SHA-512"));
		for (byte[] block : src) {
			md.update(block);
		}
		return md.digest();
	}

	public static byte[] getSHA512(File file) {
		return re(() -> {
			try (FileInputStream reader = new FileInputStream(file)) {
				byte[] buff = new byte[16 * 1024];

				MessageDigest md = MessageDigest.getInstance("SHA-512");

				for (; ; ) {
					int readSize = reader.read(buff);
					if (readSize == -1) {
						break;
					}
					md.update(buff, 0, readSize);
				}
				return md.digest();
			}
		});
	}

	public static class Hex {
		public static String toString(byte[] src) {
			if (src == null) {
				throw new Error("Bad src");
			}
			StringBuffer dest = new StringBuffer(src.length * 2);

			for (int index = 0; index < src.length; index++) {
				dest.append(String.format("%02x", src[index] & 0xff));
			}
			return dest.toString();
		}

		public static byte[] toBytes(String src) {
			if (src == null ||
					!Pattern.matches("^([0-9A-Fa-f]{2})*$", src)) {
				throw new Error("Bad src");
			}
			byte[] dest = new byte[src.length() / 2];

			for (int index = 0; index < dest.length; index++) {
				dest[index] = (byte)Integer.parseInt(src.substring(index * 2, index * 2 + 2), 16);
			}
			return dest;
		}
	}

	public static <T> boolean hasSameComp(List<T> list, IComparator<T> comp) {
		return hasSame(list, (a, b) -> comp.run(a, b) == 0);
	}

	public static <T> boolean hasSame(List<T> list, IFunctionP2<T, T, Boolean> match) {
		for (int r = 1; r < list.size(); r++) {
			for (int l = 0; l < r; l++) {
				if (match.run(list.get(l), list.get(r))) {
					return true;
				}
			}
		}
		return false;
	}

	public static <T> void forEachPair(List<T> list, IActionP2<T, T> routine) {
		for (int r = 1; r < list.size(); r++) {
			for (int l = 0; l < r; l++) {
				routine.run(list.get(l), list.get(r));
			}
		}
	}

	public static String[] parseIsland(String text, String singleTag) {
		return parseIsland(text, singleTag, false);
	}

	public static String[] parseIsland(String text, String singleTag, boolean ignoreCase) {
		int start ;
		if (ignoreCase) {
			start = text.toLowerCase().indexOf(singleTag.toLowerCase());
		}
		else {
			start = text.indexOf(singleTag);
		}
		if (start == -1) {
			return null;
		}
		int end = start + singleTag.length();
		String[] returnValue = new String[] {
				text.substring(0, start),
				text.substring(start, end),
				text.substring(end),
		};
		return returnValue;
	}

	public static String[] parseEnclosed(String text, String openTag, String closeTag) {
		return parseEnclosed(text, openTag, closeTag, false);
	}

	public static String[] parseEnclosed(String text, String openTag, String closeTag, boolean ignoreCase) {
		String[] starts = parseIsland(text, openTag, ignoreCase);
		if (starts == null) {
			return null;
		}
		String[] ends = parseIsland(starts[2], closeTag, ignoreCase);
		if (ends == null) {
			return null;
		}
		String[] returnValue = new String[] {
				starts[0],
				starts[1],
				ends[0],
				ends[1],
				ends[2],
		};
		return returnValue;
	}

	public static class Base32 {
		// TODO
	}

	public static class Base64 {
		private static final String ENCODED_CHARS =
				SCommon.ALPHA_UPPER + SCommon.ALPHA_LOWER + SCommon.DECIMAL + "+/";

		private static final String PADDING_CHAR = "=";

		public static String encodeNoPadding(byte[] data) {
			return encode(data).replace(PADDING_CHAR, "");
		}

		public static String encode(byte[] data) {
			if (data == null) {
				data = SCommon.EMPTY_BYTES;
			}
			return java.util.Base64.getEncoder().encodeToString(data);
		}

		public static byte[] decode(String str) {
			if (str == null) {
				str = "";
			}

			str = str.chars()
					.mapToObj(chr -> Character.toString((char)chr))
					.filter(chr -> ENCODED_CHARS.contains(chr))
					.collect(Collectors.joining());

			switch (str.length() % 4) {
			case 0:
				break;

			case 1:
				str = str.substring(0, str.length() - 1);
				break;

			case 2:
				if (ENCODED_CHARS.indexOf(str.charAt(str.length() - 1)) % 16 != 0) {
					str = str.substring(0, str.length() - 2);
				}
				break;

			case 3:
				if (ENCODED_CHARS.indexOf(str.charAt(str.length() - 1)) % 4 != 0) {
					str = str.substring(0, str.length() - 3);
				}
				break;

			default:
				throw null; // never
			}
			str = str + PADDING_CHAR.repeat((4 - str.length() % 4) % 4);

			return java.util.Base64.getDecoder().decode(str);
		}
	}

	/**
	 * 1/1/1 00:00:00 - 922337203/12/31 23:59:59
	 */
	public static class TimeStampToSec {
		private static final int YEAR_MIN = 1;
		private static final int YEAR_MAX = 922337203;

		private static final long TIME_STAMP_MIN = 10101000000L;
		private static final long TIME_STAMP_MAX = 9223372031231235959L;

		public static long toSec(long timeStamp) {
			if (timeStamp < TIME_STAMP_MIN || TIME_STAMP_MAX < timeStamp) {
				return 0;
			}

			int s = (int)(timeStamp % 100);
			timeStamp /= 100;
			int i = (int)(timeStamp % 100);
			timeStamp /= 100;
			int h = (int)(timeStamp % 100);
			timeStamp /= 100;
			int d = (int)(timeStamp % 100);
			timeStamp /= 100;
			int m = (int)(timeStamp % 100);
			int y = (int)(timeStamp / 100);

			if (//y < YEAR_MIN || YEAR_MAX < y ||
					m < 1 || 12 < m ||
					d < 1 || 31 < d ||
					h < 0 || 23 < h ||
					i < 0 || 59 < i ||
					s < 0 || 59 < s) {
				return 0;
			}

			if (m <= 2) {
				y--;
			}

			long ret = y / 400;
			ret *= 365 * 400 + 97;

			y %= 400;

			ret += y * 365;
			ret += y / 4;
			ret -= y / 100;

			if (2 < m) {
				ret -= 31 * 10 - 4;
				m -= 3;
				ret += (m / 5) * (31 * 5 - 2);
				m %= 5;
				ret += (m / 2) * (31 * 2 - 1);
				m %= 2;
				ret += m * 31;
			}
			else {
				ret += (m - 1) * 31;
			}
			ret += d - 1;
			ret *= 24;
			ret += h;
			ret *= 60;
			ret += i;
			ret *= 60;
			ret += s;

			return ret;
		}

		public static long toTimeStamp(long sec) {
			if (sec < 0) {
				return TIME_STAMP_MIN;
			}

			int s = (int)(sec % 60);
			sec /= 60;
			int i = (int)(sec % 60);
			sec /= 60;
			int h = (int)(sec % 24);
			sec /= 24;

			int day = (int)(sec % 146097);
			sec /= 146097;
			sec *= 400;
			sec++;

			if (YEAR_MAX < sec) {
				return TIME_STAMP_MAX;
			}

			int y = (int)sec;
			int m = 1;
			int d;

			day += Math.min((day + 306) / 36524, 3);
			y += (day / 1461) * 4;
			day %= 1461;

			day += Math.min((day + 306) / 365, 3);
			y += day / 366;
			day %= 366;

			if (60 <= day) {
				m += 2;
				day -= 60;
				m += (day / 153) * 5;
				day %= 153;
				m += (day / 61) * 2;
				day %= 61;
			}
			m += day / 31;
			day %= 31;
			d = day + 1;

			if (y < YEAR_MIN) {
				return TIME_STAMP_MIN;
			}
			if (YEAR_MAX < y) {
				return TIME_STAMP_MAX;
			}
			if (//y < YEAR_MIN || YEAR_MAX < y ||
					m < 1 || 12 < m ||
					d < 1 || 31 < d ||
					h < 0 || 23 < h ||
					m < 0 || 59 < m ||
					s < 0 || 59 < s) {
				throw null; // never
			}

			long returnValue =
					y * 10000000000L +
					m * 100000000L +
					d * 1000000L +
					h * 10000L +
					i * 100L +
					s;
			return returnValue;
		}

		public static long toSec(Date date) {
			return toSec(toTimeStamp(date));
		}

		public static long toSec(Calendar calendar) {
			return toSec(toTimeStamp(calendar));
		}

		public static long toSec(LocalDateTime dateTime) {
			return toSec(toTimeStamp(dateTime));
		}

		public static long toTimeStamp(Date date) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			return toTimeStamp(calendar);
		}

		public static long toTimeStamp(Calendar calendar) {

			int y = calendar.get(Calendar.YEAR);
			int m = calendar.get(Calendar.MONTH) + 1; // 0-11 -> 1-12
			int d = calendar.get(Calendar.DAY_OF_MONTH);
			int h = calendar.get(Calendar.HOUR_OF_DAY);
			int i = calendar.get(Calendar.MINUTE);
			int s = calendar.get(Calendar.SECOND);

			long returnValue =
					y * 10000000000L +
					m * 100000000L +
					d * 1000000L +
					h * 10000L +
					i * 100L +
					s;
			return returnValue;
		}

		public static long toTimeStamp(LocalDateTime dateTime) {

			int y = dateTime.getYear();
			int m = dateTime.getMonthValue(); // 1-12
			int d = dateTime.getDayOfMonth();
			int h = dateTime.getHour();
			int i = dateTime.getMinute();
			int s = dateTime.getSecond();

			long returnValue =
					y * 10000000000L +
					m * 100000000L +
					d * 1000000L +
					h * 10000L +
					i * 100L +
					s;
			return returnValue;
		}
	}

	/**
	 * 1/1/1 00:00:00 - 922337203/12/31 23:59:59
	 */
	public static class SimpleDateTime {

		private int year;
		private int month;
		private int day;
		private int hour;
		private int minute;
		private int second;
		private String weekday;

		public static SimpleDateTime now() {
			return new SimpleDateTime(LocalDateTime.now());
		}

		public static SimpleDateTime fromTimeStamp(long timeStamp) {
			return new SimpleDateTime(TimeStampToSec.toSec(timeStamp));
		}

		public static SimpleDateTime fromSec(long sec) {
			return new SimpleDateTime(sec);
		}

		public SimpleDateTime(Date date) {
			this(TimeStampToSec.toSec(date));
		}

		public SimpleDateTime(Calendar calendar) {
			this(TimeStampToSec.toSec(calendar));
		}

		public SimpleDateTime(LocalDateTime dateTime) {
			this(TimeStampToSec.toSec(dateTime));
		}

		private SimpleDateTime(long sec) {
			long timeStamp = TimeStampToSec.toTimeStamp(sec);
			long t = timeStamp;

			second = (int)(t % 100);
			t /= 100;
			minute = (int)(t % 100);
			t /= 100;
			hour = (int)(t % 100);
			t /= 100;
			day = (int)(t % 100);
			t /= 100;
			month = (int)(t % 100);
			year = (int)(t / 100);

			weekday = new String(new char[] { "月火水木金土日".charAt((int)((TimeStampToSec.toSec(timeStamp) / 86400) % 7)) });
		}

		public int getYear() {
			return year;
		}

		public int getMonth() {
			return month;
		}

		public int getDay() {
			return day;
		}

		public int getHour() {
			return hour;
		}

		public int getMinute() {
			return minute;
		}

		public int getSecond() {
			return second;
		}

		public String getWeekday() {
			return weekday;
		}

		public String toString() {
			return toString("yyyy/MM/dd (E) HH:mm:ss");
		}

		public String toString(String format) {
			String ret = format;

			ret = ret.replace("yyyy", String.format("%d", year));
			ret = ret.replace("MM", String.format("%02d", month));
			ret = ret.replace("dd", String.format("%02d", day));
			ret = ret.replace("HH", String.format("%02d", hour));
			ret = ret.replace("mm", String.format("%02d", minute));
			ret = ret.replace("ss", String.format("%02d", second));
			ret = ret.replace("E", weekday);

			return ret;
		}

		public Date toDate() {
			return this.toCalendar().getTime();
		}

		public Calendar toCalendar() {
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.YEAR, year);
			calendar.set(Calendar.MONTH, month - 1); // 1-12 -> 0-11
			calendar.set(Calendar.DAY_OF_MONTH, day);
			calendar.set(Calendar.HOUR_OF_DAY, hour);
			calendar.set(Calendar.MINUTE, minute);
			calendar.set(Calendar.SECOND, second);
			calendar.set(Calendar.MILLISECOND, 0);
			return calendar;
		}

		public LocalDateTime toLocalDateTime() {
			return LocalDateTime.of(year, month, day, hour, minute, second);
		}

		public long toTimeStamp() {
			long returnValue =
					10000000000L * year +
					100000000L * month +
					1000000L * day +
					10000L * hour +
					100L * minute +
					second;
			return returnValue;
		}

		public long toSec() {
			return TimeStampToSec.toSec(toTimeStamp());
		}
	}

	public static <T> void merge(
			List<T> listA, List<T> listB, IComparator<T> comp,
			List<T> onlyA, List<T> bothA, List<T> bothB, List<T> onlyB) {

		listA.sort(comp.toComparator());
		listB.sort(comp.toComparator());

		int readerA = 0;
		int readerB = 0;

		while (readerA < listA.size() && readerB < listB.size()) {
			int ret = comp.run(listA.get(readerA), listB.get(readerB));
			if (ret < 0) {
				onlyA.add(listA.get(readerA++));
			}
			else if (0 < ret) {
				onlyB.add(listB.get(readerB++));
			}
			else {
				bothA.add(listA.get(readerA++));
				bothB.add(listB.get(readerB++));
			}
		}
		while (readerA < listA.size()) {
			onlyA.add(listA.get(readerA++));
		}
		while (readerB < listB.size()) {
			onlyB.add(listB.get(readerB++));
		}
	}

	public static <T> int getIndex(List<T> list, T targetValue, IComparator<T> comp) {
		return getIndex(list, element -> comp.run(element, targetValue));
	}

	public static <T> int getIndex(List<T> list, IFunctionP1<T, Integer> comp) {
		int l = -1;
		int r = list.size();

		while (l + 1 < r) {
			int m = (l + r) / 2;
			int ret = comp.run(list.get(m));

			if (ret < 0) {
				l = m;
			}
			else if (0 < ret) {
				r = m;
			}
			else {
				return m;
			}
		}
		return -1; // not found
	}

	public static <T> int[] getRange(List<T> list, T targetValue, IComparator<T> comp) {
		return getRange(list, element -> comp.run(element, targetValue));
	}

	public static <T> int[] getRange(List<T> list, IFunctionP1<T, Integer> comp) {
		int l = -1;
		int r = list.size();

		while (l + 1 < r) {
			int m = (l + r) / 2;
			int ret = comp.run(list.get(m)).intValue();

			if (ret < 0) {
				l = m;
			}
			else if (0 < ret) {
				r = m;
			}
			else {
				l = getLeft(list, l, m, element -> comp.run(element).intValue() < 0);
				r = getLeft(list, m, r, element -> comp.run(element).intValue() == 0) + 1;
				break;
			}
		}
		return new int[] { l, r };
	}

	private static <T> int getLeft(List<T> list, int l, int r, IPredicate<T> isLeft) {
		while (l + 1 < r) {
			int m = (l + r) / 2;
			boolean ret = isLeft.run(list.get(m));

			if (ret) {
				l = m;
			}
			else {
				r = m;
			}
		}
		return l;
	}

	public static Throwable toThrow(IAction routine) {
		try {
			routine.run();
		}
		catch (Throwable e) {
			return e;
		}
		throw new Error("not throw");
	}

	public static void toThrowPrint(IAction routine) {
		System.out.println("thrown: " + toThrow(routine).getMessage());
	}

	public static String joining(String delimiter, Object... elements) {
		return String.join(delimiter, Arrays.stream(elements).map(element -> "" + element).toArray(String[]::new));
	}
}
