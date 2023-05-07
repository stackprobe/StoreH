using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Charlotte.Commons;

namespace Charlotte.Utilities
{
	public class Base64
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

			public byte[] ToArray()
			{
				return SCommon.GetPart(this.Bytes, this.Offset, this.Size);
			}
		}

		private static Base64 _i = null;

		public static Base64 I
		{
			get
			{
				if (_i == null)
					_i = new Base64();

				return _i;
			}
		}

		private const byte CHAR_PADDING = (byte)'=';

		private byte[] Chars;
		private int[] CharMap;

		private Base64()
		{
			this.Chars = Encoding.ASCII.GetBytes(SCommon.ALPHA_UPPER + SCommon.ALPHA_LOWER + SCommon.DECIMAL + "+/");
			this.CharMap = new int[256];

			for (int index = 0; index < this.Chars.Length; index++)
			{
				this.CharMap[(int)this.Chars[index]] = index;
			}
		}

		public IEnumerable<byte[]> Encode(IEnumerable<Part> src)
		{
			const int B_SZ = 3;

			if (src == null)
				src = new Part[0];

			Part data1 = new Part(new byte[0]);
			Part data2;
			Part data3;
			Part data4;

			foreach (Part srcPart in src)
			{
				if (srcPart == null)
					continue;

				if (data1.Size + srcPart.Size < B_SZ)
				{
					data4 = new Part(SCommon.Join(new byte[][] { data1.ToArray(), srcPart.ToArray() }));
				}
				else
				{
					int size1 = data1.Size;
					int size2 = B_SZ - size1;
					int size3 = srcPart.Size - size2;
					int size4 = size3 % B_SZ;
					size3 -= size4;

					data2 = new Part(SCommon.Join(new byte[][] { data1.ToArray(), SCommon.GetPart(srcPart.Bytes, srcPart.Offset, size2) }));
					data3 = new Part(srcPart.Bytes, srcPart.Offset + size2, size3);
					data4 = new Part(srcPart.Bytes, srcPart.Offset + size2 + size3, size4);

					yield return EncodeEven(data2);
					yield return EncodeEven(data3);
				}
				data1 = data4;
			}
			if (1 <= data1.Size)
			{
				yield return EncodeOdd(data1.ToArray());
			}
		}

		private byte[] EncodeEven(Part src)
		{
			byte[] dest = new byte[(src.Size / 3) * 4];
			int reader = src.Offset;
			int writer = 0;
			uint value;

			while (reader < src.Offset + src.Size)
			{
				value = (uint)src.Bytes[reader++] << 16;
				value |= (uint)src.Bytes[reader++] << 8;
				value |= (uint)src.Bytes[reader++];

				dest[writer++] = this.Chars[(value >> 18) & 0x3f];
				dest[writer++] = this.Chars[(value >> 12) & 0x3f];
				dest[writer++] = this.Chars[(value >> 6) & 0x3f];
				dest[writer++] = this.Chars[value & 0x3f];
			}
			return dest;
		}

		private byte[] EncodeOdd(byte[] oddBytes)
		{
			byte[] block = new byte[3];

			for (int index = 0; index < oddBytes.Length; index++)
				block[index] = oddBytes[index];

			byte[] encBlock = EncodeEven(new Part(block));

			for (int index = 4 - ((3 - oddBytes.Length) * 4) / 3; index < 4; index++)
				encBlock[index] = CHAR_PADDING;

			return encBlock;
		}

		public IEnumerable<byte[]> Decode(IEnumerable<Part> src)
		{
			const int B_SZ = 4;

			if (src == null)
				src = new Part[0];

			Part data1 = new Part(new byte[0]);
			Part data2;
			Part data3;
			Part data4;

			foreach (Part f_srcPart in src)
			{
				Part srcPart = f_srcPart;

				if (srcPart == null)
					continue;

				srcPart = RemovePadding(srcPart);

				if (data1.Size + srcPart.Size < B_SZ)
				{
					data4 = new Part(SCommon.Join(new byte[][] { data1.ToArray(), srcPart.ToArray() }));
				}
				else
				{
					int size1 = data1.Size;
					int size2 = B_SZ - size1;
					int size3 = srcPart.Size - size2;
					int size4 = size3 % B_SZ;
					size3 -= size4;

					data2 = new Part(SCommon.Join(new byte[][] { data1.ToArray(), SCommon.GetPart(srcPart.Bytes, srcPart.Offset, size2) }));
					data3 = new Part(srcPart.Bytes, srcPart.Offset + size2, size3);
					data4 = new Part(srcPart.Bytes, srcPart.Offset + size2 + size3, size4);

					yield return DecodeEven(data2);
					yield return DecodeEven(data3);
				}
				data1 = data4;
			}
			if (1 <= data1.Size)
			{
				yield return DecodeOdd(data1.ToArray());
			}
		}

		private byte[] DecodeEven(Part src)
		{
			byte[] data = new byte[(src.Size / 4) * 3];
			int reader = src.Offset;
			int writer = 0;
			uint value;

			while (reader < src.Offset + src.Size)
			{
				value = (uint)this.CharMap[(int)src.Bytes[reader++]] << 18;
				value |= (uint)this.CharMap[(int)src.Bytes[reader++]] << 12;
				value |= (uint)this.CharMap[(int)src.Bytes[reader++]] << 6;
				value |= (uint)this.CharMap[(int)src.Bytes[reader++]];

				data[writer++] = (byte)((value >> 16) & 0xff);
				data[writer++] = (byte)((value >> 8) & 0xff);
				data[writer++] = (byte)(value & 0xff);
			}
			return data;
		}

		private byte[] DecodeOdd(byte[] oddBytes)
		{
			byte[] block = new byte[4];

			for (int index = 0; index < oddBytes.Length; index++)
				block[index] = oddBytes[index];

			byte[] decBlock = DecodeEven(new Part(block));
			decBlock = SCommon.GetPart(decBlock, 0, (oddBytes.Length * 3) / 4);
			return decBlock;
		}

		private Part RemovePadding(Part encPart)
		{
			const int P_SZ_M = 2;
			int c;

			for (c = 0; c < P_SZ_M && c < encPart.Size; c++)
				if (encPart.Bytes[encPart.Offset + encPart.Size - c - 1] != CHAR_PADDING)
					break;

			if (1 <= c)
				encPart = new Part(encPart.Bytes, encPart.Offset, encPart.Size - c);

			return encPart;
		}
	}
}
