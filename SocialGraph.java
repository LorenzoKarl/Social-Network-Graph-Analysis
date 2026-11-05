import java.util.*;

public class SocialGraph {
    // Adjacency List: Key = User Name, Value = List of direct friends
    private final Map<String, List<String>> graph;

    public SocialGraph() {
        this.graph = new HashMap<>();
    }

    /**
     * Adds a new user node to the network.
     */
    public boolean addUser(String name) {
        if (name == null || name.trim().isEmpty()) return false;
        name = name.trim();
        
        if (!graph.containsKey(name)) {
            graph.put(name, new ArrayList<>());
            return true;
        }
        return false;
    }

    /**
     * Creates an undirected (bidirectional) connection between two users.
     */
    public boolean addFriendship(String user1, String user2) {
        if (user1 == null || user2 == null) return false;
        user1 = user1.trim();
        user2 = user2.trim();

        if (graph.containsKey(user1) && graph.containsKey(user2) && !user1.equalsIgnoreCase(user2)) {
            List<String> friends1 = graph.get(user1);
            List<String> friends2 = graph.get(user2);

            if (!friends1.contains(user2)) {
                friends1.add(user2);
                friends2.add(user1);
                return true;
            }
        }
        return false;
    }

    public Set<String> getUsers() {
        return graph.keySet();
    }

    public List<String> getFriends(String name) {
        return graph.getOrDefault(name, Collections.emptyList());
    }

    /**
     * Prints the full network topology to the terminal.
     */
    public void displayGraph() {
        if (graph.isEmpty()) {
            System.out.println("\nNetwork is currently empty.");
            return;
        }

        System.out.println("\n--- Social Network Topology ---");
        for (Map.Entry<String, List<String>> entry : graph.entrySet()) {
            String user = entry.getKey();
            List<String> friends = entry.getValue();
            String friendsStr = friends.isEmpty() ? "No connections" : String.join(", ", friends);
            System.out.println("  " + user + " -> [" + friendsStr + "]");
        }
        System.out.println("-------------------------------\n");
    }

    // ==========================================
    // ALGORITHMS SECTION
    // ==========================================

    /**
     * 1. BFS Shortest Path Routing (Degrees of Separation)
     * Finds the shortest connection path between start and target user.
     */
    public List<String> findShortestPath(String start, String target) {
        if (start == null || target == null) return Collections.emptyList();
        start = start.trim();
        target = target.trim();

        if (!graph.containsKey(start) || !graph.containsKey(target)) {
            return Collections.emptyList();
        }

        if (start.equalsIgnoreCase(target)) {
            return Collections.singletonList(start);
        }

        Queue<List<String>> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();

        // Start path containing the initial node
        List<String> initialPath = new ArrayList<>();
        initialPath.add(start);
        queue.add(initialPath);
        visited.add(start);

        while (!queue.isEmpty()) {
            List<String> path = queue.poll();
            String current = path.get(path.size() - 1);

            for (String neighbor : graph.getOrDefault(current, Collections.emptyList())) {
                if (!visited.contains(neighbor)) {
                    List<String> newPath = new ArrayList<>(path);
                    newPath.add(neighbor);

                    if (neighbor.equalsIgnoreCase(target)) {
                        return newPath; // Path found
                    }

                    visited.add(neighbor);
                    queue.add(newPath);
                }
            }
        }

        return Collections.emptyList(); // No connection exists
    }

    /**
     * 2. Degree Centrality (Most Influential User)
     * Returns the user node with the highest number of direct connections.
     */
    public String getMostInfluentialUser() {
        if (graph.isEmpty()) return null;

        String maxUser = null;
        int maxConnections = -1;

        for (Map.Entry<String, List<String>> entry : graph.entrySet()) {
            int connections = entry.getValue().size();
            if (connections > maxConnections) {
                maxConnections = connections;
                maxUser = entry.getKey();
            }
        }

        return maxUser;
    }

    /**
     * 3. Community Detection (Connected Components)
     * Groups disconnected sub-networks into separate clusters using BFS traversal.
     */
    public List<List<String>> detectCommunities() {
        List<List<String>> communities = new ArrayList<>();
        Set<String> visited = new HashSet<>();

        for (String user : graph.keySet()) {
            if (!visited.contains(user)) {
                List<String> community = new ArrayList<>();
                Queue<String> queue = new LinkedList<>();

                queue.add(user);
                visited.add(user);

                while (!queue.isEmpty()) {
                    String current = queue.poll();
                    community.add(current);

                    for (String neighbor : graph.getOrDefault(current, Collections.emptyList())) {
                        if (!visited.contains(neighbor)) {
                            visited.add(neighbor);
                            queue.add(neighbor);
                        }
                    }
                }

                communities.add(community);
            }
        }

        return communities;
    }
}