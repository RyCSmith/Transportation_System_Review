package transportationEvaluation;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

/**
 * Unit test for createAdjacencyList in Trains class.
 * @author Ryan C Smith
 */
public class TrainsTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testCreateAdjacencyList() {
		HashMap<String, ArrayList<Node>> adList = Trains.createAdjacencyList("(A,B,5) (B,C,4) (C,D,8) (D,C,8) (D,E,6) (A,D,5) (C,E,2) (E,B,3) (A,E,7)");
		String[] array = {"A","B","C","D","E"};
		for (String item:array){
			assertTrue(adList.keySet().contains(item));
		}
		assertEquals(3, adList.get("A").size());
		assertEquals(1, adList.get("B").size());
		assertEquals(2, adList.get("C").size());
		assertEquals(2, adList.get("D").size());
		assertEquals(1, adList.get("E").size());
		
		for (Node node:adList.get("A")){
			if (node.town == "B")
				assertEquals(node.distance, 5);
			if (node.town == "D")
				assertEquals(node.distance, 5);
			if (node.town == "E")
				assertEquals(node.distance, 7);
		}
	}

}
