import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import com.mongodb.MongoException;

public class GameEngine {
    public static void main(String[] args) {
        // Initialize database connection
        try {
            DatabaseManager dbManager = DatabaseManager.getInstance();
            System.out.println("Database connection successful.");
        } catch (Exception e) {
            System.err.println("Warning: Could not connect to MongoDB: " + e.getMessage());
            System.out.println("Game will run in local mode (progress won't be saved to database).");
        }
        
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== SPYMASTER ===");
        System.out.println("Enter your spy name:");
        String playerName = scanner.nextLine();
        
        // Create player (will load from DB if exists)
        Player player = new Player(playerName, 100.0);

        // Check if there are existing missions in the database
        List<Mission> missions = Mission.getAllMissions();
        Mission missionToPlay;
        
        if (missions.isEmpty()) {
            // No missions in database, create a default one
            System.out.println("Creating default mission...");
            
            List<Scenario> missionScenarios = new ArrayList<>();
            missionScenarios.add(new Scenario(
                    "You need to infiltrate a bank server. How will you proceed?",
                    new String[]{"Hack the system", "Bribe the employee"},
                    new double[]{20.0, 50.0} // money cost
            ));

            missionScenarios.add(new Scenario(
                    "A guard spots you. What do you do?",
                    new String[]{"Use gadget to distract", "Run away"},
                    new double[]{10.0, 0.0} // money cost
            ));

            missionToPlay = new Mission("Operation Silent Bank", missionScenarios);
        } else {
            // Let player choose from available missions
            System.out.println("\nAvailable Missions:");
            for (int i = 0; i < missions.size(); i++) {
                System.out.println((i + 1) + ". " + missions.get(i).getName());
            }
            
            System.out.print("Select a mission (1-" + missions.size() + "): ");
            int choice = scanner.nextInt();
            
            if (choice >= 1 && choice <= missions.size()) {
                missionToPlay = missions.get(choice - 1);
            } else {
                System.out.println("Invalid choice. Using first mission.");
                missionToPlay = missions.get(0);
            }
        }

        // Show gadgets
        player.getResourceManager().showGadgets();

        // Start mission
        missionToPlay.start(player);

        System.out.printf("\nMission complete. Final Money: %.2f\n", player.getMoney());
        
        // Close database connection
        try {
            DatabaseManager.getInstance().close();
        } catch (Exception e) {
            System.err.println("Warning: Error closing database connection: " + e.getMessage());
        }
        
        scanner.close();
    }
}
