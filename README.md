# Transportation_System_Review
Command Line tool to represent a transportation system and find different route options.

Reads a graph from a text file in the format (Chicago,New York) (Washington,Orlando) (Washington,Philadelphia) where each connection in a transportation system is listed in () separated by a comma.   

Uses modified verions of Dijkstra's and BFS to provide the following operations:  
- Compute the distance of a route.  
-Find the shortest route between two towns.  
-Find the number of routes with total distance less than x.  
-Find the number of routes with total stops <= x.  
-Find the number of routes with exactly x stops.  