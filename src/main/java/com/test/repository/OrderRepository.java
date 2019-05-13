package com.test.repository;

import com.test.entity.Order;
import com.test.entity.TradingInfo;
import com.test.vms.AccountInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
@Mapper
public interface OrderRepository extends JpaRepository<Order, UUID>, JpaSpecificationExecutor<Order>, MyBatisRepository {



}
