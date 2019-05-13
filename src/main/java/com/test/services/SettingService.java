package com.test.services;

import com.test.config.Utils;
import com.test.controller.WebSocket;
import com.test.entity.Activity;
import com.test.entity.Companys;
import com.test.entity.GoodsType;
import com.test.entity.Login;
import com.test.repository.ActivityRepository;
import com.test.repository.CompanyRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SettingService {
    public static Map<String, Companys> companyMap = new HashMap<>();
    public static Map<Integer, GoodsType> goodsTypeMap = new HashMap<>();

    @Autowired
    private Utils utils;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private WebSocket webSocket;
    @Autowired
    private ActivityRepository activityRepository;

    public String addCompany(Companys companys) throws Exception {
        companys.setId(UUID.randomUUID());
        try {
            companyRepository.save(companys);
            return "success";
        } catch (Exception e) {
            throw new Exception("创建供应商失败！");
        }
    }

    public String updateCompany(Companys companys) throws Exception {
        try {
            companyRepository.save(companys);
            return "success";
        } catch (Exception e) {
            throw new Exception("修改供应商信息失败！");
        }

    }

    public String deleteCompany(Companys companys) throws Exception {
        try {
            companyRepository.deleteById(companys.getId());
            return "success";
        } catch (Exception e) {
            throw new Exception("供应商删除失败！");
        }
    }

    public List<Companys> getCompany() {
        List<Companys> all = companyRepository.findAll();
        all.stream().forEach(companys -> {
            companyMap.put(companys.getId().toString(), companys);
        });
        return all;
    }

    public String addActivity(Login user, Activity activity) throws Exception {
        Double discount = activity.getDiscount();
        String name = activity.getName();
        LocalDateTime startDate = activity.getStartDate();
        LocalDateTime endDate = activity.getEndDate();
        utils.requireNonNull(discount, "折扣信息不能为空！");
        if (discount < 0 || discount > 10) throw new Exception("折扣信息请输入1-10");
        utils.requireNonNull(name, "请输入活动名称");
        utils.requireNonNull(startDate, "请输入活动开始时间");
        utils.requireNonNull(endDate, "请输入活动结束时间");
        if (activity.getId() == null) {
            activity.setId(UUID.randomUUID());
            activity.setStatus("stop");
        }
        try {
            activityRepository.save(activity);
        } catch (Exception e) {
            throw new Exception("活动创建失败");
        }
        webSocket.onMessage("用户" + user.getName() + "创建活动：" + name, null);
        return "success";
    }

    public String delActivity(Login user, Activity activity) throws Exception {
        if (StringUtils.isNotEmpty(activity.getStatus()) && activity.getStatus().equals("stop")) {
            activity.setStatus("start");
        } else if (StringUtils.isNotEmpty(activity.getStatus()) && activity.getStatus().equals("start")) {
            activity.setStatus("stop");
        }else if(StringUtils.isNotEmpty(activity.getStatus()) && activity.getStatus().equals("delete")&&null!=activity.getId()){
            try {
                activityRepository.deleteById(activity.getId());
                return "success";
            } catch (Exception e) {
                throw new Exception("活动删除失败");
            }
        }
        try {
            activity = activityRepository.save(activity);
        } catch (Exception e) {
            throw new Exception("操作失败");
        }
        if (activity.getStatus().equals("start")) webSocket.onMessage("用户" + user.getName() + "开始了活动："+activity.getName(), null);
        if (activity.getStatus().equals("stop")) webSocket.onMessage("用户" + user.getName() + "暂停了活动："+activity.getName(), null);
        return "success";
    }

    public List<Activity> getActivity(String type) {
        List<Activity> all = activityRepository.findAll();
        LocalDateTime now = LocalDateTime.now();
        if (type.equals("SALES"))
            all = all.stream().filter(activity -> activity.getStatus().equals("start")&&now.isAfter(activity.getStartDate()) && now.isBefore(activity.getEndDate())).collect(Collectors.toList());
        return all;
    }
}
