package com.gabo.dscatalog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gabo.dscatalog.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
