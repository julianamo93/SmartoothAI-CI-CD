package com.smartooth.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;

@Data
@Entity
@Table(name = "tb_profissional")
public class Profissional {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profissional_id")
    private Long profissionalId;
    private String nome;
    private String especialidade;
    private String experiencia;
    private String contato;

    @Temporal(TemporalType.DATE)
    @Column(name = "data_registro")
    private Date dataRegistro;

}

