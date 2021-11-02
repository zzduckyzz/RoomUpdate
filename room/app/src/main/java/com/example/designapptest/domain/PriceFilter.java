package com.example.designapptest.domain;

public class PriceFilter extends  myFilter {
    float minPrice;
    float maxPrice;

    public float getMinPrice() {
        return minPrice;
    }

    public PriceFilter(){

    }

    public PriceFilter(float minPrice, float maxPrice) {
        if(minPrice==10){
            this.name="Lớn hơn 10 triệu";
        }
        else {
            this.name = minPrice+" triệu - "+maxPrice+" triệu";
        }

        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
    }

    public void setMinPrice(float minPrice) {
        this.minPrice = minPrice;
    }

    public float getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(float maxPrice) {
        this.maxPrice = maxPrice;
    }

    @Override
    public void replace(myFilter filter) {

    }
}
