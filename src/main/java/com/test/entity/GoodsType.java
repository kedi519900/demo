package com.test.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "goods_type", schema = "public", catalog = "postgres")
public class GoodsType {
    private Integer id;
    private String type;

    @Id
    @Column(name = "id")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Basic
    @Column(name = "type")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GoodsType goodsType = (GoodsType) o;
        return Objects.equals(id, goodsType.id) &&
                Objects.equals(type, goodsType.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type);
    }
}
