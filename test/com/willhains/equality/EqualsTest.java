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
		return new HashCode()
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
