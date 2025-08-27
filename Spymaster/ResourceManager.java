import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;


public class ResourceManager {
    private List<Gadget> gadgets;

    public ResourceManager() {
        gadgets = new ArrayList<>();
        gadgets.add(new Gadget("Hacking Device", 2));
        gadgets.add(new Gadget("Lockpick", 3));
        gadgets.add(new Gadget("Disguise Kit", 1));
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