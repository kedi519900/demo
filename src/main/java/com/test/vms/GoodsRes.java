package com.test.vms;

import com.test.entity.Companys;
import com.test.entity.GoodsType;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

public class GoodsRes {
    private UUID id;
    private String name;
    private Companys company;
    private GoodsType goodType;
    private Integer number;
    private Integer minNumber;
    private String size;
    private BigDecimal price;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Companys getCompany() {
        return company;
    }

    public void setCompany(Companys company) {
        this.company = company;
    }

    public GoodsType getGoodType() {
        return goodType;
    }

    public void setGoodType(GoodsType goodType) {
        this.goodType = goodType;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getMinNumber() {
        return minNumber;
    }

    public void setMinNumber(Integer minNumber) {
        this.minNumber = minNumber;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
