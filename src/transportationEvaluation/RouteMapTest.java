package transportationEvaluation;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

/**
 * Unit Tests for Route Map Test.
 * @author Ryan C Smith
 */
public class RouteMapTest {
	RouteMap routes;
	
	@Before
	public void setUp() throws Exception {
		
		routes = new RouteMap(Trains.createAdjacencyList("(A,B,5) (B,C,4) (C,D,8) (D,C,8) (D,E,6) (A,D,5) (C,E,2) (E,B,3) (A,E,7)"));
	}

	@Test
	public void testFindShortest() {
		HashMap<String, Integer> shortestMap = new HashMap<String, Integer>();
		String[] array = {"A","B","C","D","E"};
		HashSet<String> set = new HashSet<String>();
		int i = 0;
		for (String x:array){
			shortestMap.put(x, i);
			i++;
			set.add(x);
		}
		assertEquals("A", routes.findShortest(shortestMap, set));
		set.remove("A");
		assertEquals("B", routes.findShortest(shortestMap, set));
		set.remove("B");
		assertEquals("C", routes.findShortest(shortestMap, set));
		set.remove("C");
		assertEquals("D", routes.findShortest(shortestMap, set));
		set.remove("D");
		assertEquals("E", routes.findShortest(shortestMap, set));
		set.remove("E");	
		assertEquals(null, routes.findShortest(shortestMap, set));
	}
	
	@Test
	public void testCloneSet(){
		HashSet<String> set = new HashSet<String>();
		String[] array = {"A","B","C","D","E"};
		for (int i = 0; i < 5; i++){
			set.add(array[i]);
		}
		Set<String> set2 = routes.cloneSet(set);
		assertTrue(set.contains("A"));
		assertTrue(set2.contains("A"));
		set2.remove("A");
		assertTrue(set.contains("A"));
		assertFalse(set2.contains("A"));
	}
	
	@Test
	public void testCalculateShortestRoute(){
		assertEquals(new Integer(9), routes.calculateShortestRoute("A", "C"));
		assertEquals(new Integer(9), routes.calculateShortestRoute("B", "B"));
		assertEquals(new Integer(5), routes.calculateShortestRoute("A", "D"));
		assertEquals(new Integer(5), routes.calculateShortestRoute("C", "B"));
	}
	
	@Test
	public void testCalculateRouteDistance(){
		String[] route0 = {"A","B","C"};
		assertEquals(new Integer(9), routes.calculateRouteDistance(route0));
		String[] route1 = {"A","D"};
		assertEquals(new Integer(5), routes.calculateRouteDistance(route1));
		String[] route2 = {"A","D","C"};
		assertEquals(new Integer(13), routes.calculateRouteDistance(route2));
		String[] route3 = {"A","E","B","C","D"};
		assertEquals(new Integer(22), routes.calculateRouteDistance(route3));
		String[] route4 = {"A","E","D"};
		assertEquals(null, routes.calculateRouteDistance(route4));
	}
	
	@Test
	public void testCalculateNumberOfRoutesByDistance(){
		assertEquals(new Integer(7), routes.calculateNumberOfRoutesByDistance("C", "C", 30));
		assertEquals(new Integer(3), routes.calculateNumberOfRoutesByDistance("A", "C", 15));
		assertEquals(new Integer(2), routes.calculateNumberOfRoutesByDistance("A", "C", 14));
		assertEquals(new Integer(1), routes.calculateNumberOfRoutesByDistance("A", "D", 10));
		assertEquals(new Integer(2), routes.calculateNumberOfRoutesByDistance("A", "D", 20));
	}
	
	@Test
	public void testCalculateNumberOfRoutesByStops(){
		assertEquals(new Integer(2), routes.calculateNumberOfRoutesByStops("C", "C", 3, false));
		assertEquals(new Integer(3), routes.calculateNumberOfRoutesByStops("A", "C", 4, true));
		assertEquals(new Integer(2), routes.calculateNumberOfRoutesByStops("A", "E", 2, false));
		assertEquals(new Integer(1), routes.calculateNumberOfRoutesByStops("A", "E", 2, true));
	}

}
