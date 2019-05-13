package com.test.vms;

import com.test.entity.Companys;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class SalesRecordInfo {
    private String goodsId; //商品id
    private String goodsName;//商品名称
    private Companys companys;//供应商
    private Integer salesNum;//销售数量
    private BigDecimal salesTotalPrice;//销售总价
    private Integer replenishNum;//进货数量
    private BigDecimal replenishTotalPrice;//进货总价
    private BigDecimal replenishPrice;//进货单价
    private Integer returnNum;//退货数量
    private BigDecimal returnPrice;//退货总价
    private BigDecimal profits;//利润
    private LocalDateTime date;
    private String staffId;
    private String staffName;


    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public Companys getCompanys() {
        return companys;
    }

    public void setCompanys(Companys companys) {
        this.companys = companys;
    }

    public Integer getSalesNum() {
        return salesNum;
    }

    public void setSalesNum(Integer salesNum) {
        this.salesNum = salesNum;
    }

    public BigDecimal getSalesTotalPrice() {
        return salesTotalPrice;
    }

    public void setSalesTotalPrice(BigDecimal salesTotalPrice) {
        this.salesTotalPrice = salesTotalPrice;
    }

    public Integer getReplenishNum() {
        return replenishNum;
    }

    public void setReplenishNum(Integer replenishNum) {
        this.replenishNum = replenishNum;
    }

    public BigDecimal getReplenishTotalPrice() {
        return replenishTotalPrice;
    }

    public void setReplenishTotalPrice(BigDecimal replenishTotalPrice) {
        this.replenishTotalPrice = replenishTotalPrice;
    }

    public BigDecimal getReplenishPrice() {
        return replenishPrice;
    }

    public void setReplenishPrice(BigDecimal replenishPrice) {
        this.replenishPrice = replenishPrice;
    }

    public Integer getReturnNum() {
        return returnNum;
    }

    public void setReturnNum(Integer returnNum) {
        this.returnNum = returnNum;
    }

    public BigDecimal getReturnPrice() {
        return returnPrice;
    }

    public void setReturnPrice(BigDecimal returnPrice) {
        this.returnPrice = returnPrice;
    }

    public BigDecimal getProfits() {
        return profits;
    }

    public void setProfits(BigDecimal profits) {
        this.profits = profits;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }
}
