package program;

import java.awt.*;
import java.lang.classfile.ClassFile.Option;
import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import java.util.*;
import java.util.Vector;
import java.awt.event.*;

import entity.*;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.html.parser.Entity;
import javax.swing.text.Document;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.ListSelectionEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author braydenphanna
 */

public class Main extends javax.swing.JFrame {
    private ArrayList<entity.Item> menu = new ArrayList<entity.Item>();
    private static ItemDAO itemDAO = new ItemDAO();
    private static OrderDAO orderDAO = new OrderDAO();
    private int orderIndex = 0;

    private JList<String> menuItemNames;
    private JScrollPane menuScrollPane;
    private JTextField searchField;
    private DefaultListModel<String> listModel;

    private JComboBox<String> categoryComboBox;

    public Main(){

        menu.add(new Item(0,"Glazed Donut", 1.49, "Icing,Chocolate,Vanilla\nFilling,Jelly,Cream"));
        menu.add(new Item(1,"Donut w/ Sprinkles", 1.79, "Icing,Chocolate,Vanilla\nFilling,Jelly,Cream"));
        menu.add(new Item(2,"Breakfast Sandwich", 4.50, "Icing,Chocolate,Vanilla\nFilling,Jelly,Cream"));
        menu.add(new Item(3,"Latte", 3.00, "Icing,Chocolate,Vanilla\nFilling,Jelly,Cream"));
        menu.add(new Item(4,"House Coffee", 2.00,"Sugar,A little,A lot\nCream,A little,A lot"));
        initComponents();
    }
    private void initComponents(){
        setTitle("Oak Donuts");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(1000, 600));
        pack(); 
        setLocationRelativeTo(null);
        setResizable(false);

        setLayout(new BorderLayout());

        // TITLE PANEL
        JPanel titlePanel = new JPanel();
        titlePanel.setBorder(BorderFactory.createLineBorder(Color.RED));
        add(titlePanel, BorderLayout.NORTH);

        JLabel title = new JLabel("Oak Donuts");
        title.setFont(new Font("Verdana", Font.BOLD, 20));
        title.setForeground(Color.BLACK);
        titlePanel.add(title);

        // WEST PANEL
        JPanel westPanel = new JPanel();
        westPanel.setLayout(new BoxLayout(westPanel, BoxLayout.Y_AXIS));
        westPanel.setBorder(BorderFactory.createLineBorder(Color.BLUE));
        westPanel.setPreferredSize(new Dimension(150,600));
        add(westPanel, BorderLayout.WEST);

        JLabel fitlersLabel= new JLabel("Filters");
        fitlersLabel.setFont(new Font("Verdana", Font.BOLD, 16));
        fitlersLabel.setForeground(Color.BLACK);
        westPanel.add(fitlersLabel);

        JLabel categoryLabel = new JLabel("Category:");
        categoryLabel.setFont(new Font("Verdana", Font.BOLD, 12));
        categoryLabel.setForeground(Color.BLACK);
        westPanel.add(categoryLabel);

        categoryComboBox = new JComboBox<>(new String[]{"All","Food","Drinks"});
        categoryComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.print("TEST");
                updateMenuItems();
            }
        });
        westPanel.add(categoryComboBox);

        JLabel searchLabel = new JLabel("Search:");
        searchLabel.setFont(new Font("Verdana", Font.BOLD, 12));
        searchLabel.setForeground(Color.BLACK);
        westPanel.add(searchLabel);

        searchField = new JTextField();

        Document doc = searchField.getDocument();

        listModel = new DefaultListModel<>();
        menuItemNames = new JList<String>(listModel);

        doc.addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                updateMenuItems();
            }
            public void removeUpdate(DocumentEvent e) {
                updateMenuItems();
            }
            public void changedUpdate(DocumentEvent e) {
            }
        });

        westPanel.add(searchField);
       
        System.out.print(menuItemNames.getSelectedIndex());
        westPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
        JLabel itemOptionsLabel= new JLabel("Item Options");
        itemOptionsLabel.setFont(new Font("Verdana", Font.BOLD, 16));
        itemOptionsLabel.setForeground(Color.BLACK);
        itemOptionsLabel.setVisible(false);
        westPanel.add(itemOptionsLabel);

        JLabel optionLabel = new JLabel();
        optionLabel.setFont(new Font("Verdana", Font.BOLD, 12));
        optionLabel.setForeground(Color.BLACK);
        westPanel.add(optionLabel);

        JComboBox<String> optionComboBox = new JComboBox<>();
        optionComboBox.setVisible(false);
        westPanel.add(optionComboBox);

        JLabel optionLabel2 = new JLabel();
        optionLabel2.setFont(new Font("Verdana", Font.BOLD, 12));
        optionLabel2.setForeground(Color.BLACK);
        westPanel.add(optionLabel2);

        JComboBox<String> optionComboBox2 = new JComboBox<>();
        optionComboBox2.setVisible(false);
        westPanel.add(optionComboBox2);

        
        menuItemNames.addListSelectionListener(new ListSelectionListener(){
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if ( !e.getValueIsAdjusting() && !menuItemNames.isSelectionEmpty()) {  
                    if(menuItemNames.getSelectedIndex()>=0){
                        generateItemOptions(menuItemNames, itemOptionsLabel, optionLabel, optionComboBox, optionLabel2, optionComboBox2);
                    }
                }  
            }
        });
        

        westPanel.add(Box.createVerticalStrut(600));

        // CENTER PANEL

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(BorderFactory.createLineBorder(Color.MAGENTA));
        add(centerPanel, BorderLayout.CENTER);

        JLabel menulabel= new JLabel("Menu");
        menulabel.setFont(new Font("Verdana", Font.BOLD, 16));
        menulabel.setForeground(Color.BLACK);
        centerPanel.add(menulabel);

        menuScrollPane = new JScrollPane(menuItemNames);
        updateMenuItems();
        centerPanel.add(menuScrollPane);

        JPanel lowerCenterPanel = new JPanel();
        lowerCenterPanel.setLayout(new BoxLayout(lowerCenterPanel, BoxLayout.X_AXIS));
        centerPanel.add(lowerCenterPanel);

        JSpinner quantitySpinner = new JSpinner(new SpinnerNumberModel(1,1,10,1));
        quantitySpinner.setMaximumSize(new Dimension(80,80));
        lowerCenterPanel.add(quantitySpinner);

        javax.swing.table.DefaultTableModel dtm = new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Item", "Options", "Qty", "Price", "Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false,false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        };

        JButton addButton = new JButton("Add to Order");
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedItem = menuItemNames.getSelectedValue();
                int selectedItemIndex = menuItemNames.getSelectedIndex();
                if (selectedItem != null) {
                    // Perform actions with the selected item
                    System.out.println("Selected item: " + selectedItem);

                    System.out.println("Selected item index: " + selectedItemIndex);
                    Item i = menu.get(selectedItemIndex);
                    String optionsString = i.getOptions()[0][0] + ": "+optionComboBox.getSelectedItem().toString()+"\n"+i.getOptions()[1][0] + ": "+optionComboBox2.getSelectedItem().toString();
                    addItem(i.getID(), i.getName(), i.getPrice(), optionsString);
                    for(int j = 0; j<(int)quantitySpinner.getValue(); j++){
                        if(getOrder(orderIndex).getID()==-1){
                            addOrder(orderIndex,i.getPrice(),LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),i.getName());
                            System.out.println("NEW ORDER " + getOrder(orderIndex).getID());
                        }else{
                            updateOrder(orderIndex,getOrder(orderIndex).getPrice()+i.getPrice(),LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),i.getName());
                            System.out.println("UPDATE ORDER " + getOrder(orderIndex).getID());
                        }
                    }
                    updateOrderTable(dtm, i, (int)quantitySpinner.getValue(), optionsString);
                    quantitySpinner.setValue(1);
                    menuItemNames.clearSelection();
                } else {
                    System.out.println("No item selected.");
                }
            }
        });
        lowerCenterPanel.add(addButton);


        // EAST PANEL
        JPanel eastPanel = new JPanel();
        eastPanel.setLayout(new BoxLayout(eastPanel, BoxLayout.Y_AXIS));
        eastPanel.setBorder(BorderFactory.createLineBorder(Color.GREEN));
        add(eastPanel, BorderLayout.EAST);

        JTable orderTable = new JTable();
        
        if (itemDAO.getAll()!=null){
            java.util.List<Item> currentOrder = itemDAO.getAll();
            for(Item item : currentOrder){  
                String[] options = item.getOptionsAsString().split("\n");
                
                dtm.addRow(new Object[]{item.getName(), options[0]+", " +options[1], quantitySpinner.getValue(),item.getPrice(),item.getPrice()*(int)quantitySpinner.getValue()});
            }
            orderTable.setModel(dtm);
            orderTable.getColumnModel().getColumn(0).setPreferredWidth(150);
            orderTable.getColumnModel().getColumn(1).setPreferredWidth(350);
        }
        
        JScrollPane orderScroll = new JScrollPane(orderTable);
        orderScroll.setPreferredSize(new Dimension(400, 400)); // adjust size as you like
        eastPanel.add(orderScroll);

        JPanel lowerEastPanel = new JPanel();
        lowerEastPanel.setLayout(new BorderLayout());

        JPanel lowerEastEastPanel = new JPanel();
        lowerEastEastPanel.setLayout(new BoxLayout(lowerEastEastPanel, BoxLayout.Y_AXIS));
        JLabel subtotalLabel = new JLabel("Subtotal:");
        lowerEastEastPanel.add(subtotalLabel);
        JLabel taxLabel = new JLabel("Tax (6%):");
        lowerEastEastPanel.add(taxLabel);
        JLabel totalPanel = new JLabel("Total:");
        lowerEastEastPanel.add(totalPanel);


        lowerEastPanel.add(lowerEastEastPanel, BorderLayout.WEST);

        JPanel lowerEastWestPanel = new JPanel();
        lowerEastWestPanel.setLayout(new BoxLayout(lowerEastWestPanel, BoxLayout.Y_AXIS));
        

       
        JLabel subtotalAmount = new JLabel("$0.00");
        JLabel taxAmount = new JLabel("$0.00");
        JLabel totalAmount = new JLabel("$0.00");
        if(getOrder(orderIndex).getID()!=-1){
            DecimalFormat df = new DecimalFormat("#.00");
            subtotalAmount.setText("$"+df.format(getOrder(orderIndex).getPrice()));
            taxAmount.setText("$"+df.format(getOrder(orderIndex).getPrice()*0.06));
            totalAmount.setText("$"+(df.format((getOrder(orderIndex).getPrice()+getOrder(orderIndex).getPrice()*0.06))));
        }
        lowerEastWestPanel.add(subtotalAmount);
        lowerEastWestPanel.add(taxAmount);
        lowerEastWestPanel.add(totalAmount);

        JButton clearButton = new JButton("Clear");
        lowerEastWestPanel.add(clearButton);
        JButton checkoutButton = new JButton("Checkout");
        lowerEastWestPanel.add(checkoutButton);
        

        lowerEastPanel.add(lowerEastWestPanel, BorderLayout.EAST);

        eastPanel.add(lowerEastPanel);
        setVisible(true);
    }

    private void generateItemOptions(JList<String> menuItemNames, JLabel itemsOptionsLabel, JLabel option1, JComboBox combo1, JLabel option2, JComboBox combo2){
        itemsOptionsLabel.setVisible(true);
        Item item = menu.get(menuItemNames.getSelectedIndex());
        option1.setText(item.getOptions()[0][0]);
        combo1.setModel(new DefaultComboBoxModel<>( new String[]{item.getOptions()[0][1],item.getOptions()[0][2]}));
        combo1.setVisible(true);
        try {
            option2.setText(item.getOptions()[1][0]);
            combo2.setModel(new DefaultComboBoxModel<>( new String[]{item.getOptions()[1][1],item.getOptions()[1][2]}));
            combo2.setVisible(true);
        } catch (Exception e){

        }
        revalidate();
    }

    private String[] filterMenuItems(String query){
        String category = (String)categoryComboBox.getSelectedItem();

        String[] menuStringArr = new String[menu.size()];
        int i = 0;
        int j = menu.size();
        if(category=="Food") {i = 0; j = 3;}
        if(category=="Drinks") {i = 3;  j = 5;}
        for(;i < j; i++){
            String menuItemAsString = menu.get(i).toString();
            if(menuItemAsString.toUpperCase().contains(query.toUpperCase())){
                menuStringArr[i] = menuItemAsString;
            }
        }
        return menuStringArr;
    }
    private void updateMenuItems(){
        String[] filteredItems = filterMenuItems(searchField.getText());

        // Clear the current list model (this removes existing items)
        listModel.clear();

        // Add filtered items to the list model
        for (String item : filteredItems) {
            listModel.addElement(item);  // Adds an item to the list model
        }

        // Revalidate and repaint the JScrollPane containing the JList
        menuScrollPane.revalidate();
        menuScrollPane.repaint();
    }

    private void updateOrderTable(DefaultTableModel dtm, Item item, int quanity, String optionsString){
        String[] options =optionsString.split("\n");
          dtm.addRow(new Object[]{item.getName(), options[0]+", "+ options[1], quanity,item.getPrice(),item.getPrice()*quanity});
    }
    /**
     * ITEM CRUD FUNCTIONS
    */
    private static void addItem(int id, String name, double price, String options) {
        Item item;
        item = new Item(id, name, price, options);
        itemDAO.insert(item);
    }
    
    private static void updateItem(int id, String name, double price, String options) {
        Item item;
        item = new Item(id, name, price, options);
        itemDAO.update(item);
    }
    
    private static void deleteItem(int id, String name, double price, String[][] options) {
        Item item;
        item = new Item(id, name, price, options);
        itemDAO.delete(item);
    }
    
    static Item getItem(int id) {
        Optional<Item> item = itemDAO.get(id);
        return item.orElseGet(() -> new Item(-1, "Non-exist", -1,"Non-exist"));
    }

    /**
     * ORDER CRUD FUNCTIONS
    */
    private static void addOrder(int ID, double price, String dateTime,  String itemName) {
        Order order;
        order = new Order(ID, price, dateTime, itemName);
        orderDAO.insert(order);
    }
    
    private static void updateOrder(int ID, double price, String dateTime, String itemName) {
        Order order;
        order = new Order(ID, price, dateTime, itemName);
        orderDAO.update(order);
    }
    
    private static void deleteOrder(int ID, double price, String dateTime, String itemName) {
        Order order;
        order = new Order(ID, price, dateTime, itemName);
        orderDAO.delete(order);
    }
    
    static Order getOrder(int id) {
    Optional<Order> order = orderDAO.get(id);
    if (order!=null && order.isPresent()) {
        return order.get();
    } else {
        System.out.println("Order with ID " + id + " not found.");
        return new Order(-1, -1, "Non-exist", "Non-exist");
    }
}

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            new Main();
        });
    }
}
