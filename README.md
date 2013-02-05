# Equality

An elegant API for implementing Java's `equals()` and `hashCode()` methods.

## How to use Equality

```java
@Override
public boolean equals(Object obj)
{
    Equals<MyClass> eq = Equals.compare(this, obj);
    return eq
        .and(this.name,        eq.that.name)      // <-- eq.that is guaranteed to never be null
        .and(this.productCode, eq.that.productCode)
        .and(this.colour,      eq.that.colour)
        .equals();
}

@Override
public int hashCode()
{
    return HashCode.compute()
        .with(this.name)
        .with(this.productCode)
        .with(this.colour)
        .hashCode();
}
```

## Why another `equals()` and `hashCode()` utility?

Because it's better.

Compare the code above to the equivalent using `EqualsBuilder` and `HashCodeBuilder` in Apache Commons:

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

With Equality, you write less code, it's more readable, and more maintainable. And there's nothing to memorise — if your IDE supports code completion (and, even better, dynamic display of Javadoc), you will be prompted what to do as you write the code.

## OK, but how about performance?

It's fast. Here are [some charts](http://twitter.com/willhains/status/298764277593018368/photo/1) that compare the latency of Equality with Apache Commons and Eclipse code generation.

Equality's `Equals` class performs significantly faster than both Apache's `EqualsBuilder` and Eclipse's built-in code-generated methods. Equality's `HashCode` performance is almost identical to Apache's `HashCodeBuilder`. In all cases, Equality required less code to use.

Benchmark tests are included in the source code. They produce the charts linked above. Just run the JUnit tests under the `benchmark` folder.

## Development status

Equality is currently in a beta state. Comments and contributions are welcome and encouraged. Public APIs are unlikely to change, but may do so without notice.

