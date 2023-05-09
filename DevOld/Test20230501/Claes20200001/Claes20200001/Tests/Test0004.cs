using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;
using Charlotte.Commons;

namespace Charlotte.Tests
{
	public class Test0004
	{
		public void Test01()
		{
			string str1 = SCommon.GetJChars();
			string str2 = string.Join(
				"",
				SCommon.GetJCharCodes().Select(code => SCommon.ENCODING_SJIS.GetString(new byte[] { (byte)(code >> 8), (byte)(code & 0xff) }))
				);

			if (str1 != str2)
				throw null;

			byte[] data1 = SCommon.ENCODING_SJIS.GetBytes(str1);
			byte[] data2 = SCommon.Concat(str2.Select(chr => SCommon.ENCODING_SJIS.GetBytes(new string(new char[] { chr })))).ToArray();

			if (SCommon.Comp(data1, data2) != 0) // ? 不一致
				throw null;

			// 一致しない
			//if (SCommon.Comp(data1, SCommon.GetJCharBytes().ToArray()) != 0) // ? 不一致
			//throw null;

			//File.WriteAllBytes(@"C:\temp\SJIS.txt", SCommon.GetJCharBytes().ToArray());
			//File.WriteAllBytes(@"C:\temp\U2SJIS.txt", data1);

			// 不一致箇所：
			// SJIS          U2SJIS   補足
			// ---------------------------
			// 8790          81e0     前方の同じ記号になる。(SJIS内での置換)
			// 8791          81df     前方の同じ記号になる。(SJIS内での置換)
			// 8792          81e7     前方の同じ記号になる。(SJIS内での置換)
			// 8795          81e3     前方の同じ記号になる。(SJIS内での置換)
			// 8796          81db     前方の同じ記号になる。(SJIS内での置換)
			// 8797          81da     前方の同じ記号になる。(SJIS内での置換)
			// 879a          81e6     前方の同じ記号になる。(SJIS内での置換)
			// 879b          81bf     前方の同じ記号になる。(SJIS内での置換)
			// 879c          81be     前方の同じ記号になる。(SJIS内での置換)
			// ed40 - eefc   *        NEC選定IBM拡張文字からIBM拡張文字へ置き換わる模様
			// fa4a - fa54   *        IBM拡張文字の記号がSJIS内の記号へ置き換わる模様
			// fa58 - fa5b   *        IBM拡張文字の記号がSJIS内の記号へ置き換わる模様
			//
			// ★全て異なるコードの同じ文字に置き換わっている模様

			byte[] data3 = SCommon.ENCODING_SJIS.GetBytes(SCommon.ENCODING_SJIS.GetString(data2));

			if (SCommon.Comp(data2, data3) != 0) // ? 不一致
				throw null;

			// 上記「不一致箇所」のSJISの文字のみ不可逆となるっぽい。
		}

		public void Test02()
		{
			int[] chrSJISs = SCommon.GetJCharCodes().Select(chr => (int)chr).ToArray();
			int[] chrSJISs_R = chrSJISs.Where(chr => !IsIrreversibleChar(chr)).ToArray(); // reversible
			int[] chrSJISs_IR = chrSJISs.Where(chr => IsIrreversibleChar(chr)).ToArray(); // inreversible

			int[] unicodes_R = chrSJISs_R.Select(chr =>
				(int)SCommon.ENCODING_SJIS.GetString(new byte[] { (byte)(chr >> 8), (byte)(chr & 0xff) })[0]).ToArray();
			int[] unicodes_IR = chrSJISs_IR.Select(chr =>
				(int)SCommon.ENCODING_SJIS.GetString(new byte[] { (byte)(chr >> 8), (byte)(chr & 0xff) })[0]).ToArray();

			foreach (int unicode_IR in unicodes_IR)
				if (!unicodes_R.Contains(unicode_IR))
					throw null;

			// SJIS_IR, SJIS_R 共に Unicode に変換した時点で Unicode_R に含まれるっぽい。
		}

		private bool IsIrreversibleChar(int chrSJIS)
		{
			return
				0x8790 == chrSJIS ||
				0x8791 == chrSJIS ||
				0x8792 == chrSJIS ||
				0x8795 == chrSJIS ||
				0x8796 == chrSJIS ||
				0x8797 == chrSJIS ||
				0x879a == chrSJIS ||
				0x879b == chrSJIS ||
				0x879c == chrSJIS ||
				(0xed40 <= chrSJIS && chrSJIS <= 0xeefc) ||
				(0xfa4a <= chrSJIS && chrSJIS <= 0xfa54) ||
				(0xfa58 <= chrSJIS && chrSJIS <= 0xfa5b);
		}
	}
}
