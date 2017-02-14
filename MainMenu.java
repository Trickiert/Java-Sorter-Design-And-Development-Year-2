/**
 *  MainMenu class for inventory/order management system
 */
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.*; //ArrayList; HashMap; LinkedList;

public class MainMenu extends JFrame implements ActionListener {
    //Data collections
    private Map<Integer, Customer> customers;
    private Map<Integer, Item> items;
    private List<Order> orders;
    private int newOrderID = 0;

    //GUI
    private OrderDlg orderDlg;
    private FillDlg fillDlg;
    private JButton btnReadData, btnTakeOrders, btnFillOrders,
    btnListOrders, btnListStock, btnSaveData;

    //For FileWriter
    private File file;
    private PrintWriter output;

    //To launch the application
    public static void main(String[] args) {
        MainMenu app = new MainMenu();
        app.setVisible(true);
    }

    // Constructor
    public MainMenu() {
        // Databases
        customers = new HashMap<Integer, Customer>();
        items = new HashMap<Integer, Item>();
        orders = new LinkedList<Order>();

        // GUI - create custom dialog instances
        orderDlg = new OrderDlg(this);
        fillDlg = new FillDlg(this);

        // GUI - set window properties
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(200, 100, 250, 300);

        //GUI - main menu buttons    
        JPanel mainPnl = new JPanel();
        mainPnl.setLayout(new GridLayout(6,1));

        btnReadData = new JButton("Read Data");
        btnReadData.addActionListener(this);
        mainPnl.add(btnReadData);

        btnTakeOrders = new JButton("Take Orders");
        btnTakeOrders.addActionListener(this);
        mainPnl.add(btnTakeOrders);

        btnFillOrders = new JButton("Fill Orders");
        btnFillOrders.addActionListener(this);
        mainPnl.add(btnFillOrders);

        //New Button Functionality.
        btnListOrders = new JButton("List Orders");
        btnListOrders.addActionListener(this);
        mainPnl.add(btnListOrders); 

        btnListStock = new JButton("List Stock");
        btnListStock.addActionListener(this);
        mainPnl.add(btnListStock);

        btnSaveData = new JButton("Save Data");
        btnSaveData.addActionListener(this);
        mainPnl.add(btnSaveData);

        add(mainPnl, BorderLayout.CENTER);
    } //end constructor

    //Accessors for data structures
    public Map<Integer, Customer>  getCustomers() 
    { 
        return customers;
    }

    public Map<Integer, Item>      getItems()     
    {
        return items;
    }

    public List<Order> getOrders()      
    {
        return orders; 
    }

    /**
     * When a new Order is created this function is called to increment the 
     *   static variable newOrderID and assign its value as ID. 
     * This variable is inialised as bart of the data load tasks, see below. 
     */  
    public int useNewOrderID() 
    {
        newOrderID++;
        return newOrderID;
    }

    /**
     * Actions in response to button clicks
     */
    public void actionPerformed(ActionEvent evt) 
    {
        Object src = evt.getSource();
        if (src == btnReadData) 
        { 
            readCustomerData(); //calls listCustomers()
            readItemData();     //calls listItems()
            //readOrderData();   // calls listOrders()

            /**
             * Initialisation of newOrderID autoincrement variable
             */
            newOrderID = 0;
            for (Order o: orders) {
                if (o.getID() >= newOrderID)
                    newOrderID = o.getID();
                newOrderID++;
            }
            btnReadData.setEnabled(false);      
        }
        else if (src == btnTakeOrders) 
        { // dialog will do multiple orders
            orderDlg.setVisible(true);
        }//Lists All Orders
        else if (src == btnListOrders)
        {
            listOrders();  
        }
        else if (src == btnFillOrders) 
        { 
            // show dialog to iterate through orders
            fillDlg.setVisible(true);
        }

        //List Orders
        else if (src == btnListOrders) 
        { 
            listOrders();
        } 
        //List Items
        else if (src == btnListStock) 
        { 
            listItems();
        } 
        //Save Data (Order + Items)
        else if (src == btnSaveData) 
        { 
            try  
            {
                WriteToOrders();
                WriteToItems();
            }
            catch (FileNotFoundException e) 
            {
                System.err.println(" Order File Not Found!"); 
            }

            try
            {
                WriteToItems();
            }
            catch (FileNotFoundException e) 
            {
                System.err.println(" Item File Not Found!"); 

            }
        }

    } 

    //Print Writers
    /**
     * Creates the order file and writes orders into them. 
     * 
     * 
     */  
    public void WriteToOrders() throws FileNotFoundException
    {
        file = new File("orders.txt");
        output = new PrintWriter(file);

        for (Order o: orders)
        {
            //SaveString() from order class
            output.println (o.saveString());
        }
        output.close();
    }

    /**
     * Creates the item file and writes items into them. 
     * Links for the stream are included.
     */  
    public void WriteToItems() throws FileNotFoundException
    {
        file = new File("items.txt");
        
        output = new PrintWriter(file);
        for (Item i: items.values())
        {
            output.println(i.getItemID() + "#" + i.getDescription() +"#" + i.getPrice() + "#" + i.getNumInStock() + "#"  + i.getReordLvl() + "#\n");
            
        }
        output.close();
    }

    /**
     * Read data from customers.txt using a Scanner; unpack and populate
     *   customers Map. List displyed on console.  
     */
    public void readCustomerData() {
        String fnm="", snm="", pcd="";
        int num=0, id=1;
        try {
            Scanner scnr = new Scanner(new File("customers.txt"));
            scnr.useDelimiter("\\s*#\\s*");
            while (scnr.hasNextInt()) {
                id  = scnr.nextInt();
                snm = scnr.next();
                fnm = scnr.next();
                num = scnr.nextInt();
                pcd = scnr.next();
                customers.put(new Integer(id), new Customer(id, snm, fnm, num, pcd));
            }
            //scnr.close();
            
            //Exception handing and validation
            if (id < 0)
            {
                JOptionPane.showMessageDialog(this, "ID Is Invalid!",
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            else if (snm == null)
            {
                JOptionPane.showMessageDialog(this, "Surname is Null!",
                    "Error!", JOptionPane.ERROR_MESSAGE);
                return;
            }
            else if (fnm == null)
            {
                JOptionPane.showMessageDialog(this, "Firstname is Null!",
                    "Error!", JOptionPane.ERROR_MESSAGE);
                return;

            }
            else if (num == 0)
            {
                JOptionPane.showMessageDialog(this, "Surname is Null!",
                        "Error!", JOptionPane.ERROR_MESSAGE);
                    return;
            }
            else if (pcd == null)
            {
                 JOptionPane.showMessageDialog(this, "Postcode is Null!",
                        "Error!", JOptionPane.ERROR_MESSAGE);
                    return;
            }
            else
            {
                scnr.close();
            }

        } catch (NoSuchElementException e) {
            System.out.printf("%d %s %s %d %s\n", id, snm, fnm, num, pcd);
            JOptionPane.showMessageDialog(this, e.getMessage(),
                "fetch of next token failed ", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "File Not Found",
                "Unable to open data file", JOptionPane.ERROR_MESSAGE);
        }
        listCustomers();
    } //end readCustomerData()

    // List Customers on console
    public void listCustomers() {
        System.out.println("Customers:");
        for (Customer b: customers.values()) {
            System.out.println(b);
        }
        System.out.println();
    } //listCustomers()

    /**
     * Read data from items.txt using a Scanner; unpack and populate
     *   items Map. List displyed on console.  
     */
    public void readItemData() {
        String descr="";
        double price=0.0;
        int id=1, stckLvl=0, reordLvl=0;

        try {
            Scanner scnr = new Scanner(new File("items.txt"));
            scnr.useDelimiter("\\s*#\\s*");
            while (scnr.hasNextInt()) {
                id  = scnr.nextInt();
                descr = scnr.next();
                price = scnr.nextDouble();
                stckLvl= scnr.nextInt();
                reordLvl = scnr.nextInt();
                items.put(new Integer(id), new Item(id, descr, price, stckLvl, reordLvl));

            }
            //scnr.close();
            
            if (id == 0)
            {
                 JOptionPane.showMessageDialog(this, "Item ID is Null!",
                        "Error!", JOptionPane.ERROR_MESSAGE);
                    return;
            }
            
            else if(descr == null)
            {
                 JOptionPane.showMessageDialog(this, "Description is Null!",
                        "Error!", JOptionPane.ERROR_MESSAGE);
                    return;
            }
            else if ( price == 0.0)
            {
                 JOptionPane.showMessageDialog(this, "Price Not Set",
                        "Error!", JOptionPane.ERROR_MESSAGE);
                    return;
            }
            else 
            {
                scnr.close();
            }
        } catch (NoSuchElementException e) {
            System.out.printf("%06d %s %5.2f, %d, %d\n", id, descr, price, stckLvl, reordLvl);
            JOptionPane.showMessageDialog(this, e.getMessage(),
                "fetch of next token failed ", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "File Not Found",
                "Unable to open data file", JOptionPane.ERROR_MESSAGE);
        }
        listItems();
    } 

    // List Items on console
    public void listItems() 
    {
        System.out.println("Items:");
        for (Item i: items.values()) {
            System.out.println(i);
        }
        System.out.println();
    } 

    /**
     * Reads an order via the order file and prints it in a below function.
     */

    public void readOrderData(){
        int oID = 0;
        int itemId;
        Item item;
        Customer cust;
        int custId;
        int qty;
        long timestamp;

        try {
            Scanner scnr = new Scanner(new File("orders.txt"));
            scnr.useDelimiter("\\s*#\\s*");

            while (scnr.hasNextInt()) {
                oID  = scnr.nextInt();
                itemId = scnr.nextInt();
                custId = scnr.nextInt();
                qty = scnr.nextInt();
                timestamp = scnr.nextLong();

                item = items.get(itemId);
                cust = customers.get(custId);

                orders.add(new Order(oID, item, cust, qty, timestamp));
               
            }
            scnr.close();
        } catch (NoSuchElementException e) {
            System.out.printf("");
            JOptionPane.showMessageDialog(this, e.getMessage(),
                "fetch of next token failed ", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "File Not Found",
                "Unable to open data file", JOptionPane.ERROR_MESSAGE);
        }
        listOrders();
    } 

    public void listOrders() {
        System.out.println("Orders:");
        for (Order o: orders) 
        {
            System.out.println(o);
        }
        System.out.println();
    } 
} //end class
