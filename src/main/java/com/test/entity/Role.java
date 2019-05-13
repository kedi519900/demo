package com.test.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Entity
public class Role implements Serializable {
    @Id
    @Column(name = "id")
    private UUID id;
    @Basic
    @Column(name = "role_name")
    private String roleName;


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }


    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", roleName='" + roleName + '\'' +
                '}';
    }
}
