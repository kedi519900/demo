package com.test.repository;

import com.test.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, UUID>, JpaSpecificationExecutor<Permission> {

    @Query(nativeQuery = true,
            value="SELECT * FROM permission where id in (SELECT permission_id FROM role_permission where role_id = ?)"
    )
    List<Permission> findPermission(UUID id);
}
