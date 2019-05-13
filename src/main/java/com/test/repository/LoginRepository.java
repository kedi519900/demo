package com.test.repository;

import com.test.entity.Login;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface LoginRepository extends JpaRepository<Login, UUID>, JpaSpecificationExecutor<Login> {

    Login findByUserNameAndPassWord(String userName, String passWord);

    Login findByUserName(String userName);


}
