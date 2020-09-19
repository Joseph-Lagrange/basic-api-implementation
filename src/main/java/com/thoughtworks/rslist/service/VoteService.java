package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.domain.Vote;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VoteService {

    final RsEventRepository rsEventRepository;
    final UserRepository userRepository;
    final VoteRepository voteRepository;

    public VoteService(RsEventRepository rsEventRepository, UserRepository userRepository, VoteRepository voteRepository) {
        this.rsEventRepository = rsEventRepository;
        this.userRepository = userRepository;
        this.voteRepository = voteRepository;
    }


    public ResponseEntity<List<Vote>> findAllByUserIdAndRsEventId(int userId, int rsEventId, int pageIndex) {
        Pageable pageable = PageRequest.of(pageIndex - 1, 5);
        return ResponseEntity.ok(
                voteRepository.findAllByUserIdAndRsEventId(userId, rsEventId, pageable).stream().map(
                        item -> Vote.builder().userId(item.getUser().getId())
                                .time(item.getLocalDateTime())
                                .rsEventId(item.getRsEvent().getId())
                                .voteNum(item.getNum()).build()
                ).collect(Collectors.toList()));
    }

    public ResponseEntity<List<Vote>> findAllByTime(Date startTime, Date endTime) {
        return ResponseEntity.ok(
                voteRepository.findAllByTime(startTime, endTime).stream().map(
                        item -> Vote.builder().userId(item.getUser().getId())
                                .time(item.getLocalDateTime())
                                .rsEventId(item.getRsEvent().getId())
                                .voteNum(item.getNum()).build()
                ).collect(Collectors.toList()));
    }
}
