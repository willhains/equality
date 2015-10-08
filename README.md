# Equality [![Build Status](https://travis-ci.org/willhains/equality.svg?branch=master)](https://travis-ci.org/willhains/equality)

An elegant API for implementing Java's `equals()` and `hashCode()` methods.

## How to use Equality

Just declare a private static constant `Equality`, with a list of accessors for
the properties you want to participate in `equals()` and `hashCode()`.

```java
public final MyClass
{
	private String name;
	private int productCode;
	private Color color;
	
	private static final Equality<MyClass> EQ = Equality.ofProperties(
		$ -> $.name,
		$ -> $.productCode,
		$ -> $.color);	
	@Override public boolean equals(Object o) { return EQ.compare(this, o); }
	@Override public int hashCode() { return EQ.hash(this); }
}
```

## Why another `equals()` and `hashCode()` utility?

Because it's better.

Compare the code above to the equivalent using `EqualsBuilder` and
`HashCodeBuilder` from Apache Commons:

```java
@Override
public boolean equals(Object obj)
{
    if(obj instanceof MyClass == false) return false;
    if(this == obj) return true;
    MyClass that = (MyClass)obj;
    return new EqualsBuilder()
        .append(this.name,        that.name)
        .append(this.productCode, that.productCode)
        .append(this.colour,      that.colour)
        .isEquals();
}

@Override
public int hashCode()
{
    return new HashCodeBuilder(17, 37)
        .append(this.name)
        .append(this.productCode)
        .append(this.colour)
        .toHashCode();
}
```

Or, compare it to the typical output of Eclipse code generation:

```java
@Override
public boolean equals(Object obj)
{
    if(this == obj) return true;
    if(obj == null) return false;
    if(getClass() != obj.getClass()) return false;
    MyClass other = (MyClass)obj;
    if(color == null)
    {
    	if(other.color != null) return false;
    }
    else if(!color.equals(other.color)) return false;
    if(name == null)
    {
    	if(other.name != null) return false;
    }
    else if(!name.equals(other.name)) return false;
    if(productCode != other.productCode) return false;
    return true;
}

@Override
public int hashCode()
{
    final int prime = 31;
    int result = 1;
    result = prime * result + ((color == null) ? 0 : color.hashCode());
    result = prime * result + ((name == null) ? 0 : name.hashCode());
    result = prime * result + (int)(productCode ^ (productCode >>> 32));
    return result;
}
```

With Equality, you write less code, it's more readable, and more maintainable.
And there's nothing to memorise: if your IDE supports code completion (and, even
better, dynamic display of Javadoc), you will be prompted what to do as you
write the code.

## Development status

Equality is currently in a beta state. Comments and contributions are welcome
and encouraged. Public APIs are unlikely to change, but may do so without
notice.

## Contribution

1. Fork
2. Create your feature branch (`git checkout -b my-new-feature`)
3. Commit your changes (`git commit -am 'Add some feature'`)
4. Push to the branch (`git push origin my-new-feature`)
5. Create new Pull Request
