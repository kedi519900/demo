package com.test.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Entity
public class Permission implements Serializable {
    @Id
    @Column(name = "id")
    private UUID id;
    @Column(name = "text")
    private String text;
    @Column(name = "code")
    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "Permission{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}
