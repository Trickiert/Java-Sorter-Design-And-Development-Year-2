/**
 * FillDlg
 * Custom dialog class with methods to fill orders.
 * 
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public class FillDlg extends JDialog implements ActionListener
{
    private MainMenu parent;

    //GUI
    private JButton btnNext, btnFill, btnHide;
    private JLabel oNoLbl, cLbl, iLbl, qLbl, tLbl;

    //Iterator
    Iterator <Order> itr;

    //Order Varibles
    private int oID;
    private Customer cust;
    private Item item;
    private Order order;
    private int qty;
    private long timestamp;

    /**
     * Constructor for the fill diolog and creation of Jframe.
     */
    public FillDlg(MainMenu p) {
        setTitle("Fill Orders");
        parent = p; //data structures are here
        itr = parent.getOrders().iterator();

        //Components -
        //Order Labels, used in ProcessOrders();
        oNoLbl = new JLabel("");
        cLbl = new JLabel("Customer Details: ");
        iLbl = new JLabel("Item: ");
        qLbl = new JLabel("Quantity of Items: ");
        tLbl = new JLabel("Time Of Order: ");

        //Buttons on the FillOrders GUI
        btnFill  = new JButton("Fill");
        btnNext  = new JButton("Skip");
        btnHide = new JButton("Hide");

        //Layout -
        JPanel pnl = new JPanel(), cpnl = new JPanel();
        pnl.setLayout(new BoxLayout(pnl, BoxLayout.Y_AXIS));

        //Label Layout
        pnl.add(oNoLbl);
        pnl.add(cLbl);
        pnl.add(iLbl);
        pnl.add(qLbl);
        pnl.add(tLbl);

        cpnl.add(pnl); //Add to panel

        add(cpnl, BorderLayout.CENTER);

        pnl = new JPanel();
        pnl.add(btnFill);
        pnl.add(btnNext);
        pnl.add(btnHide);
        add(pnl, BorderLayout.SOUTH);

        setBounds(100, 100, 300, 200);

        //Action Listeners.
        btnFill.addActionListener(this);
        btnNext.addActionListener(this);
        btnHide.addActionListener(this);

        //processNextOrder();
    } 

    /**
     * Actions: on click of 'Fill', make an order record and add to database;
     *          on click of 'Hide', hide the dialogue window.
     */
    public void actionPerformed(ActionEvent evt) {
        Object src = evt.getSource();
        int addToBackOrder = 0;
        itr = parent.getOrders().iterator();
        if (src == btnFill) {
            processNextOrder();
            itr.next();
            if ((item.getNumInStock() - qty) >= item.getReordLvl())
            {
                item.subNumInStock(qty);
                itr.remove();
                System.out.println("Order Number: " + oID  + " has been processed");
                processNextOrder();
            }
            else if (item.getNumInStock() != item.getReordLvl())
            {
                addToBackOrder = qty - (item.getNumInStock() - item.getReordLvl());
                item.subNumInStock(qty);
                order.setQty(addToBackOrder);
                System.out.println("Order number " + oID + " has been partially filled with: " + qty + " items. \nRemaining amount has been added to backorder");
                processNextOrder();
            }
            else {
                System.out.println("This item is currently on backorder, it can not be fillied at this point in time.");
            }
        }

        else if (src == btnNext) {
            if (itr.hasNext())
            {
                processNextOrder();
            }
        }

        else if (src == btnHide) {
            setVisible(false);
        }

    } 

    /**
     * Iterates through current orders and fills them.
     */

    public void processNextOrder() {

        if (itr.hasNext())
        {
            order = itr.next();

            //Temp values for storing and using the data taken from orders.
            oID = order.getID();
            cust = order.getCustomer();
            item = order.getItem();
            qty = order.getQty();
            timestamp = order.getTimeStamp();

            //Adding the order with associated values to the output screen. This will show the order as a label on the screen.
            oNoLbl.setText("Order : " + oID);
            cLbl.setText("Customer Infomation: " +cust.briefString());
            iLbl.setText("Item: " +item.briefString());
            qLbl.setText("Quantity: " + qty);
            tLbl.setText("Timestamp: " + new Date(timestamp));

        }
        else
        {
            JOptionPane.showMessageDialog(this,
                "There are no more open orders.",
                "Process Orders Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

