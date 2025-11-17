package entity;
/**
 * @author Gokhan
 * modfied by braydenphanna
 */
public class Order 
{
    private int ID;
    private double price;
    private String dateTime;
    private String itemName;
    
    public Order(int ID, double price, String dateTime, String itemName)
    {
        this.ID = ID;
        this.price = price;
        this.dateTime = dateTime;
        this.itemName = itemName;
    }

    public int getID() {
        return ID;
    }

    public double getPrice() {
        return price;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getItemName() {
        return itemName;
    }

    @Override
    public String toString() {
        return "Order{" + "ID=" + ID + ", price=" + price + ", dateTime=" + dateTime + ", itemName=" + itemName+'}';
    }
}
