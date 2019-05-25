package com.test.controller;

import com.test.config.AdminRequired;
import com.test.config.CurrentUser;
import com.test.config.LoginRequired;
import com.test.config.Utils;
import com.test.entity.Companys;
import com.test.entity.Login;
import com.test.entity.Permission;
import com.test.entity.Role;
import com.test.services.SettingService;
import com.test.services.UserOrLoginService;
import com.test.vms.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin
public class UserOrLoginController {

    @Autowired
    private UserOrLoginService userOrLoginService;

    @Autowired
    private SettingService settingService;
    @PostMapping(value = "/login")
    public LoginResVM login(@RequestBody Login login) throws Exception {
        LoginResVM loginResVM = userOrLoginService.login(login);
        return loginResVM;
    }

    @LoginRequired
    @AdminRequired
    @PostMapping(value = "/getUsers")
    public List<LoginVM> getUsers(@RequestBody UserReq userReq) throws Exception {
        List<LoginVM> logins = userOrLoginService.getUsers(userReq);
        return logins;
    }

    @PostMapping(value = "/logout")
    @LoginRequired
    public void logout(@CurrentUser Login user){
        userOrLoginService.logout(user);
    }

    @PutMapping(value = "/updatePws")
    @LoginRequired
    public String updatePws(@CurrentUser Login user, @RequestBody Pws pws) throws Exception {
       return  userOrLoginService.updatePws(user,pws);
    }


    @PutMapping(value = "/login")
    @LoginRequired
    @AdminRequired
    public Login login(@CurrentUser Login user, @RequestBody UserVm userVm) throws Exception {
        return userOrLoginService.createrUser(user,userVm);
    }
    @PutMapping(value = "/User")
    @LoginRequired
    @AdminRequired
    public Login updateUser(@RequestBody UserVm userVm){
        return userOrLoginService.updateUser(userVm);
    }

    @DeleteMapping(value = "/User")
    @LoginRequired
    @AdminRequired
    public String deleteUser(@RequestBody UserVm userVm) throws Exception {
        return userOrLoginService.deleteUser(userVm);
    }



    @GetMapping(value = "/getPermissions")
    @LoginRequired
    @AdminRequired
    public List<Permission> getPermissions(){
        return userOrLoginService.getPermissions();
    }

    @GetMapping(value = {"/getRole/{id}","/getRole"})
    @LoginRequired
    @AdminRequired
    public List<RoleRes> getRole(@PathVariable(required = false) String id){
        List<RoleRes> role = userOrLoginService.getRole(id);
        return role;
    }

    @GetMapping(value = {"/deleteRole/{id}"})
    @LoginRequired
    @AdminRequired
    public void deleteRole(@PathVariable(required = false) String id){
        userOrLoginService.deleteRole(id);
    }


    @GetMapping(value = "/getUser/{id}")
    @LoginRequired
    @AdminRequired
    public UserRes getUser(@PathVariable String id){
        Utils.requireNonNull(id,"用户id不能为空!!");
        return userOrLoginService.getUser(id);
    }

    @PostMapping(value = "/createRole")
    @LoginRequired
    @AdminRequired
    public Role createRole(@RequestBody RoleVM role) throws Exception {
        Role r = userOrLoginService.createRole(role);
        return r;
    }

    @PutMapping(value = "/updateUserOrRole")
    @LoginRequired
    @AdminRequired
    public String updateUserOrRole(@RequestBody UpdateUserOrRole updateUserOrRole) throws Exception {
        return userOrLoginService.updateUserOrRole(updateUserOrRole);
    }

    @GetMapping(value = "/company")
    @LoginRequired
    @AdminRequired
    public List<Companys> getCompany() throws Exception {
        return settingService.getCompany();
    }

    @PostMapping(value = "/company")
    @LoginRequired
    @AdminRequired
    public String addCompany(@RequestBody Companys companys) throws Exception {
        return settingService.addCompany(companys);
    }

    @PutMapping(value = "/company")
    @LoginRequired
    @AdminRequired
    public String updateCompany(@RequestBody Companys companys) throws Exception {
        return settingService.updateCompany(companys);
    }

    @DeleteMapping(value = "/company")
    @LoginRequired
    @AdminRequired
    public String deleteCompany(@RequestBody Companys companys) throws Exception {
        return settingService.deleteCompany(companys);
    }


}
