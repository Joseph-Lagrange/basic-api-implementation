package com.thoughtworks.rslist;

import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import com.thoughtworks.rslist.service.RsService;
import com.thoughtworks.rslist.service.UserService;
import com.thoughtworks.rslist.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Autowired
    RsEventRepository rsEventRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    VoteRepository voteRepository;

    @Bean
    public RsService rsService() {
        return new RsService(rsEventRepository, userRepository, voteRepository);
    }

    @Bean
    public UserService userService() {
        return new UserService(rsEventRepository, userRepository, voteRepository);
    }

    @Bean
    public VoteService voteService() {
        return new VoteService(rsEventRepository, userRepository, voteRepository);
    }
}
