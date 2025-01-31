package com.abs.SpringSecurityJWT.service;


import com.abs.SpringSecurityJWT.dto.UserReqResDTO;
import com.abs.SpringSecurityJWT.enitty.User;
import com.abs.SpringSecurityJWT.enums.ETAT_USER;
import com.abs.SpringSecurityJWT.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class AuthService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;


    public UserReqResDTO signUp(UserReqResDTO registrationRequest){
        UserReqResDTO resp = new UserReqResDTO();

        try {
            User user = new User();

            user.setPrenom(registrationRequest.getPrenom());
            user.setNom(registrationRequest.getNom());
            user.setTel(registrationRequest.getTel());
            user.setLogin(registrationRequest.getLogin());
            user.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
            user.setRole(registrationRequest.getRole());
            user.setEtat(ETAT_USER.ACTIF.toString());

            User userResult = userRepo.save(user);

            if (userResult != null && userResult.getId() > 0){
                resp.setUsers(userResult);
                resp.setMessage("User Save Successfully");
                resp.setStatusCode(200);
            }

        }catch (Exception e){
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }

        return resp;
    }


    public UserReqResDTO signIn(UserReqResDTO signinRequest){
        UserReqResDTO response = new UserReqResDTO();

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signinRequest.getLogin(), signinRequest.getPassword() ));
            var user = userRepo.findByLogin(signinRequest.getLogin()).orElseThrow();
            System.out.println("USER IS : "+ user);
            var jwt = jwtUtils.generateToken(user);
            var refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), user);
            response.setStatusCode(200);
            response.setToken(jwt);
            response.setRefreshToken(refreshToken);
            response.setExpirationTime("24Hr");
            response.setMessage("Successfully Signed In");
        }catch (Exception e){
            response.setStatusCode(500);
            response.setError(e.getMessage());
        }

        return response;

    }

    public UserReqResDTO refreshToken(UserReqResDTO refreshTokenRequest){
        UserReqResDTO response = new UserReqResDTO();
        String ourEmail = jwtUtils.extractUsername(refreshTokenRequest.getToken());
        User users = userRepo.findByLogin(ourEmail).orElseThrow();

        if (jwtUtils.isTokenValid(refreshTokenRequest.getToken(), users )){
            var jwt = jwtUtils.generateToken(users);
            response.setStatusCode(200);
            response.setToken(jwt);
            response.setRefreshToken(refreshTokenRequest.getToken());
            response.setExpirationTime("24Hr");
            response.setMessage("Successfully Refreshed Token");
        }

        response.setStatusCode(500);
        return response;
    }



}








