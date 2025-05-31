package com.devsuperior.desafio3.mapper;

import com.devsuperior.desafio3.dto.ClientDTO;
import com.devsuperior.desafio3.entities.Client;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ClientConverter {

    ClientConverter CONVERTER = Mappers.getMapper(ClientConverter.class);

    ClientDTO toDTO(Client client);

    Client toEntity(ClientDTO clientDTO);
}
