public class Scenario {
    private String description;
    private String[] choices;
    private double[] outcomes; // change in money

    // Updated constructor to accept double[] outcomes
    public Scenario(String description, String[] choices, double[] outcomes) {
        this.description = description;
        this.choices = choices;
        this.outcomes = outcomes;
    }

    public void playScenario(Player player) {
        System.out.println("\nScenario: " + description);
        for (int i = 0; i < choices.length; i++) {
            System.out.println((i + 1) + ". " + choices[i]);
        }

        int choice = player.makeDecision(choices.length);
        player.deductMoney(outcomes[choice - 1]);
        System.out.printf("Money change: %.2f, Current Money: %.2f\n", -outcomes[choice - 1], player.getMoney());
    }
}
