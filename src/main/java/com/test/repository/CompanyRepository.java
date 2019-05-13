package com.test.repository;

import com.test.entity.Companys;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CompanyRepository extends JpaRepository<Companys, UUID>, JpaSpecificationExecutor<Companys> {


}
