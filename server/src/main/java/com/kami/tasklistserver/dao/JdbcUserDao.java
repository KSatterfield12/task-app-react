package com.kami.tasklistserver.dao;

import com.kami.tasklistserver.model.User;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcUserDao {

    private final JdbcTemplate jdbcTemplate;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public JdbcUserDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<User> getUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT id, username, role FROM users ORDER BY username;";

        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
            while (results.next()) {
                users.add(mapRowToUser(results, false));
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to database", e);
        }

        return users;
    }

    public User getUserById(Long userId) {
        String sql = "SELECT id, username, password, role FROM users WHERE id = ?;";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
            if (results.next()) {
                return mapRowToUser(results, true);
            }
            return null;
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to database", e);
        }
    }

    public User getUserByUsername(String username) {
        String sql = "SELECT id, username, password, role FROM users WHERE LOWER(TRIM(username)) = LOWER(TRIM(?));";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, username);
            if (results.next()) {
                return mapRowToUser(results, true);
            }
            return null;
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to database", e);
        }
    }

    public User createUser(String username, String password, String role) {
        String hashedPassword = passwordEncoder.encode(password);
        String sql = "INSERT INTO users (username, password, role) VALUES (LOWER(TRIM(?)), ?, LOWER(TRIM(?))) RETURNING id;";
        try {
            Long newId = jdbcTemplate.queryForObject(sql, Long.class, username, hashedPassword, role);
            return getUserById(newId);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Username already exists or data constraint violated", e);
        }
    }

    public User updateUser(User modifiedUser) {
        // Always hash password before saving
        String hashedPassword = passwordEncoder.encode(modifiedUser.getPassword());
        String sql = "UPDATE users SET username = ?, password = ?, role = LOWER(TRIM(?)) WHERE id = ?;";
        try {
            int affected = jdbcTemplate.update(sql,
                    modifiedUser.getUsername(),
                    hashedPassword,
                    modifiedUser.getRole(),
                    modifiedUser.getId());

            if (affected == 0) {
                throw new DaoException("No rows updated. Check if user exists.");
            }
            return getUserById(modifiedUser.getId());
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Database connection failed", e);
        }
    }

    private User mapRowToUser(SqlRowSet rs, boolean includePassword) {
        User user = new User();
        user.setId(rs.getLong("id"));
        user.setUsername(rs.getString("username"));
        user.setRole(rs.getString("role"));
        if (includePassword) {
            user.setPassword(rs.getString("password"));
        }
        return user;
    }
}