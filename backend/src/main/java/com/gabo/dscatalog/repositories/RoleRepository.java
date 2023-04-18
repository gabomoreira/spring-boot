package com.gabo.dscatalog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gabo.dscatalog.entities.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

}
