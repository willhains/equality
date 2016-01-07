package com.willhains.equality;

import static java.util.stream.Collectors.*;
import java.lang.reflect.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

/**
 * A fast, safe, helpful utility for implementing {@link Object#equals} and
 * {@link Object#hashCode} easily and correctly. See {@link #ofProperties} for
 * usage.
 * <p>
 * Note: {@link #equals}, {@link #hashCode}, and {@link #toString} all throw
 * {@link UnsupportedOperationException}, to avoid accidental incorrect usage.
 * 
 * @param <T> your class.
 * @author willhains
 */
public final class Equality<T>
{
	private final Function<T, ?>[] _properties;
	
	private Equality(final Function<T, ?>[] properties)
	{
		_properties = properties;
	}
	
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
	public static <T> Equality<T> ofProperties(final Function<T, ?>... properties)
	{
		return new Equality<>(properties);
	}
	
	/** @see #ofProperties(Function...) */
	public static <T> Equality<T> ofProperties(final Function<T, ?> property)
	{
		final Function<T, ?>[] properties = new Function[] {property};
		return ofProperties(properties);
	}
	
	/**
	 * Reflection-based alternative to {@link #ofProperties}.
	 * Using this method has the advantage that new properties added to your
	 * class later will be automatically included, and the disadvantage that
	 * performance may be slower due to reflection overhead.
	 * <pre>
	 * private static final Equality&lt;MyClass&gt; EQ = Equality.reflect(MyClass.class);
	 * </pre>
	 * 
	 * @see #ofProperties(Function...)
	 */
	public static <T> Equality<T> reflect(final Class<T> type)
	{
		final Function<Field, Function<T, ?>> fieldToPropertyAccessor = field -> instance ->
		{
			try
			{
				return field.get(instance);
			}
			catch(final IllegalAccessException e)
			{
				// No choice but to convert to runtime exception
				throw new RuntimeException(e);
			}
		};
		final Function<T, ?>[] properties = Arrays
			.stream(type.getDeclaredFields())
			.filter(field -> !Modifier.isStatic(field.getModifiers()))
			.map(fieldToPropertyAccessor)
			.toArray(Function[]::new);
		return ofProperties(properties);
	}
	
	/**
	 * Compare {@code self} to {@code other} for equality.
	 * <pre>
	 * public boolean equals(Object other) { return EQ.compare(this, other); }
	 * </pre>
	 */
	public boolean compare(final T self, final Object other)
	{
		if(other == self) return true;
		if(other == null) return false;
		if(!self.getClass().equals(other.getClass())) return false;
		final T that = (T)other;
		for(final Function<T, ?> property : _properties)
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
	public int hash(final T self)
	{
		// Modified form of Joshua Bloch's hash algorithm
		int hash = 17;
		hash += 13 * self.getClass().hashCode();
		for(final Function<T, ?> property : _properties)
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
	public String format(final T self)
	{
		return Stream.of(_properties)
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
	public String format(final T self, final String formatString)
	{
		return String.format(
			formatString,
			Stream.of(_properties).map($ -> $.apply(self)).toArray(Object[]::new));
	}
	
	/**
	 * Do not use this method; call {@link #hash(Object) hash(this)} instead!
	 * 
	 * @throws UnsupportedOperationException always.
	 * @see #hash(Object)
	 */
	@Override
	public int hashCode()
	{
		throw new UnsupportedOperationException("Use hash(this) to compute your hash code");
	}
	
	/**
	 * Do not use this method; call {@link #compare(Object, Object)
	 * compare(this, other)} instead!
	 * 
	 * @throws UnsupportedOperationException always
	 * @see #compare(Object, Object)
	 */
	@Override
	public boolean equals(final Object obj)
	{
		throw new UnsupportedOperationException("Use compare(this, other) to test for equality");
	}
	
	/**
	 * Do not use this method; call {@link #format(Object) format(this)} or
	 * {@link #format(Object, String) format(this, "format string"}
	 * instead!
	 * 
	 * @throws UnsupportedOperationException always
	 * @see #format(Object)
	 * @see #format(Object, String)
	 */
	@Override
	public String toString()
	{
		throw new UnsupportedOperationException("Use format(this) to build your string");
	}
}
