package com.willhains.equality;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import java.math.*;
import java.util.*;
import org.junit.*;

public class EqualityTest
{
	static final class Address
	{
		private final String _host;
		private final int _port;
		
		Address(final String host, final int port)
		{
			_host = host;
			_port = port;
		}
		
		private static final Equality<Address> EQ =
			Equality.ofProperties($ -> $._host, $ -> $._port);
			
		@Override
		public boolean equals(final Object o)
		{
			return EQ.compare(this, o);
		}
		
		@Override
		public int hashCode()
		{
			return EQ.hash(this);
		}
		
		@Override
		public String toString()
		{
			return EQ.format(this, "http://%s:%d");
		}
	}
	
	private final Address repeatedInstance = new Address("oracle.com", 80);
	private final List<Object> objects = Arrays.asList(
		repeatedInstance,
		repeatedInstance,
		new Address("oracle.com", 80),
		new Address("oracle.com", 25),
		new Address("apple.com", 80),
		new Address("apple.com", 25),
		new Address(null, 80),
		new Address(null, 25),
		"not an Address");
		
	@Test
	public void shouldBeReflexive()
	{
		objects.forEach(a ->
		{
			assertTrue(a.equals(a));
		});
	}
	
	@Test
	public void shouldBeSymmetric()
	{
		objects.forEach(a -> objects.forEach(b ->
		{
			assertEquals(a.equals(b), b.equals(a));
		}));
	}
	
	@Test
	public void shouldBeTransitive()
	{
		objects.forEach(a -> objects.forEach(b -> objects.forEach(c ->
		{
			if(a.equals(b) && b.equals(c)) assertTrue(a.equals(c));
		})));
	}
	
	@Test
	public void shouldBeConsistent()
	{
		objects.forEach(a -> objects.forEach(b ->
		{
			assertEquals(a.equals(b), a.equals(b));
		}));
	}
	
	@Test
	public void shouldNeverEqualNull()
	{
		objects.forEach(a ->
		{
			assertFalse(a.equals(null));
		});
	}
	
	public void shouldComputeConsistentHash()
	{
		objects.forEach(a ->
		{
			assertEquals(a.hashCode(), a.hashCode());
		});
	}
	
	public void shouldBeConsistentWithEquals()
	{
		objects.forEach(a -> objects.forEach(b ->
		{
			if(a.equals(b)) assertEquals(a.hashCode(), b.hashCode());
		}));
	}
	
	static class MyClass
	{
		boolean n = false;
		byte b = 0;
		short s = 165;
		char c = '&';
		int i = 1684435131;
		long l = 1864354534681013432L;
		float f = 7873121604811054544834351266484311.15046845464544343165610f;
		double d =
			6431210645340345940345879899912.47687684351264804646454384544d;
		Object o =
			"fioj alru83j oai392 foijhw4";
			
		boolean[] nn = new boolean[] {true, false};
		byte[] bb = new byte[] {54, 111};
		short[] ss = new short[] {8451, 14534};
		char[] cc = new char[] {'P', '^'};
		int[] ii = new int[] {784811515, 420837543};
		long[] ll = new long[] {1685615125645463423L, 5L};
		float[] ff = new float[] {54543213126.16153543f, 0.0000000000001f};
		double[] dd = new double[] {
			16432464843411.5459875601564543d,
			49815654.0909090909d};
		Object[] oo = new Object[] {"Hello", "Bonjour"};
		
		private static final Equality<MyClass> EQ = Equality.ofProperties(
			$ -> $.n,
			$ -> $.b,
			$ -> $.s,
			$ -> $.c,
			$ -> $.i,
			$ -> $.l,
			$ -> $.f,
			$ -> $.d,
			$ -> $.o,
			$ -> $.nn,
			$ -> $.bb,
			$ -> $.ss,
			$ -> $.cc,
			$ -> $.ii,
			$ -> $.ll,
			$ -> $.ff,
			$ -> $.dd,
			$ -> $.oo);
			
		@Override
		public boolean equals(final Object obj)
		{
			return EQ.compare(this, obj);
		}
		
		@Override
		public int hashCode()
		{
			return EQ.hash(this);
		}
		
		@Override
		public String toString()
		{
			return EQ.format(this);
		}
	}
	
	static final class MySubClass extends MyClass
	{
		int differentAndUnrelatedInt = 88;
	}
	
	@Test
	public void shouldBeNotEqualToDifferentType()
	{
		final MyClass o1 = new MyClass();
		assertFalse(o1.equals("Hi there"));
	}
	
	@Test
	public void shouldBeNotEqualToDifferentTypeEvenIfSubclass()
	{
		final MyClass o1 = new MyClass();
		final MySubClass o2 = new MySubClass();
		assertFalse(o1.equals(o2));
		assertFalse(o2.equals(o1));
	}
	
	@Test
	public void shouldCompareArrays()
	{
		final MyClass a = new MyClass();
		final MyClass b = new MyClass();
		assertTrue(a.equals(b));
		assertEquals(a.hashCode(), b.hashCode());
		b.oo = null;
		assertFalse(a.equals(b));
		assertNotEquals(a.hashCode(), b.hashCode());
		b.oo = new Object[] {new BigDecimal("42")};
		assertFalse(a.equals(b));
		assertNotEquals(a.hashCode(), b.hashCode());
		b.dd = new double[] {7.89, 0.12};
		assertFalse(a.equals(b));
		assertNotEquals(a.hashCode(), b.hashCode());
		b.ff = new float[] {1.23f, 4.56f};
		assertFalse(a.equals(b));
		assertNotEquals(a.hashCode(), b.hashCode());
		b.ll = new long[] {1234567890, 987654321};
		assertFalse(a.equals(b));
		assertNotEquals(a.hashCode(), b.hashCode());
		b.ii = new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 0};
		assertFalse(a.equals(b));
		assertNotEquals(a.hashCode(), b.hashCode());
		b.cc = new char[] {'o', 'p'};
		assertFalse(a.equals(b));
		assertNotEquals(a.hashCode(), b.hashCode());
		b.ss = new short[] {23, 32};
		assertFalse(a.equals(b));
		assertNotEquals(a.hashCode(), b.hashCode());
		b.bb = new byte[] {6, 5};
		assertFalse(a.equals(b));
		assertNotEquals(a.hashCode(), b.hashCode());
		b.nn = new boolean[] {false, true, false, false};
		assertFalse(a.equals(b));
		assertNotEquals(a.hashCode(), b.hashCode());
	}
	
	@Test
	public void shouldIncludeAllPropertiesInToString()
	{
		final MyClass a = new MyClass();
		assertThat(a.toString(), containsString("false"));
		assertThat(a.toString(), containsString("0"));
		assertThat(a.toString(), containsString("&"));
		assertThat(a.toString(), containsString("1684435131"));
		assertThat(a.toString(), containsString("fioj alru83j oai392 foijhw4"));
		assertThat(a.toString(), containsString("[true, false]"));
		assertThat(a.toString(), containsString("[54, 111]"));
		assertThat(a.toString(), containsString("[P, ^]"));
		assertThat(a.toString(), containsString("[Hello, Bonjour]"));
	}
	
	@Test
	public void shouldHonourCustomFormatToString()
	{
		assertThat(new Address("oracle.com", 80).toString(), is(equalTo("http://oracle.com:80")));
		assertThat(new Address("oracle.com", 25).toString(), is(equalTo("http://oracle.com:25")));
		assertThat(new Address("apple.com", 80).toString(), is(equalTo("http://apple.com:80")));
		assertThat(new Address("apple.com", 25).toString(), is(equalTo("http://apple.com:25")));
		assertThat(new Address(null, 80).toString(), is(equalTo("http://null:80")));
		assertThat(new Address(null, 25).toString(), is(equalTo("http://null:25")));
	}
}
