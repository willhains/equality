package com.willhains.equality;

import org.junit.*;

/**
 * Benchmark tests for {@link Equals}.
 * 
 * @author willhains
 */
public class EqualsBenchmark extends EqualityBenchmark
{
	public EqualsBenchmark(int roundNumber, SomewhatTypicalPOJO[] pojos, Object[] objects)
	{
		super("equals", roundNumber, pojos, objects);
	}
	
	boolean equals;
	
	@Before
	public void setUpEquals()
	{
		equals = true;
	}
	
	private void _printResults(String name, long start, long stop)
	{
		final long elapsed = stop - start;
		final double latency = elapsed / (double)SIZE;
		System.out.printf("%8s:  Latency=%,-5.3g", name, latency);
		
		// Prevent HotSpot from optimising away the loop
		System.out.println(equals ? " " : "");
		
		// Add result to report
		addLatencyResult(name, latency);
	}
	
	@Test
	public void benchmarkEqualityEquals()
	{
		// Timed loop
		final long start = System.nanoTime();
		for(int i = 0; i < SIZE; i++)
		{
			equals ^= _pojos[i].equalityEquals(_objects[i]);
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
		for(int i = 0; i < SIZE; i++)
		{
			equals ^= _pojos[i].apacheEquals(_objects[i]);
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
		for(int i = 0; i < SIZE; i++)
		{
			equals ^= _pojos[i].eclipseEquals(_objects[i]);
		}
		final long stop = System.nanoTime();
		
		// Output results
		_printResults("Eclipse", start, stop);
	}
}
