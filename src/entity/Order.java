/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;
/**
 *
 * @author Gokhan
 */
public class Order 
{
    private int ID;
    private int price;
    private String dateTime;
    private Item[] items;
    private int[] itemQuanities;
    
    public Order(int ID, int price, String dateTime, Item[] items, int[] itemQuanities)
    {
        this.ID = ID;
        this.price = price;
        this.dateTime = dateTime;
        this.items = items;
        this.itemQuanities = itemQuanities;
    }

    public int getID() {
        return ID;
    }

    public int getPrice() {
        return price;
    }

    public String getDateTime() {
        return dateTime;
    }

    public Item[] getItems() {
        return items;
    }
     public int[] getItemQuanities() {
        return itemQuanities;
    }
    
    @Override
    public String toString() {
        return "Order{" + "ID=" + ID + ", price=" + price + ", dateTime=" + dateTime + ", items=" + items +'}';
    }
}
