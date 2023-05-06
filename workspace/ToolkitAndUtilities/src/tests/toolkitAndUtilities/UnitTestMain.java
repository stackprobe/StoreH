package tests.toolkitAndUtilities;

import java.io.File;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import toolkitAndUtilities.SCommon;

public class UnitTestMain {
	public static void main(String[] args) {
		try {

			// -- choose one --

			//test0001_01(); // SCommon.getIndex
			//test0002_01(); // SCommon.getRange
			//test0003_01(); // SCommon.Base64
			//test0004_01(); // RandomUnit
			//test0004_02(); // RandomUnit
			//test0005_01(); // SCommon.tokenize
			//test0005_02(); // SCommon.tokenize
			//test0005_03(); // SCommon.tokenize
			//test0005_04(); // SCommon.tokenize
			//test0006_01(); // SCommon.merge
			//test0007_01(); // SCommon.Hex
			//test0008_01(); // SCommon.SimpleDateTime
			//test0009_01(); // SCommon.TimeStampToSec
			test0009_02(); // SCommon.TimeStampToSec

			// --
		}
		catch (Throwable e) {
			e.printStackTrace();
		}
	}

	private static void test0001_01() {
		for (int a = 0; a < 10; a++) {
			for (int b = 0; b <= 1; b++) {
				for (int c = 0; c < 10; c++) {
					String str = "A".repeat(a) + "B".repeat(b) + "C".repeat(c);
					char target = 'B';
					int expect = b == 1 ? a : -1;

					test0001_01_a(str, target, expect);
				}
			}
		}
		System.out.println("OK!");
	}

	private static void test0001_01_a(String str, char target, int expect) {
		int ret = SCommon.getIndex(SCommon.toList(str.toCharArray()), target, (a, b) -> (int)a - (int)b);

		System.out.println(SCommon.joining(", ", "TEST-0001-01", str, target, expect, ret));

		boolean expectTrue =
				ret == expect;
		if (!expectTrue) {
			throw null;
		}

		System.out.println("OK");
	}

	private static void test0002_01() {
		for (int a = 0; a < 10; a++) {
			for (int b = 0; b < 10; b++) {
				for (int c = 0; c < 10; c++) {
					String str = "A".repeat(a) + "B".repeat(b) + "C".repeat(c);
					char target = 'B';
					int expectRange_L = a - 1;
					int expectRange_R = a + b;

					test0002_01_a(str, target, expectRange_L, expectRange_R);
				}
			}
		}
		System.out.println("OK!");
	}

	private static void test0002_01_a(String str, char target, int expectRange_L, int expectRange_R) {
		int[] range = SCommon.getRange(SCommon.toList(str.toCharArray()), target, (a, b) -> (int)a - (int)b);

		System.out.println(SCommon.joining(", ", "TEST-0002-01", str, target, expectRange_L, expectRange_R, range[0], range[1]));

		boolean expectTrue =
				range[0] == expectRange_L &&
				range[1] == expectRange_R;
		if (!expectTrue) {
			throw null;
		}

		System.out.println("OK");
	}

	private static void test0003_01() {
		for (int testcnt = 0; testcnt < 10000; testcnt++) {
			byte[] data = SCommon.cryptRandom.getBytes(SCommon.cryptRandom.getInt(1000));
			String str = SCommon.Base64.encode(data);

			if (str == null) {
				throw null;
			}
			if (!Pattern.matches("^[A-Za-z0-9+/]*=*$", str)) {
				throw null;
			}

			byte[] retData = SCommon.Base64.decode(str);

			if (retData == null) {
				throw null;
			}
			if (SCommon.compare(data, retData) != 0) { // ? not same
				throw null;
			}
		}
		System.out.println("OK! (TEST-0003-01)");
	}

	private static void test0004_01() {
		for (int c = 0; c < 100; c++) {
			System.out.println(String.format("%.20f", SCommon.cryptRandom.getRate()));
		}
		System.out.println("done! (TEST-0004-01)");
	}

	private static void test0004_02() {
		test0004_02_a(3.0, 7.0);
		test0004_02_a(-3.0, 7.0);
		test0004_02_a(-3.0, 3.0);
		test0004_02_a(-7.0, 3.0);
		test0004_02_a(-7.0, -3.0);

		System.out.println("done! (TEST-0004-02)");
	}

	private static void test0004_02_a(double minval, double maxval) {
		List<Double> values = new ArrayList<Double>();

		for (int c = 0; c < 100; c++) {
			values.add(SCommon.cryptRandom.getRealRange(minval, maxval));
		}
		values.sort((a, b) -> SCommon.compare(a, b));

		SCommon.writeAllLines(
				new File("C:/temp/(" + minval + ")-(" + maxval + ").txt"),
				values.stream().map(value -> "" + value).collect(Collectors.toList()),
				SCommon.CHARSET_ASCII
				);
	}

	private static void test0005_01() {
		test0005_01_a("ABC:DEF:GHI:JKL", ":", -1, new String[] { "ABC", "DEF", "GHI", "JKL" });
		test0005_01_a("ABC:DEF:GHI:JKL", ":", 2, new String[] { "ABC", "DEF:GHI:JKL" });
		test0005_01_a("ABC:DEF:GHI:JKL", ":", 3, new String[] { "ABC", "DEF", "GHI:JKL" });
		test0005_01_a("ABC:DEF:GHI:JKL", ":", 4, new String[] { "ABC", "DEF", "GHI", "JKL" });
		test0005_01_a("ABC:DEF:GHI:JKL", ":", 5, new String[] { "ABC", "DEF", "GHI", "JKL" });

		System.out.println("OK! (TEST-0005-01)");
	}

	private static void test0005_01_a(String text, String delimiters, int limit, String[] expect) {
		List<String> tokens = SCommon.tokenize(text, delimiters, false, false, limit);

		if (tokens == null) {
			throw null;
		}
		if (SCommon.compare(tokens, Arrays.asList(expect), (a, b) -> a.compareTo(b)) != 0) { // ? not same
			throw null;
		}
		System.out.println("OK");
	}

	private static void test0005_02() {
		test0005_02_a("...---...", ".", false, new String[] { "", "", "", "---", "", "", "" });
		test0005_02_a("...---...", ".", true, new String[] { "---" });

		System.out.println("OK! (TEST-0005-02)");
	}

	private static void test0005_02_a(String text, String delimiters, boolean ignoreEmpty, String[] expect) {
		List<String> tokens = SCommon.tokenize(text, delimiters, false, ignoreEmpty);

		if (tokens == null) {
			throw null;
		}
		if (SCommon.compare(tokens, Arrays.asList(expect), (a, b) -> a.compareTo(b)) != 0) { // ? not same
			throw null;
		}
		System.out.println("OK");
	}

	private static void test0005_03() {
		test0005_03_a("2023/04/05 23:30:59", "/ :", false
				, new String[] { "2023", "04", "05", "23", "30", "59" });
		test0005_03_a("2023/04/05 23:30:59", SCommon.DECIMAL, true
				, new String[] { "2023", "04", "05", "23", "30", "59" });

		System.out.println("OK! (TEST-0005-03)");
	}

	private static void test0005_03_a(String text, String delimiters, boolean meaningFlag, String[] expect) {
		List<String> tokens = SCommon.tokenize(text, delimiters, meaningFlag);

		if (tokens == null) {
			throw null;
		}
		if (SCommon.compare(tokens, Arrays.asList(expect), (a, b) -> a.compareTo(b)) != 0) { // ? not same
			throw null;
		}
		System.out.println("OK");
	}

	private static void test0005_04() {
		test0005_04_a("", "", new String[] { "" });
		test0005_04_a("", ":.;,", new String[] { "" });
		test0005_04_a("A:B.C;D,E", ":.;,", new String[] { "A", "B", "C", "D", "E" });
		test0005_04_a(":.;,", ":.;,", new String[] { "", "", "", "", "" });
		test0005_04_a("AB-CD-EF", "", new String[] { "AB-CD-EF" });

		System.out.println("OK! (TEST-0005-04)");
	}

	private static void test0005_04_a(String text, String delimiters, String[] expect) {
		List<String> tokens = SCommon.tokenize(text, delimiters);

		if (tokens == null) {
			throw null;
		}
		if (SCommon.compare(tokens, Arrays.asList(expect), (a, b) -> a.compareTo(b)) != 0) { // ? not same
			throw null;
		}
		System.out.println("OK");
	}

	private static void test0006_01() {
		test0006_01_a(30000, 10, 30000);
		test0006_01_a(10000, 30, 10000);
		test0006_01_a(3000, 100, 3000);
		test0006_01_a(1000, 300, 1000);
		test0006_01_a(300, 1000, 300);
		test0006_01_a(100, 3000, 100);
		test0006_01_a(30, 10000, 30);
		test0006_01_a(10, 30000, 10);

		test0006_01_a(30000, 100, 3000);
		test0006_01_a(10000, 300, 1000);
		test0006_01_a(3000, 1000, 300);
		test0006_01_a(1000, 3000, 100);
		test0006_01_a(300, 10000, 30);
		test0006_01_a(100, 30000, 10);

		test0006_01_a(30000, 1000, 300);
		test0006_01_a(10000, 3000, 100);
		test0006_01_a(3000, 10000, 30);
		test0006_01_a(1000, 30000, 10);

		test0006_01_a(30000, 10000, 30);
		test0006_01_a(10000, 30000, 10);

		System.out.println("OK!");
	}

	private static void test0006_01_a(int valueScale, int countScale, int testCount) {
		System.out.println(SCommon.joining(", ", "TEST-0006-01", valueScale, countScale, testCount));

		for (int testcnt = 0; testcnt < testCount; testcnt++) {
			List<String> listA = Stream.generate(() -> "" + SCommon.cryptRandom.getInt(valueScale))
					.limit(SCommon.cryptRandom.getInt(countScale))
					.collect(Collectors.toList());
			List<String> listB = Stream.generate(() -> "" + SCommon.cryptRandom.getInt(valueScale))
					.limit(SCommon.cryptRandom.getInt(countScale))
					.collect(Collectors.toList());

			List<String> inp_la = new ArrayList<String>(listA);
			List<String> inp_lb = new ArrayList<String>(listB);
			List<String> ret_oa = new ArrayList<String>();
			List<String> ret_ba = new ArrayList<String>();
			List<String> ret_bb = new ArrayList<String>();
			List<String> ret_ob = new ArrayList<String>();

			SCommon.merge(inp_la, inp_lb, (a, b) -> a.compareTo(b), ret_oa, ret_ba, ret_bb, ret_ob);

			List<String> expect_la = new ArrayList<String>(listA);
			List<String> expect_lb = new ArrayList<String>(listB);
			List<String> expect_oa = new ArrayList<String>();
			List<String> expect_ba = new ArrayList<String>();
			List<String> expect_bb = new ArrayList<String>();
			List<String> expect_ob = new ArrayList<String>();

			test0006_01_b(listA, listB, expect_ba, expect_oa);
			test0006_01_b(listB, listA, expect_bb, expect_ob);

			expect_la.sort((a, b) -> a.compareTo(b));
			expect_lb.sort((a, b) -> a.compareTo(b));
			expect_oa.sort((a, b) -> a.compareTo(b));
			expect_ba.sort((a, b) -> a.compareTo(b));
			expect_bb.sort((a, b) -> a.compareTo(b));
			expect_ob.sort((a, b) -> a.compareTo(b));

			if (SCommon.compare(inp_la, expect_la, (a, b) -> a.compareTo(b)) != 0) { // ? not same
				throw null;
			}
			if (SCommon.compare(inp_lb, expect_lb, (a, b) -> a.compareTo(b)) != 0) { // ? not same
				throw null;
			}
			if (SCommon.compare(ret_oa, expect_oa, (a, b) -> a.compareTo(b)) != 0) { // ? not same
				throw null;
			}
			if (SCommon.compare(ret_ba, expect_ba, (a, b) -> a.compareTo(b)) != 0) { // ? not same
				throw null;
			}
			if (SCommon.compare(ret_bb, expect_bb, (a, b) -> a.compareTo(b)) != 0) { // ? not same
				throw null;
			}
			if (SCommon.compare(ret_ob, expect_ob, (a, b) -> a.compareTo(b)) != 0) { // ? not same
				throw null;
			}
		}
		System.out.println("OK");
	}

	private static void test0006_01_b(List<String> list, List<String> another, List<String> both, List<String> only) {
		another = new ArrayList<String>(another);

		for (String element : list) {
			int index = another.indexOf(element);
			if (index != -1) {
				another.set(index, null);
				both.add(element);
			}
			else {
				only.add(element);
			}
		}
	}

	private static void test0007_01() {
		for (int testcnt = 0; testcnt < 10000; testcnt++) {
			byte[] data = SCommon.cryptRandom.getBytes(SCommon.cryptRandom.getInt(1000));
			String str = SCommon.Hex.toString(data);

			if (str == null) {
				throw null;
			}
			if (str.length() != data.length * 2) {
				throw null;
			}
			if (!Pattern.matches("^[0-9a-f]*$", str)) {
				throw null;
			}

			byte[] retData = SCommon.Hex.toBytes(str);

			if (retData == null) {
				throw null;
			}
			if (SCommon.compare(data, retData) != 0) { // ? not same
				throw null;
			}
		}
		System.out.println("OK! (TEST-0007-01)");
	}

	private static void test0008_01() {
		System.out.println(SCommon.SimpleDateTime.now().toDate());
		System.out.println(toSimpleDateTimeString(SCommon.SimpleDateTime.now().toCalendar()));
		System.out.println(SCommon.SimpleDateTime.now().toLocalDateTime());

		System.out.println(new SCommon.SimpleDateTime(SCommon.SimpleDateTime.now().toDate()));
		System.out.println(new SCommon.SimpleDateTime(SCommon.SimpleDateTime.now().toCalendar()));
		System.out.println(new SCommon.SimpleDateTime(SCommon.SimpleDateTime.now().toLocalDateTime()));

		System.out.println("done! (TEST-0008-01)");
	}

	private static String toSimpleDateTimeString(Calendar calendar) {
		return String.format("%d/%02d/%02d %02d:%02d:%02d"
				, calendar.get(Calendar.YEAR)
				, calendar.get(Calendar.MONTH) + 1
				, calendar.get(Calendar.DAY_OF_MONTH)
				, calendar.get(Calendar.HOUR_OF_DAY)
				, calendar.get(Calendar.MINUTE)
				, calendar.get(Calendar.SECOND));
	}

	private static void test0009_01() {
		test0009_01_a(10115235959L, 1);
		test0009_01_a(10201235959L, 3);
		test0009_01_a(10301235959L, 10);
		test0009_01_a(10401235959L, 30);
		test0009_01_a(11231235959L, 100);
		test0009_01_a(31231235959L, 300);
		test0009_01_a(101231235959L, 1000);
		test0009_01_a(301231235959L, 3000);
		test0009_01_a(1001231235959L, 10000);
		test0009_01_a(3001231235959L, 30000);
		test0009_01_a(10001231235959L, 100000);
		test0009_01_a(30001231235959L, 300000);
		test0009_01_a(90001231235959L, 1000000);

		System.out.println("OK!");
	}

	private static void test0009_01_a(long maxTimeStamp, int maxSecAdd) {
		System.out.println(SCommon.joining(", ", "TEST-0009-01", maxTimeStamp, maxSecAdd));

		long timeStamp = 10101000000L;
		long sec = 0;

		while (timeStamp <= maxTimeStamp) {
			long retTimeStamp = SCommon.TimeStampToSec.toTimeStamp(sec);
			long retSec = SCommon.TimeStampToSec.toSec(timeStamp);

			if (retTimeStamp != timeStamp) {
				throw null;
			}
			if (retSec != sec) {
				throw null;
			}

			// ----

			int secAdd = SCommon.cryptRandom.getRange(1, maxSecAdd);

			timeStamp = test_addSecToTimeStamp(timeStamp, secAdd);
			sec += secAdd;
		}
		System.out.println("OK");
	}

	private static void test0009_02() {
		test0009_02_a(10000101000000L, 30001231235959L, 86400);
		test0009_02_a(19000101000000L, 21001231235959L, 10000);
		test0009_02_a(19500101000000L, 20501231235959L, 3000);
		test0009_02_a(19900101000000L, 20101231235959L, 1000);
		test0009_02_a(20200101000000L, 20301231235959L, 300);
		test0009_02_a(20200101000000L, 20251231235959L, 100);

		System.out.println("OK!");
	}

	private static void test0009_02_a(long minTimeStamp, long maxTimeStamp, int maxSecAdd) {
		System.out.println(SCommon.joining(", ", "TEST-0006-02", minTimeStamp, maxTimeStamp, maxSecAdd));

		long timeStamp = minTimeStamp;
		long sec = test_timeStampToSec(timeStamp);

		while (timeStamp <= maxTimeStamp) {
			long retTimeStamp = SCommon.TimeStampToSec.toTimeStamp(sec);
			long retSec = SCommon.TimeStampToSec.toSec(timeStamp);

			if (retTimeStamp != timeStamp) {
				throw null;
			}
			if (retSec != sec) {
				throw null;
			}

			// ----

			int secAdd = SCommon.cryptRandom.getRange(1, maxSecAdd);

			timeStamp = test_addSecToTimeStamp(timeStamp, secAdd);
			sec += secAdd;
		}
		System.out.println("OK");
	}

	private static long test_timeStampToSec(long targetTimeStamp) {

		long timeStamp = 10101000000L;
		long sec = 0;

		for (int secAdd = 1000000; 0 < secAdd; secAdd /= 2) {

			for (; ; ) {

				long nextTimeStamp = test_addSecToTimeStamp(timeStamp, secAdd);
				long nextSec = sec + secAdd;

				if (targetTimeStamp < nextTimeStamp) {
					break;
				}

				timeStamp = nextTimeStamp;
				sec = nextSec;
			}
		}

		if (timeStamp != targetTimeStamp) {
			throw null; // 2bs
		}
		if (sec != SCommon.TimeStampToSec.toSec(targetTimeStamp)) {
			throw null; // 2bs
		}

		return sec;
	}

	private static long test_addSecToTimeStamp(long timeStamp, int secAdd) {

		int s = (int)(timeStamp % 100);
		timeStamp /= 100;
		int i = (int)(timeStamp % 100);
		timeStamp /= 100;
		int h = (int)(timeStamp % 100);
		timeStamp /= 100;
		int d = (int)(timeStamp % 100);
		timeStamp /= 100;
		int m = (int)(timeStamp % 100);
		timeStamp /= 100;
		int y = (int)timeStamp;

		s += secAdd;
		i += s / 60; s %= 60;
		h += i / 60; i %= 60;
		d += h / 24; h %= 24;

		for (; ; ) {
			int days = YearMonth.of(y, m).lengthOfMonth();

			if (d <= days) {
				break;
			}

			d -= days;
			m++;

			if (12 < m) {
				m -= 12;
				y++;
			}
		}

		long returnTimeStamp =
				y * 10000000000L +
				m * 100000000L +
				d * 1000000L +
				h * 10000L +
				i * 100L +
				s;

		return returnTimeStamp;
	}
}
