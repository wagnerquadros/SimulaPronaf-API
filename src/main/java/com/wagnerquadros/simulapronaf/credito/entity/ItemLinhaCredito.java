package com.wagnerquadros.simulapronaf.credito.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "item_linha_credito")
public class ItemLinhaCredito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "linha_credito_id", nullable = false)
    private LinhaCredito linhaCredito;

    @Column(nullable = false, unique = true, length = 100)
    private String codigo;

    @Column(nullable = false, length = 150)
    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String resumo;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Column(columnDefinition = "TEXT")
    private String publico;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal limite;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal juros;

    @Column(name = "prazo_maximo", nullable = false)
    private Integer prazoMaximo;

    @Column(name = "carencia_maxima", nullable = false)
    private Integer carenciaMaxima;

    @Column(name = "ordem_exibicao", nullable = false)
    private Integer ordemExibicao;

    @Column(length = 100)
    private String icone;

    @Column(nullable = false)
    private Boolean ativo;

    @Column(name = "data_criacao", nullable = false)
    private LocalDateTime dataCriacao;
}