package com.inovaceifa.api.repository;

import com.inovaceifa.api.model.RefResFerrugem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RefResFerrugemRepository extends JpaRepository<RefResFerrugem, Long> {

    List<RefResFerrugem> findAllByOrderByDescricaoAsc();

}