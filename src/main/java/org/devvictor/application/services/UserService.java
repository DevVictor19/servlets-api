package org.devvictor.application.services;

import org.devvictor.application.exceptions.BadRequestException;
import org.devvictor.application.exceptions.InternalServerErrorException;
import org.devvictor.application.exceptions.NotFoundException;
import org.devvictor.domain.daos.UserDAO;
import org.devvictor.domain.dtos.CreateUserRequestDTO;
import org.devvictor.domain.dtos.FindUserResponseDTO;
import org.devvictor.domain.dtos.UpdateUserRequestDTO;
import org.devvictor.domain.entities.User;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class UserService {
    private final UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public void create(CreateUserRequestDTO dto) {
        var future1 = CompletableFuture.supplyAsync(() -> userDAO.findByEmail(dto.email()));
        var future2 = CompletableFuture.supplyAsync(() -> userDAO.findByUsername(dto.username()));

        var futures = CompletableFuture.allOf(future1, future2);

        try {
            futures.join();

            Optional<User> userWithSameEmail = future1.join();

            if (userWithSameEmail.isPresent()) {
                throw new BadRequestException("Email already in use");
            }

            Optional<User> userWithSameUsername = future2.join();

            if (userWithSameUsername.isPresent()) {
                throw new BadRequestException("Username already in use");
            }

            var entity = new User();
            entity.setEmail(dto.email());
            entity.setUsername(dto.username());
            entity.setPassword(dto.password());

            userDAO.save(entity);
        } catch (CompletionException exception) {
            throw new InternalServerErrorException("Cannot search user form email and username");
        }
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
