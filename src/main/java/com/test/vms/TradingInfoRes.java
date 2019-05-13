package com.test.vms;

import com.test.entity.Companys;
import com.test.entity.GoodsType;
import com.test.entity.Login;
import com.test.entity.Type;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class TradingInfoRes{

    private UUID id;
    private UUID goodsId;
    private String goodsName;
    private Integer number;
    private LocalDateTime date;
    private BigDecimal price;
    private Login staff;
    private GoodsType goodsType;
    private Companys companys;
    private Type type;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(UUID goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Login getStaff() {
        return staff;
    }

    public void setStaff(Login staff) {
        this.staff = staff;
    }

    public GoodsType getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(GoodsType goodsType) {
        this.goodsType = goodsType;
    }

    public Companys getCompanys() {
        return companys;
    }

    public void setCompanys(Companys companys) {
        this.companys = companys;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
