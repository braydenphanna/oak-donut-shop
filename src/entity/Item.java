package entity;

import java.util.ArrayList;
import java.text.DecimalFormat;

/**
 * @author braydenphanna
 */
public class Item 
{
    private int ID;
    private String name;
    private double price;
    private ArrayList<String> options;
    
    public Item(int ID, String name, double price, ArrayList<String> options){
        this.ID = ID;
        this.name = name;
        this.price = price;
        this.options = options;
    }

    public int getID() { return ID; }

    public String getName() { return name; }

    public double getPrice() { return price; }

    public ArrayList<String> getOptions() { return options; }

    @Override
    public String toString() {
        DecimalFormat df = new DecimalFormat("#.00");
        return name + " — $"+df.format(price);
    }
}
