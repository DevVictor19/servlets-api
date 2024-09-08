package org.devvictor.domain.dtos;

public record UpdateUserRequestDTO(
        String username,
        String email,
        String password
) {}
