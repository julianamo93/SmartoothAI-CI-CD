package com.smartooth.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_prontuario")
public class Prontuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prontuario_id")
    private Long prontuarioId;

    @Column(name = "prescricao", length = 255)
    private String prescricao;

    @Column(name = "observacoes", length = 255)
    private String observacoes;

    @Temporal(TemporalType.DATE)
    @Column(name = "data_registro")
    private Date dataRegistro;
}

