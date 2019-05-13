package com.test.vms;

import java.util.List;

public class RoleVM {
    private String roleName;

    private List<String> permissionId;

    public List<String> getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(List<String> permissionId) {
        this.permissionId = permissionId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }


}
