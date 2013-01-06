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
		assertTrue(0 != new HashCode().with("Hello world").hashCode());
		assertTrue(new HashCode().with("Hello").hashCode()
			!= new HashCode().with("Goodbye").hashCode());
	}
}
