package com.test.repository;

import com.test.entity.RolePer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.UUID;

@Repository
public interface RolePerRepository extends JpaRepository<RolePer, UUID>, JpaSpecificationExecutor<RolePer> {
    @Modifying
    @Transactional
    @Query(nativeQuery = true,
            value="delete from role_permission where role_id = ?"
    )
    void deleteByRoleId(UUID fromString);

}
