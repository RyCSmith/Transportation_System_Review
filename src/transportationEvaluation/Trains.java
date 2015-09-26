package transportationEvaluation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import javax.swing.JFileChooser;

/**
 * Driver program for transportation route inquiry program.
 * @author Ryan C Smith
 */
public class Trains {
	/**
	 * Shared Scanner object for user input.
	 */
	static Scanner input = new Scanner(System.in);
	
	/**
	 * Executes the Trains program.
	 * @param args - command line arguments (not used)
	 */
	public static void main(String[] args) {
		try {
			String fileText = load();
			RouteMap currentRouteMap = new RouteMap(createAdjacencyList(fileText));
			int userChoice = 0;
			do{
				System.out.println("Please select an operation from the following options:\n"
						+ "\t1 : Compute the distance of a route.\n"
						+ "\t2 : Find the number of routes by distance or stops.\n"
						+ "\t3 : Find the shortest route between two towns.\n"
						+ "\t4 : Quit.");
				userChoice = input.nextInt();
				switch(userChoice){
					case(1): 
						distanceCase(currentRouteMap);
						break;
					case(2):
						numRoutesCase(currentRouteMap);
						break;
					case(3):
						shortestRouteCase(currentRouteMap);
				}		
			}while (userChoice != 4);
		} catch (IOException e) {
			System.out.println("An error occurred while reading from file.");
		} catch (IllegalStateException e1){
			System.out.println(e1.toString());
		}

	}
	
	/**
	 * Loads a text file containing Graph data into a String.
	 * @return Graph data in String form.
	 * @throws IOException
	 */
	 public static String load() throws IOException {
		 StringBuilder fileText = new StringBuilder();
	     JFileChooser chooser = new JFileChooser();
	     chooser.setDialogTitle("Select the file containing map input:");
	     int result = chooser.showOpenDialog(null);
	     if (result == JFileChooser.APPROVE_OPTION) {
	     File file = chooser.getSelectedFile();
	     	if (file != null) {
	     		String fileName = file.getCanonicalPath();
	            BufferedReader reader = new BufferedReader(new FileReader(fileName));
	            String line;
	            while ((line = reader.readLine()) != null) {
	            	fileText.append(line);
	            }
	            reader.close();
	        }
	    }
	    return fileText.toString();
	 }
	 
	 /**
	  * Takes graph data as a string and constructs an adjacency list representation of a graph.
	  * @param fileText - String containing graph data.
	  * @return adjacencyList - HashMap containing an adjacency list representation of a graph.
	  * @throws IllegalStateException - Thrown if program encounters improperly formatted input within the String. 
	  */
	 public static HashMap<String, ArrayList<Node>> createAdjacencyList(String fileText) throws IllegalStateException{
		 HashMap<String, ArrayList<Node>> adjacencyList = new HashMap<String, ArrayList<Node>>();
		 String[] roads = fileText.split(" ");
		 for (int i = 0; i < roads.length; i++){
			 String currentRoad = roads[i].replace("(", "").replace(")", "");;
			 String[] elements = currentRoad.split(",");
			 if (elements.length != 3)
				 throw new IllegalStateException("Improper Format in Graph File. Please Check.");
			 if (adjacencyList.get(elements[0]) == null)
				 adjacencyList.put(elements[0], new ArrayList<Node>());
			 adjacencyList.get(elements[0]).add(new Node(elements[1], Integer.parseInt(elements[2])));
		 }
		 return adjacencyList;
	 }
	
	/**
	 * Prompts the user for a route then generates and prints the total distance.
	 * @param currentRouteMap - RouteMap object containing a graph representing a transportation system.
	 */
	public static void distanceCase(RouteMap currentRouteMap){
		System.out.println("Please enter the route. Use the format townname-townname-...");
		input.nextLine();
		String[] route = input.nextLine().trim().split("-");
		Integer result = currentRouteMap.calculateRouteDistance(route);
		if (result == null)
			System.out.println("NO SUCH ROUTE");
		else
			System.out.printf("The total distance of your route is: %d\n\n", result);
	}
	
	/**
	 * Prompts the user for input, then generates the number of routes satisfying the constraints of their request.
	 * @param currentRouteMap - RouteMap object containing a graph representing a transportation system.
	 */
	public static void numRoutesCase(RouteMap currentRouteMap){
		System.out.println("Please select a detail operation:\n"
				+ "\t1 : Find the number of routes with total distance less than x:\n"
				+ "\t2 : Find the number of routes with total stops <= x:\n"
				+ "\t3 : Find the number of routes with exactly x stops:");
		int subMenuSelection = input.nextInt();
		System.out.println("Please enter the name of the starting town:");
		String start = input.next().trim();
		System.out.println("Please enter the name of the destination town:");
		String end = input.next().trim();
		System.out.println("Please enter the number constraint for the selected operation:");
		int numberConstraint = input.nextInt();
		switch(subMenuSelection){
		case(1):
			System.out.printf("The number of trips starting at %s and ending at %s are with less than"
					+ " %d total distance: %d\n\n", start, end, numberConstraint, 
					currentRouteMap.calculateNumberOfRoutesByDistance(start, end, numberConstraint));
			break;
		case(2):
			System.out.printf("The number of trips starting at %s and ending at %s are with <="
					+ " %d stops: %d\n\n", start, end, numberConstraint, 
					currentRouteMap.calculateNumberOfRoutesByStops(start, end, numberConstraint, false));
			break;
		case(3):
			System.out.printf("The number of trips starting at %s and ending at %s are with exactly"
					+ " %d stops: %d\n\n", start, end, numberConstraint, 
					currentRouteMap.calculateNumberOfRoutesByStops(start, end, numberConstraint, true));
		}
	}
	
	/**
	 * Prompts the user for a start and end town and calculates the shortest possible distance available.
	 * @param currentRouteMap - RouteMap object containing a graph representing a transportation system.
	 */
	public static void shortestRouteCase(RouteMap currentRouteMap){
		System.out.println("Please enter the name of the starting town:");
		String start = input.next().trim();
		System.out.println("Please enter the name of the destination town:");
		String end = input.next().trim();
		Integer shortestRoute = currentRouteMap.calculateShortestRoute(start, end);
		if (!(shortestRoute == null))
			System.out.printf("The shortest route between %s and %s is: %d\n\n", start, end, shortestRoute);
		else
			System.out.println("NO SUCH ROUTE");
	}
}
