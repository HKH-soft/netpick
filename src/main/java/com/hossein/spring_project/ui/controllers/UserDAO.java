package com.hossein.spring_project.ui.controllers;

import java.util.List;
import java.util.Optional;

public interface UserDAO {
    List<User> selectAllUsers();
    Optional<User> selectUserById(Integer userId);
}