package com.maki.pompompurin.repository;

import com.maki.pompompurin.model.Maki;
import org.springframework.data.jpa.repository.JpaRepository;

// JpaRepository ya trae findAll, findById, save, deleteById, existsById, count...
public interface MakiRepository extends JpaRepository<Maki, Long> {
}
