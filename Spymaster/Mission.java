import java.util.List;
import java.util.ArrayList;
import org.bson.Document;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.types.ObjectId;

public class Mission {
    private String name;
    private List<Scenario> scenarios;
    private String id; // MongoDB document ID

    public Mission(String name, List<Scenario> scenarios) {
        this.name = name;
        this.scenarios = scenarios;
        saveToDatabase();
    }
    
    // Constructor to load a mission from database
    public Mission(String id) {
        loadFromDatabase(id);
    }
    
    // Save mission to database
    private void saveToDatabase() {
        try {
            MongoCollection<Document> missionsCollection = DatabaseManager.getInstance()
                    .getDatabase().getCollection("missions");
            
            // Check if mission with this name already exists
            Document existingMission = missionsCollection.find(Filters.eq("name", name)).first();
            
            if (existingMission != null) {
                // Mission already exists, update it
                this.id = existingMission.getObjectId("_id").toString();
                
                // Update scenarios list (we'll serialize scenarios to documents)
                List<Document> scenarioDocs = new ArrayList<>();
                for (Scenario scenario : scenarios) {
                    scenarioDocs.add(scenario.toDocument());
                }
                
                missionsCollection.updateOne(
                    Filters.eq("_id", new ObjectId(id)),
                    new Document("$set", new Document("scenarios", scenarioDocs))
                );
            } else {
                // Create new mission document
                List<Document> scenarioDocs = new ArrayList<>();
                for (Scenario scenario : scenarios) {
                    scenarioDocs.add(scenario.toDocument());
                }
                
                Document missionDoc = new Document()
                    .append("name", name)
                    .append("scenarios", scenarioDocs);
                    
                missionsCollection.insertOne(missionDoc);
                this.id = missionDoc.getObjectId("_id").toString();
            }
            
        } catch (Exception e) {
            System.err.println("Error saving mission to database: " + e.getMessage());
        }
    }
    
    // Load mission from database
    private void loadFromDatabase(String id) {
        try {
            MongoCollection<Document> missionsCollection = DatabaseManager.getInstance()
                    .getDatabase().getCollection("missions");
                    
            Document missionDoc = missionsCollection.find(Filters.eq("_id", new ObjectId(id))).first();
            
            if (missionDoc != null) {
                this.id = id;
                this.name = missionDoc.getString("name");
                
                // Load scenarios
                this.scenarios = new ArrayList<>();
                List<Document> scenarioDocs = missionDoc.getList("scenarios", Document.class);
                
                for (Document scenarioDoc : scenarioDocs) {
                    Scenario scenario = Scenario.fromDocument(scenarioDoc);
                    scenarios.add(scenario);
                }
            } else {
                throw new RuntimeException("Mission not found in database: " + id);
            }
            
        } catch (Exception e) {
            System.err.println("Error loading mission from database: " + e.getMessage());
            // Create a default mission as fallback
            this.name = "Default Emergency Mission";
            this.scenarios = new ArrayList<>();
        }
    }
    
    // Get all available missions from database
    public static List<Mission> getAllMissions() {
        List<Mission> missions = new ArrayList<>();
        
        try {
            MongoCollection<Document> missionsCollection = DatabaseManager.getInstance()
                    .getDatabase().getCollection("missions");
                    
            for (Document doc : missionsCollection.find()) {
                String id = doc.getObjectId("_id").toString();
                missions.add(new Mission(id));
            }
            
        } catch (Exception e) {
            System.err.println("Error loading missions from database: " + e.getMessage());
        }
        
        return missions;
    }

    public String getName() {
        return name;
    }

    public void start(Player player) {
        System.out.println("\n--- Mission: " + name + " ---");
        
        // Record mission start in database
        try {
            MongoCollection<Document> missionProgressCollection = DatabaseManager.getInstance()
                    .getDatabase().getCollection("missionProgress");
                    
            Document progressDoc = new Document()
                    .append("missionId", id)
                    .append("playerName", player.getName())
                    .append("started", System.currentTimeMillis())
                    .append("status", "in_progress");
                    
            missionProgressCollection.insertOne(progressDoc);
        } catch (Exception e) {
            System.err.println("Error recording mission start: " + e.getMessage());
        }
        
        // Play through scenarios
        for (Scenario s : scenarios) {
            s.playScenario(player);
        }
        
        // Record mission completion
        try {
            MongoCollection<Document> missionProgressCollection = DatabaseManager.getInstance()
                    .getDatabase().getCollection("missionProgress");
                    
            Document update = new Document()
                    .append("status", "completed")
                    .append("completed", System.currentTimeMillis());
                    
            missionProgressCollection.updateOne(
                Filters.and(
                    Filters.eq("missionId", id),
                    Filters.eq("playerName", player.getName()),
                    Filters.eq("status", "in_progress")
                ),
                new Document("$set", update)
            );
        } catch (Exception e) {
            System.err.println("Error recording mission completion: " + e.getMessage());
        }
        
        System.out.println("--- Mission Completed ---");
    }
}
