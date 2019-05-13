package com.test.vms;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import com.test.entity.Type;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ExportVM extends BaseRowModel {
    @ExcelProperty(value = "供应商名称" ,index = 0)
    private String companyName;
    @ExcelProperty(value = "商品分类" ,index = 0)
    private String goodsType;
    @ExcelProperty(value = "商品名称" ,index = 0)
    private String goodsName;
    @ExcelProperty(value = "数量" ,index = 0)
    private Integer number;
    @ExcelProperty(value = "日期" ,index = 0)
    private LocalDateTime date;
    @ExcelProperty(value = "单价" ,index = 0)
    private BigDecimal price;
    @ExcelProperty(value = "出入库分类" ,index = 0)
    private Type type;
    @ExcelProperty(value = "操作员" ,index = 0)
    private String staffName;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(String goodsType) {
        this.goodsType = goodsType;
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

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }




}
