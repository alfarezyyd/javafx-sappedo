package alfarezyyd.sappedo.model;

public class TransactionModel {
  private Integer id;
  private String name;
  private BicycleModel bicycleModel;
  private Integer quantity;
  private Integer totalPrice;
  private Integer payment;

  public TransactionModel(Integer id,String name, BicycleModel bicycleModel, Integer quantity, Integer totalPrice, Integer payment) {
    this.name = name;
    this.id = id;
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
}
