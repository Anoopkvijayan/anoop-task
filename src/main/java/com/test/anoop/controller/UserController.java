package com.test.anoop.controller;


import com.test.anoop.dto.*;
import com.test.anoop.entity.UserInfo;
import com.test.anoop.service.JwtService;
import com.test.anoop.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;
    @Autowired
    AuthenticationManager authenticationManager;

    @PostMapping("/addUser")
    public ResponseEntity<Object> registerUser(@Valid @RequestBody UserInfo userInfo) {

        RegResponse res = new RegResponse();
        ResponseEntity.BodyBuilder ok = ResponseEntity.ok();
        ResponseEntity<RegResponse> body;
        try {
            userService.registerUser(userInfo, res);
            return ok.body(res);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginRequest loginRequest) throws Exception {
        long queryStartTime = System.currentTimeMillis();
        LoginResponse res = new LoginResponse();
        ResponseEntity.BodyBuilder ok = ResponseEntity.ok();
        ResponseEntity<LoginResponse> body;
        int ret = 400;
        ret = userService.login(loginRequest, res);
        return ok.body(res);


    }

    @GetMapping("/getUsers")
    public ResponseEntity<Map<String, Object>> getUserString() {
        List<UserInfo> users = userService.getUsers();
        List<UsersList> userDTOs = users.stream()
                .map(user -> new UsersList(
                        user.getId(),
                        user.getName(),
                        user.getEmail(),
                        user.getRoles(),
                        user.getGender()))
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Users list");
        response.put("users", userDTOs);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delUser/{email}")
    public ResponseEntity<Object> delUser(@PathVariable String email, Principal principal) {
        DelUserRes res = new DelUserRes();
        ResponseEntity.BodyBuilder ok = ResponseEntity.ok();
        ResponseEntity<DelUserRes> body;
        res.setMessage(userService.delUser(email, res, principal));
        return ok.body(res);

    }


}
