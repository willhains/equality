package com.willhains.equality;

import java.util.*;
import org.junit.*;
import org.junit.runner.*;
import org.junit.runners.*;

/**
 * Benchmark template for Equality.
 * 
 * @author willhains
 */
@RunWith(Parameterized.class)
public abstract class EqualityBenchmark
{
	@Parameterized.Parameters
	public static List<Object[]> data()
	{
		// Run benchmark 10 times to see the effect of HotSpot
		return Arrays.asList(new Object[10][0]);
	}
	
	protected final int size = 1 * 1000 * 1000;
	protected SomewhatTypicalPOJO[] pojos;
	protected Object[] objects;
	
	public EqualityBenchmark()
	{
		super();
	}
	
	@Before
	public void setUp() throws InterruptedException
	{
		System.gc();
		Thread.sleep(1000);
		pojos = new SomewhatTypicalPOJO[size];
		for(int i = 0; i < size; i++)
		{
			pojos[i] = new SomewhatTypicalPOJO();
		}
	}
}
