package com.willhains.equality;

import static org.junit.Assert.*;
import org.junit.*;

/**
 * @author willhains
 */
public class HashCodeTest
{
	@Test
	public void testHash()
	{
		assertTrue(0 != HashCode.compute().with("Hello world").hashCode());
		assertTrue(0 != HashCode.computeForLargeSet().with("Hello world").hashCode());
	}
	
	@Test
	public void testComplexHash()
	{
		HashCode.compute()
			.with(false)
			.with((byte)117)
			.with('g')
			.with((short)8465)
			.with(516572415)
			.with(46131344334121654L)
			.with(156453421351543454.846754348654f)
			.with(0.0000000000000000000000000000000000000000001d)
			.with("Leroy Jenkins!")
			.with(new boolean[] {false, false, true})
			.with(new byte[] {1, 2, 3})
			.with(new char[] {'1', '2', '3'})
			.with(new short[] {9, 9, 9})
			.with(new int[] {5235243, 154653, 123})
			.with(new long[] {452345234L, 63454L, 12312312L, 6456546564565L})
			.with(new float[] {1.0f, 0.1f, 0.01f, 0.001f, 0.0001f})
			.with(new double[] {Math.PI, Math.E, 0.99999999999999999999999999999999999999d})
			.with(new String[] {"NCC-1701D", "NCC-1664", "NCC-2893"})
			.hashCode(); // should throw no exceptions
		
		HashCode.computeForLargeSet()
			.with(false)
			.with((byte)117)
			.with('g')
			.with((short)8465)
			.with(516572415)
			.with(46131344334121654L)
			.with(156453421351543454.846754348654f)
			.with(0.0000000000000000000000000000000000000000001d)
			.with("Leroy Jenkins!")
			.with(new boolean[] {false, false, true})
			.with(new byte[] {1, 2, 3})
			.with(new char[] {'1', '2', '3'})
			.with(new short[] {9, 9, 9})
			.with(new int[] {5235243, 154653, 123})
			.with(new long[] {452345234L, 63454L, 12312312L, 6456546564565L})
			.with(new float[] {1.0f, 0.1f, 0.01f, 0.001f, 0.0001f})
			.with(new double[] {Math.PI, Math.E, 0.99999999999999999999999999999999999999d})
			.with(new String[] {"NCC-1701D", "NCC-1664", "NCC-2893"})
			.hashCode(); // should throw no exceptions
	}
	
	@Test
	public void testDifferent()
	{
		assertTrue(HashCode.compute()
			.with("Hello").hashCode() != HashCode.compute().with("Goodbye").hashCode());
		assertTrue(HashCode.computeForLargeSet()
			.with("Hello").hashCode() != HashCode.compute().with("Goodbye").hashCode());
	}
	
	@Test
	public void repeatable()
	{
		final int h1 = HashCode.compute().with(true).with(234838472).with("Hello").hashCode();
		final int h2 = HashCode.compute().with(true).with(234838472).with("Hello").hashCode();
		assertEquals(h1, h2);
		
		final int h3 = HashCode.computeForLargeSet().with(true).with(234838472).with("Hello").hashCode();
		final int h4 = HashCode.computeForLargeSet().with(true).with(234838472).with("Hello").hashCode();
		assertEquals(h3, h4);
	}
	
	@Test
	public void nulls()
	{
		final String nullString = null;
		final int[] nullArray = null;
		HashCode.compute().with(nullString).with(nullArray).hashCode(); // should throw no exceptions
		HashCode.computeForLargeSet().with(nullString).with(nullArray).hashCode(); // should throw no exceptions
	}
}
