# Equality [![Build Status](https://travis-ci.org/willhains/equality.svg?branch=master)](https://travis-ci.org/willhains/equality)

An elegant API for implementing Java's `equals()`, `hashCode()`, and `toString()` methods.

## How to use Equality

Just declare a private static constant `Equality`, with a list of accessors for
the properties you want to participate in `equals()` and `hashCode()`.

```java
public class MyClass
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
	@Override public String toString() { return EQ.format(this); }
}
```

There is also an even shorter way to create the `Equality` constant, if you don't mind reflection:

```java
private static final Equality<MyClass> EQ = Equality.reflect(MyClass.class);
```

## Why another `equals()` and `hashCode()` utility?

Because it's better.

With Equality, you write less code, it's more readable, and more maintainable.
And there's nothing to memorise: if your IDE supports code completion (and, even
better, dynamic display of Javadoc), you will be prompted what to do as you
write the code.

## Development Status

Equality is production-ready, and is already used in mission-critical systems
of a large financial institution. (No guarantees of safety or quality are made
or implied. Use at your own risk.) Comments and contributions are welcome
and encouraged. Public APIs are unlikely to change, but may do so without
notice.

## Contribution

1. Fork
2. Create your feature branch (`git checkout -b my-new-feature`)
3. Commit your changes (`git commit -am 'Add some feature'`)
4. Push to the branch (`git push origin my-new-feature`)
5. Create new Pull Request
