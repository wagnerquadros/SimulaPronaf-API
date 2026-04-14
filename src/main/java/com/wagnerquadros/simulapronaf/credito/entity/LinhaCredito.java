package com.wagnerquadros.simulapronaf.credito.entity;

import com.wagnerquadros.simulapronaf.credito.enums.TipoLinhaCredito;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "linha_credito")
public class LinhaCredito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private TipoLinhaCredito tipo;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Column(nullable = false)
    private Boolean ativo;

    @Column(name = "data_criacao", nullable = false)
    private LocalDateTime dataCriacao;

    @OneToMany(mappedBy = "linhaCredito", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ItemLinhaCredito> itens = new ArrayList<>();
}