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
import javax.swing.text.html.parser.Entity;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;

/**
 * @author braydenphanna
 */

public class Main extends javax.swing.JFrame {
    private ArrayList<entity.Item> menu = new ArrayList<entity.Item>();
    private static ItemDAO itemDAO = new ItemDAO();

    public Main(){
        menu.add(new Item(0,"Glazed Donut", 1.49, new ArrayList<>(Arrays.asList("Icing", "Filling"))));
        menu.add(new Item(1,"Donut w/ Sprinkles", 1.79, new ArrayList<>(Arrays.asList("Icing", "Filling"))));
        menu.add(new Item(2,"House Coffee", 2.00, new ArrayList<>(Arrays.asList("TEST"))));
        menu.add(new Item(3,"Latte", 3.00, new ArrayList<>(Arrays.asList("TEST"))));
        menu.add(new Item(4,"Breakfast Sandwich", 4.50, new ArrayList<>(Arrays.asList("Meat", "Extras"))));

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
        menuItemNames.addListSelectionListener(new ListSelectionListener(){
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if ( !e.getValueIsAdjusting() && !menuItemNames.isSelectionEmpty()) {  
                    System.out.print("test");
                    if(menuItemNames.getSelectedIndex()>=0){
                            generateItemOptions(menuItemNames,westPanel);
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
                    addItem(i.getID(), i.getName(), i.getPrice(), i.getOptions());
                    initComponents();
                } else {
                    System.out.println("No item selected.");
                }
            }
        });
        centerPanel.add(addButton);


        // EAST PANEL
        JPanel eastPanel = new JPanel();
        eastPanel.setBorder(BorderFactory.createLineBorder(Color.GREEN));
        add(eastPanel, BorderLayout.EAST);

        JTable orderTable = new JTable();
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
        if (itemDAO.getAll()!=null){
                java.util.List<Item> currentOrder = itemDAO.getAll();
            for(Item item : currentOrder){
                dtm.addRow(new Object[]{item.getName(), item.getOptionsAsString(), 1,item.getPrice(),item.getPrice()*1});
            }
            orderTable.setModel(dtm);
        }
        
        JScrollPane orderScroll = new JScrollPane(orderTable);
        orderScroll.setPreferredSize(new Dimension(400, 400)); // adjust size as you like
        eastPanel.add(orderScroll);

        setVisible(true);
    }

    private void generateItemOptions(JList menuItemNames, JPanel westPanel){
        System.out.print(menuItemNames.getSelectedIndex());
        westPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
        JLabel itemOptionsLabel= new JLabel("Item Options");
        itemOptionsLabel.setFont(new Font("Verdana", Font.BOLD, 16));
        itemOptionsLabel.setForeground(Color.BLACK);
        westPanel.add(itemOptionsLabel);

        for (String option : menu.get(menuItemNames.getSelectedIndex()).getOptions()) {
            JLabel optionLabel = new JLabel(option+":");
            optionLabel.setFont(new Font("Verdana", Font.BOLD, 12));
            optionLabel.setForeground(Color.BLACK);
            westPanel.add(optionLabel);

            JComboBox<String> optionComboBox = new JComboBox<>(new String[]{"All","1","2"});
            westPanel.add(optionComboBox);
        }

        revalidate();
    }
    /**
     * ITEM CRUD FUNCTIONS
    */
    private static void addItem(int id, String name, double price, ArrayList<String> options) {
        Item item;
        item = new Item(id, name, price, options);
        itemDAO.insert(item);
    }
    
    private static void updateItem(int id, String name, double price, ArrayList<String> options) {
        Item item;
        item = new Item(id, name, price, options);
        itemDAO.update(item);
    }
    
    private static void deleteItem(int id, String name, double price, ArrayList<String> options) {
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
