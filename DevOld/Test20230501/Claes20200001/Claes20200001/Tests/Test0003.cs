using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Numerics;
using Charlotte.Commons;

namespace Charlotte.Tests
{
	public class Test0003
	{
		public void Test01()
		{
			Test01_a(1, 100000UL, 1UL);
			Test01_a(1, 1000000UL, 10UL);
			Test01_a(1, 10000000UL, 100UL);
			Test01_a(1, 100000000UL, 1000UL);
			Test01_a(1, 1000000000UL, 10000UL);
			Test01_a(1, 10000000000UL, 100000UL);
			Test01_a(1, 100000000000UL, 1000000UL);
			Test01_a(1, 1000000000000UL, 10000000UL);
			Test01_a(1, 10000000000000UL, 100000000UL);
			Test01_a(1, 100000000000000UL, 1000000000UL);
			Test01_a(1, 1000000000000000UL, 10000000000UL);
			Test01_a(1, 10000000000000000UL, 100000000000UL);
			Test01_a(1, 100000000000000000UL, 1000000000000UL);
			Test01_a(1, 1000000000000000000UL, 10000000000000UL);
			Test01_a(1, 18440000000000000000UL, 100000000000000UL);

			for (int count = 0; count < 1000000; count++)
			{
				Test01_b(ulong.MaxValue - (ulong)count);
			}
			Console.WriteLine("OK!");
		}

		private void Test01_a(ulong start, ulong end, ulong stepScale)
		{
			Console.WriteLine(start + ", " + end + ", " + stepScale); // cout

			for (int testcnt = 0; testcnt < 5; testcnt++)
			{
				ulong count;
				for (count = start; count <= end; count++)
				{
					Test01_b(count);
					count += Rand.Next() % stepScale + 1;
				}
				Console.WriteLine(count); // cout
			}
			Console.WriteLine("OK");
		}

		private void Test01_b(ulong count)
		{
			ulong ans1 = GetI2P64Mod(count);
			ulong ans2 = GetI2P64Mod_ChatGPT(count);

			if (ans1 != ans2)
				throw null;
		}

		private ulong GetI2P64Mod(ulong mod)
		{
			return (ulong.MaxValue % mod + 1) % mod;
		}

		private ulong GetI2P64Mod_ChatGPT(ulong mod)
		{
			ulong x = mod;
			BigInteger pow = BigInteger.Pow(2, 64);
			BigInteger remainder = pow % x;
			ulong result = (ulong)remainder;
			return result;
		}

		private XorShoft64 Rand = new XorShoft64();

		private class XorShoft64
		{
			private ulong State = 1;

			public ulong Next()
			{
				State ^= State << 13;
				State ^= State >> 7;
				State ^= State << 17;
				return State;
			}
		}
	}
}
