package com.hossein.spring_project.ui.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

@Repository
public class UserDataAccessService implements UserDAO {
    private static List<User> users;

	static{
		users = new ArrayList<>();
		User hossein = new User(
			1,
			"hossein",
			"hossein@khk.com",
			19
		);
		users.add(hossein);
		User mike = new User(
			2,
			"mike",
			"mike@khk.com",
			21
		);
		users.add(mike);
	}

    @Override
    public List<User> selectAllUsers() {
        return users;
    }

    @Override
    public Optional<User> selectUserById(Integer userId){
        return users.stream()
		.filter(u -> u.getId().equals(userId))
		.findFirst();
    }
    
}
