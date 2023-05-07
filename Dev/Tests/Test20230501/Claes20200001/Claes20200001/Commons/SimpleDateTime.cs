using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Charlotte.Commons
{
	/// <summary>
	/// 日時の範囲：1/1/1 00:00:00 ～ 922337203/12/31 23:59:59
	/// </summary>
	public class SimpleDateTime
	{
		public readonly int Year;
		public readonly int Month;
		public readonly int Day;
		public readonly int Hour;
		public readonly int Minute;
		public readonly int Second;
		public readonly string Weekday;

		public static SimpleDateTime Now()
		{
			return new SimpleDateTime(DateTime.Now);
		}

		public static SimpleDateTime FromTimeStamp(long timeStamp)
		{
			return new SimpleDateTime(SCommon.TimeStampToSec.ToSec(timeStamp));
		}

		public static SimpleDateTime FromSec(long sec)
		{
			return new SimpleDateTime(sec);
		}

		public SimpleDateTime(DateTime dateTime)
			: this(SCommon.TimeStampToSec.ToSec(dateTime))
		{ }

		private SimpleDateTime(long sec)
		{
			long timeStamp = SCommon.TimeStampToSec.ToTimeStamp(sec);
			long t = timeStamp;

			this.Second = (int)(t % 100);
			t /= 100;
			this.Minute = (int)(t % 100);
			t /= 100;
			this.Hour = (int)(t % 100);
			t /= 100;
			this.Day = (int)(t % 100);
			t /= 100;
			this.Month = (int)(t % 100);
			this.Year = (int)(t / 100);

			this.Weekday = new string(new char[] { "月火水木金土日"[(int)((SCommon.TimeStampToSec.ToSec(timeStamp) / 86400) % 7)] });
		}

		public override string ToString()
		{
			return this.ToString("{0}/{1:D2}/{2:D2} ({3}) {4:D2}:{5:D2}:{6:D2}");
		}

		public string ToString(string format)
		{
			return string.Format(format, this.Year, this.Month, this.Day, this.Weekday, this.Hour, this.Minute, this.Second);
		}

		public DateTime ToDateTime()
		{
			return new DateTime(this.Year, this.Month, this.Day, this.Hour, this.Minute, this.Second);
		}

		public long ToTimeStamp()
		{
			return
				10000000000L * this.Year +
				100000000L * this.Month +
				1000000L * this.Day +
				10000L * this.Hour +
				100L * this.Minute +
				this.Second;
		}

		public long ToSec()
		{
			return SCommon.TimeStampToSec.ToSec(this.ToTimeStamp());
		}

		public static SimpleDateTime operator +(SimpleDateTime instance, long sec)
		{
			return new SimpleDateTime(instance.ToSec() + sec);
		}

		public static SimpleDateTime operator +(long sec, SimpleDateTime instance)
		{
			return new SimpleDateTime(instance.ToSec() + sec);
		}

		public static SimpleDateTime operator -(SimpleDateTime instance, long sec)
		{
			return new SimpleDateTime(instance.ToSec() - sec);
		}

		public static long operator -(SimpleDateTime a, SimpleDateTime b)
		{
			return a.ToSec() - b.ToSec();
		}

		private long GetValueForCompare()
		{
			return this.ToTimeStamp();
		}

		public static bool operator ==(SimpleDateTime a, SimpleDateTime b)
		{
			return a.GetValueForCompare() == b.GetValueForCompare();
		}

		public static bool operator !=(SimpleDateTime a, SimpleDateTime b)
		{
			return a.GetValueForCompare() != b.GetValueForCompare();
		}

		public override bool Equals(object another)
		{
			return another is SimpleDateTime && this == (SimpleDateTime)another;
		}

		public override int GetHashCode()
		{
			return this.GetValueForCompare().GetHashCode();
		}

		public static bool operator <(SimpleDateTime a, SimpleDateTime b)
		{
			return a.GetValueForCompare() < b.GetValueForCompare();
		}

		public static bool operator >(SimpleDateTime a, SimpleDateTime b)
		{
			return a.GetValueForCompare() > b.GetValueForCompare();
		}

		public static bool operator <=(SimpleDateTime a, SimpleDateTime b)
		{
			return a.GetValueForCompare() <= b.GetValueForCompare();
		}

		public static bool operator >=(SimpleDateTime a, SimpleDateTime b)
		{
			return a.GetValueForCompare() >= b.GetValueForCompare();
		}
	}
}
