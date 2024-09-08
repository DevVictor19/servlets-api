package org.devvictor.application.services;

import org.devvictor.application.exceptions.NotFoundException;
import org.devvictor.domain.daos.UserDAO;
import org.devvictor.domain.dtos.CreateUserRequestDTO;
import org.devvictor.domain.dtos.FindUserResponseDTO;
import org.devvictor.domain.dtos.UpdateUserRequestDTO;
import org.devvictor.domain.entities.User;

import java.util.List;
import java.util.Optional;

public class UserService {
    private final UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    private User findById(long id) {
        Optional<User> result = userDAO.findById(id);

        if (result.isEmpty()) {
            throw new NotFoundException("User not found");
        }

        return result.get();
    }

    public void create(CreateUserRequestDTO dto) {
        var entity = new User();
        entity.setEmail(dto.email());
        entity.setUsername(dto.username());
        entity.setPassword(dto.password());

        userDAO.save(entity);
    }

    public List<FindUserResponseDTO> find() {
        return userDAO.find()
                .stream()
                .map(e -> new FindUserResponseDTO(
                        e.getId(),
                        e.getUsername(),
                        e.getEmail()))
                .toList();
    }

    public User findByEmail(String email) {
        Optional<User> result = userDAO.findByEmail(email);

        if (result.isEmpty()) {
            throw new NotFoundException("User not found");
        }

        return result.get();
    }

    public void update(long id, UpdateUserRequestDTO dto) {
        User user = findById(id);
        user.setUsername(dto.username());
        user.setEmail(dto.email());
        user.setPassword(dto.password());

        userDAO.update(user);
    }

    public void delete(long id) {
        User user = findById(id);

        userDAO.delete(user);
    }
}
