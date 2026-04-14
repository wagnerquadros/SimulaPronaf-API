package com.wagnerquadros.simulapronaf.usuarios.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nome;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(name = "google_subject", nullable = false, unique = true, length = 100)
    private String googleSubject;

    @Column(nullable = false)
    private Boolean ativo;

    @Column(name = "data_criacao", nullable = false)
    private LocalDateTime dataCriacao;

    public Usuario(String nome, String email, String googleSubject) {
        this.nome = nome;
        this.email = email;
        this.googleSubject = googleSubject;
        this.ativo = true;
        this.dataCriacao = LocalDateTime.now();
    }
}
