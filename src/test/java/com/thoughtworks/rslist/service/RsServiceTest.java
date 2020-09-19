package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.domain.Vote;
import com.thoughtworks.rslist.exception.RsEventNotValidException;
import com.thoughtworks.rslist.po.RsEventPO;
import com.thoughtworks.rslist.po.UserPO;
import com.thoughtworks.rslist.po.VotePO;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class RsServiceTest {

    RsService rsService;

    @Mock
    RsEventRepository rsEventRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    VoteRepository voteRepository;

    Date localDataTime;

    @BeforeEach
    void setUp() {
        initMocks(this);
        rsService = new RsService(rsEventRepository, userRepository, voteRepository);
        localDataTime = new Date();
    }

    @Test
    public void should_vote_succeeded() {
        Vote vote = Vote.builder().rsEventId(1).userId(1)
                .voteNum(3).time(localDataTime).build();
        UserPO userPO = UserPO.builder().id(1).voteNum(5)
                .userName("Mike").gender("male").age(20)
                .phone("13688896832").email("a@b.com").build();
        RsEventPO rsEventPO = RsEventPO.builder().id(1).voteNum(1)
                .keyWord("FirstEvent").eventName("Economy")
                .userPO(userPO).build();

        when(userRepository.findById(anyInt())).thenReturn(Optional.of(userPO));
        when(rsEventRepository.findById(anyInt())).thenReturn(Optional.of(rsEventPO));

        rsService.vote(vote, 1);

        verify(userRepository).save(userPO);
        verify(rsEventRepository).save(rsEventPO);
        verify(voteRepository).save(VotePO.builder()
                            .localDateTime(localDataTime)
                            .rsEvent(rsEventPO)
                            .user(userPO)
                            .num(3)
                            .build());

        assertEquals(userPO.getVoteNum(), 2);
        assertEquals(rsEventPO.getVoteNum(), 4);
    }

    @Test
    public void should_throw_exception_when_vote_num_not_valid() {
        Vote vote = Vote.builder().rsEventId(1).userId(1)
                .voteNum(3).time(localDataTime).build();
        UserPO userPO = UserPO.builder().id(1).voteNum(2)
                .userName("Mike").gender("male").age(20)
                .phone("13688896832").email("a@b.com").build();
        RsEventPO rsEventPO = RsEventPO.builder().id(1).voteNum(1)
                .keyWord("FirstEvent").eventName("Economy")
                .userPO(userPO).build();

        when(userRepository.findById(anyInt())).thenReturn(Optional.of(userPO));
        when(rsEventRepository.findById(anyInt())).thenReturn(Optional.of(rsEventPO));

        assertThrows(RsEventNotValidException.class, () -> {
            rsService.vote(vote, 1);
        });
    }
}
