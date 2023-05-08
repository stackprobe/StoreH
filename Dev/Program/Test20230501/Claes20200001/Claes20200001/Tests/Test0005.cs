using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Charlotte.Tests
{
	public class Test0005
	{
		public void Test01()
		{
			Console.WriteLine(double.PositiveInfinity); // ∞

			double value = double.Parse("∞");
			Console.WriteLine(value); // ∞

			Console.WriteLine(double.NegativeInfinity); // -∞

			value = double.Parse("-∞");
			Console.WriteLine(value); // -∞

			Console.WriteLine(double.NaN); // NaN

			value = double.Parse("NaN");
			Console.WriteLine(value); // NaN
		}
	}
}
