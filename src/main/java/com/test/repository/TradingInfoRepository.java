package com.test.repository;

import com.test.entity.TradingInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository
public interface TradingInfoRepository extends JpaRepository<TradingInfo, UUID>, JpaSpecificationExecutor<TradingInfo> {


    @Query(nativeQuery = true,
            value="SELECT * FROM trading_info where goods_id in :collect and type = 'REPLENISH' and date >= :startDate and date <= :endDate"
    )
    List<TradingInfo> findTradingInfo(@Param("collect") Set<UUID> collect, @Param("startDate")LocalDateTime startDate,@Param("endDate") LocalDateTime endDate);
}
