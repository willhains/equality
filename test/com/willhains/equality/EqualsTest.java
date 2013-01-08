package com.willhains.equality;

import static org.junit.Assert.*;
import org.junit.*;

/**
 * @author willhains
 */
public class EqualsTest extends GeneralContractTest<MyClass>
{
	@Override
	protected MyClass createEquivalent(MyClass o)
	{
		return new MyClass();
	}
	
	@Override
	protected MyClass createImp() throws Exception
	{
		return new MyClass();
	}
	
	@Test
	public void basicEquals()
	{
		final MyClass o1 = new MyClass();
		final MyClass o2 = new MyClass();
		assertTrue(o1.equals(o2));
	}
	
	@Test
	public void unequalObjects()
	{
		final MyClass o1 = new MyClass();
		final MyClass o2 = new MyClass();
		o1.d = 123345d;
		assertFalse(o1.equals(o2));
		o2.d = o1.d;
		assertTrue(o1.equals(o2));
		o2.i = 42;
		assertFalse(o1.equals(o2));
	}
	
	@Test
	public void nullFields()
	{
		final MyClass o1 = new MyClass();
		final MyClass o2 = new MyClass();
		o1.o = null;
		assertFalse(o1.equals(o2));
		o2.o = null;
		assertTrue(o1.equals(o2));
		o2.ii = null;
		assertFalse(o1.equals(o2));
		o1.ii = null;
		assertTrue(o1.equals(o2));
	}
	
	@Test
	public void equalToNull()
	{
		final MyClass o1 = new MyClass();
		assertFalse(o1.equals(null));
	}
	
	@Test
	public void differentTypes()
	{
		final MyClass o1 = new MyClass();
		assertFalse(o1.equals("Hi there"));
	}
	
	static class MySubClass extends MyClass
	{
		@SuppressWarnings("unused")
		private int differentAndUnrelatedInt = 88;
	}
	
	@Test
	public void equalsSubclass()
	{
		final MyClass o1 = new MyClass();
		final MySubClass o2 = new MySubClass();
		assertTrue(o1.equals(o2));
		assertFalse(o2.equals(o1)); // type of o1 is not assignable to type of o2
		o2.differentAndUnrelatedInt = 11;
		assertTrue(o1.equals(o2));
	}
	
	@Test(expected = AssertionError.class)
	public void nullCaller()
	{
		final MyClass o1 = null;
		final MyClass o2 = new MyClass();
		Equals.compare(o1, o2); // naughty
	}
	
	@Test(expected = AssertionError.class)
	public void nullStrictlyCaller()
	{
		final MyClass o1 = null;
		final MyClass o2 = new MyClass();
		Equals.compareStrictly(o1, o2); // naughty
	}
	
	@Test
	public void directEqualityCondition()
	{
		final MyClass o1 = new MyClass();
		final MyClass o2 = new MyClass();
		
		assertTrue(Equals.compare(o1, o2).and(true).equals());
		assertFalse(Equals.compare(o1, o2).and(false).equals());
	}
	
	@Test
	public void compareStrictly()
	{
		final MyClass o1 = new MyClass();
		final MyClass o2 = new MyClass();
		final MyClass o3 = new MySubClass();
		
		assertTrue(Equals.compare(o1, o2).equals());
		assertTrue(Equals.compare(o1, o3).equals());
		assertTrue(Equals.compareStrictly(o1, o2).equals());
		assertFalse(Equals.compareStrictly(o1, o3).equals());
	}
}

class MyClass
{
	boolean n = false;
	byte b = 0;
	short s = 165;
	char c = '&';
	int i = 1684435131;
	long l = 1864354534681013432L;
	float f = 7873121604811054544834351266484311.15046845464544343165610f;
	double d = 6431210645340345940345879899912.47687684351264804646454384544d;
	Object o = "fioj alru83j oai392 foijhw4 0v902QUI jfio;ajo;zvjslkdfjg ;z";
	
	boolean[] nn = new boolean[] {true, false};
	byte[] bb = new byte[] {54, 111};
	short[] ss = new short[] {8451, 14534};
	char[] cc = new char[] {'P', '^'};
	int[] ii = new int[] {784811515, 420837543};
	long[] ll = new long[] {1685615125645463423L, 5L};
	float[] ff = new float[] {54543213126.16153543f, 0.0000000000001f};
	double[] dd = new double[] {16432464843411.5459875601564543d, 49815654.0909090909d};
	Object[] oo = new Object[] {"Hello", "Bonjour"};
	
	@Override
	public boolean equals(Object obj)
	{
		final Equals<MyClass> eq = Equals.compare(this, obj);
		return eq
			.and(this.n, eq.that.n)
			.and(this.b, eq.that.b)
			.and(this.s, eq.that.s)
			.and(this.c, eq.that.c)
			.and(this.i, eq.that.i)
			.and(this.l, eq.that.l)
			.and(this.f, eq.that.f)
			.and(this.d, eq.that.d)
			.and(this.o, eq.that.o)
			.and(this.nn, eq.that.nn)
			.and(this.bb, eq.that.bb)
			.and(this.ss, eq.that.ss)
			.and(this.cc, eq.that.cc)
			.and(this.ii, eq.that.ii)
			.and(this.ll, eq.that.ll)
			.and(this.ff, eq.that.ff)
			.and(this.dd, eq.that.dd)
			.and(this.oo, eq.that.oo)
			.equals();
	}
	
	@Override
	public int hashCode()
	{
		return HashCode.compute()
			.with(this.n)
			.with(this.b)
			.with(this.s)
			.with(this.c)
			.with(this.i)
			.with(this.l)
			.with(this.f)
			.with(this.d)
			.with(this.o)
			.with(this.nn)
			.with(this.bb)
			.with(this.ss)
			.with(this.cc)
			.with(this.ii)
			.with(this.ll)
			.with(this.ff)
			.with(this.dd)
			.with(this.oo)
			.hashCode();
	}
}
