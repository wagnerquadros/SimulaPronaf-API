package com.wagnerquadros.simulapronaf.credito.repository;

import com.wagnerquadros.simulapronaf.credito.entity.LinhaCredito;
import com.wagnerquadros.simulapronaf.credito.enums.TipoLinhaCredito;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LinhaCreditoRepository extends JpaRepository<LinhaCredito, Long> {

    List<LinhaCredito> findByTipo(TipoLinhaCredito tipo);

    List<LinhaCredito> findByAtivoTrue();

    List<LinhaCredito> findByTipoAndAtivoTrue(TipoLinhaCredito tipo);
}
