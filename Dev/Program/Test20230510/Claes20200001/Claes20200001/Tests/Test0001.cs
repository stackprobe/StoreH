using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Drawing;
using System.IO;
using Charlotte.Commons;

namespace Charlotte.Tests
{
	public class Test0001
	{
		private class EncodePairInfo
		{
			public int SJISChar;
			public int Unicode;
			public int SJISChar_R;
			public List<int> SJISChar_IRs = new List<int>();
		}

		public void Test01()
		{
			EncodePairInfo[] rows = SCommon.GetJCharCodes().Select(chrSJIS =>
			{
				char unicode = SJISCharToUnicode(chrSJIS);
				UInt16 chrSJIS_R = UnicodeToSJISChar(unicode);

				return new EncodePairInfo()
				{
					SJISChar = (int)chrSJIS,
					Unicode = (int)unicode,
					SJISChar_R = (int)chrSJIS_R,
				};
			})
			.ToArray();

			foreach (var row in rows)
				if (row.SJISChar == row.SJISChar_R)
					row.SJISChar_R = -1;

			foreach (var row in rows)
			{
				if (row.SJISChar_R != -1)
				{
					int index = SCommon.IndexOf(rows, r =>
						r.SJISChar != -1 &&
						r.Unicode == row.Unicode &&
						r.SJISChar_R == -1
						);

					if (index == -1)
						throw null;

					rows[index].SJISChar_IRs.Add(row.SJISChar);
					row.SJISChar = -1;
					//row.Unicode = -1;
					//row.SJISChar_R = -1;
					//row.SJISChar_IRs = null;
				}
			}
			rows = rows.Where(row => row.SJISChar != -1).ToArray();

			File.WriteAllLines(SCommon.NextOutputPath() + ".txt", rows.Select(row =>
			{
				string line;

				if (row.SJISChar_IRs.Count == 0)
					line = string.Format("[{0:x4}] <<---->> [{1:x4}]", row.SJISChar, row.Unicode);
				else
					line = string.Format("[{0:x4}] <<---->> [{1:x4}] <<------ {2}"
						, row.SJISChar
						, row.Unicode
						, string.Join(", ", row.SJISChar_IRs.Select(chr => string.Format("[{0:x4}]", chr))));

				return line;
			})
			, SCommon.ENCODING_SJIS);
		}

		private char SJISCharToUnicode(UInt16 chrSJIS)
		{
			byte[] bytes = new byte[]
			{
				(byte)(chrSJIS >> 8),
				(byte)(chrSJIS & 0xff),
			};

			string str = SCommon.ENCODING_SJIS.GetString(bytes);

			if (str.Length != 1)
				throw null;

			return str[0];
		}

		private UInt16 UnicodeToSJISChar(char unicode)
		{
			string str = new string(new char[] { unicode });
			byte[] bytes = SCommon.ENCODING_SJIS.GetBytes(str);

			if (bytes.Length != 2)
				throw null;

			UInt16 chrSJIS = (UInt16)(((int)bytes[0] << 8) | (int)bytes[1]);
			return chrSJIS;
		}

		public void Test02()
		{
			EncodePairInfo[] rows = SCommon.GetJCharCodes().Select(chrSJIS =>
			{
				char unicode = SJISCharToUnicode(chrSJIS);
				UInt16 chrSJIS_R = UnicodeToSJISChar(unicode);

				return new EncodePairInfo()
				{
					SJISChar = (int)chrSJIS,
					Unicode = (int)unicode,
					SJISChar_R = (int)chrSJIS_R,
				};
			})
			.ToArray();

			File.WriteAllLines(
				SCommon.NextOutputPath() + ".txt",
				rows.Select(row => string.Format("[{0:x4}] ---->> [{1:x4}]", row.SJISChar, row.Unicode)),
				SCommon.ENCODING_SJIS
				);

			rows = rows.DistinctOrderBy((a, b) => a.Unicode - b.Unicode).ToArray();

			File.WriteAllLines(
				SCommon.NextOutputPath() + ".txt",
				rows.Select(row => string.Format("[{0:x4}] ---->> [{1:x4}]", row.Unicode, row.SJISChar_R)),
				SCommon.ENCODING_SJIS
				);
		}
	}
}
