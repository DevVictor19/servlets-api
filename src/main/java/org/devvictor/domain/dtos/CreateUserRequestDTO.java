package org.devvictor.domain.dtos;

public record CreateUserRequestDTO(
        String username,
        String email,
        String password
) {
}
