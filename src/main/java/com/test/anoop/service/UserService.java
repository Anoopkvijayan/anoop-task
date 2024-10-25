package com.test.anoop.service;

import com.test.anoop.dto.DelUserRes;
import com.test.anoop.dto.LoginRequest;
import com.test.anoop.dto.LoginResponse;
import com.test.anoop.dto.RegResponse;
import com.test.anoop.entity.UserInfo;
import com.test.anoop.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepo userRepo;

    @Autowired
    @Lazy
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    public void registerUser(UserInfo userInfo, RegResponse res) throws Exception {

        if (userRepo.findByEmail(userInfo.getEmail()).isPresent() || userRepo.findByName(userInfo.getName()).isPresent()) {
            res.setMessage("Email/Username is already in use");
        } else {
            userInfo.setPassword(passwordEncoder.encode(userInfo.getPassword()));
            res.setId(userRepo.save(userInfo).getId());
            res.setMessage("User registered successfully");

        }

    }

    @Override
    public UserDetails loadUserByUsername(String name) {
        Optional<UserInfo> userInfo = userRepo.findByEmail(name);
        return userInfo.map(UserInfoDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found" + name));
    }

    public int login(LoginRequest loginRequest, LoginResponse res) throws Exception {
        int ret = 0;
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(), loginRequest.getPassword()
        ));
        if (authentication.isAuthenticated()) {
            res.setToken(jwtService.generateToken(loginRequest.getEmail()));
            res.setSuccess(true);
        } else {
            ret = 400;
        }
        return ret;
    }

    public List<UserInfo> getUsers() {
        return userRepo.findAll();
    }

    public String delUser(String email, DelUserRes res, Principal principal) {
        Optional<UserInfo> userInfo = userRepo.findByEmail(email);
        if (userInfo.isPresent()) {
            if (!hasDeletePermission(principal, email)) {
                res.setMessage("You do not have permission to delete this user");
            }
            userRepo.delete(userInfo.get());
            res.setMessage("User removed successfully");
        } else {
            res.setMessage("User not found!");
        }
        return res.getMessage();
    }

    private boolean hasDeletePermission(Principal principal, String email) {
        String loggedInUserEmail = principal.getName();
        return loggedInUserEmail.equals(email);
    }
}
