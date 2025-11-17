package entity;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import core.*;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
/**
 *
 * @author Gokhan
 * modfied by braydenphanna
 */
public class ItemDAO implements DAO<Item>
{   
    public ItemDAO() {
        
    }
    List<Item> items;
    /**
     * Get a single item entity as a item object
     * @param id
     * @return 
     */
    @Override
    public Optional<Item> get(int id) {
        return get(id,"HD_Item");
    }
    public Optional<Item> get(int id, String table) {
        DB db = DB.getInstance();
        ResultSet rs = null;
        try {
            String sql = "SELECT * FROM "+table+" WHERE Item_ID = ?";
            PreparedStatement stmt = db.getPreparedStatement(sql);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            Item item = null;
            while (rs.next()) {
                item = new Item(rs.getInt("Item_ID"), rs.getString("Item_Name"), rs.getDouble("Item_Price"), rs.getString("Item_Options"));
            }
            return Optional.ofNullable(item);
        } catch (SQLException ex) {
            System.err.println(ex.toString());
            return null;
        }
    }
    
    /**
     * Get all item entities as a List
     * @return 
     */
    @Override
    public List<Item> getAll() {
        DB db = DB.getInstance();
        ResultSet rs = null;
        items = new ArrayList<>();
        try {
            String sql = "SELECT * FROM HD_Item";
            rs = db.executeQuery(sql);
            Item item = null;
            while (rs.next()) {
                item = new Item(rs.getInt("Item_ID"), rs.getString("Item_Name"), rs.getDouble("Item_Price"), rs.getString("Item_Options"));
                items.add(item);
            }
            return items;
        } catch (SQLException ex) {
            System.err.println(ex.toString());
            return null;
        }
    }
    
    /**
     * Insert a item object into item table
     * @param item 
     */
    @Override
    public void insert(Item item)
    {
        DB db = DB.getInstance();
        try {
            String sql = "INSERT INTO HD_Item(Item_ID, Item_Name, Item_Price, Item_Options) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = db.getPreparedStatement(sql);
            stmt.setInt(1, item.getID());
            stmt.setString(2, item.getName());
            stmt.setDouble(3, item.getPrice());
            stmt.setString(4, item.getOptionsAsString());
            int rowInserted = stmt.executeUpdate();
            if (rowInserted > 0) {
                System.out.println("A new item was inserted successfully!");
            }
        } catch (SQLException ex) {
            System.err.println(ex.toString());
        }
    }
    
    /**
     * Update a item entity in database if it exists using a item object
     * @param item
     */
    @Override
    public void update(Item item) {
        DB db = DB.getInstance();
        try {
            String sql = "UPDATE HD_Item SET Item_Name=?, Item_Price=?, Item_Options=? WHERE Item_ID=?";
            PreparedStatement stmt = db.getPreparedStatement(sql);
            stmt.setInt(1, item.getID());
            stmt.setString(2, item.getName());
            stmt.setDouble(3, item.getPrice());
            stmt.setString(4, item.getOptionsAsString());
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("An existing item was updated successfully!");
            }
        } catch (SQLException ex) {
            System.err.println(ex.toString());
        }
    }
    
    /**
     * Delete a item from item table if the entity exists
     * @param item 
     */
    @Override
    public void delete(Item item) {
        DB db = DB.getInstance();
        try {
            String sql = "DELETE FROM HD_Item WHERE Item_ID = ?";
            PreparedStatement stmt = db.getPreparedStatement(sql);
            stmt.setInt(1, item.getID());
            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("An item was deleted successfully!");
            }
        } catch (SQLException ex) {
            System.err.println(ex.toString());
        }
    }
    
    /**
     * Get all column names in a list array
     * @return 
     */
    @Override
    public List<String> getColumnNames() {
        DB db = DB.getInstance();
        ResultSet rs = null;
        List<String> headers = new ArrayList<>();
        try {
            String sql = "SELECT * FROM HD_Item WHERE Item_ID = -1";//We just need this sql query to get the column headers
            rs = db.executeQuery(sql);
            ResultSetMetaData rsmd = rs.getMetaData();
            //Get number of columns in the result set
            int numberCols = rsmd.getColumnCount();
            for (int i = 1; i <= numberCols; i++) {
                headers.add(rsmd.getColumnLabel(i));//Add column headers to the list
            }
            return headers;
        } catch (SQLException ex) {
            System.err.println(ex.toString());
            return null;
        } 
    }
}
