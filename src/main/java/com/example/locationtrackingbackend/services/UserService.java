package com.example.locationtrackingbackend.services;

import com.example.locationtrackingbackend.Exceptions.UserException;
import com.example.locationtrackingbackend.models.User;

public interface UserService {

    public User findUserProfileByJwt(String jwt) throws UserException;
}
