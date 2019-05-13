package com.test.dao;

import com.test.vms.AccountInfo;
import com.test.vms.SalesRecord;
import com.test.vms.SalesRecordInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface MyBatisMapper {
    // 执行任意SELECT语句（利用LinkedHashMap接收返回的结果）
    @Select("${sql}")
    List<AccountInfo> getAccountInfo(@Param("sql") String sql);


    @Select("${sql}")
    List<SalesRecord> getAccountByStaff(@Param("sql") String sql);

    @Select("${sql}")
    List<SalesRecordInfo> getAccountInfoByStaff(@Param("sql") String sql);

    // 执行任意INSERT、UPDATE、DELETE语句
    @Update("${sql}")
    int myBatisUpdateSQL(@Param("sql") String sql);


}
