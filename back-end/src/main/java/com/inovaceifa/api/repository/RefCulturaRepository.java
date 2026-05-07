package com.inovaceifa.api.repository;

import com.inovaceifa.api.model.RefCultura;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RefCulturaRepository extends JpaRepository<RefCultura, Long> {

    List<RefCultura> findAllByOrderByDescricaoAsc();

}