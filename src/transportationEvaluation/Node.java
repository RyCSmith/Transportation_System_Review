package transportationEvaluation;

import java.util.ArrayList;
/**
 * Node object used in adjacency list representation of graph data.
 * Basic node contains only the name of the town it represents and the distance the town 
 * is located from the town that holds this node.
 * @author Ryan C Smith
 */
public class Node {
	String town;
	int distance;
	public Node(String townName, int distance){
		town = townName;
		this.distance = distance;
	}
	
	/**
	 * Takes a list of Nodes and loops to see if a Node for a given town is in the list (returns a reference if so).
	 * Used for checking if a road exists between two towns.
	 * @param list - list of all roads leaving a given town.
	 * @param town - town being sought.
	 * @return reference to Node representing road to town being sought.
	 */
	public static Node findNode(ArrayList<Node> list, String town){
		for (Node node:list){
			if (node.town.equals(town))
				return node;
		}
		return null;
	}
}
