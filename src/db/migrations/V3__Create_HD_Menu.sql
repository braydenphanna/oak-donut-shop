CREATE TABLE HD_Menu (
    Item_ID int NOT NULL PRIMARY KEY,
    Item_Name VARCHAR(20) NOT NULL,
    Item_Price VARCHAR(20) NOT NULL,
    Item_Options VARCHAR(70) NOT NULL
);

INSERT INTO HD_Menu (Item_ID, Item_Name, Item_Price, Item_Options)
VALUES (0, 'Glazed Donut', '1.49', 'Type: Plain, Chocolate&&Fillings: Jelly, Cream');

INSERT INTO HD_Menu (Item_ID, Item_Name, Item_Price, Item_Options)
VALUES (1, 'Donut with Sprinkles', '1.79', 'Icing: Chocolate, Vanilla&&Fillings: Jelly, Cream');

INSERT INTO HD_Menu (Item_ID, Item_Name, Item_Price, Item_Options)
VALUES (2, 'Breakfast Sandwich', '4.50', 'Meat: None, Bacon, Sausage&&Toppings: None, Avocado, Hashbrown');

INSERT INTO HD_Menu (Item_ID, Item_Name, Item_Price, Item_Options)
VALUES (3, 'Latte', '3.00', 'Flavors: Vanilla, Caramel&&Milk: Regular, Almond');

INSERT INTO HD_Menu (Item_ID, Item_Name, Item_Price, Item_Options)
VALUES (4, 'House Coffee', '2.00', 'Sugar: A little, A lot&&Cream: A little, A lot');