package com.hossein.spring_project.ui.controllers;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hossein.spring_project.exception.ResourceNotFound;

@Service
public class UserService {
    private final UserDAO userDao;

    public UserService(UserDAO userDao) {
        this.userDao = userDao;
    }

    public List<User> getAllUsers(){
        return userDao.selectAllUsers();
    }

    public User getUserById(Integer id){
        return userDao.selectUserById(id)
            .orElseThrow(() -> new ResourceNotFound(
                "user with the id: [%s] was not found!".formatted(id)));
    }
}
