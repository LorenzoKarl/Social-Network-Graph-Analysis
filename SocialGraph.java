import java.util.*;

public class SocialGraph {
    private final Map<String, List<String>> graph;

    public SocialGraph() {
        this.graph = new HashMap<>();
    }

    public boolean addUser(String name) {
        if (name == null || name.trim().isEmpty()) return false;
        name = name.trim();
        if (!graph.containsKey(name)) {
            graph.put(name, new ArrayList<>());
            return true;
        }
        return false;
    }

    public boolean addConnection(String user1, String user2) {
        if (!graph.containsKey(user1) || !graph.containsKey(user2)) return false;
        if (user1.equalsIgnoreCase(user2)) return false;
        if (!graph.get(user1).contains(user2)) {
            graph.get(user1).add(user2);
            graph.get(user2).add(user1);
            return true;
        }
        return false;
    }

    public Set<String> getAllUsers() {
        return graph.keySet();
    }

    public List<String> getFriends(String user) {
        return graph.getOrDefault(user, Collections.emptyList());
    }

    // --- ANIMATED TERMINAL BFS TRAVERSAL ---
    public List<String> getShortestPathAnimated(String start, String target) {
        if (!graph.containsKey(start) || !graph.containsKey(target)) return Collections.emptyList();

        Queue<String> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();
        Map<String, String> parentMap = new HashMap<>();

        queue.add(start);
        visited.add(start);

        System.out.println("\n\u001B[36m[BFS TRAVERSAL STARTED]\u001B[0m Initializing search queue from: \u001B[1m" + start + "\u001B[0m");
        
        boolean found = false;
        while (!queue.isEmpty()) {
            String current = queue.poll();
            
            // Terminal animation pulse effect
            printAnimatedPulse("Exploring node connections for: \u001B[33m" + current + "\u001B[0m");

            if (current.equals(target)) {
                found = true;
                break;
            }

            for (String neighbor : graph.getOrDefault(current, Collections.emptyList())) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    parentMap.put(neighbor, current);
                    queue.add(neighbor);
                }
            }
        }

        if (!found) return Collections.emptyList();

        // Reconstruct path
        List<String> path = new ArrayList<>();
        String curr = target;
        while (curr != null) {
            path.add(0, curr);
            curr = parentMap.get(curr);
        }
        return path;
    }

    // --- MUTUAL FRIENDS RECOMMENDATION ENGINE ---
    public Map<String, Integer> getFriendRecommendations(String user) {
        Map<String, Integer> recommendations = new HashMap<>();
        if (!graph.containsKey(user)) return recommendations;

        List<String> userFriends = graph.get(user);

        for (String friend : userFriends) {
            for (String friendOfFriend : graph.get(friend)) {
                if (!friendOfFriend.equals(user) && !userFriends.contains(friendOfFriend)) {
                    recommendations.put(friendOfFriend, recommendations.getOrDefault(friendOfFriend, 0) + 1);
                }
            }
        }
        return recommendations;
    }

    public String getMostInfluentialUser() {
        String topUser = null;
        int maxDegree = -1;
        for (Map.Entry<String, List<String>> entry : graph.entrySet()) {
            if (entry.getValue().size() > maxDegree) {
                maxDegree = entry.getValue().size();
                topUser = entry.getKey();
            }
        }
        return topUser != null ? topUser + " (" + maxDegree + " direct connections)" : null;
    }

    public List<List<String>> getCommunities() {
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
                    for (String neighbor : graph.get(current)) {
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

    public void displayAsciiMatrix() {
        List<String> users = new ArrayList<>(graph.keySet());
        if (users.isEmpty()) {
            System.out.println("Graph is empty.");
            return;
        }

        System.out.println("\n\u001B[35m=========================================================\u001B[0m");
        System.out.println("\u001B[1m              LIVE ADJACENCY MATRIX (CLI VIEW)          \u001B[0m");
        System.out.println("\u001B[35m=========================================================\u001B[0m");
        
        System.out.printf("%-12s |", "Users");
        for (String u : users) {
            System.out.printf(" %-8s |", u.length() > 8 ? u.substring(0, 7) : u);
        }
        System.out.println("\n" + "-".repeat(15 + users.size() * 11));

        for (String u1 : users) {
            System.out.printf("%-12s |", u1.length() > 12 ? u1.substring(0, 11) : u1);
            List<String> friends = graph.get(u1);
            for (String u2 : users) {
                if (u1.equals(u2)) {
                    System.out.print("    -     |");
                } else if (friends.contains(u2)) {
                    System.out.print("   \u001B[32m[ * ]\u001B[0m  |");
                } else {
                    System.out.print("    .     |");
                }
            }
            System.out.println();
        }
        System.out.println("\u001B[35m=========================================================\u001B[0m");
        System.out.println("Legend: \u001B[32m[ * ]\u001B[0m Direct Connection  |  . Unconnected  |  - Self");
    }

    private void printAnimatedPulse(String message) {
        String[] spinners = {"|", "/", "-", "\\"};
        for (int i = 0; i < 4; i++) {
            System.out.print("\r\u001B[34m" + spinners[i] + "\u001B[0m " + message);
            try {
                Thread.sleep(80);
            } catch (InterruptedException ignored) {}
        }
        System.out.println("\r\u001B[32m✓\u001B[0m " + message);
    }
}