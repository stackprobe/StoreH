using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Charlotte.Commons;

namespace Charlotte.XUtilities
{
	public class Base32
	{
		public class Part
		{
			public readonly byte[] Bytes;
			public readonly int Offset;
			public readonly int Size;

			public Part(byte[] bytes)
				: this(bytes, 0)
			{ }

			public Part(byte[] bytes, int offset)
				: this(bytes, offset, bytes.Length - offset)
			{ }

			public Part(byte[] bytes, int offset, int size)
			{
				if (
					bytes == null ||
					offset < 0 || bytes.Length < offset ||
					size < 0 || bytes.Length - offset < size
					)
					throw new Exception("Bad params");

				this.Bytes = bytes;
				this.Offset = offset;
				this.Size = size;
			}
		}

		private static Base32 _i = null;

		public static Base32 I
		{
			get
			{
				if (_i == null)
					_i = new Base32();

				return _i;
			}
		}

		private const int CHAR_MAP_SIZE = 256;
		private const byte CHAR_PADDING = (byte)'=';

		private byte[] Chars;
		private int[] CharMap;

		private Base32()
		{
			this.Chars = Encoding.ASCII.GetBytes(SCommon.ALPHA_UPPER + SCommon.DECIMAL.Substring(2, 6));
			this.CharMap = new int[CHAR_MAP_SIZE];

			for (int index = 0; index < CHAR_MAP_SIZE; index++)
				this.CharMap[index] = 0xff;

			for (int index = 0; index < this.Chars.Length; index++)
				this.CharMap[(int)this.Chars[index]] = index;
		}

		public IEnumerable<byte[]> Encode(IEnumerable<Part> src)
		{
			if (src == null)
				src = new Part[0];

			byte[] oddBlock = new byte[5];
			int oddPartSize = 0;

			foreach (Part f_srcPart in src)
			{
				Part srcPart = f_srcPart;

				if (srcPart == null)
					continue;

				if (oddPartSize + srcPart.Size < 5)
				{
					Array.Copy(oddBlock, oddPartSize, srcPart.Bytes, srcPart.Offset, srcPart.Size);
					oddPartSize += srcPart.Size;
					continue;
				}
				if (1 <= oddPartSize)
				{
					int s = 5 - oddPartSize;
					Array.Copy(srcPart.Bytes, srcPart.Offset, oddBlock, oddPartSize, s);
					yield return EncodeEven(new Part(oddBlock, 0, 5));
					oddPartSize = 0;
					srcPart = new Part(srcPart.Bytes, srcPart.Offset + s, srcPart.Size - s);
				}
				if (srcPart.Size % 5 != 0)
				{
					oddPartSize = srcPart.Size % 5;
					Array.Copy(srcPart.Bytes, srcPart.Offset + srcPart.Size - oddPartSize, oddBlock, 0, oddPartSize);
					srcPart = new Part(srcPart.Bytes, srcPart.Offset, srcPart.Size - oddPartSize);
				}
				yield return EncodeEven(srcPart);
			}
			if (1 <= oddPartSize)
			{
				Array.Clear(oddBlock, oddPartSize, 5 - oddPartSize);
				byte[] odd = EncodeEven(new Part(oddBlock, 0, 5));

				for (int index = 8 - ((5 - oddPartSize) * 8) / 5; index < 8; index++)
					odd[index] = CHAR_PADDING;

				yield return odd;
			}
		}

		private byte[] EncodeEven(Part src)
		{
			byte[] dest = new byte[(src.Size / 5) * 8];
			int reader = src.Offset;
			int writer = 0;
			ulong value;

			while (reader < src.Offset + src.Size)
			{
				value = (ulong)src.Bytes[reader++] << 32;
				value |= (ulong)src.Bytes[reader++] << 24;
				value |= (ulong)src.Bytes[reader++] << 16;
				value |= (ulong)src.Bytes[reader++] << 8;
				value |= (ulong)src.Bytes[reader++];

				dest[writer++] = this.Chars[(value >> 35) & 0x1f];
				dest[writer++] = this.Chars[(value >> 30) & 0x1f];
				dest[writer++] = this.Chars[(value >> 25) & 0x1f];
				dest[writer++] = this.Chars[(value >> 20) & 0x1f];
				dest[writer++] = this.Chars[(value >> 15) & 0x1f];
				dest[writer++] = this.Chars[(value >> 10) & 0x1f];
				dest[writer++] = this.Chars[(value >> 5) & 0x1f];
				dest[writer++] = this.Chars[value & 0x1f];
			}
			return dest;
		}

		public IEnumerable<byte[]> Decode(IEnumerable<byte[]> src)
		{
			throw null; // TODO
		}
	}
}
