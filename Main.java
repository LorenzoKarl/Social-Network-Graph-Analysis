import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        SocialGraph network = new SocialGraph();
        seedSampleData(network);

        boolean running = true;
        System.out.println("=================================================");
        System.out.println("  SOCIAL NETWORK GRAPH ANALYTICS ENGINE (v1.0)  ");
        System.out.println("=================================================");

        while (running) {
            printMenu();
            System.out.print("Select an option [1-7]: ");
            String input = scanner.nextLine().trim();

            switch (input) {
                case "1":
                    handleAddUser(network);
                    break;
                case "2":
                    handleAddFriendship(network);
                    break;
                case "3":
                    network.displayGraph();
                    break;
                case "4":
                    handleShortestPath(network);
                    break;
                case "5":
                    handleDegreeCentrality(network);
                    break;
                case "6":
                    handleCommunityDetection(network);
                    break;
                case "7":
                    running = false;
                    System.out.println("\nExiting Graph Engine. Goodbye!");
                    break;
                default:
                    System.out.println("\n[!] Invalid selection. Please enter a number from 1 to 7.");
            }
        }
        scanner.close();
    }

    private static void printMenu() {
        System.out.println("\n--- MAIN MENU ---");
        System.out.println("1. Add New User Node");
        System.out.println("2. Create Friendship Connection");
        System.out.println("3. Display Full Network Topology");
        System.out.println("4. Find Shortest Path (BFS Routing)");
        System.out.println("5. View Most Influential User (Degree Centrality)");
        System.out.println("6. Detect Sub-Communities (Connected Components)");
        System.out.println("7. Exit System");
        System.out.println("-----------------");
    }

    private static void handleAddUser(SocialGraph network) {
        System.out.print("Enter new user name: ");
        String name = scanner.nextLine();
        if (network.addUser(name)) {
            System.out.println("[✓] User '" + name.trim() + "' added successfully.");
        } else {
            System.out.println("[!] Failed to add user. User may already exist or input was empty.");
        }
    }

    private static void handleAddFriendship(SocialGraph network) {
        System.out.print("Enter first user name: ");
        String user1 = scanner.nextLine();
        System.out.print("Enter second user name: ");
        String user2 = scanner.nextLine();

        if (network.addFriendship(user1, user2)) {
            System.out.println("[✓] Friendship created between '" + user1.trim() + "' and '" + user2.trim() + "'.");
        } else {
            System.out.println("[!] Connection failed. Ensure both users exist and are not already connected.");
        }
    }

    private static void handleShortestPath(SocialGraph network) {
        System.out.print("Enter starting user: ");
        String start = scanner.nextLine();
        System.out.print("Enter target user: ");
        String target = scanner.nextLine();

        // TODO: Assigned to BFS Subgroup
        System.out.println("\n[Feature Pending] BFS pathfinding algorithm call goes here.");
    }

    private static void handleDegreeCentrality(SocialGraph network) {
        // TODO: Assigned to Centrality Subgroup
        System.out.println("\n[Feature Pending] Degree Centrality calculation call goes here.");
    }

    private static void handleCommunityDetection(SocialGraph network) {
        // TODO: Assigned to Community Detection Subgroup
        System.out.println("\n[Feature Pending] Connected components clustering algorithm call goes here.");
    }

    private static void seedSampleData(SocialGraph network) {
        String[] sampleUsers = {"Alice", "Bob", "Charlie", "David", "Eve", "Frank"};
        for (String user : sampleUsers) {
            network.addUser(user);
        }

        String[][] connections = {
            {"Alice", "Bob"},
            {"Alice", "Charlie"},
            {"Bob", "David"},
            {"Charlie", "David"},
            {"David", "Eve"}
        };

        for (String[] edge : connections) {
            network.addFriendship(edge[0], edge[1]);
        }
    }
}
