# Abstract Tests for Common Java API Contracts

This package contains some handy abstract JUnit test classes to help with checking that your classes obey the general contracts for well-behaved objects.

- `AbstractTest` is a general-purpose abstract test superclass. You probably won't interact with this directly.
- `GeneralContractTest` is the one to subclass when you're testing a class that implements `equals()` and `hashCode()`.
- `ComparableTest` is for testing classes that implement the `Comparable` interface.

## If I'm using Equality, why test my `equals()` and `hashCode()` methods?

Equality gives you a nice, clean API to implement these methods, but if you choose the wrong internal state to compare/hash, your implementation will be pretty but wrong. Need more convincing? Bugs in `equals()` and `hashCode()` have a nasty way of not showing up during normal functional and integration testing, then rearing their ugly head in production. Always test every method you implement, no matter how much help you get from libraries like Equality.
