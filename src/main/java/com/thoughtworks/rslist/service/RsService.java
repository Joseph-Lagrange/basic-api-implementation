package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.Vote;
import com.thoughtworks.rslist.exception.RsEventNotValidException;
import com.thoughtworks.rslist.po.RsEventPO;
import com.thoughtworks.rslist.po.UserPO;
import com.thoughtworks.rslist.po.VotePO;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class RsService {

    final RsEventRepository rsEventRepository;

    final UserRepository userRepository;

    final VoteRepository voteRepository;

    public RsService(RsEventRepository rsEventRepository, UserRepository userRepository, VoteRepository voteRepository) {
        this.rsEventRepository = rsEventRepository;
        this.userRepository = userRepository;
        this.voteRepository = voteRepository;
    }

    public ResponseEntity vote(Vote vote, int rsEventId) {
        Optional<RsEventPO> eventOptional = rsEventRepository.findById(rsEventId);
        Optional<UserPO> userOptional = userRepository.findById(vote.getUserId());
        if (!eventOptional.isPresent() || !userOptional.isPresent()
                || userOptional.get().getVoteNum() < vote.getVoteNum()) {
            throw new RsEventNotValidException("invalid vote num");
        }
        VotePO votePO = VotePO.builder().localDateTime(vote.getTime()).num(vote.getVoteNum())
                .rsEvent(eventOptional.get())
                .user(userOptional.get())
                .build();
        voteRepository.save(votePO);
        UserPO userPO = userOptional.get();
        userPO.setVoteNum(userPO.getVoteNum() - vote.getVoteNum());
        userRepository.save(userPO);
        RsEventPO rsEventPO = eventOptional.get();
        rsEventPO.setVoteNum(rsEventPO.getVoteNum() + vote.getVoteNum());
        rsEventRepository.save(rsEventPO);
        return ResponseEntity.ok().build();
    }

    public List<RsEvent> findAll() {
        return rsEventRepository.findAll().stream()
                .map(item -> RsEvent.builder().eventName(item.getEventName())
                        .keyWord(item.getKeyWord()).userId(item.getId())
                        .voteNum(item.getVoteNum()).build())
                .collect(Collectors.toList());
    }

    public ResponseEntity deleteById(int rsEventId) {
        rsEventRepository.deleteById(rsEventId);
        return ResponseEntity.ok().build();
    }

    public void save(RsEventPO rsEventPO) {
        rsEventRepository.save(rsEventPO);
    }

    public Optional<RsEventPO> findById(int rsEventId) {
        return rsEventRepository.findById(rsEventId);
    }
}
