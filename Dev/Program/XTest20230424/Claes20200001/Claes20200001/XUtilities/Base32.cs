using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Charlotte.Commons;

namespace Charlotte.XUtilities
{
	public class Base32
	{
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

		public byte[] Encode(byte[] src)
		{
			if (src == null)
				src = SCommon.EMPTY_BYTES;

			byte[] dest = new byte[((src.Length + 4) / 5) * 8];

			if (src.Length % 5 == 0)
			{
				EncodeEven(src, 0, src.Length, dest, 0);
			}
			else
			{
				EncodeEven(src, 0, src.Length - src.Length % 5, dest, 0);

				byte[] finalBlock = SCommon.Join(new byte[][]
				{
					SCommon.GetPart(src,src.Length - src.Length % 5),
					Enumerable.Repeat((byte)0, 5 - src.Length % 5).ToArray(),
				});

				EncodeEven(finalBlock, 0, 5, dest, dest.Length - 8);

				for (int index = dest.Length - ((5 - src.Length % 5) * 8) / 5; index < dest.Length; index++)
					dest[index] = CHAR_PADDING;
			}
			return dest;
		}

		private void EncodeEven(byte[] src, int reader, int readerEnd, byte[] dest, int writer)
		{
			ulong value;

			while (reader < readerEnd)
			{
				value = (ulong)src[reader++] << 32;
				value |= (ulong)src[reader++] << 24;
				value |= (ulong)src[reader++] << 16;
				value |= (ulong)src[reader++] << 8;
				value |= (ulong)src[reader++];

				dest[writer++] = this.Chars[(value >> 35) & 0x1f];
				dest[writer++] = this.Chars[(value >> 30) & 0x1f];
				dest[writer++] = this.Chars[(value >> 25) & 0x1f];
				dest[writer++] = this.Chars[(value >> 20) & 0x1f];
				dest[writer++] = this.Chars[(value >> 15) & 0x1f];
				dest[writer++] = this.Chars[(value >> 10) & 0x1f];
				dest[writer++] = this.Chars[(value >> 5) & 0x1f];
				dest[writer++] = this.Chars[value & 0x1f];
			}
		}

		public byte[] Decode(byte[] src)
		{
			return SCommon.Join(DecodeTwoBlock(src));
		}

		public byte[][] DecodeTwoBlock(byte[] src)
		{
			if (src == null)
				src = SCommon.EMPTY_BYTES;

			int size = src.Length;

			while (1 <= size && src[size - 1] == CHAR_PADDING)
				size--;

			byte[] dest1 = new byte[(size / 8) * 5];
			byte[] dest2 = new byte[0];

			if (size % 8 == 0)
			{
				DecodeEven(src, 0, size, dest1, 0);
			}
			else
			{
				DecodeEven(src, 0, size - size % 8, dest1, 0);

				byte[] finalBlock = SCommon.Join(new byte[][]
				{
					SCommon.GetPart(src, size - size % 8),
					Enumerable.Repeat(this.Chars[0], 8 - size % 8).ToArray(),
				});

				dest2 = new byte[5];
				DecodeEven(finalBlock, 0, 8, dest2, 0);
				dest2 = SCommon.GetPart(dest2, 0, ((size % 8) * 5) / 8);
			}
			return new byte[][] { dest1, dest2 };
		}

		private void DecodeEven(byte[] src, int reader, int readerEnd, byte[] dest, int writer)
		{
			ulong value;

			while (reader < readerEnd)
			{
				value = (ulong)(uint)this.CharMap[src[reader++]] << 35;
				value |= (ulong)(uint)this.CharMap[src[reader++]] << 30;
				value |= (ulong)(uint)this.CharMap[src[reader++]] << 25;
				value |= (ulong)(uint)this.CharMap[src[reader++]] << 20;
				value |= (ulong)(uint)this.CharMap[src[reader++]] << 15;
				value |= (ulong)(uint)this.CharMap[src[reader++]] << 10;
				value |= (ulong)(uint)this.CharMap[src[reader++]] << 5;
				value |= (ulong)(uint)this.CharMap[src[reader++]];

				dest[writer++] = (byte)((value >> 32) & 0xff);
				dest[writer++] = (byte)((value >> 24) & 0xff);
				dest[writer++] = (byte)((value >> 16) & 0xff);
				dest[writer++] = (byte)((value >> 8) & 0xff);
				dest[writer++] = (byte)(value & 0xff);
			}
		}
	}
}
