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
public abstract class Equality<T>
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
	public static final <T> Equality<T> ofProperties(final Function<T, ?>... properties)
	{
		return new _TupleEquality<>(properties);
	}
	
	/** @see #ofProperties(Function...) */
	public static final <T> Equality<T> ofProperties(final Function<T, ?> property)
	{
		return new _SimpleEquality<>(property);
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
	public static final <T> Equality<T> reflect(final Class<T> type)
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
		@SuppressWarnings("unchecked") final Function<T, ?>[] properties = Arrays
			.stream(type.getDeclaredFields())
			.filter(field -> !Modifier.isStatic(field.getModifiers()))
			.map(fieldToPropertyAccessor)
			.toArray(Function[]::new);
		return ofProperties(properties);
	}
	
	private Equality()
	{
		// private subclasses only
	}
	
	/**
	 * Compare {@code self} to {@code other} for equality.
	 * <pre>
	 * public boolean equals(Object other) { return EQ.compare(this, other); }
	 * </pre>
	 */
	public final boolean compare(final T self, final Object other)
	{
		if(other == self) return true;
		if(other == null) return false;
		if(!self.getClass().equals(other.getClass())) return false;
		@SuppressWarnings("unchecked") final T that = (T)other;
		return compareTyped(self, that);
	}
	
	protected abstract boolean compareTyped(T self, T that);
	
	/**
	 * Compute a hash code for {@code self}.
	 * <pre>
	 * public int hashCode() { return EQ.hash(this); }
	 * </pre>
	 */
	public abstract int hash(T self);
	
	/**
	 * Build a simple, pipe-delimited string representation for
	 * {@code self},
	 * useful for {@link Object#toString()} implementations.
	 * <pre>
	 * public String toString() { return EQ.format(this); }
	 * </pre>
	 */
	public abstract String format(T self);
	
	/**
	 * Build a formatted string representation for {@code self},
	 * using the specified {@linkplain Formatter format string}.
	 * <pre>
	 * public String toString() { return EQ.format(this, "%s: %,d (%s)"); }
	 * </pre>
	 * Note: The properties of {@code self} will be applied to the format
	 * string
	 * in the order they are given to {@link #ofProperties}.
	 */
	public abstract String format(T self, String format);
	
	private static final <T> int _hash(final T self, final Function<T, ?> property)
	{
		final Object value = property.apply(self);
		if(value == null) return 7;
		if(value.getClass().isArray())
		{
			if(value instanceof Object[]) return Arrays.deepHashCode((Object[])value);
			if(value instanceof int[]) return Arrays.hashCode((int[])value);
			if(value instanceof byte[]) return Arrays.hashCode((byte[])value);
			if(value instanceof boolean[]) return Arrays.hashCode((boolean[])value);
			if(value instanceof long[]) return Arrays.hashCode((long[])value);
			if(value instanceof double[]) return Arrays.hashCode((double[])value);
			if(value instanceof float[]) return Arrays.hashCode((float[])value);
			if(value instanceof char[]) return Arrays.hashCode((char[])value);
			return Arrays.hashCode((short[])value);
		}
		return value.hashCode();
	}
	
	private static final <T> boolean _compare(
		final T self,
		final T that,
		final Function<T, ?> property)
	{
		final Object v1 = property.apply(self);
		final Object v2 = property.apply(that);
		if(v1 == v2) return true;
		if(v1 == null || v2 == null) return false;
		if(v1.equals(v2)) return true;
		if(v1.getClass().isArray())
		{
			if(v1 instanceof Object[]) return Arrays.deepEquals((Object[])v1, (Object[])v2);
			if(v1 instanceof int[]) return Arrays.equals((int[])v1, (int[])v2);
			if(v1 instanceof byte[]) return Arrays.equals((byte[])v1, (byte[])v2);
			if(v1 instanceof boolean[]) return Arrays.equals((boolean[])v1, (boolean[])v2);
			if(v1 instanceof long[]) return Arrays.equals((long[])v1, (long[])v2);
			if(v1 instanceof double[]) return Arrays.equals((double[])v1, (double[])v2);
			if(v1 instanceof float[]) return Arrays.equals((float[])v1, (float[])v2);
			if(v1 instanceof char[]) return Arrays.equals((char[])v1, (char[])v2);
			return Arrays.equals((short[])v1, (short[])v2);
		}
		return false;
	}
	
	private static final <T> String _format(final T self, final Function<T, ?> property)
	{
		final Object value = property.apply(self);
		if(value == null) return "null";
		if(value.getClass().isArray())
		{
			if(value instanceof Object[]) return Arrays.toString((Object[])value);
			if(value instanceof int[]) return Arrays.toString((int[])value);
			if(value instanceof byte[]) return Arrays.toString((byte[])value);
			if(value instanceof boolean[]) return Arrays.toString((boolean[])value);
			if(value instanceof long[]) return Arrays.toString((long[])value);
			if(value instanceof double[]) return Arrays.toString((double[])value);
			if(value instanceof float[]) return Arrays.toString((float[])value);
			if(value instanceof char[]) return Arrays.toString((char[])value);
			return Arrays.toString((short[])value);
		}
		return String.valueOf(value);
	}
	
	/**
	 * Do not use this method; call {@link #hash(Object) hash(this)} instead!
	 * 
	 * @throws UnsupportedOperationException always.
	 * @see #hash(Object)
	 */
	@Override
	public final int hashCode()
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
	public final boolean equals(final Object obj)
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
	public final String toString()
	{
		throw new UnsupportedOperationException("Use format(this) to build your string");
	}
	
	private static final class _TupleEquality<T> extends Equality<T>
	{
		private final Function<T, ?>[] _properties;
		
		private _TupleEquality(final Function<T, ?>[] properties)
		{
			_properties = properties;
		}
		
		@Override
		protected boolean compareTyped(final T self, final T that)
		{
			return Stream.of(_properties).allMatch(prop -> _compare(self, that, prop));
		}
		
		@Override
		public int hash(final T self)
		{
			// Modified form of Joshua Bloch's hash algorithm
			int hash = 17;
			hash += self.getClass().hashCode();
			for(final Function<T, ?> prop : _properties)
			{
				hash = 37 * hash + _hash(self, prop);
			}
			return hash;
		}
		
		@Override
		public String format(final T self)
		{
			return Stream.of(_properties).map(prop -> _format(self, prop)).collect(joining("|"));
		}
		
		@Override
		public String format(final T self, final String formatString)
		{
			return String.format(
				formatString,
				Stream.of(_properties).map(prop -> prop.apply(self)).toArray(Object[]::new));
		}
	}
	
	private static final class _SimpleEquality<T> extends Equality<T>
	{
		private final Function<T, ?> _property;
		
		_SimpleEquality(final Function<T, ?> property)
		{
			_property = property;
		}
		
		@Override
		protected boolean compareTyped(final T self, final T that)
		{
			return _compare(self, that, _property);
		}
		
		@Override
		public int hash(final T self)
		{
			return _hash(self, _property);
		}
		
		@Override
		public String format(final T self)
		{
			return _format(self, _property);
		}
		
		@Override
		public String format(final T self, final String format)
		{
			return String.format(format, _property.apply(self));
		}
	}
}
