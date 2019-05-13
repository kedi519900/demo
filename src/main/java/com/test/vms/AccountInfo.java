package com.test.vms;

import com.test.entity.Companys;

import java.math.BigDecimal;
import java.util.UUID;

public class AccountInfo {

    /**
     * 账单应该查询，应指定查询日期区间，
     * 返回字段有：商品名称，生产厂家，销售数量，销售总价，进货总价，进货数量，进货单价（平均值，有误差）部分展示
     * 盈利需要计算，先算出指定日期区间的进货平均单价，乘以销售数量为销售数额的进货总价，
     * 销售总价减去进货总价为当前日期区间的大概盈利。（只供参考，有误差）
     */
    private String goodsId; //商品id
    private String goodsName;//商品名称
    private Companys companys;//供应商
    private Integer salesNum;//销售数量
    private BigDecimal salesTotalPrice;//销售总价
    private Integer replenishNum;//进货数量
    private BigDecimal replenishTotalPrice;//进货总价
    private BigDecimal replenishPrice;//进货单价
    private BigDecimal profits;//利润


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

    public BigDecimal getProfits() {
        return profits;
    }

    public void setProfits(BigDecimal profits) {
        this.profits = profits;
    }
}
