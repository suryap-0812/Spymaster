public class Gadget {
    private String name;
    private int quantity;
    private String playerName; // To associate with a player in the database

    public Gadget(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }
    
    // Constructor with player name
    public Gadget(String name, int quantity, String playerName) {
        this.name = name;
        this.quantity = quantity;
        this.playerName = playerName;
    }

    public String getName() {
        return name;
    }  

    public int getQuantity() {
        return quantity;
    }
    
    public String getPlayerName() {
        return playerName;
    }
    
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public void use() {
        if (quantity > 0) {
            quantity--;
            System.out.println(name + " used. Remaining quantity: " + quantity);
            
            // Update the gadget in the database if we have a player name
            if (playerName != null) {
                try {
                    DatabaseManager.getInstance()
                        .getDatabase()
                        .getCollection("gadgets")
                        .updateOne(
                            new org.bson.Document("playerName", playerName)
                                .append("name", name),
                            new org.bson.Document("$set", 
                                new org.bson.Document("quantity", quantity))
                        );
                } catch (Exception e) {
                    System.err.println("Failed to update gadget in database: " + e.getMessage());
                }
            }
        } else {
            System.out.println(name + " is unavailable.");
        }
    }
}