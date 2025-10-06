import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoException;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class DatabaseManager {
    private static DatabaseManager instance;
    private MongoClient mongoClient;
    private MongoDatabase database;

    private DatabaseManager() {
        try {
            // Load MongoDB connection string from config file
            String connectionString = loadConnectionString();
            
            // Create connection string
            ConnectionString connString = new ConnectionString(connectionString);
            
            // Configure server API settings
            ServerApi serverApi = ServerApi.builder()
                    .version(ServerApiVersion.V1)
                    .build();
            
            // Configure the MongoClient with additional SSL options
            MongoClientSettings settings = MongoClientSettings.builder()
                    .applyConnectionString(connString)
                    .serverApi(serverApi)
                    .applyToSslSettings(builder -> {
                        builder.enabled(true);
                        builder.invalidHostNameAllowed(true); // Allow connections to servers with invalid hostnames
                    })
                    .build();
            
            // Create a new client and connect to the server
            mongoClient = MongoClients.create(settings);
            database = mongoClient.getDatabase("spymaster");
            
            System.out.println("Connected successfully to MongoDB Atlas!");
            
        } catch (MongoException e) {
            System.err.println("MongoDB Connection Error: " + e.getMessage());
            throw e;
        } catch (IOException e) {
            System.err.println("Error reading MongoDB configuration: " + e.getMessage());
            throw new RuntimeException("Failed to load MongoDB configuration", e);
        }
    }

    // Load connection string from config file
    private String loadConnectionString() throws IOException {
        // First try to load from mongodb.config file
        String configPath = "mongodb.config";
        
        if (Files.exists(Paths.get(configPath))) {
            Properties props = new Properties();
            try (FileInputStream fis = new FileInputStream(configPath)) {
                props.load(fis);
                
                String username = props.getProperty("USERNAME", "").trim();
                String password = props.getProperty("PASSWORD", "").trim();
                String clusterUrl = props.getProperty("CLUSTER_URL", "").trim();
                String dbName = props.getProperty("DATABASE_NAME", "spymaster").trim();
                
                // Check if credentials are provided or still default values
                if (username.equals("your_username") || 
                    password.equals("your_password") || 
                    clusterUrl.equals("your_cluster_url.mongodb.net")) {
                    
                    System.err.println("Warning: Default MongoDB credentials detected in mongodb.config.");
                    System.err.println("Please update the mongodb.config file with your actual MongoDB Atlas credentials.");
                }
                
                // Add the appName parameter and SSL settings
                return String.format("mongodb+srv://%s:%s@%s/%s?retryWrites=true&w=majority&appName=Cluster0&ssl=true",
                        username, password, clusterUrl, dbName);
            }
        } else {
            // Fallback to environment variables or hardcoded default (for testing only)
            String username = System.getenv("MONGODB_USERNAME");
            String password = System.getenv("MONGODB_PASSWORD");
            String clusterUrl = System.getenv("MONGODB_CLUSTER_URL");
            String dbName = System.getenv("MONGODB_DATABASE_NAME");
            
            if (username != null && password != null && clusterUrl != null) {
                if (dbName == null) dbName = "spymaster";
                
                return String.format("mongodb+srv://%s:%s@%s/%s?retryWrites=true&w=majority",
                        username, password, clusterUrl, dbName);
            } else {
                throw new IOException("MongoDB configuration file not found and environment variables not set.");
            }
        }
    }

    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    public MongoDatabase getDatabase() {
        return database;
    }

    public void close() {
        if (mongoClient != null) {
            mongoClient.close();
            System.out.println("MongoDB connection closed.");
        }
    }
}