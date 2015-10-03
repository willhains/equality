package com.willhains.equality;

import java.util.function.*;
import javax.annotation.*;

/**
 * A fast, safe, helpful utility for implementing {@link Object#equals} and
 * {@link Object#hashCode} easily and correctly. See {@link #ofProperties} for
 * usage.
 * 
 * @param <T> your class.
 */
public interface Equality<T>
{
	/**
	 * Return an array of functions that each retrieve an internal property from
	 * a given instance of {@code T}. This method is used internally; you don't
	 * have to implement it.
	 */
	Function<T, ?>[] properties();
	
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
	static <T> Equality<T> ofProperties(Function<T, ?>... properties)
	{
		return () -> properties;
	}
	
	/**
	 * Compare {@code self} to {@code other} for equality.
	 * <pre>
	 * public boolean equals(Object other) { return EQ.compare(this, other); }
	 * </pre>
	 */
	default boolean compare(T self, @Nullable Object other)
	{
		if(other == self) return true;
		if(other == null) return false;
		if(!self.getClass().isAssignableFrom(other.getClass())) return false;
		final T that = (T)other;
		for(Function<T, ?> property: properties())
		{
			final @Nullable Object thisValue = property.apply(self);
			final @Nullable Object thatValue = property.apply(that);
			final boolean equal = thisValue == null
				? thatValue == null
				: thisValue.equals(thatValue);
			if(!equal) return false;
		}
		return true;
	}
	
	/**
	 * Compute a hash code for {@code self}.
	 * <pre>
	 * public int hashCode() { return EQ.hash(this); }
	 * </pre>
	 */
	default int hash(T self)
	{
		int hash = 17;
		for(Function<T, ?> property: properties())
		{
			final @Nullable Object value = property.apply(self);
			final int propertyHash = value == null ? 7 : value.hashCode();
			hash += 37 * propertyHash;
		}
		return hash;
	}
}
