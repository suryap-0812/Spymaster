import org.bson.Document;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;

public class Scenario {
    private String description;
    private String[] choices;
    private double[] outcomes; // change in money
    private String id; // MongoDB document ID

    // Updated constructor to accept double[] outcomes
    public Scenario(String description, String[] choices, double[] outcomes) {
        this.description = description;
        this.choices = choices;
        this.outcomes = outcomes;
    }
    
    // Convert scenario to MongoDB document
    public Document toDocument() {
        Document doc = new Document();
        doc.append("description", description);
        doc.append("choices", Arrays.asList(choices));
        
        // Convert double[] to List<Double>
        List<Double> outcomesList = new ArrayList<>();
        for (double outcome : outcomes) {
            outcomesList.add(outcome);
        }
        doc.append("outcomes", outcomesList);
        
        return doc;
    }
    
    // Create scenario from MongoDB document
    public static Scenario fromDocument(Document doc) {
        String description = doc.getString("description");
        List<String> choicesList = doc.getList("choices", String.class);
        List<Double> outcomesList = doc.getList("outcomes", Double.class);
        
        // Convert lists to arrays
        String[] choices = choicesList.toArray(new String[0]);
        double[] outcomes = new double[outcomesList.size()];
        for (int i = 0; i < outcomesList.size(); i++) {
            outcomes[i] = outcomesList.get(i);
        }
        
        return new Scenario(description, choices, outcomes);
    }

    public void playScenario(Player player) {
        System.out.println("\nScenario: " + description);
        for (int i = 0; i < choices.length; i++) {
            System.out.println((i + 1) + ". " + choices[i]);
        }

        int choice = player.makeDecision(choices.length);
        player.deductMoney(outcomes[choice - 1]);
        System.out.printf("Money change: %.2f, Current Money: %.2f\n", -outcomes[choice - 1], player.getMoney());
        
        // Record player choice in database
        try {
            MongoCollection<Document> choicesCollection = DatabaseManager.getInstance()
                    .getDatabase().getCollection("playerChoices");
                    
            Document choiceDoc = new Document()
                    .append("playerName", player.getName())
                    .append("scenarioDescription", description)
                    .append("choiceMade", choices[choice - 1])
                    .append("moneyCost", outcomes[choice - 1])
                    .append("timestamp", System.currentTimeMillis());
                    
            choicesCollection.insertOne(choiceDoc);
        } catch (Exception e) {
            System.err.println("Error recording player choice: " + e.getMessage());
        }
    }
}
