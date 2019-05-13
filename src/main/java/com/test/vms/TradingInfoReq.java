package com.test.vms;

import com.test.entity.Type;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class TradingInfoReq {
    private UUID orderId;//订单号，退货时必传
    private UUID goodsId; //商品id
    private UUID companyId;//供应商id
    private Type type;//出入库类型
    private Integer number;//入库数量
    private UUID activity_id;//折扣
    private String customer_name;//客户id
    private String customer_phone;//客户id
    private int returnNum;//退货数量，如果是退货的话，此数量为入库数量
    private BigDecimal returnPrice;//退货单价
    private LocalDate date;//操作日期
    private BigDecimal price;//出售单价
    private BigDecimal totalPrice;//出售总价
    private BigDecimal salesPrice;//入库时填写的出售价格
    private UUID staffId;//员工id
    private BigDecimal salesTotalPrice;//实际出售总价

    public UUID getOrderId() {
        return orderId;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }

    public UUID getActivity_id() {
        return activity_id;
    }

    public void setActivity_id(UUID activity_id) {
        this.activity_id = activity_id;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getCustomer_phone() {
        return customer_phone;
    }

    public void setCustomer_phone(String customer_phone) {
        this.customer_phone = customer_phone;
    }

    public int getReturnNum() {
        return returnNum;
    }

    public void setReturnNum(int returnNum) {
        this.returnNum = returnNum;
    }

    public BigDecimal getReturnPrice() {
        return returnPrice;
    }

    public void setReturnPrice(BigDecimal returnPrice) {
        this.returnPrice = returnPrice;
    }

    public UUID getCompanyId() {
        return companyId;
    }

    public void setCompanyId(UUID companyId) {
        this.companyId = companyId;
    }

    public UUID getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(UUID goodsId) {
        this.goodsId = goodsId;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public BigDecimal getSalesPrice() {
        return salesPrice;
    }

    public void setSalesPrice(BigDecimal salesPrice) {
        this.salesPrice = salesPrice;
    }

    public UUID getStaffId() {
        return staffId;
    }

    public void setStaffId(UUID staffId) {
        this.staffId = staffId;
    }

    public BigDecimal getSalesTotalPrice() {
        return salesTotalPrice;
    }

    public void setSalesTotalPrice(BigDecimal salesTotalPrice) {
        this.salesTotalPrice = salesTotalPrice;
    }
}
