import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Graph graph = new Graph();
        Scanner scanner = new Scanner(System.in);

        int choice;

        do {
            System.out.println("\n=== SOCIAL NETWORK GRAPH ANALYSIS ===");
            System.out.println("1. Add User");
            System.out.println("2. Add Connection");
            System.out.println("3. Display Network");
            System.out.println("4. Find Shortest Path");
            System.out.println("5. Degree Centrality");
            System.out.println("6. Community Detection");
            System.out.println("7. Exit");
            System.out.print("Enter choice: ");

            choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    System.out.print("Enter username: ");
                    graph.addUser(scanner.nextLine());
                    break;
                case 2:
                    System.out.print("First user: ");
                    String user1 = scanner.nextLine();
                    System.out.print("Second user: ");
                    String user2 = scanner.nextLine();
                    graph.addConnection(user1, user2);
                    break;
                case 3:
                    graph.displayNetwork();
                    break;
                case 4:
                    System.out.print("Starting user: ");
                    String start = scanner.nextLine();
                    System.out.print("Destination user: ");
                    String end = scanner.nextLine();
                    graph.bfs(start, end);
                    break;
                case 5:
                    graph.degreeCentrality();
                    break;
                case 6:
                    graph.communityDetection();
                    break;
                case 7:
                    System.out.println("Program terminated.");
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        } while (choice != 7);

        scanner.close();
    }
}

