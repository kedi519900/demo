package com.test.repository;

import com.test.entity.GoodsType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface GoodsTypeRepository extends JpaRepository<GoodsType, UUID>, JpaSpecificationExecutor<GoodsType> {


}
