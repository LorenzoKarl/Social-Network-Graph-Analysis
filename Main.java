import java.util.*;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final SocialGraph socialGraph = new SocialGraph();

    // ANSI Color Constants for Terminal UI
    public static final String RESET = "\u001B[0m";
    public static final String CYAN = "\u001B[36m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String RED = "\u001B[31m";
    public static final String BOLD = "\u001B[1m";

    public static void main(String[] args) {
        seedSampleData();

        boolean exit = false;
        while (!exit) {
            printMainMenu();
            String input = scanner.nextLine().trim();

            switch (input) {
                case "1": handleAddUser(); break;
                case "2": handleAddConnection(); break;
                case "3": handleDisplayMatrix(); break;
                case "4": handleAnimatedBFS(); break;
                case "5": handleFindMostInfluential(); break;
                case "6": handleDetectCommunities(); break;
                case "7": handleFriendRecommendations(); break;
                case "0":
                    System.out.println("\n" + GREEN + "Exiting Social Network Engine. Goodbye!" + RESET);
                    exit = true;
                    break;
                default:
                    System.out.println("\n" + RED + "Invalid choice! Please select 0-7." + RESET);
            }
        }
        scanner.close();
    }

    private static void printMainMenu() {
        System.out.println("\n" + CYAN + "=========================================================" + RESET);
        System.out.println(BOLD + "       SOCIAL NETWORK GRAPH ANALYTICS ENGINE (CLI v2.0)" + RESET);
        System.out.println(CYAN + "=========================================================" + RESET);
        System.out.println("1. Add New User");
        System.out.println("2. Establish Connection (Add Friendship)");
        System.out.println("3. Display Full Network (ASCII Matrix)");
        System.out.println("4. Animated Shortest Path Search (BFS)");
        System.out.println("5. Identify Most Influential User (Centrality)");
        System.out.println("6. Detect Communities (Connected Subgraphs)");
        System.out.println("7. Get Smart Friend Recommendations");
        System.out.println("0. Exit Engine");
        System.out.print(YELLOW + "Select option (0-7): " + RESET);
    }

    private static void handleAddUser() {
        System.out.print("\nEnter user name (or '0' to Go Back): ");
        String name = scanner.nextLine().trim();
        if (name.equals("0") || name.isEmpty()) return;

        if (socialGraph.addUser(name)) {
            System.out.println(GREEN + "✓ User '" + name + "' registered successfully." + RESET);
        } else {
            System.out.println(RED + "✗ User '" + name + "' already exists." + RESET);
        }
    }

    private static void handleAddConnection() {
        System.out.print("\nEnter First User (or '0' to Go Back): ");
        String u1 = scanner.nextLine().trim();
        if (u1.equals("0") || u1.isEmpty()) return;

        System.out.print("Enter Second User (or '0' to Go Back): ");
        String u2 = scanner.nextLine().trim();
        if (u2.equals("0") || u2.isEmpty()) return;

        if (socialGraph.addConnection(u1, u2)) {
            System.out.println(GREEN + "✓ Connection established: " + u1 + " <---> " + u2 + RESET);
        } else {
            System.out.println(RED + "✗ Failed to connect. Check if both users exist." + RESET);
        }
    }

    private static void handleDisplayMatrix() {
        socialGraph.displayAsciiMatrix();
        promptToContinue();
    }

    private static void handleAnimatedBFS() {
        System.out.print("\nEnter Source User (or '0' to Go Back): ");
        String start = scanner.nextLine().trim();
        if (start.equals("0") || start.isEmpty()) return;

        System.out.print("Enter Target User (or '0' to Go Back): ");
        String target = scanner.nextLine().trim();
        if (target.equals("0") || target.isEmpty()) return;

        List<String> path = socialGraph.getShortestPathAnimated(start, target);
        
        System.out.print("\nCalculating metrics ");
        renderProgressBar();

        if (path.isEmpty()) {
            System.out.println("\n" + RED + "NO PATH FOUND: '" + start + "' and '" + target + "' are disconnected." + RESET);
        } else {
            System.out.println("\n" + GREEN + BOLD + "SHORTEST PATH FOUND:" + RESET);
            System.out.println("  " + String.join("  -->  ", path));
            System.out.println("  Degrees of Separation: " + (path.size() - 1));
        }
        promptToContinue();
    }

    private static void handleFindMostInfluential() {
        System.out.println("\n--- DEGREE CENTRALITY ANALYSIS ---");
        String top = socialGraph.getMostInfluentialUser();
        if (top != null) {
            System.out.println("Most Central Node: " + GREEN + BOLD + top + RESET);
        } else {
            System.out.println(RED + "Network is currently empty." + RESET);
        }
        promptToContinue();
    }

    private static void handleDetectCommunities() {
        System.out.println("\n--- COMMUNITY DETECTION ---");
        List<List<String>> communities = socialGraph.getCommunities();
        System.out.println("Disjoint Subgraphs Found: " + BOLD + communities.size() + RESET);
        for (int i = 0; i < communities.size(); i++) {
            System.out.println("  Community Cluster #" + (i + 1) + ": " + YELLOW + communities.get(i) + RESET);
        }
        promptToContinue();
    }

    private static void handleFriendRecommendations() {
        System.out.print("\nEnter User Name for Recommendations (or '0' to Go Back): ");
        String user = scanner.nextLine().trim();
        if (user.equals("0") || user.isEmpty()) return;

        Map<String, Integer> recs = socialGraph.getFriendRecommendations(user);
        System.out.println("\n--- MUTUAL FRIEND RECOMMENDATIONS FOR " + user.toUpperCase() + " ---");
        if (recs.isEmpty()) {
            System.out.println("No recommendations found.");
        } else {
            recs.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .forEach(e -> System.out.println("  User: " + BOLD + e.getKey() + RESET + " (" + e.getValue() + " mutual friend/s)"));
        }
        promptToContinue();
    }

    private static void renderProgressBar() {
        System.out.print("[");
        for (int i = 0; i < 20; i++) {
            System.out.print("=");
            try { Thread.sleep(25); } catch (InterruptedException ignored) {}
        }
        System.out.print("] 100%\n");
    }

    private static void promptToContinue() {
        System.out.print("\nPress ENTER to return to main menu...");
        scanner.nextLine();
    }

    private static void seedSampleData() {
        socialGraph.addUser("Alice");
        socialGraph.addUser("Bob");
        socialGraph.addUser("Charlie");
        socialGraph.addUser("David");
        socialGraph.addUser("Eve");
        socialGraph.addUser("Frank");

        socialGraph.addConnection("Alice", "Bob");
        socialGraph.addConnection("Alice", "Charlie");
        socialGraph.addConnection("Bob", "David");
        socialGraph.addConnection("Charlie", "David");
        socialGraph.addConnection("David", "Eve");
    }
}