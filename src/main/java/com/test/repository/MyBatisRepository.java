package com.test.repository;

import com.test.vms.AccountInfo;
import com.test.vms.SalesRecord;
import com.test.vms.SalesRecordInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MyBatisRepository {
    // 执行任意SELECT语句（利用LinkedHashMap接收返回的结果）
    List<AccountInfo> getAccountInfo(@Param("sql") String sql);

    List<SalesRecord> getAccountByStaff(@Param("sql") String sql);

    List<SalesRecordInfo> getAccountInfoByStaff(@Param("sql") String sql);

    // 执行任意INSERT、UPDATE、DELETE语句
    int myBatisUpdateSQL(@Param("sql") String sql);


}
