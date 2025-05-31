package com.devsuperior.desafio3.services;

import com.devsuperior.desafio3.dto.ClientDTO;
import com.devsuperior.desafio3.entities.Client;
import com.devsuperior.desafio3.mapper.ClientConverter;
import com.devsuperior.desafio3.repositories.ClientRepository;
import com.devsuperior.desafio3.services.exceptions.DatabaseException;
import com.devsuperior.desafio3.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClientService {

    @Autowired
    private ClientRepository repository;

    @Transactional(readOnly = true)
    public Page<ClientDTO> findAll(Pageable pageable){
        Page<Client> result = repository.findAll(pageable);
        return result.map(ClientConverter.CONVERTER::toDTO);
    }

    @Transactional(readOnly = true)
    public ClientDTO findById(Long id){
        Client entity = repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(id));
        return ClientConverter.CONVERTER.toDTO(entity);
    }

    @Transactional
    public ClientDTO insert(ClientDTO dto){
        Client entity = ClientConverter.CONVERTER.toEntity(dto);
        return ClientConverter.CONVERTER.toDTO(repository.save(entity));
    }
    @Transactional
    public ClientDTO update(Long id, ClientDTO dto){
        try {
        Client entity = repository.getReferenceById(id);
        return dataUpdate(entity,dto);
        } catch (EntityNotFoundException e){
            throw  new ResourceNotFoundException(id);
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id){
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException(id);
        }
        try {
        repository.deleteById(id);
        } catch (DataIntegrityViolationException e){
            throw new DatabaseException(e.getMessage());
        }
    }

    private ClientDTO dataUpdate(Client entity, ClientDTO dto){
        entity.setName(dto.getName() != null ? dto.getName() : entity.getName());
        entity.setCpf(dto.getCpf() != null ? dto.getCpf() : entity.getCpf());
        entity.setIncome(dto.getIncome() != null ? dto.getIncome() : entity.getIncome());
        entity.setBirthDate(dto.getBirthDate() != null ? dto.getBirthDate() : entity.getBirthDate());
        entity.setChildren(dto.getChildren() != null ? dto.getChildren() : entity.getChildren());
        return ClientConverter.CONVERTER.toDTO(entity);
    }
}
