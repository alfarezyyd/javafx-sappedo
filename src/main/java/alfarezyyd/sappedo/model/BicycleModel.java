package alfarezyyd.sappedo.model;

public class BicycleModel {
  private Integer id;
  private String name;
  private Integer price;
  private Integer stock;
  private Integer modelId;
  public BicycleModel(Integer id, String name, Integer price, Integer stock, Integer modelId) {
    this.id = id;
    this.name = name;
    this.price = price;
    this.stock = stock;
    this.modelId = modelId;
  }

  public Integer getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public Integer getPrice() {
    return price;
  }

  public Integer getStock() {
    return stock;
  }

  public Integer getModelId() {
    return modelId;
  }
}
