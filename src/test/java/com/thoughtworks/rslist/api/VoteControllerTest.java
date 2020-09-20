package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.po.RsEventPO;
import com.thoughtworks.rslist.po.UserPO;
import com.thoughtworks.rslist.po.VotePO;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class VoteControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RsEventRepository rsEventRepository;

    @Autowired
    VoteRepository voteRepository;

    UserPO userPO;

    RsEventPO rsEventPO;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        rsEventRepository.deleteAll();
        voteRepository.deleteAll();
        userPO = userRepository.save(UserPO.builder().userName("Mike").age(20).phone("13386688553")
                .email("mike@thoughtworks.com").gender("male").voteNum(20).build());
        rsEventPO = rsEventRepository.save(RsEventPO.builder().eventName("ThirdEvent").keyWord("Entertainment")
                .voteNum(0).userPO(userPO).build());
    }

    @Test
    public void should_get_vote_record() throws Exception {
        for (int i = 0; i < 8; i++) {
            VotePO votePO = VotePO.builder().user(userPO).rsEvent(rsEventPO)
                    .localDateTime(new Date())
                    .num(i + 1)
                    .build();
            voteRepository.save(votePO);
        }

        mockMvc.perform(get("/voteRecord")
                .param("userId", String.valueOf(userPO.getId()))
                .param("rsEventId", String.valueOf(rsEventPO.getId()))
                .param("pageIndex", "1"))
                .andExpect(jsonPath("$", hasSize(5)))
                .andExpect(jsonPath("$[0].userId", is(userPO.getId())))
                .andExpect(jsonPath("$[0].rsEventId", is(rsEventPO.getId())))
                .andExpect(jsonPath("$[0].voteNum", is(1)))
                .andExpect(jsonPath("$[1].voteNum", is(2)))
                .andExpect(jsonPath("$[2].voteNum", is(3)))
                .andExpect(jsonPath("$[3].voteNum", is(4)))
                .andExpect(jsonPath("$[4].voteNum", is(5)));
    }

    @Test
    public void should_get_vote_record_within_time() throws Exception {
        for (int i = 0; i < 8; i++) {
            VotePO votePO = VotePO.builder().user(userPO).rsEvent(rsEventPO)
                    .localDateTime(new Date())
                    .num(i + 1)
                    .build();
            voteRepository.save(votePO);
        }

        mockMvc.perform(get("/voteRecords")
                .param("startTimeString", "2020-09-18")
                .param("endTimeString", "2020-09-21"))
                .andExpect(jsonPath("$", hasSize(8)))
                .andExpect(jsonPath("$[0].userId", is(userPO.getId())))
                .andExpect(jsonPath("$[0].rsEventId", is(rsEventPO.getId())))
                .andExpect(jsonPath("$[0].voteNum", is(1)))
                .andExpect(jsonPath("$[1].voteNum", is(2)))
                .andExpect(jsonPath("$[2].voteNum", is(3)))
                .andExpect(jsonPath("$[3].voteNum", is(4)))
                .andExpect(jsonPath("$[4].voteNum", is(5)))
                .andExpect(jsonPath("$[5].voteNum", is(6)))
                .andExpect(jsonPath("$[6].voteNum", is(7)))
                .andExpect(jsonPath("$[7].voteNum", is(8)));
    }

    @Test
    public void should_not_get_vote_record_without_time() throws Exception {
        for (int i = 0; i < 8; i++) {
            VotePO votePO = VotePO.builder().user(userPO).rsEvent(rsEventPO)
                    .localDateTime(new Date())
                    .num(i + 1)
                    .build();
            voteRepository.save(votePO);
        }

        mockMvc.perform(get("/voteRecords")
                .param("startTimeString", "2020-09-21")
                .param("endTimeString", "2020-09-23"))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void should_get_part_vote_record_within_time() throws Exception {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date time = simpleDateFormat.parse("2020-09-10");
        for (int i = 0; i < 3; i++) {
            VotePO votePO = VotePO.builder().user(userPO).rsEvent(rsEventPO)
                    .localDateTime(time)
                    .num(i + 1)
                    .build();
            voteRepository.save(votePO);
        }
        for (int i = 0; i < 8; i++) {
            VotePO votePO = VotePO.builder().user(userPO).rsEvent(rsEventPO)
                    .localDateTime(new Date())
                    .num(i + 1)
                    .build();
            voteRepository.save(votePO);
        }

        mockMvc.perform(get("/voteRecords")
                .param("startTimeString", "2020-09-18")
                .param("endTimeString", "2020-09-21"))
                .andExpect(jsonPath("$", hasSize(8)))
                .andExpect(jsonPath("$[0].userId", is(userPO.getId())))
                .andExpect(jsonPath("$[0].rsEventId", is(rsEventPO.getId())))
                .andExpect(jsonPath("$[0].voteNum", is(1)))
                .andExpect(jsonPath("$[1].voteNum", is(2)))
                .andExpect(jsonPath("$[2].voteNum", is(3)))
                .andExpect(jsonPath("$[3].voteNum", is(4)))
                .andExpect(jsonPath("$[4].voteNum", is(5)))
                .andExpect(jsonPath("$[5].voteNum", is(6)))
                .andExpect(jsonPath("$[6].voteNum", is(7)))
                .andExpect(jsonPath("$[7].voteNum", is(8)));
    }
}
