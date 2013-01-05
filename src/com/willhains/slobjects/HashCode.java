/*
 * Significant portions of this file were adapted from http://people.apache.org/~yonik/code/hash/Hash.java
 * which is distributed under the Apache License, Version 2.0: http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package com.willhains.slobjects;

import java.util.*;

/**
 * A utility for implementing near-ideal {@link Object#hashCode()} methods, using a clean, easy-to-read API. Sample:
 * 
 * <pre>
 * public int hashCode()
 * {
 *     return new HashCode()
 *         .with(super.hashCode()) // Include super.hashCode() if your superclass implements it meaningfully
 *         .with(this.name)
 *         .with(this.productCode)
 *         .with(this.colour)
 *         .hashCode();
 * }
 * </pre>
 * 
 * This implementation uses an adaptation of Bob Jenkins's "lookup3" hash algorithm, with some tweaks for performance in
 * Java.
 * 
 * @author willhains
 */
public final class HashCode
{
	// Collect a stream of bits for hashing at the end
	private int[] data = new int[4];
	private int length = 0;
	
	private HashCode _with(final int datum)
	{
		// Add the new value to the stream of bits
		data[length++] = datum;
		
		// Resize the stream of bits as needed
		if(length >= data.length)
		{
			final int[] bigger = new int[data.length * 2];
			System.arraycopy(data, 0, bigger, 0, data.length);
			data = bigger;
		}
		return this;
	}
	
	// @formatter:off
	public HashCode with(  byte   b) { return _with(b); }
	public HashCode with(  char   c) { return _with(c); }
	public HashCode with( short   s) { return _with(s); }
	public HashCode with(   int   i) { return _with(i); } 
	public HashCode with(  long   l) { return _with((int)l)._with((int)(l >>>32)); }
	public HashCode with( float   f) { return _with(Float.floatToIntBits(f)); }
	public HashCode with(double   d) { return with(Double.doubleToLongBits(d)); }
	public HashCode with(Object   o) { return _with(o == null ? 0 : o.hashCode()); }
	public HashCode with(  byte[] b) { return _with(Arrays.hashCode(b)); }
	public HashCode with(  char[] c) { return _with(Arrays.hashCode(c)); }
	public HashCode with( short[] s) { return _with(Arrays.hashCode(s)); }
	public HashCode with(   int[] i) { return _with(Arrays.hashCode(i)); } 
	public HashCode with(  long[] l) { return _with(Arrays.hashCode(l)); }
	public HashCode with( float[] f) { return _with(Arrays.hashCode(f)); }
	public HashCode with(double[] d) { return _with(Arrays.hashCode(d)); }
	public HashCode with(Object[] o) { return _with(Arrays.deepHashCode(o)); }
	// @formatter:on
	
	@Override
	@SuppressWarnings("fallthrough")
	public int hashCode()
	{
		int a, b, c;
		a = b = c = 486187739 + (length << 2) + 92821;
		
		int i = 0;
		while(length > 3)
		{
			a += data[i];
			b += data[i + 1];
			c += data[i + 2];
			
			// Note: recent JVMs (Sun JDK6) turn pairs of shifts (needed to do a rotate)
			// into real x86 rotate instructions.
			// @formatter:off
			a -= c; a ^= c << 4  | c >>> -4;  c += b;
			b -= a; b ^= a << 6  | a >>> -6;  a += c;
			c -= b; c ^= b << 8  | b >>> -8;  b += a;
			a -= c; a ^= c << 16 | c >>> -16; c += b;
			b -= a; b ^= a << 19 | a >>> -19; a += c;
			c -= b; c ^= b << 4  | b >>> -4;  b += a;
			// @formatter:on
			
			length -= 3;
			i += 3;
		}
		
		switch(length)
		{
			case 3:
				c += data[i + 2];
				// fall through
			case 2:
				b += data[i + 1];
				// fall through
			case 1:
				a += data[i + 0];
				// @formatter:off
				c ^= b; c -= b << 14 | b >>> -14;
				a ^= c; a -= c << 11 | c >>> -11;
				b ^= a; b -= a << 25 | a >>> -25;
				c ^= b; c -= b << 16 | b >>> -16;
				a ^= c; a -= c << 4  | c >>> -4;
				b ^= a; b -= a << 14 | a >>> -14;
				c ^= b; c -= b << 24 | b >>> -24;
				// @formatter:on
				// fall through
		}
		return c;
	}
}
