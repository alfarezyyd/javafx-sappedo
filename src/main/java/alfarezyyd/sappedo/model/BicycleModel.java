package alfarezyyd.sappedo.model;

public class BicycleModel {
  private Integer id;
  private String name;
  private Integer price;
  private Integer stock;
  private Integer modelId;
  private String imagePath;
  private String color;

  public BicycleModel(Integer id, String name, Integer price, Integer stock, Integer modelId, String imagePath, String color) {
    this.id = id;
    this.name = name;
    this.price = price;
    this.stock = stock;
    this.modelId = modelId;
    this.imagePath = imagePath;
    this.color = color;
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

  public String getImagePath() {
    return imagePath;
  }

  public String getColor() {
    return color;
  }

  @Override
  public String toString() {
    return "BicycleModel{" +
        "id=" + id +
        ", name='" + name + '\'' +
        ", price=" + price +
        ", stock=" + stock +
        ", modelId=" + modelId +
        ", imagePath='" + imagePath + '\'' +
        ", color='" + color + '\'' +
        '}';
  }
}
