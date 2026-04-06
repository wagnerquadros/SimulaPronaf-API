package com.wagnerquadros.simulapronaf.credito.repository;

import com.wagnerquadros.simulapronaf.credito.entity.ItemLinhaCredito;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ItemLinhaCreditoRepository extends JpaRepository<ItemLinhaCredito, Long> {

    List<ItemLinhaCredito> findByLinhaCreditoId(Long linhaCreditoId);

    List<ItemLinhaCredito> findByLinhaCreditoIdAndAtivoTrueOrderByOrdemExibicaoAsc(Long linhaCreditoId);

    Optional<ItemLinhaCredito> findByCodigo(String codigo);
}
