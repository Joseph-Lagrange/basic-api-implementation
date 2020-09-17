package com.thoughtworks.rslist.api;

import com.google.common.collect.Lists;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.exception.RsEventNotValidException;
import com.thoughtworks.rslist.po.UserPO;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
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

    public static List<User> users = Lists.newArrayList();

    @PostMapping("/user")
    public void register(@RequestBody @Valid User user) {
        UserPO userPO = new UserPO();
        userPO.setUserName(user.getUserName());
        userPO.setGender(user.getGender());
        userPO.setAge(user.getAge());
        userPO.setEmail(user.getEmail());
        userPO.setPhone(user.getPhone());
        userPO.setVoteNum(user.getVoteNum());
        userRepository.save(userPO);
    }

    @GetMapping("/user")
    public ResponseEntity getUser() {
        return ResponseEntity.ok(users);
    }

    @GetMapping("/users")
    public ResponseEntity getUsers() {
        return ResponseEntity.ok(users);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity getOneUser(@PathVariable int id) {
        Optional<UserPO> userPO = userRepository.findById(id);
        return ResponseEntity.ok(userPO.get());
    }

    @DeleteMapping("/user/{id}")
    @Transactional
    public ResponseEntity deleteUser(@PathVariable int id) {
        userRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
