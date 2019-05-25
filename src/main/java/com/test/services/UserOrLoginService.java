package com.test.services;

import com.alibaba.fastjson.JSON;
import com.test.config.TokenUtils;
import com.test.config.Utils;
import com.test.controller.WebSocket;
import com.test.entity.*;
import com.test.repository.*;
import com.test.vms.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
public class UserOrLoginService {


    @Autowired
    private Utils utils;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RolePerRepository rolePerRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private WebSocket webSocket;

    @Autowired
    private LoginRepository loginRepository;

    public LoginResVM login(Login login) throws Exception {
        LoginResVM loginResVM = new LoginResVM();
        String userName = login.getUserName();
        String passWord = login.getPassWord();
        utils.requireNonNull(userName, "用户名不能为空！");
        utils.requireNonNull(passWord, "登录密码不能为空！");
        Login user = loginRepository.findByUserNameAndPassWord(userName, passWord);
        utils.requireNonNull(user, "登录失败，请检查账号密码！");
        String s = tokenUtils.createJwtToken(userName);
        loginResVM.setName(user.getName());
        loginResVM.setToken(s);
        loginResVM.setStaffId(user.getId());
        webSocket.onMessage("用户" + user.getName() + "登录系统", null);
        utils.put(userName, JSON.toJSONString(user));
        utils.putToken(userName, s);
        return loginResVM;
    }


    public Login createrUser(Login user, UserVm userVm) throws Exception {
        String userName1 = user.getUserName();
        Login login = new Login();
        String userName = userVm.getPhone();
        List<String> roleIds = userVm.getRoleId();
        utils.requireNonNull(userName, "用户名不能为空！");
        Login loginUser = loginRepository.findByUserName(userName);
        if (loginUser != null) {
            throw new Exception("用户名已存在，请重新输入！");
        }
        if (roleIds == null || roleIds.size() <= 0) {
            roleIds = new ArrayList<>();
            roleIds.add("a8ca8cae-7eb7-4ce9-8a3c-cb722c4c2454");
        }
        login.setId(UUID.randomUUID());
        login.setUserName(userName);
        login.setPassWord("123456");
        login.setAge(userVm.getAge());
        login.setGender(userVm.getGender());
        login.setName(userVm.getName());
        login.setPhone(userVm.getPhone());
        Login save = loginRepository.save(login);
        associatedUserRole(login.getId().toString(), roleIds);
        return save;
    }

    private String associatedUserRole(String id, List<String> roleIds) throws Exception {
        try {
            UUID uuid = UUID.fromString(id);
            userRoleRepository.deleteByUserId(uuid);
            for (String roleId : roleIds) {
                Role one = null;
                Optional<Role> byId = roleRepository.findById(UUID.fromString(roleId));
                if (byId.isPresent()) {
                    one = byId.get();
                    UserRole userRole = new UserRole();
                    userRole.setId(UUID.randomUUID());
                    userRole.setUserId(UUID.fromString(id));
                    userRole.setRoleId(one.getId());
                    userRoleRepository.save(userRole);
                }
            }
            return "success";
        } catch (Exception e) {
            throw new Exception("用户设置失败！！\n");
        }

    }

    public List<Permission> getPermissions() {
        List<Permission> all = permissionRepository.findAll();
        List<Permission> collect = all.stream().filter(permission -> !permission.getId().toString().equals("9740d598-6217-4660-bba2-babe3414d324"))
                .collect(Collectors.toList());
        return collect;
    }

    public Role createRole(RoleVM role) throws Exception {
        Role save = new Role();
        utils.requireNonNull(role.getRoleName(), "角色名不能为空！");
        List<Role> byRoleName = roleRepository.findByRoleName(role.getRoleName());
        if (byRoleName.size() > 0) {
            throw new Exception("已存在的角色名，请重新输入 ！！");
        }
        save.setId(UUID.randomUUID());
        save.setRoleName(role.getRoleName());
        Role role1 = roleRepository.save(save);
        List<String> permissionIds = role.getPermissionId();
        if (permissionIds == null || permissionIds.size() <= 0) {
            permissionIds = new ArrayList<>();
            permissionIds.add("d3434641-aec4-46d2-be90-d464ebda8448");
        }
        associatedRolePer(save.getId().toString(), permissionIds);
        return role1;
    }

    private String associatedRolePer(String roleId, List<String> permissionIds) throws Exception {
        try {
            rolePerRepository.deleteByRoleId(UUID.fromString(roleId));
            for (String permissionId : permissionIds) {
                Permission one = permissionRepository.getOne(UUID.fromString(permissionId));
                if (one != null) {
                    RolePer rolePer = new RolePer();
                    rolePer.setId(UUID.randomUUID());
                    rolePer.setRoleId(UUID.fromString(roleId));
                    rolePer.setPermissionId(one.getId());
                    rolePerRepository.save(rolePer);
                }
            }
            return "success";
        } catch (Exception e) {
            throw new Exception("设置权限失败！");
        }
    }

    public Login findByUserName(String userName) {
        String s = utils.get(userName);
        if (s != null) {
            return JSON.parseObject(s, Login.class);
        }
        return null;
    }

    public List<RoleRes> getRole(String id) {
        List<RoleRes> roleRes = new ArrayList<>();
        if (StringUtils.isNotBlank(id)) {
            UUID uuid = UUID.fromString(id);
            Role byRoleName = roleRepository.getOne(uuid);
            RoleRes roleRe = new RoleRes();
            List<Permission> permission = permissionRepository.findPermission(byRoleName.getId());
            roleRe.setId(byRoleName.getId());
            roleRe.setRoleName(byRoleName.getRoleName());
            roleRe.setPermissions(permission == null ? null : permission);
            roleRes.add(roleRe);
            return roleRes;
        }
        List<Role> all = roleRepository.findAll();
        roleRes = all.stream().filter(role -> !role.getId().toString().equals("72761409-9824-4981-a284-404c11b9afd3"))
                .map(role -> {
                    RoleRes roleRe = new RoleRes();
                    List<Permission> permission = permissionRepository.findPermission(role.getId());
                    roleRe.setId(role.getId());
                    roleRe.setRoleName(role.getRoleName());
                    roleRe.setPermissions(permission == null ? null : permission);
                    return roleRe;
                }).collect(Collectors.toList());
        return roleRes;
    }

    public UserRes getUser(String id) {
        UUID uuid = UUID.fromString(id);
        Login one = loginRepository.getOne(uuid);
        UserRes userRes = new UserRes();
        List<Role> roles = roleRepository.findRole(one.getId());
        userRes.setId(one.getId());
        userRes.setUserName(one.getUserName());
        userRes.setRoleName(roles);
        return userRes;
    }

    public void logout(Login login) {
        utils.delete(login.getUserName());
    }

    public String updateUserOrRole(UpdateUserOrRole updateUserOrRole) throws Exception {
        String type = updateUserOrRole.getType();
        utils.requireNonNull(type, "修改类型不能为空！");
        if (type.equals("USER")) {
            if (updateUserOrRole.getId().equals("cee6ae76-b80d-4715-9715-3b7ce1dd64af")) {
                throw new Exception("admin用户不可修改！");
            }
            List<String> ids = updateUserOrRole.getIds();
            if (ids == null || ids.size() == 0) {
                ids = new ArrayList<>();
                ids.add("a8ca8cae-7eb7-4ce9-8a3c-cb722c4c2454");
            }
            return associatedUserRole(updateUserOrRole.getId(), ids);
        } else if (type.equals("ROLE")) {
            if (updateUserOrRole.getId().equals("72761409-9824-4981-a284-404c11b9afd3")) {
                throw new Exception("admin角色不可修改！");
            }
            List<String> ids = updateUserOrRole.getIds();
            if (ids == null || ids.size() == 0) {
                ids = new ArrayList<>();
                ids.add("d3434641-aec4-46d2-be90-d464ebda8448");
            }
            return associatedRolePer(updateUserOrRole.getId(), ids);
        }
        return "fail";
    }


    public String updatePws(Login user, Pws pws) throws Exception {
        utils.requireNonNull(pws.getOrdPws(), "请输入原始密码！");
        utils.requireNonNull(pws.getNewPws(), "请输入新密码！");
        utils.requireNonNull(pws.getPws(), "请输入新密码！");
        if (!user.getPassWord().equals(pws.getOrdPws())) {
            throw new Exception("原始密码输入不正确！请重新输入！！");
        }
        if (!pws.getNewPws().equals(pws.getPws())) {
            throw new Exception("两次密码输入不一致！请重新输入！！");
        }
        if (pws.getNewPws().length() < 6 || pws.getNewPws().length() > 16) {
            throw new Exception("密码长度需要6-16位！请重新输入！！");
        }
        user.setPassWord(pws.getNewPws());
        try {
            loginRepository.save(user);
            webSocket.onMessage("用户" + user.getName() + "密码修改成功", null);
            utils.put(user.getUserName(), JSON.toJSONString(user));
            return "success";
        } catch (Exception e) {
            throw new Exception("密码修改失败！");
        }
    }

    public List<LoginVM> getUsers(UserReq userReq) {
        List<LoginVM> loginVMS = new ArrayList<>();
        String name = userReq.getName();
        String phone = userReq.getPhone();
        List<Login> all = loginRepository.findAll();
        loginVMS = all.stream().map(login -> {
            LoginVM loginVM = new LoginVM();
            BeanUtils.copyProperties(login, loginVM);
            return loginVM;
        }).collect(Collectors.toList());
        if (StringUtils.isNotEmpty(name)){
            loginVMS = loginVMS.stream().filter(loginVM -> loginVM.getName().equals(name))
            .collect(Collectors.toList());
        }
        if (StringUtils.isNotEmpty(phone)){
            loginVMS = loginVMS.stream().filter(loginVM -> loginVM.getPhone().equals(phone))
                    .collect(Collectors.toList());
        }
        return loginVMS;
    }

    public Login updateUser(UserVm userVm) {
        UUID id = userVm.getId();
        utils.requireNonNull(id,"用户id不能为空");
        Login one = loginRepository.getOne(id);
        Integer age = userVm.getAge();
        String gender = userVm.getGender();
        String phone = userVm.getPhone();
        if (age!=null)one.setAge(age);
        if (StringUtils.isNotEmpty(gender))one.setGender(gender);
        if (StringUtils.isNotEmpty(phone)){one.setPhone(phone);}
        return loginRepository.save(one);
    }

    public String  deleteUser(UserVm userVm) throws Exception {
        UUID id = userVm.getId();
        utils.requireNonNull(id,"用户id不能为空");
        try {
            loginRepository.deleteById(id);
            userRoleRepository.deleteByUserId(id);
            return "success";
        } catch (Exception e) {
            throw new Exception("员工删除失败！！");
        }

    }

    public void deleteRole(String id) {
        UUID uuid = UUID.fromString(id);
        try {
            roleRepository.deleteById(uuid);
            rolePerRepository.deleteByRoleId(uuid);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
