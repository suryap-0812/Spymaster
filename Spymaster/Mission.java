import java.util.List;

public class Mission {
    private String name;
    private List<Scenario> scenarios;

    public Mission(String name, List<Scenario> scenarios) {
        this.name = name;
        this.scenarios = scenarios;
    }

    public void start(Player player) {
        System.out.println("\n--- Mission: " + name + " ---");
        for (Scenario s : scenarios) {
            s.playScenario(player);
        }
        System.out.println("--- Mission Completed ---");
    }
}
