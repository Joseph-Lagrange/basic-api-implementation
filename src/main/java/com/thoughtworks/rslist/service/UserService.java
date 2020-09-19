package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.po.UserPO;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    final RsEventRepository rsEventRepository;
    final UserRepository userRepository;
    final VoteRepository voteRepository;

    public UserService(RsEventRepository rsEventRepository, UserRepository userRepository, VoteRepository voteRepository) {
        this.rsEventRepository = rsEventRepository;
        this.userRepository = userRepository;
        this.voteRepository = voteRepository;
    }

    public ResponseEntity register(User user) {
        UserPO userPO = new UserPO();
        userPO.setUserName(user.getUserName());
        userPO.setGender(user.getGender());
        userPO.setAge(user.getAge());
        userPO.setEmail(user.getEmail());
        userPO.setPhone(user.getPhone());
        userPO.setVoteNum(user.getVoteNum());
        userRepository.save(userPO);
        return ResponseEntity.created(null).build();
    }

    public ResponseEntity findAll() {
        List<UserPO> userPOs = userRepository.findAll();
        return ResponseEntity.ok(userPOs);
    }

    public ResponseEntity deleteById(int id) {
        userRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity findById(int id) {
        Optional<UserPO> userPO = userRepository.findById(id);
        return ResponseEntity.ok(userPO.get());
    }
}
