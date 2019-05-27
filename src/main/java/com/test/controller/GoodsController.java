package com.test.controller;


import com.test.config.AdminRequired;
import com.test.config.CurrentUser;
import com.test.config.LoginRequired;
import com.test.entity.*;
import com.test.services.GoodsService;
import com.test.services.SettingService;
import com.test.vms.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.util.List;

@RestController
@CrossOrigin
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private SettingService settingService;

    @PostMapping(value = {"/goods"})
    @LoginRequired
    @AdminRequired
    public Goods addGoods(@CurrentUser Login user, @RequestBody CreateGoodsVM vm) throws Exception {
        return goodsService.addGoods(user,vm);
    }

    @GetMapping(value = {"/getGoodsType"})
    @LoginRequired
    public List<GoodsType> getGoodsType(){
        return goodsService.getGoodsType();
    }

    @GetMapping(value = {"/getCompany"})
    @LoginRequired
    public List<Companys> getCompany(){
        return goodsService.getCompany();
    }

    @PostMapping(value = {"/findGoods"})
    @LoginRequired
    public List<GoodsRes> findGoods(@RequestBody GoodsReq goodsReq){
        return goodsService.findGoods(goodsReq);
    }


    @PostMapping(value = {"/findTradingInfo"})
    @LoginRequired
    public List<TradingInfoRes> findTradingInfo(@RequestBody GoodsReq goodsReq){
        return goodsService.findTradingInfo(goodsReq);
    }


    @PutMapping(value = {"/goods"})
    @LoginRequired
    public String Goods(@CurrentUser Login user,@RequestBody TradingInfoReq goodsReq) throws Exception {
        return goodsService.updateGoodsInfo(user,goodsReq);
    }

    @GetMapping(value = {"/activity/{type}"})
    @LoginRequired
    public List<Activity> getActivity(@PathVariable String type) throws Exception {
        return settingService.getActivity(type);
    }

    @PostMapping(value = {"/activity"})
    @LoginRequired
    public String addActivity(@CurrentUser Login user,@RequestBody Activity activity) throws Exception {
        return settingService.addActivity(user,activity);
    }

    @PutMapping(value = {"/activity"})
    @LoginRequired
    public String delActivity(@CurrentUser Login user,@RequestBody Activity activity) throws Exception {
        return settingService.delActivity(user,activity);
    }

    @GetMapping(value = {"/orderList"})
    @LoginRequired
    public List<OrderRes> orderList() {
        return goodsService.getOrder();
    }

    @PostMapping(value = {"/getAccountInfo"})
    @LoginRequired
    @AdminRequired
    private List<AccountInfo> getAccountInfo(@RequestBody AccountInfoReq accountInfoReq){
        return goodsService.getAccountInfo(accountInfoReq);
    }


    @PostMapping(value = {"/getAccountByStaff"})
    @LoginRequired
    @AdminRequired
    private List<SalesRecord> getAccountByStaff(@RequestBody AccountInfoReq accountInfoReq){
        return goodsService.getAccountByStaff(accountInfoReq);
    }

    @PostMapping(value = {"/getAccountInfoByStaff"})
    @LoginRequired
    @AdminRequired
    private List<SalesRecordInfo> getAccountInfoByStaff(@RequestBody AccountInfoReq accountInfoReq){
        return goodsService.getAccountInfoByStaff(accountInfoReq);
    }


    @PostMapping(value = {"/submit"})
    @LoginRequired
    public String orderList(@RequestBody GoodsReq goodsReq) {
        return goodsService.submit(goodsReq);
    }

    @GetMapping(value = {"/export/{id}"})
    public void orderList(@PathVariable String id, HttpServletResponse response) throws Exception {
        goodsService.export(id,response);
    }

}
