package com.willhains.equality;

import java.util.*;

/**
 * A utility for implementing near-ideal {@link Object#equals(Object)} methods, using a clean, easy-to-read API. Sample:
 * 
 * <pre>
 * public boolean equals(Object obj)
 * {
 *     Equals&lt;MyClass&gt; eq = Equals.compare(this, obj);
 *     return eq
 *         .and(this.name,        eq.that.name)      // <-- eq.that is guaranteed to never be null
 *         .and(this.productCode, eq.that.productCode)
 *         .and(this.colour,      eq.that.colour)
 *         .equals();
 * }
 * </pre>
 * 
 * Note: If your superclass contains state that is checked in its {@link Object#equals(Object)} implementation, you
 * should call it as follows:
 * 
 * <pre>
 * public boolean equals(Object obj)
 * {
 *     Equals&lt;MyClass&gt; eq = Equals.compare(this, obj);
 *     return eq
 *         .and(super.equals(obj))                   // <-- note the call to super.equals(obj)
 *         .and(this.name,        eq.that.name)
 *         .and(this.productCode, eq.that.productCode)
 *         .and(this.colour,      eq.that.colour)
 *         .equals();
 * }
 * </pre>
 * 
 * @param <V> the class whose {@link Object#equals(Object)} method is being implemented.
 * @see HashCode
 * @author willhains
 */
public final class Equals<V>
{
	// The equality comparison result: innocent until proven guilty
	private boolean _eq = true;
	
	// Short circuit for when it is pointless to continue comparison (false when conclusion is foregone)
	private boolean _ok = true;
	
	/**
	 * Object to be compared against, guaranteed to never be null. In reality, this might not be the object passed into
	 * the {@link Object#equals(Object)} method, but you can code as though it is. There is a magic trick here, so don't
	 * use it for anything except the call-chaining pattern documented above.
	 */
	public final V that;
	
	/**
	 * Initialises the state for the most efficient comparison. There are two possible scenarios:
	 * (a) we are comparing two different, but potentially equal objects, or
	 * (b) we can determine equality/inequality immediately.
	 */
	@SuppressWarnings("unchecked")
	private Equals(final V thiss, final Object obj, boolean strict)
	{
		// Scenario A: Comparing two different, but potentially equal objects
		if(thiss != obj && obj != null && thiss.getClass().isAssignableFrom(obj.getClass()))
		{
			// We will compare against the actual object
			that = (V)obj;
			
			// For stricter comparisons, short-circuit when the classes aren't exactly the same
			if(strict && obj.getClass() != thiss.getClass()) _eq = _ok = false;
		}
		
		// Scenario B: We can determine equality/inequality immediately and skip comparison of object state
		else
		{
			// Use 'thiss' as a decoy to guarantee 'that' will never be null
			that = thiss;
			
			// Determine equality/inequality now
			_eq = obj == thiss;
			
			// Skip all comparisons
			_ok = false;
		}
	}
	
	/**
	 * Initiates a comparison between two objects. This implementation counts objects as equal if the second object is
	 * an instance of the first object's class.
	 * 
	 * @param thiss the object where {@link Object#equals(Object)} is being implemented (ALWAYS pass 'this').
	 * @param obj the object passed into {@link Object#equals(Object)}.
	 */
	public static <V> Equals<V> compare(final V thiss, final Object obj)
	{
		assert thiss != null : "You must pass 'this' into the 'thiss' argument (so it should never be null).";
		return new Equals<V>(thiss, obj, false);
	}
	
	/**
	 * Initiates a comparison between two objects. This implementation counts objects as equal only if they are
	 * instances of exactly the same class.
	 * 
	 * @param thiss the object where {@link Object#equals(Object)} is being implemented (ALWAYS pass 'this').
	 * @param obj the object passed into {@link Object#equals(Object)}.
	 */
	public static <V> Equals<V> compareStrictly(final V thiss, final Object obj)
	{
		assert thiss != null : "You must pass 'this' into the 'thiss' argument (so it should never be null).";
		return new Equals<V>(thiss, obj, true);
	}
	
	/**
	 * Adds a pre-calculated equality condition to the overall evaluation of equality. Typically you would pass
	 * <code>super.equals(obj)</code> into this method, if your superclass implements {@link Object#equals(Object)}
	 * meaningfully.
	 * 
	 * @param equalityCondition a pre-calculated equality condition to include in the overall evaluation of equality.
	 */
	public Equals<V> and(boolean equalityCondition)
	{
		// Updates the state to skip subsequent comparisons once inequality is determined
		if(_ok && !equalityCondition) _eq = _ok = false;
		return this;
	}
	
	// @formatter:off
	public Equals<V> and(boolean   n, boolean   nn) { if(_ok) if(n != nn) _eq = _ok = false; return this; } 
	public Equals<V> and(   byte   b,    byte   bb) { if(_ok) if(b != bb) _eq = _ok = false; return this; }
	public Equals<V> and(   char   c,    char   cc) { if(_ok) if(c != cc) _eq = _ok = false; return this; }
	public Equals<V> and(  short   s,   short   ss) { if(_ok) if(s != ss) _eq = _ok = false; return this; }
	public Equals<V> and(    int   i,     int   ii) { if(_ok) if(i != ii) _eq = _ok = false; return this; }
	public Equals<V> and(   long   l,    long   ll) { if(_ok) if(l != ll) _eq = _ok = false; return this; }
	public Equals<V> and(  float   f,   float   ff) { if(_ok) if(f != ff) _eq = _ok = false; return this; }
	public Equals<V> and( double   d,  double   dd) { if(_ok) if(d != dd) _eq = _ok = false; return this; }
	public Equals<V> and( Object   o,  Object   oo) { if(_ok) if(o == null ? oo != null : !o.equals(oo)) _eq = _ok = false; return this; }
	public Equals<V> and(boolean[] n, boolean[] nn) { if(_ok) if(!Arrays.equals(n, nn)) _eq = _ok = false; return this; }
	public Equals<V> and(   byte[] b,    byte[] bb) { if(_ok) if(!Arrays.equals(b, bb)) _eq = _ok = false; return this; }
	public Equals<V> and(   char[] c,    char[] cc) { if(_ok) if(!Arrays.equals(c, cc)) _eq = _ok = false; return this; }
	public Equals<V> and(  short[] s,   short[] ss) { if(_ok) if(!Arrays.equals(s, ss)) _eq = _ok = false; return this; }
	public Equals<V> and(    int[] i,     int[] ii) { if(_ok) if(!Arrays.equals(i, ii)) _eq = _ok = false; return this; }
	public Equals<V> and(   long[] l,    long[] ll) { if(_ok) if(!Arrays.equals(l, ll)) _eq = _ok = false; return this; }
	public Equals<V> and(  float[] f,   float[] ff) { if(_ok) if(!Arrays.equals(f, ff)) _eq = _ok = false; return this; }
	public Equals<V> and( double[] d,  double[] dd) { if(_ok) if(!Arrays.equals(d, dd)) _eq = _ok = false; return this; }
	public Equals<V> and( Object[] o,  Object[] oo) { if(_ok) if(!Arrays.deepEquals(o, oo)) _eq = _ok = false; return this; }
	// @formatter:on
	
	/**
	 * Returns the result of the comparison.
	 */
	public boolean equals()
	{
		return _eq;
	}
}
