package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.po.UserPO;
import com.thoughtworks.rslist.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.Optional;

@RestController
public class UserController {

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
        Optional<UserPO> userPO = userService.findById(id);
        return ResponseEntity.ok(userPO.get());
    }

    @DeleteMapping("/user/{id}")
    @Transactional
    public ResponseEntity deleteUser(@PathVariable int id) {
        return userService.deleteById(id);
    }
}
