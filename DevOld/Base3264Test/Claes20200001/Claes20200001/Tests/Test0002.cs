using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Drawing;
using Charlotte.Commons;
using Charlotte.Utilities;

namespace Charlotte.Tests
{
	public class Test0002
	{
		public void Test01()
		{
			for (int testcnt = 0; testcnt < 1000; testcnt++)
			{
				byte[] data = SCommon.CRandom.GetBytes(SCommon.CRandom.GetInt(100));

				byte[] enc1 = SCommon.Join(Base64.I.Encode(new Base64.Part[] { new Base64.Part(data) }).ToArray());
				byte[] enc2 = Encoding.ASCII.GetBytes(SCommon.Base64.I.Encode(data));

				if (SCommon.Comp(enc1, enc2) != 0) // ? 不一致
					throw null;

				byte[] dec1 = SCommon.Join(Base64.I.Decode(new Base64.Part[] { new Base64.Part(enc1) }).ToArray());

				if (SCommon.Comp(dec1, data) != 0) // ? 不一致
					throw null;
			}
			Console.WriteLine("OK! (TEST-0002-01)");
		}

		public void Test02()
		{
			Test02_a(3000, 10, 3000);
			Test02_a(1000, 30, 1000);
			Test02_a(300, 100, 300);
			Test02_a(100, 300, 100);
			Test02_a(30, 1000, 30);
			Test02_a(10, 3000, 10);

			Test02_a(300, 10, 3000);
			Test02_a(100, 30, 1000);
			Test02_a(30, 100, 300);
			Test02_a(10, 300, 100);

			Test02_a(30, 10, 3000);
			Test02_a(10, 30, 1000);

			Console.WriteLine("OK!");
		}

		private void Test02_a(int dataScale, int divideScale, int testCount)
		{
			Console.WriteLine(string.Join(", ", "TEST-0002-02", dataScale, divideScale, testCount));

			for (int testcnt = 0; testcnt < testCount; testcnt++)
			{
				byte[] data = SCommon.CRandom.GetBytes(SCommon.CRandom.GetInt(dataScale));

				Base64.Part[] divData = TEST_Divide(data, divideScale);

				byte[] enc1 = SCommon.Join(Base64.I.Encode(divData).ToArray());
				byte[] enc2 = Encoding.ASCII.GetBytes(SCommon.Base64.I.Encode(data));

				if (SCommon.Comp(enc1, enc2) != 0) // ? 不一致
					throw null;

				Base64.Part[] divEnc1 = TEST_Divide(enc1, divideScale);

				byte[] dec1 = SCommon.Join(Base64.I.Decode(divEnc1).ToArray());

				if (SCommon.Comp(dec1, data) != 0) // ? 不一致
					throw null;
			}
			Console.WriteLine("OK");
		}

		private Base64.Part[] TEST_Divide(byte[] data, int divideScale)
		{
			int divCount = SCommon.CRandom.GetInt(divideScale);
			byte[][] divs = new byte[][] { data };

			for (int count = 0; count < divCount; count++)
			{
				int index = SCommon.CRandom.GetInt(divs.Length);
				int divPos = SCommon.CRandom.GetInt(divs[index].Length + 1);

				List<byte[]> divList = new List<byte[]>();

				divList.AddRange(divs.Take(index));
				divList.Add(SCommon.GetPart(divs[index], 0, divPos));
				divList.Add(SCommon.GetPart(divs[index], divPos));
				divList.AddRange(divs.Skip(index + 1));

				divs = divList.ToArray();
			}

			return divs.Select(div =>
			{
				int a = SCommon.CRandom.GetInt(10);
				int b = SCommon.CRandom.GetInt(10);

				byte[] bytes = SCommon.Join(new byte[][]
				{
					SCommon.CRandom.GetBytes(a),
					div,
					SCommon.CRandom.GetBytes(b),
				});

				return new Base64.Part(bytes, a, div.Length);
			})
			.ToArray();
		}
	}
}
