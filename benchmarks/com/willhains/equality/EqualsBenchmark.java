package com.willhains.equality;

import org.junit.*;

/**
 * Benchmark tests for {@link Equals}.
 * 
 * @author willhains
 */
public class EqualsBenchmark extends EqualityBenchmark
{
	boolean equals;
	
	@Before
	public void setUpEquals()
	{
		equals = true;
		objects = new Object[size];
		for(int i = 0; i < size; i++)
		{
			objects[i] =
				i % 7 == 0 ? pojos[i] :
					i % 13 == 0 ? null :
						i % 19 == 0 ? "String" :
							new SomewhatTypicalPOJO();
		}
	}
	
	private void _printResults(String name, long start, long stop)
	{
		final long elapsed = stop - start;
		final double latency = elapsed / (double)size;
		System.out.printf("%8s:  Latency=%,-5.3g", name, latency);
		
		// Prevent HotSpot from optimising away the loop
		System.out.println(equals ? " " : "");
	}
	
	@Test
	public void benchmarkBaselineEquals()
	{
		// Timed loop
		final long start = System.nanoTime();
		for(int i = 0; i < size; i++)
		{
			equals ^= i % 2 == 0;
		}
		final long stop = System.nanoTime();
		
		// Output results
		_printResults("Baseline", start, stop);
	}
	
	@Test
	public void benchmarkEqualityEquals()
	{
		// Timed loop
		final long start = System.nanoTime();
		for(int i = 0; i < size; i++)
		{
			equals ^= pojos[i].equalityEquals(objects[i]);
		}
		final long stop = System.nanoTime();
		
		// Output results
		_printResults("Equality", start, stop);
	}
	
	@Test
	public void benchmarkApacheEquals()
	{
		// Timed loop
		final long start = System.nanoTime();
		for(int i = 0; i < size; i++)
		{
			equals ^= pojos[i].apacheEquals(objects[i]);
		}
		final long stop = System.nanoTime();
		
		// Output results
		_printResults("Apache", start, stop);
	}
	
	@Test
	public void benchmarkEclipseEquals()
	{
		// Timed loop
		final long start = System.nanoTime();
		for(int i = 0; i < size; i++)
		{
			equals ^= pojos[i].eclipseEquals(objects[i]);
		}
		final long stop = System.nanoTime();
		
		// Output results
		_printResults("Eclipse", start, stop);
	}
}
