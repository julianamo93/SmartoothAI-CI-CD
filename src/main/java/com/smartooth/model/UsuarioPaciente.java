package com.smartooth.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_usuario_paciente")
public class UsuarioPaciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "paciente_id")
    private Long usuarioPacienteId;

    private String nome;
    private String sobrenome;
    private String email;

    @Temporal(TemporalType.DATE)
    @Column(name = "data_nasc")
    private Date dataNasc;

    private String genero;
    private String cep;
    private String logradouro;

    @Column(name = "numero") // é um CHAR(8 CHAR) no banco
    private String numero;

    private String complemento;
    private String bairro;
    private String cidade;

    @Column(name = "uf") // é um CHAR(2 CHAR) no banco
    private String uf;

    private String contato;

    @Column(name = "descontos")
    private Double descontos;

    @Column(name = "senha", nullable = false)
    @JsonProperty("senha")
    private String senha;

}




