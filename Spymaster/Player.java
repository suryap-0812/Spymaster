import java.util.Scanner;

public class Player{
    private String name;
    private double money;
    private ResourceManager resourceManager;

    public Player(String name, double money) {
        this.name = name;
        this.money = money;
        this.resourceManager = new ResourceManager();
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
    }

    public void addMoney(double amount) { // optional method if needed
        money += amount;
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