package program;

import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.Vector;
import java.awt.event.*;

import entity.*;
import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.html.parser.Entity;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;

/**
 * @author braydenphanna
 */

public class Main extends javax.swing.JFrame {
    private ArrayList<entity.Item> menu = new ArrayList<entity.Item>();
    private static ItemDAO itemDAO = new ItemDAO();
    private static OrderDAO orderDAO = new OrderDAO();

    public Main(){
        menu.add(new Item(0,"Glazed Donut", 1.49, "Icing,Chocolate,Vanilla\nFilling,Jelly,Cream"));
        menu.add(new Item(1,"Donut w/ Sprinkles", 1.79, "Icing,Chocolate,Vanilla\nFilling,Jelly,Cream"));
        menu.add(new Item(2,"House Coffee", 2.00,"Sugar,A little,A lot\nCream,A little,A lot"));
        menu.add(new Item(3,"Latte", 3.00, "Icing,Chocolate,Vanilla\nFilling,Jelly,Cream"));
        menu.add(new Item(4,"Breakfast Sandwich", 4.50, "Icing,Chocolate,Vanilla\nFilling,Jelly,Cream"));
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

        JComboBox<String> categoryComboBox = new JComboBox<>(new String[]{"All","1","2"});
        westPanel.add(categoryComboBox);

        JLabel searchLabel = new JLabel("Search:");
        searchLabel.setFont(new Font("Verdana", Font.BOLD, 12));
        searchLabel.setForeground(Color.BLACK);
        westPanel.add(searchLabel);

        JTextField searchField = new JTextField();
        westPanel.add(searchField);

        String[] menuStringArr = new String[menu.size()];
        for(int i = 0; i < menu.size(); i++){
            String menuItemAsString = menu.get(i).toString();
            if(menuItemAsString.contains(searchField.getText())){
                menuStringArr[i] = menuItemAsString;
            }
        }
        JList<String> menuItemNames = new JList<String>(menuStringArr);
       
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
                    System.out.print("test");
                    if(menuItemNames.getSelectedIndex()>=0){
                        generateItemOptions(menuItemNames, itemOptionsLabel, optionLabel, optionComboBox, optionLabel2, optionComboBox2);
                    }
                }  
                
            }
        });

        westPanel.add(Box.createVerticalStrut(305));

        // CENTER PANEL

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(BorderFactory.createLineBorder(Color.MAGENTA));
        add(centerPanel, BorderLayout.CENTER);

        JLabel menulabel= new JLabel("Menu");
        menulabel.setFont(new Font("Verdana", Font.BOLD, 16));
        menulabel.setForeground(Color.BLACK);
        centerPanel.add(menulabel);

        JScrollPane menuScrollPane = new JScrollPane(menuItemNames);
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
                    addItem(i.getID(), i.getName(), i.getPrice(), ""+optionComboBox.getSelectedItem().toString()+"\n"+optionComboBox2.getSelectedItem().toString());
                    updateOrderTable(dtm, i, (int)quantitySpinner.getValue());
                } else {
                    System.out.println("No item selected.");
                }
            }
        });
        lowerCenterPanel.add(addButton);


        // EAST PANEL
        JPanel eastPanel = new JPanel();
        eastPanel.setBorder(BorderFactory.createLineBorder(Color.GREEN));
        add(eastPanel, BorderLayout.EAST);

        JTable orderTable = new JTable();
        
        if (itemDAO.getAll()!=null){
                java.util.List<Item> currentOrder = itemDAO.getAll();
            for(Item item : currentOrder){
                dtm.addRow(new Object[]{item.getName(), item.getOptionsAsString(), quantitySpinner.getValue(),item.getPrice(),item.getPrice()*(int)quantitySpinner.getValue()});
            }
            orderTable.setModel(dtm);
        }
        
        JScrollPane orderScroll = new JScrollPane(orderTable);
        orderScroll.setPreferredSize(new Dimension(400, 400)); // adjust size as you like
        eastPanel.add(orderScroll);

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

    private void updateOrderTable(DefaultTableModel dtm, Item item, int quanity){
          dtm.addRow(new Object[]{item.getName(), item.getOptionsAsString(), quanity,item.getPrice(),item.getPrice()*quanity});
    }
    /**
     * ITEM CRUD FUNCTIONS
    */
    private static void addItem(int id, String name, double price, String options) {
        Item item;
        item = new Item(id, name, price, options);
        itemDAO.insert(item);
    }
    
    private static void updateItem(int id, String name, double price, String[][] options) {
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

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            new Main();
        });
    }
}
