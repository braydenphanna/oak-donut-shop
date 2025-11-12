package entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.text.DecimalFormat;

/**
 * @author braydenphanna
 */
public class Item 
{
    private int ID;
    private String name;
    private double price;
    private String[][] options;
    
    public Item(int ID, String name, double price, String[][] options){
        this.ID = ID;
        this.name = name;
        this.price = price;
        this.options = options;
    }
    public Item(int ID, String name, double price, String options){
        this.ID = ID;
        this.name = name;
        this.price = price;
        String[] arr = options.split("\n");
        this.options  = new String[arr.length][];
        for(int r=0; r<arr.length; r++)
            this.options[r] = arr[r].split(",");
    }

    public int getID() { return ID; }

    public String getName() { return name; }

    public double getPrice() { return price; }

    public String[][] getOptions() { return options; }
    
    public String getOptionsAsString() { 
        String[] rowStrings = new String[options.length];
        for (int i = 0; i < options.length; i++) {
            rowStrings[i] = String.join(", ", options[i]);
        }

       return String.join("\n", rowStrings);
    }

    @Override
    public String toString() {
        DecimalFormat df = new DecimalFormat("#.00");
        return name + " — $"+df.format(price);
    }
}
