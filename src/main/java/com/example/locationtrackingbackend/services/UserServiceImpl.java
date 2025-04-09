package com.example.locationtrackingbackend.services;

import com.example.locationtrackingbackend.Exceptions.UserException;
import com.example.locationtrackingbackend.config.JwtProvider;
import com.example.locationtrackingbackend.models.User;
import com.example.locationtrackingbackend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User findUserProfileByJwt(String jwt) throws UserException {
        String email= JwtProvider.getEmailFromJwtToken(jwt);


        User user = userRepository.findByEmail(email);

        if(user==null) {
            throw new UserException("user not exist with email "+email);
        }
        return user;
    }
}
