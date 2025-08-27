public class Gadget{
    private String name;
    private int quantity;

    public Gadget(String name, int quantity){
        this.name = name;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }  

    public int getQuantity() {
        return quantity;
    }

    public void use(){
        if(quantity > 0){
            quantity--;
            System.out.println(name + " used. Remaining quantity: " + quantity);
        } else {
            System.out.println(name + " is unavailable.");
        }
    }
}