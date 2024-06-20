package camisetastematicas.es;

import java.util.*;

class Main {

    public static void main(String[] args) {
        System.out.print(graphChallenge(args,"X", "W"));
    }

//    public static int graphChallenge(String[] graph, String start, String end) {
//        checkGraphInput(graph,start,end);
//        return findShortestPath(graph,start,end);
//    }

    public static List<String> graphChallenge(String[] graph, String start, String end) {
        checkGraphInput(graph,start,end);
        return findShortestPathString(graph,start,end);
    }

    public static int findShortestPath(String[] graph, String start, String end) {

        if (start.equals(end)) {
            return 0;  // 0 steps required if start and end are the same node
        }

        HashMap<String, List<String>> adjacencyList = createAdjacencyList(graph);

        // If the start or end nodes aren't in our graph, return -1
        if (!adjacencyList.containsKey(start) || !adjacencyList.containsKey(end)) {
            return -1;
        }

        HashSet<String> visited = new HashSet<>();
        Queue<String> queue = new ArrayDeque<>();
        queue.add(start);

        int steps = 0;

        while (!queue.isEmpty()) {
            steps++;

            int size = queue.size();

            for (int i = 0; i < size; i++) {
                String node = queue.remove();
                visited.add(node);

                List<String> neighbours = adjacencyList.get(node);

                if (neighbours != null) {
                    for (String neighbour : neighbours) {
                        if (neighbour.equals(end)) {
                            return steps;
                        }
                        if (!visited.contains(neighbour)) {
                            queue.add(neighbour);
                        }
                    }
                }
            }
        }

        return -1;  // return -1 if no path exists
    }

    public static List<String> findShortestPathString(String[] graph, String start, String end) {
        if (start.equals(end)) {
            return Collections.singletonList(start); // we start and end at the same node
        }

        HashMap<String, List<String>> adjacencyList = createAdjacencyList(graph);

        // If the start or end nodes are not in our graph, return an empty list
        if (!adjacencyList.containsKey(start) || !adjacencyList.containsKey(end)) {
            return new ArrayList<>();
        }

        HashMap<String, String> predecessors = new HashMap<>();
        HashSet<String> visited = new HashSet<>();
        Queue<String> queue = new ArrayDeque<>();
        queue.add(start);

        while (!queue.isEmpty()) {
            String currentNode = queue.remove();

            if (currentNode.equals(end)) {
                // We have found the end node, now let's reconstruct the path
                List<String> path = new ArrayList<>();
                String pathNode = end;
                while (pathNode != null) {
                    path.add(0, pathNode);
                    pathNode = predecessors.get(pathNode);
                }
                return path;
            }

            visited.add(currentNode);

            List<String> neighbors = adjacencyList.get(currentNode);

            if (neighbors != null) {
                for (String neighbor : neighbors) {
                    if (!visited.contains(neighbor)) {
                        queue.add(neighbor);
                        // Store the current node as the predecessor of the neighbor if it doesn't have a saved predecessor yet
                        predecessors.putIfAbsent(neighbor, currentNode);
                    }
                }
            }
        }

        return new ArrayList<>(); // return an empty list if there is no path
    }

    private static HashMap<String, List<String>> createAdjacencyList(String[] graph) {

        HashMap<String, List<String>> adjacencyList = new HashMap<>();
        int numberOfNodes = Integer.parseInt(graph[0]);

        for (int i = numberOfNodes + 1; i < graph.length; i++) {
            String[] nodes = graph[i].split("-");

            List<String> list1 = adjacencyList.getOrDefault(nodes[0], new ArrayList<>());
            list1.add(nodes[1]);
            adjacencyList.put(nodes[0], list1);

            List<String> list2 = adjacencyList.getOrDefault(nodes[1], new ArrayList<>());
            list2.add(nodes[0]);
            adjacencyList.put(nodes[1], list2);
        }

        return adjacencyList;
    }

    private static void checkGraphInput(String[] graph, String start, String end) {

        checkNodeIsValid(start, start);
        checkNodeIsValid(end, end);

        // Check if the array is null or has less than 2 elements.
        checkGraphIsValid(graph);
        int numberOfNodes = validateNumberOfNodes(graph[0]);
        Set<String> validNodes = validateAndStoreNodeFormat(graph, numberOfNodes);
        validateNodeConnections(graph, numberOfNodes, validNodes);
    }

    private static void checkGraphIsValid(String[] graph) {
        if (graph == null || graph.length < 2) {
            throw new IllegalArgumentException("Input array is invalid!");
        }
    }

    private static void checkNodeIsValid(String node, String type) {
        if (node == null
                || node.length() != 1
                || !Character.isLetter(node.charAt(0))
                || !Character.isUpperCase(node.charAt(0))) {
            throw new IllegalArgumentException(type + " variable needs to be a single, uppercase letter");
        }
    }

    private static int validateNumberOfNodes(String firstElem) {
        // Check if the first element is a number
        try {
            return Integer.parseInt(firstElem);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("First element of the array must be a number representing number of nodes!.");
        }
    }

    private static Set<String> validateAndStoreNodeFormat(String[] graph, int numberOfNodes) {
        /* This loop checks the elements of the strArr array starting from the second element (index 1) up to the defined number of nodes.
           It verifies each node with the following criteria:
           1. The node is a single character.
           2. The character is a letter.
           3. The letter is uppercase.
           If any of these conditions are not met, it throws an IllegalArgumentException.
           This is to ensure a valid format for the nodes in the array.*/
        Set<String> validNodes = new HashSet<>();
        for (int i = 1; i < numberOfNodes + 1; i++) {
            String node = graph[i];
            if (node.length() != 1
                    || !Character.isLetter(node.charAt(0))
                    || !Character.isUpperCase(node.charAt(0))) {
                throw new IllegalArgumentException("\"" + node + "\" is an invalid node format!");
            }
            validNodes.add(node);
        }
        return validNodes;
    }

    private static void validateNodeConnections(String[] connections, int numberOfNodes, Set<String> validNodes) {
        // Check if the connections are of the correct format
        for (int i = numberOfNodes + 1; i < connections.length; i++) {
            if (!connections[i].contains("-")
                    || connections[i].split("-").length != 2) {
                throw new IllegalArgumentException("\"" + connections[i] + "\" is an invalid connection format! Must be \"node-node\"");
            }
            String[] nodesInConnection = connections[i].split("-");

            for (String node : nodesInConnection) {
                if (!validNodes.contains(node)) {
                    throw new IllegalArgumentException("\"" + node + "\" is not a valid node!, valid nodes are " + validNodes);
                }
            }
        }
    }
}