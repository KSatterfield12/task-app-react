package com.kami.tasklistserver.dao;

import com.kami.tasklistserver.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDaoImpl implements UserDao {

    @PersistenceContext
    public EntityManager entityManager;

    @Override
    public List<User> getUsers() {
        return entityManager.createQuery("SELECT u FROM User u", User.class).getResultList();
    }

    @Override
    public User getUserById(Long userId) {
        return entityManager.find(User.class, userId);
    }

    @Override
    public User getUserByUsername(String username) {
        return entityManager.createQuery(
                        "SELECT u FROM User u WHERE u.username = :username", User.class)
                .setParameter("username", username)
                .getSingleResult();
    }

    @Override
    public User createUser(String username, String password, String role) {
        User newUser = new User(username, password, role);
        entityManager.persist(newUser);
        return newUser;
    }

    @Override
    public User updateUser(User modifiedUser) {
        return entityManager.merge(modifiedUser);
    }
}