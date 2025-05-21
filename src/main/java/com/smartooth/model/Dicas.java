package com.smartooth.model;
import jakarta.persistence.*;
import lombok.Data;
@Data
@Entity
@Table(name = "tb_dicas")
public class Dicas {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dica_id")
    private Long dicaId;
    @Column(name = "descricao", length = 255)
    private String descricao;
    @ManyToOne
    @JoinColumn(name = "prontuario_id", nullable = false)
    private Prontuario prontuario;
    @ManyToOne
    @JoinColumn(name = "usuario_paciente_id", nullable = false)
    private UsuarioPaciente usuarioPaciente;
}