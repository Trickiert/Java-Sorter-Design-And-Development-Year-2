/**
 * OrderDlg
 * Custom dialog class with methods to input details of an order,
 *  and to create an order record.
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*; //Date, collection classes

public class OrderDlg extends JDialog implements ActionListener {
  private MainMenu parent;

  //GUI
  private JTextField txtItemID;
  private JTextField txtCustomerID;
  private JTextField txtQty;
  private JButton btnSubmit, btnHide;

  // Constructor
  public OrderDlg(MainMenu p) {
    setTitle("Create Orders");
    parent = p; //data structures are here

    //Components -
    txtItemID = new JTextField(10); //input field, 10 columns wide
    txtCustomerID = new JTextField(10); //input field, 10 columns wide
    txtQty = new JTextField(6); 
    btnSubmit = new JButton("Submit");
    btnHide   = new JButton("Hide");

    //Layout -
    JPanel pnl = new JPanel(), cpnl = new JPanel();
    pnl.add(new JLabel("Item code:"));
    pnl.add(txtItemID);
    cpnl.add(pnl);
    pnl = new JPanel();
    pnl.add(new JLabel("Customer ID:"));
    pnl.add(txtCustomerID);
    cpnl.add(pnl);
    pnl = new JPanel();
    pnl.add(new JLabel("Quantity:"));
    pnl.add(txtQty);
    cpnl.add(pnl);
    add(cpnl, BorderLayout.CENTER);

    pnl = new JPanel();
    pnl.add(btnSubmit);
    pnl.add(btnHide);
    add(pnl, BorderLayout.SOUTH);

    setBounds(100, 100, 300, 200);

    //Action
    btnSubmit.addActionListener(this);
    btnHide.addActionListener(this);
  } //end constructor

  /**
   * Actions: on click of 'Submit', make an order record and add to database;
   *          on click of 'Hide', hide the dialogue window.
   */
  public void actionPerformed(ActionEvent evt) {
    Object src = evt.getSource();
    if (src == btnHide) {
      setVisible(false);
      txtItemID.setText("");
      txtCustomerID.setText("");
      txtQty.setText("");
    }
    else if (src == btnSubmit) {
      processOrder();
      txtItemID.setText("");
      txtCustomerID.setText("");
      txtQty.setText("");
    }
  } //end actionPerformed

  /**
   * Does the actual work of creating an order record and adding it.
   */
  public void processOrder() {
    try {
      Integer cstID = new Integer(txtCustomerID.getText()),
              itmID = new Integer(txtItemID.getText());
      Customer cst = parent.getCustomers().get(cstID);
      Item itm = parent.getItems().get(itmID);
      int qty = Integer.parseInt(txtQty.getText());
      if (cst == null) {
      JOptionPane.showMessageDialog(this, "Customer not found",
            "Error", JOptionPane.ERROR_MESSAGE);
        return;
      }
      if (itm == null) {
      JOptionPane.showMessageDialog(this, "Item not found",
            "Error", JOptionPane.ERROR_MESSAGE);
        return;
      }
      if (qty == 0) {
      JOptionPane.showMessageDialog(this, "order for 0 items!",
            "Error", JOptionPane.ERROR_MESSAGE);
        return;
      }

      //Having validated the data, create the order record and add to DB...
      parent.getOrders().add(
        new Order(parent.useNewOrderID(), itm, cst, qty));
      System.out.printf("Order registered: %d of [%s][%s]\n\n",
        qty, itm.getDescription(), cst.getName());
    } catch (NumberFormatException ex) {
      JOptionPane.showMessageDialog(this, ex.getMessage(),
            "Number format error", JOptionPane.ERROR_MESSAGE);
    }
  } //end processOrder()

} //end class

