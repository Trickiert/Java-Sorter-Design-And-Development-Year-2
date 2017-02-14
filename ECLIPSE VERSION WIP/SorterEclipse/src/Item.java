/**
 * Item
 * Encapsulates items stocked by the business
 */
public class Item {
  private Integer itemID;
  private String description;
  private double price;
  private int numInStock;
  private int reordLvl;
  
  //Constructor
  public Item(Integer iID, String dsc, double pr, int nStk, int lvl) {
    itemID = iID;
    description = dsc;
    price = pr;
    numInStock = nStk;
    reordLvl = lvl;
  }

  //Accessors  
  public Integer getItemID()          { return itemID; } 
  public String getDescription()      { return description; }
  public double getPrice()            { return price;  }
  public void setPrice(double pr)     { price = pr;  }

  public int  getNumInStock()         { return numInStock; }
  public void addNumInStock(int n)    { numInStock += n; }
  public void subNumInStock(int n)    { numInStock -= n; }

  public int getReordLvl()            { return reordLvl; }

  public String toString() {
    return String.format("%05d: %s: %5.2f: %d in stock; min %d",
      itemID, description, price, numInStock, reordLvl);
  }

  public String briefString() {
    return String.format("%05d: %s", itemID, description);
  }

}
