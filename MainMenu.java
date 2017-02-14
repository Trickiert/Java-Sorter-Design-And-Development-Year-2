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
  // data collections
  private Map<Integer, Customer> customers;
  private Map<Integer, Item> items;
  private List<Order> orders;
  private int newOrderID = 0;
  
  //GUI
  private OrderDlg orderDlg;
  private JButton btnReadData, btnTakeOrders, btnFillOrders,
                  btnListOrders, btnListStock, btnSaveData;

  //To launch the application
  public static void main(String[] args) {
    MainMenu app = new MainMenu();
    app.setVisible(true);
  }

  // Constructor
  public MainMenu() {
    // Database
    customers = new HashMap<Integer, Customer>();
    items = new HashMap<Integer, Item>();
    orders = new LinkedList<Order>();

    // GUI - create custom dialog instances
    orderDlg = new OrderDlg(this);

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
 
    //TODO: More button setup
   
    add(mainPnl, BorderLayout.CENTER);
  } //end constructor

  //Accessors for data structures
  public Map<Integer, Customer>  getCustomers() { return customers; }
  public Map<Integer, Item>      getItems()     { return items;  }
  public List<Order> getOrders()      { return orders; }

  /**
   * When a new Order is created this function is called to increment the 
   *   static variable newOrderID and assign its value as ID. 
   * This variable is inialised as bart of the data load tasks, see below. 
   */  
  public int useNewOrderID() {
    newOrderID++;
    return newOrderID;
  }

  /**
   * Actions in response to button clicks
   */
  public void actionPerformed(ActionEvent evt) {
    Object src = evt.getSource();
    //read customers, items, orders JUST ONCE to initialise the system data.
    if (src == btnReadData) { 
      readCustomerData(); //calls listCustomers()
      readItemData();     //calls listItems()
      //TODO: call a function to read orders from file

      // Initialisation of newOrderID autoincrement variable ...
      newOrderID = 0;
      for (Order o: orders) { //newOrderID should be max of all existing ones
        if (o.getID() >= newOrderID)
          newOrderID = o.getID();
      }
      btnReadData.setEnabled(false);      
    }
    else if (src == btnTakeOrders) { // dialog will do multiple orders
      orderDlg.setVisible(true);
    }
    else if (src == btnFillOrders) { // show dialog to iterate through orders
      //TODO ....... 
    }
    //TODO: three more cases: list stock; list orders; save data (orders, items)
  } // end actionPerformed()
  
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
        //fields delimited by '#' with optional leading and traililng spaces
      while (scnr.hasNextInt()) {
        id  = scnr.nextInt();
        snm = scnr.next();
        fnm = scnr.next();
        num = scnr.nextInt();
        pcd = scnr.next();
        customers.put(new Integer(id), new Customer(id, snm, fnm, num, pcd));
      }
      scnr.close();
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
      scnr.close();
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
  } //end readItemData()

  // List Items on console
  public void listItems() {
    System.out.println("Items:");
    for (Item i: items.values()) {
      System.out.println(i);
    }
    System.out.println();
  } //listItems()

  //TODO Functions to list orders, save orders and items, read saved orders

} //end class
