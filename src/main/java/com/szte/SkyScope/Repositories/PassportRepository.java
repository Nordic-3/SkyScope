package com.szte.SkyScope.Repositories;

import com.szte.SkyScope.Models.Passport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PassportRepository extends JpaRepository<Passport, Long> {}
