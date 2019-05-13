package com.test.services;

import com.test.config.Utils;
import com.test.controller.WebSocket;
import com.test.entity.*;
import com.test.repository.*;
import com.test.vms.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.test.entity.Type.REPLENISH;
import static com.test.services.SettingService.companyMap;
import static com.test.services.SettingService.goodsTypeMap;

@Service
public class GoodsService {
    @Autowired
    private ExcelExport excelExport;

    @Autowired
    private Utils utils;
    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private LoginRepository loginRepository;

    @Autowired
    private SettingService settingService;

    @Autowired
    private WebSocket webSocket;

    @Autowired
    private TradingInfoRepository tradingInfoRepository;

    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    private GoodsTypeRepository goodsTypeRepository;

    @Autowired
    private CompanyRepository companyRepository;

    private static HashMap<String, List<ExportVM>> downLoadData = new HashMap<>();


    public Goods addGoods(Login user, CreateGoodsVM vm) throws Exception {
        String name = vm.getName();
        UUID companyId = vm.getCompanyId();
        int typeId = vm.getTypeId();
        utils.requireNonNull(name, "商品名称不能为空");
        utils.requireNonNull(companyId, "供应商不能为空");
        utils.requireNonNull(typeId, "商品种类不能为空");
        Goods goods = null;
        String id = vm.getId();
        if (StringUtils.isNotBlank(id)) {
            goods = goodsRepository.getOne(UUID.fromString(id));
            if (goods == null) {
                throw new Exception("没有此商品！！");
            }
        } else {
            goods = new Goods();
            goods.setId(UUID.randomUUID());
        }
        goods.setCompanyId(vm.getCompanyId());
        goods.setName(vm.getName());
        goods.setTypeId(vm.getTypeId());
        goods.setSize(vm.getSize());
        goods.setMinNumber(vm.getMinNum());
        if (vm.getPrice() != null)
            goods.setPrice(vm.getPrice());
        Goods save = goodsRepository.save(goods);
        webSocket.onMessage("用户：" + user.getName() + "添加商品：" + vm.getName(), null);
        return save;
    }

    public List<GoodsType> getGoodsType() {
        List<GoodsType> all = goodsTypeRepository.findAll();
        all.stream().forEach(goodsType -> {
            goodsTypeMap.put(goodsType.getId(), goodsType);
        });
        return all;
    }

    public List<Companys> getCompany() {
        return companyRepository.findAll();
    }

    public List<GoodsRes> findGoods(GoodsReq goodsReq) {
        settingService.getCompany();
        getGoodsType();
        List<GoodsRes> list = new ArrayList<>();
        Specification<Goods> goodsSpecification = searchGoods(goodsReq);
        List<Goods> all = goodsRepository.findAll(goodsSpecification);
        all.stream().forEach(goods -> {
            GoodsRes goodsRes = new GoodsRes();
            goodsRes.setId(goods.getId());
            Companys companys = companyMap.get(goods.getCompanyId().toString());
            goodsRes.setCompany(companys);
            GoodsType goodsType = goodsTypeMap.get(goods.getTypeId());
            goodsRes.setGoodType(goodsType);
            goodsRes.setMinNumber(goods.getMinNumber());
            goodsRes.setName(goods.getName());
            goodsRes.setNumber(goods.getNumber());
            goodsRes.setPrice(goods.getPrice());
            goodsRes.setSize(goods.getSize());
            list.add(goodsRes);
        });
        companyMap.clear();
        goodsTypeMap.clear();
        return list;
    }

    private Specification<Goods> searchGoods(GoodsReq vm) {
        return (root, query, cb) -> {
            ArrayList<Predicate> predicates = new ArrayList<>();
            if (StringUtils.isNotBlank(vm.getName())) {
                predicates.add(cb.like(root.get("name"), "%" + vm.getName() + "%"));
            }
            if (null != vm.getCompany()) {
                predicates.add(cb.equal(root.get("companyId"), vm.getCompany()));
            }
            if (null != vm.getGoodType()) {
                predicates.add(cb.equal(root.get("typeId"), vm.getGoodType()));
            }
            return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        };
    }


    public synchronized String updateGoodsInfo(Login user, TradingInfoReq goodsReq) throws Exception {
        TradingInfo tradingInfo = new TradingInfo();
        Order order = new Order();
        Utils.requireNonNull(goodsReq.getGoodsId(), "商品id不能为空");
        UUID goodsId = goodsReq.getGoodsId();
        Goods one = goodsRepository.getOne(goodsId);
        if (one.getNumber() == null) {
            one.setNumber(0);
        }
        Integer number = goodsReq.getNumber();
        if (one == null) {
            throw new Exception("查询不到此商品！");
        }
        Type type = goodsReq.getType();
        switch (type) {
            case SALES:
                if (null != one.getNumber() && one.getNumber() > number) {
                    one.setNumber(one.getNumber() - number);
                } else {
                    webSocket.onMessage("商品：" + one.getName() + "库存不足！请及时补货！！当前数量：" + one.getNumber(), null);
                    throw new Exception("库存不足！");
                }
                if (one.getNumber() < one.getMinNumber()) {
                    webSocket.onMessage("商品：" + one.getName() + "即将售罄！请及时补货！！当前数量：" + one.getNumber(), null);
                }
                tradingInfo.setPrice(one.getPrice());
                tradingInfo.setTotalPrice(one.getPrice().multiply(new BigDecimal(goodsReq.getNumber())));
                //TODO   这里需要判断是否参加活动
                Activity activity = null;
                if (goodsReq.getActivity_id() != null) {
                    activity = activityRepository.getOne(goodsReq.getActivity_id());
                }
                if (activity != null) {
                    Double discount = activity.getDiscount();
                    BigDecimal divide = new BigDecimal(discount).divide(new BigDecimal(10));
                    tradingInfo.setSalesTotalPrice(tradingInfo.getTotalPrice().multiply(divide));
                    order.setDiscount(discount.toString());
                    order.setConfirmPrice(one.getPrice().multiply(new BigDecimal(number)).multiply(divide));
                } else {
                    tradingInfo.setSalesTotalPrice(tradingInfo.getTotalPrice());
                    order.setConfirmPrice(one.getPrice().multiply(new BigDecimal(number)));
                }
                order.setId(UUID.randomUUID());
                order.setGoodsId(goodsId);
                order.setGoodsName(one.getName());
                order.setCompanyId(one.getCompanyId());
                order.setCreateDate(LocalDateTime.now());
                order.setPrice(one.getPrice());
                order.setNumber(number);
                order.setTotalPrice(one.getPrice().multiply(new BigDecimal(number)));
                order.setReturnPrice(new BigDecimal(0));
                order.setCustomerName(goodsReq.getCustomer_name());
                order.setCustomerPhone(goodsReq.getCustomer_phone());
                order.setStaffId(user.getId());
                order.setStatus("success");
                break;
            case RETURN:
                if (goodsReq.getReturnNum() <= 0) {
                    throw new Exception("退货数量异常！");
                }
                UUID orderId = goodsReq.getOrderId();//订单号
                order = orderRepository.getOne(orderId);
                int returnNum = order.getReturnNum();
                if (order.getNumber() < returnNum + goodsReq.getReturnNum()) {
                    throw new Exception("退货数量异常！");
                }
                order.setCreateDate(LocalDateTime.now());
                order.setReturnNum(returnNum + goodsReq.getReturnNum());
                if (order.getReturnPrice() == null) {
                    order.setReturnPrice(order.getConfirmPrice().divide(new BigDecimal(order.getNumber())).multiply(new BigDecimal(goodsReq.getReturnNum())));
                } else {
                    order.setReturnPrice(order.getReturnPrice().add(order.getConfirmPrice().divide(new BigDecimal(order.getNumber())).multiply(new BigDecimal(goodsReq.getReturnNum()))));
                }
                if (order.getReturnNum() == order.getNumber()) {
                    order.setStatus("complete");
                }
                order.setStaffId(user.getId());
                one.setNumber(one.getNumber() + goodsReq.getReturnNum());
                //由于可能会有折扣，所以这里的单价是实际支付的价格除以当时出售的数量
                tradingInfo.setPrice(order.getConfirmPrice().divide(new BigDecimal(order.getNumber())));
                tradingInfo.setTotalPrice(tradingInfo.getPrice().multiply(new BigDecimal(goodsReq.getReturnNum())));
                break;
            case REPLENISH:
                Utils.requireNonNull(goodsReq.getPrice(), "商品进货价不能为空");
                Utils.requireNonNull(goodsReq.getSalesPrice(), "商品售价不能为空");
                one.setNumber(one.getNumber() + number);
                if (StringUtils.isNotBlank(goodsReq.getSalesPrice().toString())) {
                    one.setPrice(goodsReq.getSalesPrice());
                }
                if (one.getNumber() < one.getMinNumber()) {
                    webSocket.onMessage("商品：" + one.getName() + "即将售罄！请及时补货！！当前数量：" + one.getNumber(), null);
                }
                tradingInfo.setPrice(goodsReq.getPrice());
                tradingInfo.setTotalPrice(goodsReq.getPrice().multiply(new BigDecimal(goodsReq.getNumber())));
                break;
        }
        tradingInfo.setId(UUID.randomUUID());
        tradingInfo.setGoodsId(goodsId);
        tradingInfo.setCompanyId(one.getCompanyId());
        tradingInfo.setTypeId(one.getTypeId());
        tradingInfo.setDate(LocalDateTime.now());
        tradingInfo.setGoodsName(one.getName());
        tradingInfo.setNumber(goodsReq.getNumber());
        tradingInfo.setType(type);
        tradingInfo.setStaffId(user.getId());
        if (type != REPLENISH){
            try {
                orderRepository.save(order);
            } catch (Exception e) {
                throw new Exception("订单保存失败！");
            }
        }
        try {
            goodsRepository.save(one);
        } catch (Exception e) {
            throw new Exception("库存操作失败！");
        }
        try {
            tradingInfoRepository.save(tradingInfo);
        } catch (Exception e) {
            throw new Exception("出入库操作失败！");
        }
        return "success";
    }

    public List<TradingInfoRes> findTradingInfo(GoodsReq goodsReq) {
        List<TradingInfoRes> list = new ArrayList<>();
        settingService.getCompany();
        getGoodsType();
        Specification<TradingInfo> goodsSpecification = searchTradingInfo(goodsReq);
        List<TradingInfo> all = tradingInfoRepository.findAll(goodsSpecification);
        all.stream().forEach(tradingInfo -> {
            TradingInfoRes goodsRes = new TradingInfoRes();
            goodsRes.setId(tradingInfo.getId());
            GoodsType goodsType = goodsTypeMap.get(tradingInfo.getTypeId());
            goodsRes.setGoodsType(goodsType);
            goodsRes.setType(tradingInfo.getType());
            Companys companys = companyMap.get(tradingInfo.getCompanyId().toString());
            goodsRes.setCompanys(companys);
            goodsRes.setGoodsId(tradingInfo.getGoodsId());
            goodsRes.setDate(tradingInfo.getDate());
            goodsRes.setGoodsName(tradingInfo.getGoodsName());
            goodsRes.setNumber(tradingInfo.getNumber());
            goodsRes.setPrice(tradingInfo.getPrice());
            Login one = loginRepository.getOne(tradingInfo.getStaffId());
            goodsRes.setStaff(one);
            list.add(goodsRes);
        });
        companyMap.clear();
        goodsTypeMap.clear();
        return list;
    }

    private Specification<TradingInfo> searchTradingInfo(GoodsReq goodsReq) {
        return (root, query, cb) -> {
            ArrayList<Predicate> predicates = new ArrayList<>();
            if (StringUtils.isNotEmpty(goodsReq.getName())) {
                predicates.add(cb.like(root.get("goodsName"), "%" + goodsReq.getName() + "%"));
            }
            if (null != (goodsReq.getGoodType())) {
                predicates.add(cb.equal(root.get("typeId"), goodsReq.getGoodType()));
            }
            if (null != (goodsReq.getCompany())) {
                predicates.add(cb.equal(root.get("companyId"), goodsReq.getCompany()));
            }
            if (null != (goodsReq.getFromDate()) && null == (goodsReq.getToDate())) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("date"), goodsReq.getFromDate()));
            }
            if (null == (goodsReq.getFromDate()) && null != (goodsReq.getToDate())) {
                predicates.add(cb.lessThanOrEqualTo(root.get("date"), goodsReq.getToDate()));
            }
            if (null != (goodsReq.getFromDate()) && null != (goodsReq.getToDate())) {
                predicates.add(cb.between(root.get("date"), goodsReq.getFromDate(), goodsReq.getToDate()));
            }
            if (null != (goodsReq.getType())) {
                predicates.add(cb.equal(root.get("type"), goodsReq.getType()));
            }
            return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        };
    }

    public List<OrderRes> getOrder() {
        List<OrderRes> list = new ArrayList<>();
        settingService.getCompany();
        List<Order> orderList = orderRepository.findAll();
        orderList.stream().forEach(order -> {
            OrderRes orderRes = new OrderRes();
            BeanUtils.copyProperties(order, orderRes);
            orderRes.setCompanyName(companyMap.get(order.getCompanyId().toString())==null?null:(companyMap.get(order.getCompanyId().toString()).getCompanyName()));
            Login one = loginRepository.getOne(order.getStaffId());
            if (one != null) {
                orderRes.setStaffName(one.getName());
                orderRes.setStaffPhone(one.getPhone());
            }
            list.add(orderRes);
        });
        companyMap.clear();
        return list;
    }

    public List<AccountInfo> getAccountInfo(AccountInfoReq accountInfoReq) {
        settingService.getCompany();
        LocalDateTime startDate = accountInfoReq.getStartDate();
        LocalDateTime endDate = accountInfoReq.getEndDate();
        String sql = "SELECT goods_id goodsId,goods_name goodsname,sum(\"number\")-sum(\"return_num\") salesnum,sum(confirm_price)-sum(return_price) salesTotalPrice FROM order_info where create_date BETWEEN '"+startDate+"' and '"+endDate+"' GROUP BY goods_name,goods_id";
        List<AccountInfo> linkedHashMaps = orderRepository.getAccountInfo(sql);
        Set<UUID> collect = linkedHashMaps.stream().map(AccountInfo::getGoodsId).map(s -> {return UUID.fromString(s);}).collect(Collectors.toSet());
        List<TradingInfo> tradingInfos =  tradingInfoRepository.findTradingInfo(collect,startDate,endDate);
        for (AccountInfo linkedHashMap : linkedHashMaps) {
            for (TradingInfo tradingInfo : tradingInfos) {
                if (linkedHashMap.getGoodsId().equals(tradingInfo.getGoodsId().toString())){
                    linkedHashMap.setReplenishNum((linkedHashMap.getReplenishNum()==null?0:linkedHashMap.getReplenishNum())+tradingInfo.getNumber());
                    linkedHashMap.setReplenishTotalPrice((linkedHashMap.getReplenishTotalPrice()==null?new BigDecimal(0):linkedHashMap.getReplenishTotalPrice()).add(tradingInfo.getTotalPrice()));
                }
                if (linkedHashMap.getReplenishNum()!=null && linkedHashMap.getReplenishTotalPrice()!=null){
                    linkedHashMap.setReplenishPrice(linkedHashMap.getReplenishTotalPrice().divide(new BigDecimal(linkedHashMap.getReplenishNum()), 2, RoundingMode.HALF_UP));
                    BigDecimal multiply = linkedHashMap.getReplenishPrice().multiply(new BigDecimal(linkedHashMap.getSalesNum()));
                    linkedHashMap.setProfits(linkedHashMap.getSalesTotalPrice().subtract(multiply));
                    Companys companys = companyMap.get(tradingInfo.getCompanyId().toString());
                    linkedHashMap.setCompanys(companys);
                }
            }
        }
        companyMap.clear();
        return linkedHashMaps;
    }

    public List<SalesRecord> getAccountByStaff(AccountInfoReq accountInfoReq) {
        settingService.getCompany();
        LocalDateTime startDate = accountInfoReq.getStartDate();
        LocalDateTime endDate = accountInfoReq.getEndDate();
        String sql = "SELECT staff_id ,(SELECT name from \"login\" where \"id\" = staff_id) StaffName  ,sum(\"number\") num,sum(confirm_price) price,sum(return_num) returnNum,sum(return_price) returnPrice FROM order_info where create_date BETWEEN '"+startDate+"' and '"+endDate+"' GROUP BY staff_id";
        List<SalesRecord> linkedHashMaps = orderRepository.getAccountByStaff(sql);
        return linkedHashMaps;
    }

    public List<SalesRecordInfo> getAccountInfoByStaff(AccountInfoReq accountInfoReq) {
        String staffId = accountInfoReq.getStaffId();
        utils.requireNonNull(staffId,"员工id不能为空！");
        LocalDateTime startDate = accountInfoReq.getStartDate();
        LocalDateTime endDate = accountInfoReq.getEndDate();
        String sql="SELECT goods_id,goods_name,\"number\" salesNum ,confirm_price salesTotalPrice ,return_num,return_price ,staff_id,(SELECT name from \"login\" where \"id\" = staff_id) StaffName ,create_date date FROM order_info where  create_date BETWEEN '"+startDate+"' and '"+endDate+"' and staff_id = '"+staffId+"'";
        List<SalesRecordInfo> accountInfoByStaff = orderRepository.getAccountInfoByStaff(sql);
        Set<UUID> collect = accountInfoByStaff.stream().map(SalesRecordInfo::getGoodsId).map(s -> {return UUID.fromString(s);}).collect(Collectors.toSet());
        List<TradingInfo> tradingInfos =  tradingInfoRepository.findTradingInfo(collect,startDate,endDate);
        for (SalesRecordInfo salesRecordInfo : accountInfoByStaff) {
            for (TradingInfo tradingInfo : tradingInfos) {
                if (salesRecordInfo.getGoodsId().equals(tradingInfo.getGoodsId().toString())){
                    salesRecordInfo.setReplenishNum((salesRecordInfo.getReplenishNum()==null?0:salesRecordInfo.getReplenishNum())+tradingInfo.getNumber());
                    salesRecordInfo.setReplenishTotalPrice((salesRecordInfo.getReplenishTotalPrice()==null?new BigDecimal(0):salesRecordInfo.getReplenishTotalPrice()).add(tradingInfo.getTotalPrice()));
                }
                if (salesRecordInfo.getReplenishNum()!=null && salesRecordInfo.getReplenishTotalPrice()!=null){
                    salesRecordInfo.setReplenishPrice(salesRecordInfo.getReplenishTotalPrice().divide(new BigDecimal(salesRecordInfo.getReplenishNum()), 2, RoundingMode.HALF_UP));
                    Companys companys = companyMap.get(tradingInfo.getCompanyId().toString());
                    int num = salesRecordInfo.getSalesNum() - salesRecordInfo.getReturnNum();//实际销售数量
                    BigDecimal multiply = salesRecordInfo.getReplenishPrice().multiply(new BigDecimal(num));//实际成本
                    BigDecimal subtract = salesRecordInfo.getSalesTotalPrice().subtract(salesRecordInfo.getReturnPrice());//实际销售总价
                    salesRecordInfo.setProfits(subtract.subtract(multiply));
                    salesRecordInfo.setCompanys(companys);
                }
            }
        }
        companyMap.clear();
        return accountInfoByStaff;
    }

    public String submit(GoodsReq goodsReq) {
        List<TradingInfoRes> tradingInfo = findTradingInfo(goodsReq);
        List<ExportVM> list = new ArrayList<>();
        UUID uuid = UUID.randomUUID();
        tradingInfo.stream().forEach(tradingInfoRes -> {
            ExportVM exportVM = new ExportVM();
            exportVM.setCompanyName(tradingInfoRes.getGoodsName());
            exportVM.setGoodsName(tradingInfoRes.getGoodsName());
            exportVM.setDate(tradingInfoRes.getDate());
            exportVM.setGoodsType(tradingInfoRes.getGoodsType()==null?"":tradingInfoRes.getGoodsType().getType());
            exportVM.setNumber(tradingInfoRes.getNumber());
            exportVM.setPrice(tradingInfoRes.getPrice());
            exportVM.setStaffName(tradingInfoRes.getStaff()==null?"":tradingInfoRes.getStaff().getName());
            exportVM.setType(tradingInfoRes.getType());
            list.add(exportVM);
        });
        downLoadData.put(uuid.toString(),list);
        return "http://127.0.0.1:8080/export/"+uuid;
    }

    public void export(String id, HttpServletResponse response) throws Exception {
        List<ExportVM> list = downLoadData.get(id);
        excelExport.export(list,response);
    }
}
