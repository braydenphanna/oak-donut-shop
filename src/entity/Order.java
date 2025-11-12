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
    private String itemName;
    
    public Order(int ID, int price, String dateTime, String itemName)
    {
        this.ID = ID;
        this.price = price;
        this.dateTime = dateTime;
        this.itemName = itemName;
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

    public String getItemName() {
        return itemName;
    }
    @Override
    public String toString() {
        return "Order{" + "ID=" + ID + ", price=" + price + ", dateTime=" + dateTime + ", itemName=" + itemName +'}';
    }
}
