using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Drawing;
using Charlotte.Commons;
using Charlotte.ZUtilities;

namespace Charlotte.Tests
{
	public class Test0001
	{
		public void Test01()
		{
			for (int testcnt = 0; testcnt < 1000; testcnt++)
			{
				byte[] data = SCommon.CRandom.GetBytes(SCommon.CRandom.GetInt(100));

				byte[] enc1 = SCommon.Join(Base32.I.Encode(new Base32.Part[] { new Base32.Part(data) }).ToArray());
				byte[] enc2 = Encoding.ASCII.GetBytes(SCommon.Base32.I.Encode(data));

				if (SCommon.Comp(enc1, enc2) != 0) // ? 不一致
					throw null;
			}
			Console.WriteLine("OK! (TEST-0001-01)");
		}
	}
}
