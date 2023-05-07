using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Charlotte.Commons;

namespace Charlotte.Tests
{
	/// <summary>
	/// SimpleDateTime テスト
	/// </summary>
	public class Test0012
	{
		public void Test01()
		{
			Console.WriteLine(SimpleDateTime.Now().ToDateTime());
			Console.WriteLine(new SimpleDateTime(SimpleDateTime.Now().ToDateTime()));

			Console.WriteLine("done! (TEST-0012-01)");
		}
	}
}
