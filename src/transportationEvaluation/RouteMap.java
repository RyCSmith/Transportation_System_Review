package transportationEvaluation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

/**
 * Represents a transportation system and provides operations for evaluating available routes.
 * @author Ryan C Smith
 */
public class RouteMap {
	HashMap<String, ArrayList<Node>> adList;
	
	public RouteMap(HashMap<String, ArrayList<Node>> adList){
		this.adList = adList;
	}
	
	/**
	 * Calculates the distance between 2 towns. 
	 * @param towns - String array containing the name of each town to be visited in order.
	 * @return - Integer with the total distance for the route. Null if no route exists.
	 */
	public Integer calculateRouteDistance(String[] towns){
		//base cases for recursion
		if (towns.length == 0 || towns.length == 1){
			return 0;
		}
		else if (towns.length == 2){
			if (Node.findNode(adList.get(towns[0]), towns[1]) == null)
				return null;
			else
				return Node.findNode(adList.get(towns[0]), towns[1]).distance;
		}
		//recursion
		else{
			if (Node.findNode(adList.get(towns[0]), towns[1]) == null ||
					calculateRouteDistance(Arrays.copyOfRange(towns, 1, towns.length)) == null){
				return null;
			}
			else
				return Node.findNode(adList.get(towns[0]), towns[1]).distance + calculateRouteDistance(Arrays.copyOfRange(towns, 1, towns.length));
		}
	}
	
	/**
	 * Uses a modified version of DFS to traverse a graph representing a transportation system and return the 
	 * number of distinct routes between a start town and end town that have a distance of less than maxDistance.
	 * @param start - origin town in String form
	 * @param end - destination town in String form
	 * @param maxDistance - distance limit for a given route
	 * @return number of distinct routes with distance < maxDistance between start and end towns.
	 */
	public Integer calculateNumberOfRoutesByDistance(String start, String end, int maxDistance){
		ArrayList<ArrayList<String>> masterPathsList = new ArrayList<ArrayList<String>>();
		Queue<ArrayList<String>> pathQueue = new LinkedList<ArrayList<String>>();
		ArrayList<String> pathBuilder = new ArrayList<String>();
		pathBuilder.add(start);
		pathQueue.add(pathBuilder);
		while (!pathQueue.isEmpty()){
			ArrayList<String> currentPartialPath = pathQueue.poll();
			String lastTownInPath = currentPartialPath.get(currentPartialPath.size() - 1);	
			String[] pathAsArray = new String[currentPartialPath.size()];
			pathAsArray = currentPartialPath.toArray(pathAsArray);
			Integer currentPathLength = calculateRouteDistance(pathAsArray);
			if (lastTownInPath.equals(end) && currentPartialPath.size() > 1 && currentPathLength < maxDistance)
				masterPathsList.add(currentPartialPath);
			if (currentPathLength < maxDistance){
				for (Node node: adList.get(lastTownInPath)){
					ArrayList<String> updatedPath = new ArrayList<String>();
					updatedPath.addAll(currentPartialPath);
					updatedPath.add(node.town);
					pathQueue.add(updatedPath);
				}
			}
		}
		return masterPathsList.size();	
	}
	
	/**
	 * Uses a modified version of BFS to traverse a graph representing a transportation system and return the 
	 * number of distinct routes between a start town and end town with <= maxStops. If boolean filter == true, 
	 * the number returned will reflect the number of routes with exactly maxStops.
	 * @param start - origin town in String form
	 * @param end - destination town in String form
	 * @param maxStops - the maximum number of stops allowed on a route.
	 * @param filter - boolean, determines whether to return routes with exact number of stops or <= number of stops.
	 * @return filter boolean is used, when true returns counts for routes with stops == maxStops, when false returns routes with stops <= maxStops
	 */
	public Integer calculateNumberOfRoutesByStops(String start, String end, int maxStops, boolean filter){
		ArrayList<ArrayList<String>> masterPathsList = new ArrayList<ArrayList<String>>();
		Queue<ArrayList<String>> pathQueue = new LinkedList<ArrayList<String>>();
		ArrayList<String> pathBuilder = new ArrayList<String>();
		pathBuilder.add(start);
		pathQueue.add(pathBuilder);
		while (!pathQueue.isEmpty()){
			ArrayList<String> currentPartialPath = pathQueue.poll();
			String lastTownInPath = currentPartialPath.get(currentPartialPath.size() - 1);	
			if (lastTownInPath.equals(end) && currentPartialPath.size() > 1 && currentPartialPath.size() <= (maxStops + 1))
				masterPathsList.add(currentPartialPath);
			if (currentPartialPath.size() <= (maxStops + 1)){
				for (Node node: adList.get(lastTownInPath)){
					ArrayList<String> updatedPath = new ArrayList<String>();
					updatedPath.addAll(currentPartialPath);
					updatedPath.add(node.town);
					pathQueue.add(updatedPath);
				}
			}
		}
		return filter ? filterBySize(masterPathsList, maxStops + 1) : masterPathsList.size();	
	}
	
	/**
	 * Helper function for calculateNumberOfRoutesByStops. Returns number of routes containing exactly the
	 * desired number of stops.
	 * @param pathsList - List of routes with <= desired number of stops.
	 * @param desiredSize - Exact number of stops.
	 * @return Number of routes with exact number of stops.
	 */
	protected Integer filterBySize(ArrayList<ArrayList<String>> pathsList, int desiredSize){
		int counter = 0;
		for (ArrayList<String> list : pathsList){
			if (list.size() == desiredSize)
				counter++;
		}
		return counter;
	}
	/**
	 * Uses a modified version of Djikstra's algorithm to calculate the shortest route between 2 towns. 
	 * @param start - start town in String form.
	 * @param end - destination town in String form.
	 * @return Total distance of shortest path between two towns. Returns null if no route exists.
	 */
	public Integer calculateShortestRoute(String start, String end){
		//create and fill arrays with starting values
		HashMap<String, Integer> shortestPath = new HashMap<String, Integer>();
		for (String town: adList.keySet()){
			if (town.equals(start))
				shortestPath.put(town, 0);
			else
				shortestPath.put(town, Integer.MAX_VALUE);
		}
		//djikstra's
		Set<String> townSet = cloneSet(adList.keySet());
		while (!townSet.isEmpty()){
			String currentClosestTown = findShortest(shortestPath, townSet);
			townSet.remove(currentClosestTown);
			for (Node node: adList.get(currentClosestTown)){
				if ((shortestPath.get(currentClosestTown) + node.distance) < shortestPath.get(node.town)){
					if (!((shortestPath.get(currentClosestTown) + node.distance) < shortestPath.get(currentClosestTown)))//detects overflows
						shortestPath.put(node.town, (shortestPath.get(currentClosestTown) + node.distance));
				}
			}
		}
		//handle case of cycle
		if (start.equals(end)){
			Integer shortestCycle = Integer.MAX_VALUE;
			for (String item:shortestPath.keySet()){
				for (Node node: adList.get(item)){
					if (node.town.equals(start))
						if (shortestPath.get(item) + node.distance < shortestCycle)
							if (!((shortestPath.get(item) + node.distance) < shortestPath.get(item)))
								shortestCycle = shortestPath.get(item) + node.distance;
				}
			}
			return (shortestCycle == Integer.MAX_VALUE) ? null : shortestCycle; //if distance remains MAX_VALUE, no path exists
		}
		return (shortestPath.get(end) == Integer.MAX_VALUE) ? null : shortestPath.get(end);
	}
	
	/**
	 * Helper method for calculateShortestRoute. Makes a deep copy of a set.
	 * @param original - Set of Strings
	 * @return Deep copy of Set of Strings
	 */
	protected Set<String> cloneSet(Set<String> original){
		Set<String> clonedSet = new HashSet<String>();
		for (String item: original){
			clonedSet.add(new String(item));
		}
		return clonedSet;
	}
	
	/**
	 * Helper method for calculateShortestRoute. Finds the town in the set with the shortest current distance from the origin town.
	 * @param towns - HashMap of towns and their current shortest distances from the origin town.
	 * @param townSet - Set of whose routes have not yet been optimized.
	 * @return String name of the town with the current shortest route from the origin town.
	 */
	protected String findShortest(HashMap<String, Integer> towns, Set<String> townSet){
		String shortestTown = null;
		Integer shortestDistance = Integer.MAX_VALUE;
		if (townSet.size() == 1)
			return (String) townSet.toArray()[0];
		for (String town:townSet){
			if (towns.get(town) < shortestDistance){
				shortestDistance = towns.get(town);
				shortestTown = town;
			}
		}
		return shortestTown;
	}
}
