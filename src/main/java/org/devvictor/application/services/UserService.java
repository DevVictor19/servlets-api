package org.devvictor.application.services;

import org.devvictor.application.exceptions.BadRequestException;
import org.devvictor.application.exceptions.NotFoundException;
import org.devvictor.domain.daos.UserDAO;
import org.devvictor.domain.dtos.CreateUserRequestDTO;
import org.devvictor.domain.dtos.FindUserResponseDTO;
import org.devvictor.domain.dtos.UpdateUserRequestDTO;
import org.devvictor.domain.entities.User;

import java.util.List;

public class UserService {
    private final UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public void create(CreateUserRequestDTO dto) {
        var userWithSameEmail = userDAO.findByEmail(dto.email());

        if (userWithSameEmail.isPresent()) {
            throw new BadRequestException("Email already in use");
        }

        var userWithSameUsername = userDAO.findByUsername(dto.username());

        if (userWithSameUsername.isPresent()) {
            throw new BadRequestException("Username already in use");
        }

        var entity = new User();
        entity.setEmail(dto.email());
        entity.setUsername(dto.username());
        entity.setPassword(dto.password());

        userDAO.save(entity);
    }

    public List<FindUserResponseDTO> find(int page, int itemsPerPage) {
        return userDAO.find(page, itemsPerPage)
                .stream()
                .map(e -> new FindUserResponseDTO(
                        e.getId(),
                        e.getUsername(),
                        e.getEmail()))
                .toList();
    }

    public void update(long id, UpdateUserRequestDTO dto) {
        User user = userDAO.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));

        user.setUsername(dto.username());
        user.setEmail(dto.email());
        user.setPassword(dto.password());

        userDAO.update(user);
    }

    public void delete(long id) {
        User user = userDAO.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));

        userDAO.delete(user);
    }
}
