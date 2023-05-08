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

			// 8790～879c, ed40～eefc, fa4a～fa54, fa58～fa5b のみ不可逆ということみたい。
		}
	}
}
