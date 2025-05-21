package com.smartooth.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "tb_procedimento")
public class Procedimento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "procedimento_id")
    private Long procedimentoId;

    @Column(name = "nome_procedimento", length = 85, nullable = false)
    private String nomeProcedimento;

    @Column(name = "descricao", length = 255)
    private String descricao;

    @Column(name = "inclusao_plano", length = 1)
    private Character inclusaoPlano;

    @ManyToOne
    @JoinColumn(name = "prontuario_id", nullable = false)
    private Prontuario prontuario;

    @ManyToOne
    @JoinColumn(name = "sistema_pontos_id", nullable = false)
    private SistemaPontos sistemaPontos;

    @ManyToOne
    @JoinColumn(name = "plano_id", nullable = false)
    private Plano plano;

    @ManyToOne
    @JoinColumn(name = "usuario_paciente_id", nullable = false)
    private UsuarioPaciente usuarioPaciente;
}


