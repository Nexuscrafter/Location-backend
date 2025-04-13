package com.example.locationtrackingbackend.controllers;

import com.example.locationtrackingbackend.DTO.AuthResponse;
import com.example.locationtrackingbackend.DTO.LoginRequest;
import com.example.locationtrackingbackend.Exceptions.UserException;
import com.example.locationtrackingbackend.config.JwtConstant;
import com.example.locationtrackingbackend.config.JwtProvider;
import com.example.locationtrackingbackend.models.User;
import com.example.locationtrackingbackend.repositories.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserDetailsService userDetailsService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUserHandler(
            @RequestBody User user) throws UserException {


        User isEmailExist = userRepository.findByEmail(user.getEmail());

        if (isEmailExist!=null) {

            throw new UserException("User with same Email Already Exist");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        if (user.getRole() == null || user.getRole().isBlank()) {
            user.setRole("USER");
        }


        userRepository.save(user);






        AuthResponse authResponse = new AuthResponse();

        authResponse.setMessage("Register Success");

        return new ResponseEntity<AuthResponse>(authResponse, HttpStatus.OK);

    }

    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> signing(@RequestBody LoginRequest loginRequest) throws UserException, MessagingException {

        String username = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        System.out.println(username + " ----- " + password);

        Authentication authentication = authenticate(username, password);


        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = JwtProvider.generateToken(authentication);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setMessage("Login Success");
        authResponse.setJwt(token);

        return ResponseEntity.ok().body(authResponse);
    }

    private Authentication authenticate(String username, String password) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        System.out.println("sign in userDetails - " + userDetails);

        if (userDetails == null) {

            throw new BadCredentialsException("Invalid username or password");
        }
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            System.out.println("sign in userDetails - password not match " + userDetails);
            throw new BadCredentialsException("Invalid username or password");
        }
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    @GetMapping("/login/google")
    public void redirectToGoogle(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        // Redirect to the Google OAuth2 authorization URI

        response.sendRedirect("/login/oauth2/authorization/google");
    }

    //	/login/oauth2/code/google
    @GetMapping("/login/oauth2/code/google")
    public ResponseEntity<AuthResponse> handleGoogleCallback(@RequestParam(required = false,name = "code") String code,
                                     @RequestParam(required = false,name = "state") String state,
                                     OAuth2AuthenticationToken authentication) {

        // Extract user details from the authentication object or access token
        String email = authentication.getPrincipal().getAttribute("email");
        String fullName = authentication.getPrincipal().getAttribute("name");
        // You can extract more details as needed
        String password = JwtConstant.SECRET_KEY;


        if(userRepository.findByEmail(email)!=null) {
            throw new UserException("User with same Email Already Exist");
        }
        User user=new User();
        user.setEmail(email);
        user.setFullName(fullName);
        user.setPassword(passwordEncoder.encode(password));

        userRepository.save(user);

        Authentication authenticationToken = authenticate(email, password);

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        String token = JwtProvider.generateToken(authenticationToken);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setMessage("Login Success");
        authResponse.setJwt(token);

        return ResponseEntity.ok().body(authResponse);


    }




}
