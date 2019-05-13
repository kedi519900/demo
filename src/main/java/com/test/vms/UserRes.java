package com.test.vms;

import com.test.entity.Role;

import java.util.List;
import java.util.UUID;

public class UserRes {
    private UUID id;
    private String userName;
    private List<Role> roleName;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<Role> getRoleName() {
        return roleName;
    }

    public void setRoleName(List<Role> roleName) {
        this.roleName = roleName;
    }
}
