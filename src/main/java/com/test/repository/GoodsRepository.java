package com.test.repository;

import com.test.entity.Goods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface GoodsRepository extends JpaRepository<Goods, UUID>,JpaSpecificationExecutor<Goods> {


}
