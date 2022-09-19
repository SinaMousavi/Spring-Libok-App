package com.libok.springlibrarymallapp.repository;

import com.libok.springlibrarymallapp.model.user.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;



public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    UserRole findByRole(String role);
}