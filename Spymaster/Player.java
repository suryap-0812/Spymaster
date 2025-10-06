import java.util.Scanner;
import org.bson.Document;
import org.bson.types.ObjectId;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.UpdateResult;

public class Player {
    private String name;
    private double money;
    private ResourceManager resourceManager;
    private String id; // MongoDB document ID

    public Player(String name, double money) {
        this.name = name;
        this.money = money;
        this.resourceManager = new ResourceManager();
        loadOrCreatePlayer();
    }

    // Load player from database or create new one
    private void loadOrCreatePlayer() {
        try {
            MongoCollection<Document> playersCollection = DatabaseManager.getInstance()
                    .getDatabase().getCollection("players");

            // Try to find existing player by name
            Document playerDoc = playersCollection.find(Filters.eq("name", name)).first();

            if (playerDoc != null) {
                // Player exists, load data
                this.id = playerDoc.getObjectId("_id").toString();
                this.money = playerDoc.getDouble("money");
                System.out.println("Player '" + name + "' loaded from database.");
            } else {
                // Player doesn't exist, create new document
                Document newPlayer = new Document()
                        .append("name", name)
                        .append("money", money);
                
                playersCollection.insertOne(newPlayer);
                this.id = newPlayer.getObjectId("_id").toString();
                System.out.println("New player '" + name + "' created in database.");
            }

            // Load player's gadgets
            resourceManager.loadGadgetsFromDatabase(name);

        } catch (Exception e) {
            System.err.println("Error loading/creating player: " + e.getMessage());
            // Fallback to local mode if database isn't available
            System.out.println("Falling back to local mode (changes won't be saved)");
        }
    }

    // Save player data to database
    public void saveToDatabase() {
        try {
            MongoCollection<Document> playersCollection = DatabaseManager.getInstance()
                    .getDatabase().getCollection("players");

            UpdateResult result = playersCollection.updateOne(
                Filters.eq("name", name),
                new Document("$set", new Document("money", money))
            );
            
            // Save gadgets
            resourceManager.saveGadgetsToDatabase(name);
            
        } catch (Exception e) {
            System.err.println("Error saving player data: " + e.getMessage());
        }
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public double getMoney() {
        return money;
    }

    public ResourceManager getResourceManager() {
        return resourceManager;
    }

    public void deductMoney(double amount) { // method now accepts double
        money -= amount;
        saveToDatabase(); // Auto-save when money changes
    }

    public void addMoney(double amount) { // optional method if needed
        money += amount;
        saveToDatabase(); // Auto-save when money changes
    }

    public int makeDecision(int numChoices) {
        // Implement decision-making logic here
        Scanner sc = new Scanner(System.in);
        int choice = 0;
        while(choice < 1 || choice > numChoices) {
            System.out.print("Enter your choice (1-" + numChoices + "): ");
            if(sc.hasNextInt()) {
                choice = sc.nextInt();
                if(choice < 1 || choice > numChoices) {
                    System.out.println("Invalid choice. Please try again.");
                }
            } else {
                sc.next(); // clear invalid input
            }
        }
        return choice;
    }
}