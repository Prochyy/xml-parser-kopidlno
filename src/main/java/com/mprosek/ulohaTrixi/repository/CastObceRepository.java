package com.mprosek.ulohaTrixi.repository;

import com.mprosek.ulohaTrixi.entity.CastObce;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CastObceRepository extends JpaRepository<CastObce, Integer> {
    
}
