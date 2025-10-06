# SPYMASTER 

## Description
- A Text-based game that changes the storyline according to player's choices.
- Players act as secret agents managing missions, resources, and gadgets.
- Now with MongoDB Atlas integration for persistent game data!

## Features
- Multiple mission types: 
    - Hacking
    - Bribery
    - Sabotage
- Resource and gadget management.
- Decision-based story progression.
- **NEW**: MongoDB Atlas integration for player progress tracking
- **NEW**: Persistent game data across sessions
- **NEW**: Mission and player choice analytics

## Overview story:

### SpyMaster: Operation Control:

#### Agent story:
You are a secret agent working for an elite global intelligence agency called "ShadowOps". The world is on the brink of a destructive cyberwar. Criminal agencies and rogue nations are attempting to overtake financial systems, steal classified technologies, and sabotage critical infrastructures. ***Maintain your secret identity***

**YOUR MISSION IS TO STOP CRIMINALS AND ROGUE NATIONS!**
    
#### Player Role:
You are an Agent, Codename: [Player Name], a versatile spy skilled in hacking, infiltration, bribery, and sabotage. 
Your *choices*, *strategy*, and *resource management* determine whether missions ***succeed*** or ***fail***.

## MongoDB Setup
1. Create a free MongoDB Atlas account at https://www.mongodb.com/cloud/atlas
2. Create a new cluster
3. Set up database access user and password
4. Configure network access (IP whitelist)
5. Get your connection string
6. Update the `CONNECTION_STRING` constant in the `DatabaseManager.java` file with your MongoDB Atlas connection string
   ```
   private static final String CONNECTION_STRING = "mongodb+srv://<username>:<password>@<cluster-url>/spymaster?retryWrites=true&w=majority";
   ```
7. Replace `<username>`, `<password>`, and `<cluster-url>` with your actual MongoDB Atlas credentials

## Database Collections
- `players`: Stores player information and money
- `gadgets`: Tracks player gadget inventory
- `missions`: Stores mission data and scenarios
- `missionProgress`: Tracks player mission progress
- `playerChoices`: Records player decisions for analytics


# MongoDB Atlas Network Setup

To ensure your application can connect to MongoDB Atlas, please follow these steps:

## 1. Add Your Current IP Address to the MongoDB Atlas Whitelist

1. Log in to your MongoDB Atlas account at https://cloud.mongodb.com/
2. Select your project
3. Click on the "Network Access" tab in the left sidebar
4. Click "Add IP Address"
5. Click "Add Current IP Address" to automatically add your current IP address
6. Alternatively, use "0.0.0.0/0" to allow connections from anywhere (not recommended for production)
7. Click "Confirm"

## 2. Create a Database User

1. Navigate to "Database Access" in the left sidebar
2. Click "Add New Database User"
3. Enter the username and password that matches what you've configured in your mongodb.config file
4. Select "Password" as the authentication method
5. Under "Database User Privileges", choose "Built-in Role" with "Atlas Admin" or "readWriteAnyDatabase"
6. Click "Add User"

## 3. SSL Configuration

If you continue to experience SSL/TLS connection issues:

1. Make sure you're using JDK 11 or newer
2. Try using a different JVM implementation (OpenJDK vs Oracle JDK)
3. Check your firewall settings to ensure MongoDB Atlas ports are not blocked

## Troubleshooting Connection Issues

If you see "Timed out after 30000 ms while waiting for a server" errors:
1. Verify your mongodb.config file has the correct USERNAME, PASSWORD, and CLUSTER_URL
2. Check that your IP address is whitelisted in MongoDB Atlas
3. Test your network connection to MongoDB Atlas using ping or traceroute
4. Ensure your system time is accurate and synchronized