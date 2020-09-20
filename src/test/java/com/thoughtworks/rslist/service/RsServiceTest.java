package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.Vote;
import com.thoughtworks.rslist.exception.RsEventNotValidException;
import com.thoughtworks.rslist.po.RsEventPO;
import com.thoughtworks.rslist.po.UserPO;
import com.thoughtworks.rslist.po.VotePO;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.TradeRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
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

    @Mock
    TradeRepository tradeRepository;

    Date localDateTime;

    Vote vote;

    RsEvent rsEvent;

    @BeforeEach
    void setUp() {
        initMocks(this);
        rsService = new RsService(rsEventRepository, userRepository, voteRepository, tradeRepository);
        localDateTime = new Date();
        vote = Vote.builder().rsEventId(1).userId(1)
                .voteNum(3).time(localDateTime).build();
        rsEvent = RsEvent.builder().eventName("ForthEvent").keyWord("Entertainment")
                .voteNum(0).userId(1).build();
    }

    @Test
    public void should_vote_succeeded() {
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
                .localDateTime(localDateTime)
                .rsEvent(rsEventPO)
                .user(userPO)
                .num(3)
                .build());

        assertEquals(userPO.getVoteNum(), 2);
        assertEquals(rsEventPO.getVoteNum(), 4);
    }

    @Test
    public void should_throw_exception_when_vote_num_not_valid() {
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

    @Test
    void should_buy_event_when_rank_num_is_exist() {
        UserPO userDto = UserPO.builder().userName("Mike").age(20).phone("13386688553")
                .email("mike@thoughtworks.com").gender("male").voteNum(20).build();
        RsEventPO rsEventDto = RsEventPO.builder().eventName("FirstEvent").keyWord("Economy")
                .voteNum(10).userPO(userDto).build();

        when(rsEventRepository.findByRankNum(anyInt())).thenReturn(Optional.of(rsEventDto));
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(userDto));

        rsService.buy(100, 1, rsEvent);

        verify(rsEventRepository).save(rsEventDto);

        assertEquals(rsEventDto.getAmount(), 100);
        assertEquals(rsEventDto.getRankNum(), 1);
    }

    @Test
    void should_not_buy_event_when_rank_num_or_user_id_is_not_exist() {
        when(rsEventRepository.findByRankNum(anyInt())).thenReturn(Optional.empty());
        when(userRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            rsService.buy(100, 10, rsEvent);
        });
    }
}
