package com.thoughtworks.rslist.api;

import com.google.common.collect.Lists;
import com.thoughtworks.rslist.domain.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
public class UserController {

    private List<User> userList = Lists.newArrayList();

    @PostMapping("/user")
    public ResponseEntity addUser(@RequestBody @Valid User user) {
        userList.add(user);
        return ResponseEntity.created(null).header("index", String.valueOf(userList.size() - 1)).build();
    }

    @GetMapping("/user")
    public ResponseEntity getUserList() {
        return ResponseEntity.ok(userList);
    }

    @GetMapping("/users")
    public ResponseEntity getAllUsers() {
        return ResponseEntity.ok(userList);
    }
}
