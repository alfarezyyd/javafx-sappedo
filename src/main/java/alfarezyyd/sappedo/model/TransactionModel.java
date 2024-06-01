package alfarezyyd.sappedo.model;

public class TransactionModel {
  private Integer id;
  private final String date;
  private final String name;
  private final BicycleModel bicycleModel;
  private final Integer quantity;
  private Integer totalPrice;
  private Integer payment;

  public TransactionModel(Integer id, String date, String name, BicycleModel bicycleModel, Integer quantity, Integer totalPrice, Integer payment) {
    this.id = id;
    this.date = date;
    this.name = name;
    this.bicycleModel = bicycleModel;
    this.quantity = quantity;
    this.totalPrice = totalPrice;
    this.payment = payment;
  }


  public Integer getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public BicycleModel getBicycleModel() {
    return bicycleModel;
  }

  public Integer getQuantity() {
    return quantity;
  }

  public Integer getTotalPrice() {
    return totalPrice;
  }

  public Integer getPayment() {
    return payment;
  }

  public String getDate() {
    return date;
  }
}
