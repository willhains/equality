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
	// Number of iterations per test
	protected static final int SIZE = 1 * 1000 * 1000;
	
	// Run benchmark 10 times to see the effect of HotSpot
	private static final int REPETITIONS = 10;
	
	// Generated test data shared across all subclasses
	private static Object[][] parameters;
	
	@Parameterized.Parameters
	public static List<Object[]> data()
	{
		// Lazily initialise test data
		if(parameters == null)
		{
			System.out.println("Generating test data");
			parameters = new Object[REPETITIONS][2];
			for(int r = 0; r < REPETITIONS; r++)
			{
				System.out.printf("%4d:  ", r + 1);
				
				// Create a set of POJOs
				System.out.print("POJOs...  ");
				final SomewhatTypicalPOJO[] pojos = new SomewhatTypicalPOJO[SIZE];
				for(int p = 0; p < SIZE; p++)
				{
					pojos[p] = new SomewhatTypicalPOJO();
				}
				parameters[r][0] = pojos;
				
				// Create a set of possibly-equal Objects
				System.out.print("Objects...  ");
				final Object[] objects = new Object[SIZE];
				for(int o = 0; o < SIZE; o++)
				{
					objects[o] =
						o % 7 == 0 ? pojos[o] :
							o % 13 == 0 ? null :
								o % 19 == 0 ? "String" :
									new SomewhatTypicalPOJO();
				}
				parameters[r][1] = objects;
				
				System.out.println("DONE");
			}
		}
		return Arrays.asList(parameters);
	}
	
	protected final SomewhatTypicalPOJO[] _pojos;
	protected final Object[] _objects;
	
	public EqualityBenchmark(SomewhatTypicalPOJO[] pojos, Object[] objects)
	{
		super();
		_pojos = pojos;
		_objects = objects;
	}
	
	@Before
	public void resetHeap() throws InterruptedException
	{
		// Reset JVM heap before each test
		System.gc();
		Thread.sleep(1000);
	}
}
