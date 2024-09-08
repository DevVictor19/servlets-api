package org.devvictor.domain.daos;

import org.devvictor.domain.entities.User;

import java.util.List;
import java.util.Optional;

public interface UserDAO {
    void save(User entity);
    List<User> find(int page, int itemsPerPage);
    Optional<User> findById(long id);
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    void update (User entity);
    void delete (User entity);
}
