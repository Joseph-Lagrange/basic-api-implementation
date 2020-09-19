package com.thoughtworks.rslist.api;

import com.google.common.collect.Lists;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.po.UserPO;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RsEventRepository rsEventRepository;

    @Autowired
    UserService userService;

    @PostMapping("/user")
    public ResponseEntity register(@RequestBody @Valid User user) {
        return userService.register(user);
    }

    @GetMapping("/users")
    public ResponseEntity getUsers() {
        return userService.findAll();
    }

    @GetMapping("/user/{id}")
    public ResponseEntity getOneUser(@PathVariable int id) {
        return userService.findById(id);
    }

    @DeleteMapping("/user/{id}")
    @Transactional
    public ResponseEntity deleteUser(@PathVariable int id) {
        return userService.deleteById(id);
    }
}
