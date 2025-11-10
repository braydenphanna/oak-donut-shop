package program;

import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

import entity.*;
import javax.swing.*;
import javax.swing.text.html.parser.Entity;

/**
 * @author braydenphanna
 */

public class Main extends javax.swing.JFrame {
    public ArrayList<entity.Item> menu = new ArrayList<entity.Item>();

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


        westPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
        JLabel itemOptionsLabel= new JLabel("Item Options");
        itemOptionsLabel.setFont(new Font("Verdana", Font.BOLD, 16));
        itemOptionsLabel.setForeground(Color.BLACK);
        westPanel.add(itemOptionsLabel);

        ArrayList<String> testOptions = new ArrayList<String>();
        testOptions.add("Icing");
        testOptions.add("Filling");
        entity.Item testItem = new entity.Item(0, "donut", 1.59,testOptions);
        for (String option : testItem.getOptions()) {
            JLabel optionLabel = new JLabel(option+":");
            optionLabel.setFont(new Font("Verdana", Font.BOLD, 12));
            optionLabel.setForeground(Color.BLACK);
            westPanel.add(optionLabel);

            JComboBox<String> optionComboBox = new JComboBox<>(new String[]{"All","1","2"});
            westPanel.add(optionComboBox);
        }

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

        List test = new List(4);
        test.add("test");
        test.add("testtoo");

        List menuItemNames = new List(menu.size());
        for (Item item : menu) {
            menuItemNames.add(item.toString()+"\n");
        }
        JScrollPane menuScrollPane = new JScrollPane(menuItemNames);
        centerPanel.add(menuScrollPane);

        // EAST PANEL
        JPanel eastPanel = new JPanel();
        eastPanel.setBorder(BorderFactory.createLineBorder(Color.GREEN));
        add(eastPanel, BorderLayout.EAST);

        JButton addButton = new JButton("Add to Order");
        centerPanel.add(addButton);

        JTable orderTable = new JTable();
        orderTable.setModel(new javax.swing.table.DefaultTableModel(
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
        });
        orderTable.setCellSelectionEnabled(true);
        
        JScrollPane orderScroll = new JScrollPane(orderTable);
        orderScroll.setPreferredSize(new Dimension(400, 400)); // adjust size as you like
        eastPanel.add(orderScroll);

        setVisible(true);
    }
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            new Main();
        });
    }
}
