import java.util.Date;

/**
 * Order: class to record order of some quantity of an item by a customer
 */
public class Order {
  private int orderID;
  private Item item;
  private Customer cust;
  private int qty;
  private long timeStamp;

  // Constructors
  public Order(int oID, Item i, Customer c, int q, long ts) {
    orderID = oID;
    item = i; cust = c; 
    qty = q;  timeStamp = ts;
  }

  //Another constructor: usually, we create an order timestamped 'now'
  public Order(int oID, Item i, Customer c, int q) {
    this (oID, i, c, q, System.currentTimeMillis());
  }

  //Accessors
  public int getID()            { return orderID; }
  public Item getItem()         { return item; }
  public Customer getCustomer() { return cust; }
  public int getQty()           { return qty;  }
  public long getTimeStamp()    { return timeStamp; }

  public void setQty(int q)     { qty = q; }
  public void updateTimeStamp() { timeStamp = System.currentTimeMillis(); }

  //NB new Date(timeStamp).toString() converts timeStamp to a date/time string
  public String toString() {
    return String.format("%06d: %d of [%s] ordered by %s at %s", orderID,
      qty, item.briefString(), cust.briefString(), new Date(timeStamp));
  }
  
   public String saveString() {
    return String.format("%d #%d #%d #%d #%s", orderID,
      qty, item.getItemID(), cust.getCstID(), timeStamp);
  }

} //end class

