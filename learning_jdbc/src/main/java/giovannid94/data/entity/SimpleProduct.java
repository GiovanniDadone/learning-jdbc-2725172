package giovannid94.data.entity;

import java.math.BigDecimal;
import java.util.UUID;

public class SimpleProduct {
  private UUID productId;
  private String name;
  private BigDecimal price;
  private UUID vendorId;
  private String vendorName;
  private String contact;
  private String phoneNumber;
  private String email;
  private String address;


  /*GETTERS */
  public UUID getProductId() {
    return productId;
  }
  public String getName() {
    return name;
  }
  public BigDecimal getPrice() {
    return price;
  }
  public UUID getVendorId() {
    return vendorId;
  }
  public String getVendorName() {
    return vendorName;
  }
  public String getContact() {
    return contact;
  }
  public String getPhoneNumber() {
    return phoneNumber;
  }
  public String getEmail() {
    return email;
  }
  public String getAddress() {
    return address;
  }


  /* SETTERS */

  public void setProductId(UUID productId) {
    this.productId = productId;
  }
  public void setName(String name) {
    this.name = name;
  }
  public void setPrice(BigDecimal price) {
    this.price = price;
  }
  public void setVendorId(UUID vendorId) {
    this.vendorId = vendorId;
  }
  public void setVendorName(String vendorName) {
    this.vendorName = vendorName;
  }
  public void setContact(String contact) {
    this.contact = contact;
  }
  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }
  public void setEmail(String email) {
    this.email = email;
  }
  public void setAddress(String address) {
    this.address = address;
  }
  
  @Override
  public String toString() {
    return "SimpleProduct [productId=" + productId + ", name=" + name + ", price=" + price + ", vendorId=" + vendorId
        + ", vendorName=" + vendorName + ", contact=" + contact + ", phoneNumber=" + phoneNumber + ", email=" + email
        + ", address=" + address + "]";
  }
  
  
}
