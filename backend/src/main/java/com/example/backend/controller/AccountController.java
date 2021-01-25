package com.example.backend.controller;

import java.util.Optional;

import javax.validation.Valid;

import com.example.backend.dao.UserDao;
import com.example.backend.model.BasicResponse;
import com.example.backend.model.user.SignupRequest;
import com.example.backend.model.user.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@ApiResponses(value = { @ApiResponse(code = 401, message = "Unauthorized", response = BasicResponse.class),
        @ApiResponse(code = 403, message = "Forbidden", response = BasicResponse.class),
        @ApiResponse(code = 404, message = "Not Found", response = BasicResponse.class),
        @ApiResponse(code = 500, message = "Failure", response = BasicResponse.class) })

@CrossOrigin(origins = { "*" })

@RestController
public class AccountController {

        @Autowired
        UserDao userDao;

        @GetMapping("/account/login")
        @ApiOperation(value="로그인")
        public Object login(@RequestParam(required = true) final String email,
                @RequestParam(required = true) final String password) {

                Optional<User> userOpt = userDao.findUserByEmailAndPassword(email, password);

                ResponseEntity<Object> response = null;
                
                if (userOpt.isPresent()) {
                        final BasicResponse result = new BasicResponse();
                        result.status = true;
                        result.data = "success";
                        response = new ResponseEntity<>(result, HttpStatus.OK);
                } else {
                        response = new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
                }

                return response;
        }

        @PostMapping("/account/signup")
        @ApiOperation(value = "가입하기")

        public Object signup(@Valid @RequestBody final SignupRequest request) {

                final User email_check = userDao.getUserByEmail(request.getEmail());
                final User nickname_check = userDao.getUserByNickname(request.getNickname());
                
                ResponseEntity<Object> response = null;
                if(email_check != null) {
                        final BasicResponse result = new BasicResponse();
                        result.status = false;
                        result.data = "이메일 중복";
                        response = new ResponseEntity< >(result, HttpStatus.OK);
                } else if(nickname_check != null) {
                        final BasicResponse result = new BasicResponse();
                        result.status = false;
                        result.data = "닉네임 중복";
                        response = new ResponseEntity< >(result, HttpStatus.OK);
                } else {
                        final User user = new User();
                        user.setEmail(request.getEmail());
                        user.setPassword(request.getPassword());
                        user.setNickname(request.getNickname());
                        user.setPhone(request.getPhone());

                }
        }
}