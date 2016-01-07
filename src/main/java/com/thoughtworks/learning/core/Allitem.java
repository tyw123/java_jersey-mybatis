package com.thoughtworks.learning.core;

public class Allitem {

   // private Integer id;//不是int,int是基本数据类型，这是个复杂数据类型，是一个类，
    // 具体看http://developer.51cto.com/art/200906/130459.htm
    private int id;
    private String barcode;
    private String name;
    private String unit;
    private float price;

    public String getBarcode() {
        return barcode;
    }
    public String getName() {return name;}
    public String getUnit() {return unit;}
    public float getPrice() {return price;}
    public int getId() {
      return this.id;
    }
}
