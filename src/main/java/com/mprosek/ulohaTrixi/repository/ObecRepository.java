package com.mprosek.ulohaTrixi.repository;

import com.mprosek.ulohaTrixi.entity.Obec;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ObecRepository extends JpaRepository<Obec, Integer> {
}
