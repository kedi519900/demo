package com.test.repository;

import com.test.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID>, JpaSpecificationExecutor<Role> {

    List<Role> findByRoleName(String roleName);

    @Query(nativeQuery = true,
            value="SELECT * FROM role where id in (SELECT role_id FROM user_role where user_id = ?)"
    )
    List<Role> findRole(UUID id);
}
