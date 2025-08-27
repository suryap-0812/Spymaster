import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GameEngine {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter your spy name:");
        String playerName = scanner.nextLine();
        Player player = new Player(playerName, 100.0);

        // Create a sample mission
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

        Mission mission1 = new Mission("Operation Silent Bank", missionScenarios);

        // Show gadgets
        player.getResourceManager().showGadgets();

        // Start mission
        mission1.start(player);

        System.out.printf("\nGame Over. Final Money: %.2f\n", player.getMoney());
    }
}
