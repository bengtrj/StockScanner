package com.stockscanner.pivotal.pivotalstockscanner;

class ProductData {

    private int quantityInStock;
    private String type;
    private String brand;
    private String flavor;

    public ProductData() {
    }

    public ProductData(String type, String brand, String flavor, int quantityInStock) {
        this.quantityInStock = quantityInStock;
        this.type = type;
        this.brand = brand;
        this.flavor = flavor;
    }

    public int getQuantityInStock() {
        return quantityInStock;
    }

    public void setQuantityInStock(int quantityInStock) {
        this.quantityInStock = quantityInStock;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getFlavor() {
        return flavor;
    }

    public void setFlavor(String flavor) {
        this.flavor = flavor;
    }
}
