package com.willhains.equality;

import static java.util.stream.Collectors.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

/**
 * A fast, safe, helpful utility for implementing {@link Object#equals} and
 * {@link Object#hashCode} easily and correctly. See {@link #ofProperties} for
 * usage.
 * 
 * @param <T> your class.
 * @author willhains
 */
public interface Equality<T> extends Supplier<Function<T, ?>[]>
{
	/**
	 * Create an {@link Equality} instance for your class, by supplying an array
	 * of functions for retrieving the values of significant instance variables.
	 * <pre>
	 * private static final Equality&lt;MyClass&gt; EQ = Equality.ofProperties(
	 *     $ -> $.instanceVariable1,
	 *     $ -> $.instanceVariable2); // etc.
	 * </pre>
	 * See {@link #compare} and {@link #hash} for how to use this in your
	 * {@link Object#equals} and {@link Object#hashCode} implementations.
	 */
	@SafeVarargs
	static <T> Equality<T> ofProperties(final Function<T, ?>... properties)
	{
		return () -> properties;
	}
	
	/**
	 * Compare {@code self} to {@code other} for equality.
	 * <pre>
	 * public boolean equals(Object other) { return EQ.compare(this, other); }
	 * </pre>
	 */
	default boolean compare(final T self, final Object other)
	{
		if(other == self) return true;
		if(other == null) return false;
		if(!self.getClass().equals(other.getClass())) return false;
		final T that = (T)other;
		for(final Function<T, ?> property: get())
		{
			final Object v1 = property.apply(self);
			final Object v2 = property.apply(that);
			if(v1 == null)
			{
				if(v2 == null) continue;
				return false;
			}
			if(v2 == null) return false;
			if(v1.equals(v2)) continue;
			if(v1 instanceof boolean[] && Arrays.equals((boolean[])v1, (boolean[])v2)) continue;
			if(v1 instanceof byte[] && Arrays.equals((byte[])v1, (byte[])v2)) continue;
			if(v1 instanceof short[] && Arrays.equals((short[])v1, (short[])v2)) continue;
			if(v1 instanceof char[] && Arrays.equals((char[])v1, (char[])v2)) continue;
			if(v1 instanceof int[] && Arrays.equals((int[])v1, (int[])v2)) continue;
			if(v1 instanceof long[] && Arrays.equals((long[])v1, (long[])v2)) continue;
			if(v1 instanceof float[] && Arrays.equals((float[])v1, (float[])v2)) continue;
			if(v1 instanceof double[] && Arrays.equals((double[])v1, (double[])v2)) continue;
			if(v1 instanceof Object[] && Arrays.deepEquals((Object[])v1, (Object[])v2)) continue;
			return false;
		}
		return true;
	}
	
	/**
	 * Compute a hash code for {@code self}.
	 * <pre>
	 * public int hashCode() { return EQ.hash(this); }
	 * </pre>
	 */
	default int hash(final T self)
	{
		// Modified form of Joshua Bloch's hash algorithm
		int hash = 17;
		hash += 13 * self.getClass().hashCode();
		for(final Function<T, ?> property: get())
		{
			final Object value = property.apply(self);
			final int propertyHash;
			if(value == null) propertyHash = 7;
			else if(value instanceof Object[]) propertyHash = Arrays.deepHashCode((Object[])value);
			else if(value instanceof int[]) propertyHash = Arrays.hashCode((int[])value);
			else if(value instanceof long[]) propertyHash = Arrays.hashCode((long[])value);
			else if(value instanceof boolean[]) propertyHash = Arrays.hashCode((boolean[])value);
			else if(value instanceof double[]) propertyHash = Arrays.hashCode((double[])value);
			else if(value instanceof float[]) propertyHash = Arrays.hashCode((float[])value);
			else if(value instanceof char[]) propertyHash = Arrays.hashCode((char[])value);
			else if(value instanceof byte[]) propertyHash = Arrays.hashCode((byte[])value);
			else if(value instanceof short[]) propertyHash = Arrays.hashCode((short[])value);
			else propertyHash = value.hashCode();
			hash += 37 * propertyHash;
		}
		return hash;
	}
	
	/**
	 * Build a simple, pipe-delimited string representation for {@code self},
	 * useful for {@link Object#toString()} implementations.
	 * <pre>
	 * public String toString() { return EQ.format(this); }
	 * </pre>
	 */
	default String format(final T self)
	{
		final String string = Stream
			.of(get())
			.map($ -> $.apply(self))
			.map(value ->
			{
				if(value == null) return "null";
				else if(value instanceof Object[]) return Arrays.toString((Object[])value);
				else if(value instanceof int[]) return Arrays.toString((int[])value);
				else if(value instanceof long[]) return Arrays.toString((long[])value);
				else if(value instanceof boolean[]) return Arrays.toString((boolean[])value);
				else if(value instanceof double[]) return Arrays.toString((double[])value);
				else if(value instanceof float[]) return Arrays.toString((float[])value);
				else if(value instanceof char[]) return Arrays.toString((char[])value);
				else if(value instanceof byte[]) return Arrays.toString((byte[])value);
				else if(value instanceof short[]) return Arrays.toString((short[])value);
				else return String.valueOf(value);
			})
			.collect(joining("|"));
		return "[" + string + "]";
	}
	
	/**
	 * Build a formatted string representation for {@code self},
	 * using the specified {@linkplain Formatter format string}.
	 * <pre>
	 * public String toString() { return EQ.format(this, "%s: %,d (%s)"); }
	 * </pre>
	 * Note: The properties of {@code self} will be applied to the format string
	 * in the order they are given to {@link #ofProperties}.
	 */
	default String format(final T self, final String formatString)
	{
		return String.format(
			formatString,
			Stream.of(get()).map($ -> $.apply(self)).toArray(Object[]::new));
	}
}
