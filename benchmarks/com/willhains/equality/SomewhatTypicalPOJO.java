package com.willhains.equality;

import java.math.*;
import java.security.*;
import java.util.concurrent.*;
import org.apache.commons.lang3.builder.*;

/**
 * Used in benchmarks.
 * 
 * @author willhains
 */
public final class SomewhatTypicalPOJO
{
	static final SecureRandom RND = new SecureRandom();
	
	private final long _long;
	private final String _string1, _string2;
	private final TimeUnit _enum;
	private final BigDecimal _decimal;
	
	/**
	 * Creates a POJO with random contents. Do not call this constructor from within a benchmark; rather, prepare a list
	 * of input POJO instances prior to the benchmark starting.
	 */
	public SomewhatTypicalPOJO()
	{
		_long = RND.nextLong();
		_string1 = RND.nextInt() % 17 == 0 ? null : new BigInteger(130, RND).toString(32);
		_string2 = RND.nextInt() % 37 == 0 ? null : new BigInteger(130, RND).toString(32);
		_enum = RND.nextInt() % 47 == 0 ? null : TimeUnit.values()[RND.nextInt(TimeUnit.values().length)];
		_decimal = RND.nextInt() % 67 == 0 ? null : new BigDecimal(new BigInteger(130, RND), RND.nextInt(19));
	}
	
	/**
	 * An {@link Object#equals(Object)} implementation using Equality's {@link Equals} class.
	 */
	public boolean equalityEquals(Object obj)
	{
		final Equals<SomewhatTypicalPOJO> eq = Equals.compare(this, obj);
		return eq
			.and(this._long, eq.that._long)
			.and(this._string1, eq.that._string1)
			.and(this._string2, eq.that._string2)
			.and(this._enum, eq.that._enum)
			.and(this._decimal, eq.that._decimal)
			.equals();
	}
	
	/**
	 * An {@link Object#equals(Object)} implementation using the {@link EqualsBuilder} from Apache Commons.
	 */
	public boolean apacheEquals(Object obj)
	{
		if(obj instanceof SomewhatTypicalPOJO == false) return false;
		if(this == obj) return true;
		final SomewhatTypicalPOJO that = (SomewhatTypicalPOJO)obj;
		return new EqualsBuilder()
			.append(this._long, that._long)
			.append(this._string1, that._string1)
			.append(this._string2, that._string2)
			.append(this._enum, that._enum)
			.append(this._decimal, that._decimal)
			.isEquals();
	}
	
	/**
	 * An {@link Object#equals(Object)} implementation generated by Eclipse.
	 */
	public boolean eclipseEquals(Object obj)
	{
		if(this == obj) return true;
		if(obj == null) return false;
		if(!(obj instanceof SomewhatTypicalPOJO)) return false;
		final SomewhatTypicalPOJO other = (SomewhatTypicalPOJO)obj;
		if(_decimal == null)
		{
			if(other._decimal != null) return false;
		}
		else if(!_decimal.equals(other._decimal)) return false;
		if(_enum != other._enum) return false;
		if(_long != other._long) return false;
		if(_string1 == null)
		{
			if(other._string1 != null) return false;
		}
		else if(!_string1.equals(other._string1)) return false;
		if(_string2 == null)
		{
			if(other._string2 != null) return false;
		}
		else if(!_string2.equals(other._string2)) return false;
		return true;
	}
	
	/**
	 * An {@link Object#hashCode()} implementation using Equality's {@link HashCode#compute()} implementation.
	 */
	public int equalityHashCode()
	{
		return HashCode.compute()
			.with(this._long)
			.with(this._string1)
			.with(this._string2)
			.with(this._enum)
			.with(this._decimal)
			.hashCode();
	}
	
	/**
	 * An {@link Object#hashCode()} implementation using Equality's {@link HashCode#computeForLargeSet()}
	 * implementation.
	 */
	public int equalityLargeSetHashCode()
	{
		return HashCode.computeForLargeSet()
			.with(this._long)
			.with(this._string1)
			.with(this._string2)
			.with(this._enum)
			.with(this._decimal)
			.hashCode();
	}
	
	/**
	 * An {@link Object#hashCode()} implementation using the {@link HashCodeBuilder} class from Apache Commons.
	 */
	public int apacheHashCode()
	{
		return new HashCodeBuilder(17, 37)
			.append(this._long)
			.append(this._string1)
			.append(this._string2)
			.append(this._enum)
			.append(this._decimal)
			.toHashCode();
	}
	
	/**
	 * An {@link Object#hashCode()} implementation generated by Eclipse.
	 */
	public int eclipseHashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + (_decimal == null ? 0 : _decimal.hashCode());
		result = prime * result + (_enum == null ? 0 : _enum.hashCode());
		result = prime * result + (int)(_long ^ _long >>> 32);
		result = prime * result + (_string1 == null ? 0 : _string1.hashCode());
		result = prime * result + (_string2 == null ? 0 : _string2.hashCode());
		return result;
	}
}
