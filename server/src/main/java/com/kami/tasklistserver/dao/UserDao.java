package com.kami.tasklistserver.dao;

import com.kami.tasklistserver.model.User;
import java.util.List;


public interface UserDao {


    List<User> getUsers();

    User getUserById(Long userId);

    User getUserByUsername(String username);

    User createUser(String username, String password, String role);

    User updateUser(User modifiedUser);
}