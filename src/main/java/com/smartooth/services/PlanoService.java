package com.smartooth.services;

import com.smartooth.exceptions.ResourceNotFoundException;
import com.smartooth.model.UsuarioPaciente;
import com.smartooth.repository.PlanoRepository;
import com.smartooth.repository.UsuarioPacienteRepository;
import com.smartooth.dto.PlanoDTO;
import com.smartooth.model.Plano;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlanoService {

    private final PlanoRepository planoRepository;
    private final UsuarioPacienteRepository usuarioPacienteRepository;

    public List<PlanoDTO> getAllPlanos() {
        List<Plano> planos = planoRepository.findAll();
        return planos.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ResponseEntity<PlanoDTO> getPlanoById(Long id) {
        Plano plano = planoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Plano não encontrado com o ID: " + id));
        return ResponseEntity.ok(convertToDTO(plano));
    }

    @Transactional
    public ResponseEntity<String> createPlano(PlanoDTO planoDTO) {
        log.info("Criando plano para usuarioPacienteId = {}", planoDTO.getUsuarioPacienteId());

        UsuarioPaciente usuarioPaciente = usuarioPacienteRepository.findById(planoDTO.getUsuarioPacienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuário Paciente não encontrado com o ID: " + planoDTO.getUsuarioPacienteId()));

        Plano plano = convertToEntity(planoDTO, usuarioPaciente);



        System.out.println(plano);
        planoRepository.save(plano);
        return ResponseEntity.status(201).body("Plano criado com sucesso.");
    }

    @Transactional
    public ResponseEntity<String> updatePlano(Long id, PlanoDTO planoDTO) {
        Plano plano = planoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Plano não encontrado com o ID: " + id));

        UsuarioPaciente usuarioPaciente = usuarioPacienteRepository.findById(planoDTO.getUsuarioPacienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuário Paciente não encontrado com o ID: " + planoDTO.getUsuarioPacienteId()));

        plano.setDescricao(planoDTO.getDescricao());
        plano.setTipoPagamento(planoDTO.getTipoPagamento());
        plano.setTipoPlano(planoDTO.getTipoPlano());
        plano.setMarcaPlano(planoDTO.getMarcaPlano());
        plano.setUsuarioPaciente(usuarioPaciente);

        planoRepository.save(plano);
        return ResponseEntity.ok("Plano atualizado com sucesso.");
    }

    @Transactional
    public ResponseEntity<String> deletePlano(Long id) {
        planoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Plano não encontrado com o ID: " + id));

        planoRepository.deleteById(id);
        return ResponseEntity.ok("Plano deletado com sucesso.");
    }

    // Métodos de conversão de DTO para entidade
    private Plano convertToEntity(PlanoDTO dto, UsuarioPaciente usuarioPaciente) {
        Plano entity = new Plano();
        entity.setPlanoId(dto.getPlanoId());
        entity.setDescricao(dto.getDescricao());
        entity.setTipoPagamento(dto.getTipoPagamento());
        entity.setTipoPlano(dto.getTipoPlano());
        entity.setMarcaPlano(dto.getMarcaPlano());
        entity.setUsuarioPaciente(usuarioPaciente);
        return entity;
    }

    private PlanoDTO convertToDTO(Plano entity) {
        PlanoDTO dto = new PlanoDTO();
        dto.setPlanoId(entity.getPlanoId());
        dto.setDescricao(entity.getDescricao());
        dto.setTipoPagamento(entity.getTipoPagamento());
        dto.setTipoPlano(entity.getTipoPlano());
        dto.setMarcaPlano(entity.getMarcaPlano());
        dto.setUsuarioPacienteId(entity.getUsuarioPaciente().getUsuarioPacienteId());
        return dto;
    }
}

