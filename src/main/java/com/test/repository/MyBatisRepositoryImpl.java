package com.test.repository;

import com.test.dao.MyBatisMapper;
import com.test.vms.AccountInfo;
import com.test.vms.SalesRecord;
import com.test.vms.SalesRecordInfo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class MyBatisRepositoryImpl implements MyBatisRepository {
    @Autowired
    private MyBatisMapper myBatisMapper;

    @Override
    public List<AccountInfo> getAccountInfo(String sql) {
        return myBatisMapper.getAccountInfo(sql);
    }

    @Override
    public List<SalesRecord> getAccountByStaff(String sql) {
        return myBatisMapper.getAccountByStaff(sql);
    }

    @Override
    public List<SalesRecordInfo> getAccountInfoByStaff(String sql) {
        return myBatisMapper.getAccountInfoByStaff(sql);
    }

    @Override
    public int myBatisUpdateSQL(String sql) {
        return myBatisMapper.myBatisUpdateSQL(sql);
    }

}
