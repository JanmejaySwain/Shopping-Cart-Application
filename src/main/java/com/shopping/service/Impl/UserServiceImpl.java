package com.shopping.service.Impl;

import com.shopping.entity.User;
import com.shopping.repository.UserRepository;
import com.shopping.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Override
    public Object createUser(User user) {
            if(userRepository.existsByUsername(user.getUsername())){
                throw new RuntimeException("username already taken");
            }
            if(userRepository.existsByEmail(user.getEmail())){
               throw new RuntimeException("This email is already registered with another user");
            }
            userRepository.save(user);
        return user;
    }
}
