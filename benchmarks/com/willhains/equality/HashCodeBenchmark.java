package com.willhains.equality;

import java.util.*;
import org.junit.*;
import org.junit.experimental.categories.*;

/**
 * Benchmark tests for {@link HashCode}.
 * 
 * @author willhains
 */
@Category(Runtime.class)
public class HashCodeBenchmark extends EqualityBenchmark
{
	public HashCodeBenchmark(
		int roundNumber,
		SomewhatTypicalPOJO[] pojos,
		Object[] objects)
	{
		super("hashCode", roundNumber, pojos, objects);
	}
	
	int[] hashCodes;
	
	@Before
	public void setUpHashCodes()
	{
		hashCodes = new int[SIZE];
	}
	
	private int _countCollisions()
	{
		final Map<Integer, SomewhatTypicalPOJO> hashes =
			new HashMap<Integer, SomewhatTypicalPOJO>();
		int collisions = 0;
		for(int i = 0; i < SIZE; i++)
		{
			// Possible collision found
			if(hashes.containsKey(hashCodes[i]))
			{
				final SomewhatTypicalPOJO collider = _pojos[i];
				final Object collidedWith = hashes.get(i);
				
				// Actual collision
				if(!collider.equals(collidedWith)) collisions++;
			}
		}
		return collisions;
	}
	
	private static final int NUM_BUCKETS = 16;
	
	private int[] _hashDistribution()
	{
		final long bucketSize =
			((long)Integer.MAX_VALUE - (long)Integer.MIN_VALUE) / NUM_BUCKETS
				+ 1L;
		final int[] histogram = new int[NUM_BUCKETS];
		for(int i = 0; i < SIZE; i++)
		{
			final long bucket =
				((long)hashCodes[i] - (long)Integer.MIN_VALUE) / bucketSize;
			histogram[(int)bucket]++;
		}
		return histogram;
	}
	
	private void _printResults(String name, long start, long stop)
	{
		final int collisions = _countCollisions();
		final int[] histogram = _hashDistribution();
		final long elapsed = stop - start;
		final double latency = elapsed / (double)SIZE;
		final double percentage = collisions * 100 / (double)SIZE;
		int min = Integer.MAX_VALUE, max = 0;
		for(final int element : histogram)
		{
			min = Math.min(min, element);
			max = Math.max(max, element);
		}
		final int range = max - min;
		System.out.printf(
			"%8s:  Latency=%,-5.3g  Range=%,-5d  Collisions=%,-2d (%.3g%%)%n",
			name, latency, range, collisions, percentage);
		
		// Add results to report
		addLatencyResult(name, latency);
	}
	
	@Test
	public void benchmarkEqualityHashCode()
	{
		// Timed loop
		final long start = System.nanoTime();
		for(int i = 0; i < SIZE; i++)
		{
			hashCodes[i] = _pojos[i].equalityHashCode();
		}
		final long stop = System.nanoTime();
		
		// Output results
		_printResults("Equality", start, stop);
	}
	
	@Test
	public void benchmarkApacheHashCode()
	{
		// Timed loop
		final long start = System.nanoTime();
		for(int i = 0; i < SIZE; i++)
		{
			hashCodes[i] = _pojos[i].apacheHashCode();
		}
		final long stop = System.nanoTime();
		
		// Output results
		_printResults("Apache", start, stop);
	}
	
	@Test
	public void benchmarkEclipseHashCode()
	{
		// Timed loop
		final long start = System.nanoTime();
		for(int i = 0; i < SIZE; i++)
		{
			hashCodes[i] = _pojos[i].eclipseHashCode();
		}
		final long stop = System.nanoTime();
		
		// Output results
		_printResults("Eclipse", start, stop);
	}
}
