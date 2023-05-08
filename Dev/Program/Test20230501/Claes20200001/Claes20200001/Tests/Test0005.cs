﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Charlotte.Commons;

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

			// ----

			Console.WriteLine(double.NegativeInfinity < double.PositiveInfinity); // True
			Console.WriteLine(double.NegativeInfinity > double.PositiveInfinity); // False
			Console.WriteLine(double.NaN < double.PositiveInfinity); // False
			Console.WriteLine(double.NaN > double.PositiveInfinity); // False
			Console.WriteLine(double.NaN < double.NegativeInfinity); // False
			Console.WriteLine(double.NaN > double.NegativeInfinity); // False
			Console.WriteLine(double.NaN < double.NaN); // False
			Console.WriteLine(double.NaN > double.NaN); // False

			// ----

			Console.WriteLine(SCommon.ToDouble("∞", double.NegativeInfinity, double.PositiveInfinity, 123.4)); // 123.4
			Console.WriteLine(SCommon.ToDouble("77.88", double.NegativeInfinity, double.PositiveInfinity, 9.111)); // 77.88
		}
	}
}