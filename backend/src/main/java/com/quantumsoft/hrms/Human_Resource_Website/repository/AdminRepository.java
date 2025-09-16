package com.quantumsoft.hrms.Human_Resource_Website.repository;

import com.quantumsoft.hrms.Human_Resource_Website.entity.Admin;
import com.quantumsoft.hrms.Human_Resource_Website.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
    boolean existsByRole(Role role);
    Optional<Admin> findByUsernameIgnoreCase(String username);

    Optional<Admin> findByUsername(String currentUsername);
}
