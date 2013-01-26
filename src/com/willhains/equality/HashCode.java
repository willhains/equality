package com.willhains.equality;

import java.util.*;

/**
 * A utility for implementing near-ideal {@link Object#hashCode()} methods, using a clean, easy-to-read API. Sample:
 * 
 * <pre>
 * public int hashCode()
 * {
 *     return HashCode.compute()
 *         .with(super.hashCode()) // <-- Include super.hashCode() if your superclass implements it meaningfully
 *         .with(this.name)
 *         .with(this.productCode)
 *         .with(this.colour)
 *         .hashCode();
 * }
 * </pre>
 * 
 * This implementation is adapted from the recipe described in Effective
 * Java by Joshua Bloch, which is a fast, general-purpose hash algorithm. Use this implementation if you're not
 * sure, or if your class will be used as the key for relatively small hash maps.
 * 
 * @see Equals
 * @author willhains
 */
public final class HashCode
{
	// Hash code result
	private int _hashCode = 17;
	
	private HashCode()
	{
		// Use static factory methods
	}
	
	/**
	 * Initiates the computation of a hash code.
	 */
	public static HashCode compute()
	{
		return new HashCode();
	}
	
	// @formatter:off
	public HashCode with(boolean   n) { _hashCode = 31 * _hashCode + (n ? 31 : 127);                 return this; }
	public HashCode with(   byte   b) { _hashCode = 31 * _hashCode + b;                              return this; }
	public HashCode with(   char   c) { _hashCode = 31 * _hashCode + c;                              return this; }
	public HashCode with(  short   s) { _hashCode = 31 * _hashCode + s;                              return this; }
	public HashCode with(    int   i) { _hashCode = 31 * _hashCode + i;                              return this; }
	public HashCode with(  float   f) { _hashCode = 31 * _hashCode + Float.floatToIntBits(f);        return this; }
	public HashCode with(   long   l) { _hashCode = 31 * _hashCode + (int)(l ^ l >>> 32);            return this; }
	public HashCode with( double   d) { return with(Double.doubleToLongBits(d));                                  }
	public HashCode with( Object   o) { _hashCode = 31 * _hashCode + (o == null ? 0 : o.hashCode()); return this; }
	public HashCode with(boolean[] n) { _hashCode = 31 * _hashCode + Arrays.hashCode(n);             return this; }
	public HashCode with(   byte[] b) { _hashCode = 31 * _hashCode + Arrays.hashCode(b);             return this; }
	public HashCode with(   char[] c) { _hashCode = 31 * _hashCode + Arrays.hashCode(c);             return this; }
	public HashCode with(  short[] s) { _hashCode = 31 * _hashCode + Arrays.hashCode(s);             return this; }
	public HashCode with(    int[] i) { _hashCode = 31 * _hashCode + Arrays.hashCode(i);             return this; }
	public HashCode with(  float[] f) { _hashCode = 31 * _hashCode + Arrays.hashCode(f);             return this; }
	public HashCode with(   long[] l) { _hashCode = 31 * _hashCode + Arrays.hashCode(l);             return this; }
	public HashCode with( double[] d) { _hashCode = 31 * _hashCode + Arrays.hashCode(d);             return this; }
	public HashCode with( Object[] o) { _hashCode = 31 * _hashCode + Arrays.deepHashCode(o);         return this; }
	// @formatter:on
	
	/**
	 * @return the computed hash code.
	 */
	@Override
	public int hashCode()
	{
		return _hashCode;
	}
}
