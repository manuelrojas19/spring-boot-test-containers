package com.globant.training.docker.dctc.repository;

import com.globant.training.docker.dctc.entity.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<ApplicationUser, Long> {
}
