package com.wagnerquadros.simulapronaf.usuarios.repository;

import com.wagnerquadros.simulapronaf.usuarios.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findByGoogleSubject(String googleSubject);
    boolean existsByEmail(String email);
    boolean existsByGoogleSubject(String googleSubject);
}
