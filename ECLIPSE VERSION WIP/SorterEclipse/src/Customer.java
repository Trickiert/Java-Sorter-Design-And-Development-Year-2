/**
 * Customer
 * Encapsulates a registered customer of the business
 */
public class Customer {
  private int cstID;
  private String surName;
  private String frstName;
  private int houseNumber;
  private String postCode;
  
  //Constructor
  public Customer(Integer cID, String sn, String fn, int hn, String pcd) {
    cstID = cID;
    surName = sn;
    frstName = fn;
    houseNumber = hn;
    postCode = pcd;
  }

  //Accessors
  public Integer getCstID()   {  return cstID;       }
  public String getSurName()  {  return surName;     }
  public String getFrstName() {  return frstName;    }
  public String getName()     {  return surName+ ", " + frstName; }

  public int getHouseNumber() {  return houseNumber; }
  public String getPostCode() {  return postCode;    }
  
  public String toString() {
    return String.format("%03d: %s %s (%d, %s)",
      cstID, frstName, surName, houseNumber, postCode);
  }

  public String briefString() {
    return String.format("%03d: %s %s", cstID, frstName, surName);
  }

}
