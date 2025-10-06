import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import org.bson.Document;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;

public class ResourceManager {
    private List<Gadget> gadgets;
    private String playerName; // To associate gadgets with a specific player

    public ResourceManager() {
        gadgets = new ArrayList<>();
        // Default gadgets are loaded from DB or initialized with default values
    }
    
    // Load gadgets from database for a specific player
    public void loadGadgetsFromDatabase(String playerName) {
        this.playerName = playerName;
        gadgets.clear();
        
        try {
            MongoCollection<Document> gadgetsCollection = DatabaseManager.getInstance()
                    .getDatabase().getCollection("gadgets");
            
            // Find gadgets for this player
            MongoCursor<Document> cursor = gadgetsCollection.find(
                    Filters.eq("playerName", playerName)).iterator();
            
            boolean hasGadgets = false;
            
            while (cursor.hasNext()) {
                hasGadgets = true;
                Document gadgetDoc = cursor.next();
                String name = gadgetDoc.getString("name");
                int quantity = gadgetDoc.getInteger("quantity");
                gadgets.add(new Gadget(name, quantity));
            }
            
            // If player has no gadgets yet, create defaults
            if (!hasGadgets) {
                createDefaultGadgets();
            }
            
        } catch (Exception e) {
            System.err.println("Error loading gadgets from database: " + e.getMessage());
            createDefaultGadgets();
        }
    }
    
    // Create default gadgets for new players
    private void createDefaultGadgets() {
        gadgets.add(new Gadget("Hacking Device", 2));
        gadgets.add(new Gadget("Lockpick", 3));
        gadgets.add(new Gadget("Disguise Kit", 1));
        
        // Save these defaults to the database
        if (playerName != null) {
            saveGadgetsToDatabase(playerName);
        }
    }
    
    // Save gadgets to the database
    public void saveGadgetsToDatabase(String playerName) {
        try {
            MongoCollection<Document> gadgetsCollection = DatabaseManager.getInstance()
                    .getDatabase().getCollection("gadgets");
            
            // Delete existing gadgets for this player
            gadgetsCollection.deleteMany(Filters.eq("playerName", playerName));
            
            // Insert all current gadgets
            for (Gadget gadget : gadgets) {
                Document gadgetDoc = new Document()
                        .append("playerName", playerName)
                        .append("name", gadget.getName())
                        .append("quantity", gadget.getQuantity());
                        
                gadgetsCollection.insertOne(gadgetDoc);
            }
            
        } catch (Exception e) {
            System.err.println("Error saving gadgets to database: " + e.getMessage());
        }
    }

    public void showGadgets() {
        System.out.println("Available Gadgets:");
        for (Gadget g : gadgets) {
            System.out.println("- " + g.getName() + " x" + g.getQuantity());
        }
    }

    public Gadget getGadget(int index) {
        if (index >= 0 && index < gadgets.size()) {
            return gadgets.get(index);
        }
        return null;
    }
}