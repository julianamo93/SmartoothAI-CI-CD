package com.smartooth.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "tb_sistema_pontos")
public class SistemaPontos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sistema_pontos_id")
    private Long sistemaPontosId;

    private Double totalPontos;
    private String tipoPontos;

    @ManyToOne
    @JoinColumn(name = "plano_id")
    private Plano plano;
}
