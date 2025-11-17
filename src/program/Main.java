package program;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Optional;
import java.awt.event.*;
import entity.*;
import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.ColorUIResource;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.Document;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.ListSelectionEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author braydenphanna
 * 11/16/25
 * 
 * This is a project developed for CMPSC 221 
 * The code is based of Gokhan's Hot Dog stand gui and the appearance is based off of Professor Oakes's gui mockup.
 * It utilizes Java Swing to create a user friendly and intuitive user interface. 
 * This project also uses a derby database to perform all the CRUD operations.
 */

public class Main extends javax.swing.JFrame {
    // Menu list
    private ArrayList<Item> menu = new ArrayList<Item>();
    // DAOS
    private static ItemDAO itemDAO = new ItemDAO();
    private static OrderDAO orderDAO = new OrderDAO();

    // Indices
    private int orderIndex = 0;
    private int itemIndex = 0;

    // Necessary global components
    private JList<String> menuItemNames;
    private JScrollPane menuScrollPane;
    private JTextField searchField;
    private DefaultListModel<String> listModel;
    private JComboBox<String> categoryComboBox;
    private JLabel subtotalAmount;
    private JLabel taxAmount;
    private JLabel totalAmount;

    // Main constructor
    public Main(){
        // Populates the menu from db
        int i = 0;
        while(getItem(i,"HD_Menu").getID()!=-1){
            menu.add(getItem(i,"HD_Menu"));
            i++;
        }

        // Make gui
        initComponents();
    }
    // Does the bulk of setting up the gui elements
    private void initComponents(){
        // FRAME

        setTitle("Oak Donuts");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(1000, 600));
        pack(); 
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());

        /*
         * My frame is made up of four major panels.
         * The TITLE PANEL goes across the top header
         * The WEST, CENTER, and EAST PANELS make up the body in that order
         * Various smaller panels and components are in each one
         */

        /* 
         * TITLE PANEL
         */

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BorderLayout());
        titlePanel.setPreferredSize(new Dimension(1000,60));
        titlePanel.setBackground( new Color(75, 42, 40));

        add(titlePanel, BorderLayout.NORTH);

        JPanel bodyPanel = new JPanel();
        bodyPanel.setLayout(new BoxLayout(bodyPanel, BoxLayout.X_AXIS));
        bodyPanel.setBackground( new Color(245, 195, 102));
        bodyPanel.setBorder(BorderFactory.createLineBorder(new Color(245, 195, 102),10));

        // Create rainbow title text

        Color[] colors = {new Color(239, 83, 68), new Color(237, 134, 0), new Color(242, 239, 100), new Color(27, 162, 72), new Color(100, 71, 173), new Color(247, 116, 179)};

        String text = "Oak Donuts";
        StringBuilder htmlText = new StringBuilder("<html>");
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            Color letterColor = colors[i % colors.length]; 
            
            htmlText.append("<font color=\"rgb(")
                    .append(letterColor.getRed()).append(",")
                    .append(letterColor.getGreen()).append(",")
                    .append(letterColor.getBlue()).append(")\">")
                    .append(c)
                    .append("</font>");
        }
        htmlText.append("</html>");

        JLabel title = new JLabel(htmlText.toString());
        title.setFont(new Font("Verdana", Font.BOLD, 50));
        title.setHorizontalAlignment(JLabel.CENTER);
        titlePanel.add(title, BorderLayout.CENTER);

        /* 
         * WEST PANEL
         */

        JPanel westPanel = new JPanel();
        westPanel.setLayout(new BoxLayout(westPanel, BoxLayout.Y_AXIS));
        westPanel.setPreferredSize(new Dimension(150,600));
        bodyPanel.add(westPanel, BorderLayout.WEST);

        JLabel fitlersLabel= new JLabel("Filters");
        fitlersLabel.setFont(new Font("Verdana", Font.BOLD, 16));
        fitlersLabel.setForeground(Color.BLACK);
        fitlersLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        westPanel.add(fitlersLabel);

        JLabel categoryLabel = new JLabel("Category:");
        categoryLabel.setFont(new Font("Verdana", Font.BOLD, 12));
        categoryLabel.setForeground(Color.BLACK);
        categoryLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        westPanel.add(categoryLabel);

        // Category combobox listener
        categoryComboBox = new JComboBox<>(new String[]{"All","Food","Drinks"});
        categoryComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateMenuItems();
            }
        });

        categoryComboBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        westPanel.add(categoryComboBox);

        JLabel searchLabel = new JLabel("Search:");
        searchLabel.setFont(new Font("Verdana", Font.BOLD, 12));
        searchLabel.setForeground(Color.BLACK);
        searchLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        westPanel.add(searchLabel);

        searchField = new JTextField();
        searchField.setAlignmentX(Component.LEFT_ALIGNMENT);

        Document doc = searchField.getDocument();

        listModel = new DefaultListModel<>();
        menuItemNames = new JList<String>(listModel);

        // Search field input listener
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
        itemOptionsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        westPanel.add(itemOptionsLabel);

        JLabel optionLabel = new JLabel();
        optionLabel.setFont(new Font("Verdana", Font.BOLD, 12));
        optionLabel.setForeground(Color.BLACK);
        optionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        westPanel.add(optionLabel);

        JComboBox<String> optionComboBox = new JComboBox<>();
        optionComboBox.setVisible(false);
        optionComboBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        westPanel.add(optionComboBox);

        JLabel optionLabel2 = new JLabel();
        optionLabel2.setFont(new Font("Verdana", Font.BOLD, 12));
        optionLabel2.setForeground(Color.BLACK);
        optionLabel2.setAlignmentX(Component.LEFT_ALIGNMENT);
        westPanel.add(optionLabel2);

        JComboBox<String> optionComboBox2 = new JComboBox<>();
        optionComboBox2.setVisible(false);
        optionComboBox2.setAlignmentX(Component.LEFT_ALIGNMENT);
        westPanel.add(optionComboBox2);

        // Menu selection listener
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
        
        // Extra space to fill out box layout
        westPanel.add(Box.createVerticalStrut(500));

        /* 
         * CENTER PANEL
         */

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        centerPanel.setPreferredSize(new Dimension(400,600));
        bodyPanel.add(centerPanel, BorderLayout.CENTER);
        
        JLabel menulabel= new JLabel("Menu", SwingConstants.CENTER);
        menulabel.setFont(new Font("Verdana", Font.BOLD, 16));
        menulabel.setAlignmentX(Component.CENTER_ALIGNMENT);
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

        // table model for order table
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

        // Add to order button with logic
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

                    for(int j = 0; j<(int)quantitySpinner.getValue(); j++){
                        addItem(itemIndex++, i.getName(), i.getPrice(), optionsString);
                        // Check if there is a current order, if not make one
                        if(getOrder(orderIndex).getID()==-1){
                            addOrder(orderIndex,i.getPrice(),LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),i.getName());
                            System.out.println("NEW ORDER " + getOrder(orderIndex).getID());
                        }else{
                            updateOrder(orderIndex,getOrder(orderIndex).getPrice()+i.getPrice(),LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),i.getName());
                            System.out.println("UPDATE ORDER " + getOrder(orderIndex).getID()+" | ITEMS: "+ itemIndex);
                        }
                    }
                    updateOrderTable(dtm, i, (int)quantitySpinner.getValue(), optionsString);
                    updatePrices(subtotalAmount,taxAmount,totalAmount);
                    quantitySpinner.setValue(1);
                    menuItemNames.clearSelection();
                } else {
                    System.out.println("No item selected.");
                }
            }
        });
        lowerCenterPanel.add(addButton);

        /* 
         * EAST PANEL
         */

        JPanel eastPanel = new JPanel();
        eastPanel.setLayout(new BoxLayout(eastPanel, BoxLayout.Y_AXIS));
        bodyPanel.add(eastPanel, BorderLayout.EAST);

        JLabel orderLabel= new JLabel("Order", SwingConstants.CENTER);
        orderLabel.setFont(new Font("Verdana", Font.BOLD, 16));
        orderLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        eastPanel.add(orderLabel);


        JTable orderTable = new JTable();
        
        // get first blank order
        int o = 0;
        while(getOrder(o).getID()!=-1){
            o++;
        }
        orderIndex=o;

        // print current order, combine if necessary
        int i = 0;
        while(getOrder(orderIndex).getID()!=-1 && getItem(i).getID()!=-1){  
            Item item = getItem(i);
            String[] options = item.getOptionsAsString().split("\n");
            
            boolean found = false;
            for(int j =0; j<dtm.getRowCount(); j++){
                Item previouslyAddedItem = getItem(j);
                System.out.print(previouslyAddedItem.getName() +" == "+item.getName() +" & "+previouslyAddedItem.getOptionsAsString() +" = "+item.getOptionsAsString());
                if(previouslyAddedItem.getName().equals(item.getName()) && previouslyAddedItem.getOptionsAsString().equals(item.getOptionsAsString())){
                    dtm.setValueAt((int)dtm.getValueAt(j, 2)+1, j, 2);
                    dtm.setValueAt(Double.parseDouble((dtm.getValueAt(j, 4).toString().replace("$", "")))+item.getPrice(), j, 4);
                    found = true;
                    break;
                }
            }
            DecimalFormat df = new DecimalFormat("#.00");
            if(found==false) dtm.addRow(new Object[]{item.getName(), options[0]+", " +options[1], 1,"$"+df.format(item.getPrice()),"$"+df.format(item.getPrice())});
            i++;
        }
        itemIndex=i;
        orderTable.setModel(dtm);
        // Force name and options panels to be specific length
        orderTable.getColumnModel().getColumn(0).setPreferredWidth(150);
        orderTable.getColumnModel().getColumn(1).setPreferredWidth(350);
        
        JScrollPane orderScroll = new JScrollPane(orderTable);
        orderScroll.setPreferredSize(new Dimension(400, 500));
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
        totalPanel.setFont(new Font("Verdana", Font.BOLD, 16));
        lowerEastEastPanel.add(totalPanel);

        lowerEastPanel.add(lowerEastEastPanel, BorderLayout.WEST);

        JPanel lowerEastWestPanel = new JPanel();
        lowerEastWestPanel.setLayout(new BoxLayout(lowerEastWestPanel, BoxLayout.Y_AXIS));
        
        subtotalAmount = new JLabel("$0.00");
        taxAmount = new JLabel("$0.00");
        totalAmount = new JLabel("$0.00");
        totalAmount.setFont(new Font("Verdana", Font.BOLD, 16));
        if(getOrder(orderIndex).getID()!=-1) updatePrices(subtotalAmount,taxAmount,totalAmount);
        lowerEastWestPanel.add(subtotalAmount);
        lowerEastWestPanel.add(taxAmount);
        lowerEastWestPanel.add(totalAmount);

        JPanel lowerlowerEastPanel = new JPanel();
        lowerlowerEastPanel.setLayout(new BoxLayout(lowerlowerEastPanel, BoxLayout.X_AXIS));
        JButton clearButton = new JButton("Clear");

        // Clear button listener with logic
        clearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int i = itemIndex-1;
                Item item;
                while(getItem(i).getID()>=0){  
                    item = getItem(i);
                    deleteItem(item.getID(), item.getName(), item.getPrice(), item.getOptions());
                    i--;
                }
                Order order = getOrder(orderIndex);
                deleteOrder(order.getID(),order.getPrice(), order.getDateTime(), order.getItemName());

                itemIndex=0;
                dtm.setRowCount(0);

                subtotalAmount.setText("$0.00");
                taxAmount.setText("$0.00");
                totalAmount.setText("$0.00");
            }
        });
        lowerlowerEastPanel.add(clearButton);
        
        // Checkout button listener with logic
        JButton checkoutButton = new JButton("Checkout");
        checkoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(dtm.getRowCount()>0){
                    int i = itemIndex-1;

                    Item item;
                    while(getItem(i).getID()>=0){  
                        item = getItem(i);
                        deleteItem(item.getID(), item.getName(), item.getPrice(), item.getOptions());
                        i--;
                    }

                    dtm.setRowCount(0);

                    DecimalFormat df = new DecimalFormat("#.00");

                    subtotalAmount.setText("$0.00");
                    taxAmount.setText("$0.00");
                    totalAmount.setText("$0.00");
                    UIManager.put("OptionPane.background", new ColorUIResource(new Color(245, 195, 102)));     
                    UIManager.put("Panel.background", new ColorUIResource(new Color(245, 195, 102)));
                    ImageIcon donutIcon = new ImageIcon("donuticon.png"); 
                    JOptionPane.showMessageDialog(null, "Thank you for your purchase!\nYour total is $"+ df.format(getOrder(orderIndex).getPrice()*1.06),"Thank you!",JOptionPane.INFORMATION_MESSAGE,donutIcon);

                    itemIndex=0;
                    orderIndex++;
                    
                }
            }
        });
        lowerlowerEastPanel.add(checkoutButton);
        
        lowerEastPanel.add(lowerEastWestPanel, BorderLayout.EAST);

        eastPanel.add(lowerEastPanel);
        eastPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
        eastPanel.add(lowerlowerEastPanel);

        // Set panels to donut background color
        westPanel.setBackground( new Color(245, 195, 102));
        centerPanel.setBackground( new Color(245, 195, 102));
        eastPanel.setBackground( new Color(245, 195, 102));

        add(bodyPanel);
        setVisible(true);
    }

    // Sets up the item options drop down menus for the currently selected item
    private void generateItemOptions(JList<String> menuItemNames, JLabel itemsOptionsLabel, JLabel option1, JComboBox combo1, JLabel option2, JComboBox combo2){
        itemsOptionsLabel.setVisible(true);
        Item item = menu.get(menuItemNames.getSelectedIndex());
        option1.setText(item.getOptions()[0][0]+":");
        if(item.getOptions()[0].length==3) combo1.setModel(new DefaultComboBoxModel<>( new String[]{item.getOptions()[0][1],item.getOptions()[0][2]}));
        else if (item.getOptions()[0].length==4) combo1.setModel(new DefaultComboBoxModel<>( new String[]{item.getOptions()[0][1],item.getOptions()[0][2],item.getOptions()[0][3]}));
        combo1.setVisible(true);
        try {
            option2.setText(item.getOptions()[1][0]+":");
            if(item.getOptions()[1].length==3) combo2.setModel(new DefaultComboBoxModel<>( new String[]{item.getOptions()[1][1],item.getOptions()[1][2]}));
            else if (item.getOptions()[1].length==4) combo2.setModel(new DefaultComboBoxModel<>( new String[]{item.getOptions()[1][1],item.getOptions()[1][2],item.getOptions()[1][3]}));
            combo2.setVisible(true);
        } catch (Exception e){

        }
        revalidate();
    }

    // Filters the list of menu items based upon the category and search term
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

    // Writes the sorted list onto the Menu ScrollPane
    private void updateMenuItems(){
        String[] filteredItems = filterMenuItems(searchField.getText());

        listModel.clear();

        for (String item : filteredItems) {
            listModel.addElement(item); 
        }

        menuScrollPane.revalidate();
        menuScrollPane.repaint();
    }

    // Adds a new row to the order table based on the inputted arguments
    private void updateOrderTable(DefaultTableModel dtm, Item item, int quanity, String optionsString){
        String[] options =optionsString.split("\n");
        DecimalFormat df = new DecimalFormat("#.00");
        dtm.addRow(new Object[]{item.getName(), options[0]+", " +options[1], quanity,"$"+df.format(item.getPrice()),"$"+df.format(item.getPrice()*quanity)});
    }

    // Updates the prices for the current order
    private void updatePrices(JLabel subtotalAmount, JLabel taxAmount, JLabel totalAmount){
        DecimalFormat df = new DecimalFormat("#.00");
        subtotalAmount.setText("$"+df.format(getOrder(orderIndex).getPrice()));
        taxAmount.setText("$"+df.format(getOrder(orderIndex).getPrice()*0.06));
        totalAmount.setText("$"+(df.format((getOrder(orderIndex).getPrice()+getOrder(orderIndex).getPrice()*0.06))));
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
    static Item getItem(int id, String table) {
        Optional<Item> item = itemDAO.get(id, table);
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

    // Where it all begins
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            new Main();
        });
    }
}
