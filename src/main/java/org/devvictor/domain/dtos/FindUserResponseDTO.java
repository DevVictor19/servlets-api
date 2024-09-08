package org.devvictor.domain.dtos;

public record FindUserResponseDTO(
        long id,
        String username,
        String email
) {}
