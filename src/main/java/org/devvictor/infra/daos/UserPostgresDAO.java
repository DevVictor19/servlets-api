package org.devvictor.infra.daos;

import org.devvictor.application.exceptions.BadRequestException;
import org.devvictor.application.exceptions.InternalServerErrorException;
import org.devvictor.domain.daos.UserDAO;
import org.devvictor.domain.entities.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserPostgresDAO implements UserDAO {

    private Connection getConnection() throws SQLException {
        final String url = "jdbc:postgresql://servlet-crud-database:5432/servlet_api";
        final String user = "admin";
        final String password = "admin";

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("PostgreSQL JDBC Driver not found!", e);
        }

        return DriverManager.getConnection(url, user, password);
    }

    @Override
    public void save(User entity) {
        try (Connection connection = getConnection()) {
            String query = "INSERT INTO users (username, email, password) VALUES (?, ?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, entity.getUsername());
            preparedStatement.setString(2, entity.getEmail());
            preparedStatement.setString(3, entity.getPassword());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new InternalServerErrorException("Cannot insert new user");
        }
    }

    @Override
    public List<User> find(int page, int itemsPerPage) {
        if (page < 0) throw new BadRequestException("The page must be a positive value");

        if (itemsPerPage < 1 || itemsPerPage > 100) {
            throw new BadRequestException("Items per page must be in between 1 and 100");
        }

        try (Connection connection = getConnection()) {
            String query = "SELECT * FROM users ORDER BY id LIMIT ? OFFSET ?;";

            int offset = (page - 1) * itemsPerPage;

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, itemsPerPage);
            preparedStatement.setInt(2, offset);

            ResultSet resultSet = preparedStatement.executeQuery();

            List<User> users = new ArrayList<>();

            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt("id"));
                user.setUsername(resultSet.getString("username"));
                user.setEmail(resultSet.getString("email"));
                user.setPassword(resultSet.getString("password"));
                users.add(user);
            }

            return users;
        } catch (SQLException e) {
            throw new InternalServerErrorException("Cannot find users");
        }
    }

    @Override
    public Optional<User> findById(long id) {
        try (Connection connection = getConnection()) {
            String query = "SELECT * FROM users WHERE id = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong("id"));
                user.setUsername(resultSet.getString("username"));
                user.setEmail(resultSet.getString("email"));
                user.setPassword(resultSet.getString("password"));
                return Optional.of(user);
            } else {
                return Optional.empty();
            }

        } catch (SQLException e) {
            throw new InternalServerErrorException("Cannot find user by id");
        }
    }

    @Override
    public Optional<User> findByEmail(String email) {
        try (Connection connection = getConnection()) {
            String query = "SELECT * FROM users WHERE email = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong("id"));
                user.setUsername(resultSet.getString("username"));
                user.setEmail(resultSet.getString("email"));
                user.setPassword(resultSet.getString("password"));
                return Optional.of(user);
            } else {
                return Optional.empty();
            }

        } catch (SQLException e) {
            throw new InternalServerErrorException("Cannot find user by email");
        }
    }

    @Override
    public Optional<User> findByUsername(String username) {
        try (Connection connection = getConnection()) {
            String query = "SELECT * FROM users WHERE username = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong("id"));
                user.setUsername(resultSet.getString("username"));
                user.setEmail(resultSet.getString("email"));
                user.setPassword(resultSet.getString("password"));
                return Optional.of(user);
            } else {
                return Optional.empty();
            }

        } catch (SQLException e) {
            throw new InternalServerErrorException("Cannot find user by username");
        }
    }

    @Override
    public void update(User entity) {
        try (Connection connection = getConnection()) {
            String query = "UPDATE users SET username = ?, email = ?, password = ? WHERE id = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, entity.getUsername());
            preparedStatement.setString(2, entity.getEmail());
            preparedStatement.setString(3, entity.getPassword());
            preparedStatement.setLong(4, entity.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new InternalServerErrorException("Cannot update user");
        }
    }

    @Override
    public void delete(User entity) {
        try (Connection connection = getConnection()) {
            String query = "DELETE FROM users WHERE id = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setLong(1, entity.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new InternalServerErrorException("Cannot delete user");
        }
    }
}
