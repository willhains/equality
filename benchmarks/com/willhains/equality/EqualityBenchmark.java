package com.willhains.equality;

import java.io.*;
import java.net.*;
import java.text.*;
import java.util.*;
import org.junit.*;
import org.junit.experimental.categories.*;
import org.junit.runner.*;
import org.junit.runners.*;

/**
 * Benchmark template for Equality.
 * 
 * @author willhains
 */
@Category(Runtime.class)
@RunWith(Parameterized.class)
public abstract class EqualityBenchmark
{
	// Number of iterations per test
	protected static final int SIZE = 1000;
	
	// Run benchmark 10 times to see the effect of HotSpot
	private static final int REPETITIONS = 500;
	
	// Generated test data shared across all subclasses
	private static Object[][] parameters;
	
	@Parameterized.Parameters
	public static List<Object[]> data()
	{
		// Lazily initialise test data
		if(parameters == null)
		{
			System.out.println("Generating test data");
			parameters = new Object[REPETITIONS][3];
			
			// Create a set of POJOs
			System.out.println("  POJOs...");
			final SomewhatTypicalPOJO[] pojos = new SomewhatTypicalPOJO[SIZE];
			for(int p = 0; p < SIZE; p++)
			{
				pojos[p] = new SomewhatTypicalPOJO();
			}
			
			// Create a set of possibly-equal Objects
			System.out.println("  Objects...");
			final Object[] objects = new Object[SIZE];
			for(int o = 0; o < SIZE; o++)
			{
				objects[o] =
					o % 7 == 0 ? pojos[o] :
						o % 13 == 0 ? null :
							o % 19 == 0 ? "String" :
								new SomewhatTypicalPOJO();
			}
			
			// Load up for parameterised tests
			for(int r = 0; r < REPETITIONS; r++)
			{
				parameters[r][0] = r;
				parameters[r][1] = pojos;
				parameters[r][2] = objects;
			}
			System.out.println("DONE");
		}
		return Arrays.asList(parameters);
	}
	
	private static String _method;
	private final int _roundNumber;
	protected final SomewhatTypicalPOJO[] _pojos;
	protected final Object[] _objects;
	
	public EqualityBenchmark(
		String method,
		int roundNumber,
		SomewhatTypicalPOJO[] pojos,
		Object[] objects)
	{
		super();
		_method = method;
		_roundNumber = roundNumber;
		_pojos = pojos;
		_objects = objects;
	}
	
	@Before
	public void resetHeap() throws InterruptedException
	{
		// Reset JVM heap before each test
		System.gc();
		Thread.sleep(1);
	}
	
	// Latency results
	private static final List<Map<String, Double>> latencyResults =
		new ArrayList<Map<String, Double>>();
	
	/**
	 * Add a result to the HTML report generated at the end of the test.
	 */
	protected final void addLatencyResult(String library, double latency)
	{
		final Map<String, Double> resultsForRound;
		if(latencyResults.size() > _roundNumber) resultsForRound =
			latencyResults.get(_roundNumber);
		else latencyResults
			.add(resultsForRound = new HashMap<String, Double>());
		resultsForRound.put(library, latency);
	}
	
	@AfterClass
	public static void generateLatencyReport() throws IOException
	{
		final String timestamp =
			new SimpleDateFormat("yyyy-MM-dd..HH.mm").format(new Date());
		final PrintStream report =
			new PrintStream(
				"benchmark-" + _method + "-" + timestamp + ".html",
				"UTF-8");
		
		report
			.println("<html><head><title>Equality Benchmark Results</title></head><body>");
		final String hostName = InetAddress.getLocalHost().getHostName();
		report.println("<p>Equality Benchmark Results &mdash; " + hostName
			+ ", " + timestamp + "</p>");
		report.println("<div id='" + _method + "-chart'></div>");
		report
			.println("<script type='text/javascript' src='https://www.google.com/jsapi'></script>");
		report.println("<script type='text/javascript'>");
		report
			.println("google.load('visualization', '1.0', { 'packages': [ 'corechart' ] });");
		report.println("google.setOnLoadCallback(drawChart_" + _method + ");");
		report.println("function drawChart_" + _method + "() {");
		report.print("var options = { ");
		report.print("'title': '" + _method + "() latency', ");
		report.print("'width': 800, ");
		report.print("'height': 600, ");
		report
			.print("'vAxis': { 'title': 'Latency (ns)', 'logScale': true }, ");
		report
			.print("'hAxis': { 'title': 'Iterations', 'gridlines': { 'count': 6 } }, ");
		report.print("'pointSize': 1 ");
		report.println("};");
		report.println("var data = new google.visualization.DataTable();");
		report.println("data.addColumn('number', 'Round #');");
		for(final String library : latencyResults.get(0).keySet())
		{
			report.println("data.addColumn('number', '" + library + "');");
		}
		report.println("data.addRows([");
		for(int round = 0; round < latencyResults.size(); round++)
		{
			final Map<String, Double> resultsForRound =
				latencyResults.get(round);
			report.print("[ " + round + ", ");
			{
				boolean first = true;
				for(final String library : latencyResults.get(0).keySet())
				{
					if(!first) report.print(", ");
					report.print(resultsForRound.get(library));
					first = false;
				}
			}
			report.print(" ]");
			if(round < latencyResults.size() - 1) report.print(",");
			report.println();
		}
		report.println("]);");
		report.println("var chart = new google.visualization.ScatterChart(");
		report.println("document.getElementById('" + _method + "-chart'));");
		report.println("chart.draw(data, options); }");
		report.println("</script>");
		report.println("</body></html>");
		report.close();
	}
}
