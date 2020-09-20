package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.Vote;
import com.thoughtworks.rslist.po.RsEventPO;
import com.thoughtworks.rslist.po.UserPO;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class RsControllerTest {

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

    ObjectMapper objectMapper;

    Date localDataTime;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        rsEventRepository.deleteAll();
        voteRepository.deleteAll();
        userPO = userRepository.save(UserPO.builder().userName("Mike").age(20).phone("13386688553")
                .email("mike@thoughtworks.com").gender("male").voteNum(20).build());
        rsEventRepository.save(RsEventPO.builder().eventName("FirstEvent").keyWord("Economy")
                .voteNum(10).userPO(userPO).rankNum(1).build());
        rsEventRepository.save(RsEventPO.builder().eventName("SecondEvent").keyWord("Politics")
                .voteNum(10).userPO(userPO).rankNum(2).build());
        rsEventPO = rsEventRepository.save(RsEventPO.builder().eventName("ThirdEvent").keyWord("Cultural")
                .voteNum(10).userPO(userPO).rankNum(3).build());
        objectMapper = new ObjectMapper();
        localDataTime = new Date();
    }

    @DirtiesContext
    @Test
    public void should_get_rs_event_list() throws Exception {
        mockMvc.perform(get("/rs/events"))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].eventName", is("FirstEvent")))
                .andExpect(jsonPath("$[0].keyWord", is("Economy")))
                .andExpect(jsonPath("$[0].voteNum", is(10)))
                .andExpect(jsonPath("$[0]", not(hasKey("user"))))
                .andExpect(jsonPath("$[1].eventName", is("SecondEvent")))
                .andExpect(jsonPath("$[1].keyWord", is("Politics")))
                .andExpect(jsonPath("$[1].voteNum", is(10)))
                .andExpect(jsonPath("$[1]", not(hasKey("user"))))
                .andExpect(jsonPath("$[2].eventName", is("ThirdEvent")))
                .andExpect(jsonPath("$[2].keyWord", is("Cultural")))
                .andExpect(jsonPath("$[2].voteNum", is(10)))
                .andExpect(jsonPath("$[2]", not(hasKey("user"))))
                .andExpect(status().isOk());
    }

    @DirtiesContext
    @Test
    public void should_get_one_rs_event() throws Exception {
        mockMvc.perform(get("/rs/1"))
                .andExpect(jsonPath("$.eventName", is("FirstEvent")))
                .andExpect(jsonPath("$.keyWord", is("Economy")))
                .andExpect(jsonPath("$", not(hasKey("user"))))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/2"))
                .andExpect(jsonPath("$.eventName", is("SecondEvent")))
                .andExpect(jsonPath("$.keyWord", is("Politics")))
                .andExpect(jsonPath("$", not(hasKey("user"))))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/3"))
                .andExpect(jsonPath("$.eventName", is("ThirdEvent")))
                .andExpect(jsonPath("$.keyWord", is("Cultural")))
                .andExpect(jsonPath("$", not(hasKey("user"))))
                .andExpect(status().isOk());
    }

    @DirtiesContext
    @Test
    public void should_rs_event_between() throws Exception {
        mockMvc.perform(get("/rs/events?start=1&end=2"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].eventName", is("FirstEvent")))
                .andExpect(jsonPath("$[0].keyWord", is("Economy")))
                .andExpect(jsonPath("$[0]", not(hasKey("user"))))
                .andExpect(jsonPath("$[1].eventName", is("SecondEvent")))
                .andExpect(jsonPath("$[1].keyWord", is("Politics")))
                .andExpect(jsonPath("$[1]", not(hasKey("user"))))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/events?start=2&end=3"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].eventName", is("SecondEvent")))
                .andExpect(jsonPath("$[0].keyWord", is("Politics")))
                .andExpect(jsonPath("$[0]", not(hasKey("user"))))
                .andExpect(jsonPath("$[1].eventName", is("ThirdEvent")))
                .andExpect(jsonPath("$[1].keyWord", is("Cultural")))
                .andExpect(jsonPath("$[1]", not(hasKey("user"))))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/events?start=1&end=3"))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].eventName", is("FirstEvent")))
                .andExpect(jsonPath("$[0].keyWord", is("Economy")))
                .andExpect(jsonPath("$[0]", not(hasKey("user"))))
                .andExpect(jsonPath("$[1].eventName", is("SecondEvent")))
                .andExpect(jsonPath("$[1].keyWord", is("Politics")))
                .andExpect(jsonPath("$[1]", not(hasKey("user"))))
                .andExpect(jsonPath("$[2].eventName", is("ThirdEvent")))
                .andExpect(jsonPath("$[2].keyWord", is("Cultural")))
                .andExpect(jsonPath("$[2]", not(hasKey("user"))))
                .andExpect(status().isOk());
    }

    @DirtiesContext
    @Test
    public void should_add_rs_event() throws Exception {
        RsEvent rsEvent = RsEvent.builder().eventName("ForthEvent").keyWord("Entertainment")
                .userId(userPO.getId()).voteNum(0).build();

        String jsonString = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/event").content(jsonString)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/rs/events"))
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[0].eventName", is("FirstEvent")))
                .andExpect(jsonPath("$[0].keyWord", is("Economy")))
                .andExpect(jsonPath("$[0]", not(hasKey("user"))))
                .andExpect(jsonPath("$[1].eventName", is("SecondEvent")))
                .andExpect(jsonPath("$[1].keyWord", is("Politics")))
                .andExpect(jsonPath("$[1]", not(hasKey("user"))))
                .andExpect(jsonPath("$[2].eventName", is("ThirdEvent")))
                .andExpect(jsonPath("$[2].keyWord", is("Cultural")))
                .andExpect(jsonPath("$[2]", not(hasKey("user"))))
                .andExpect(jsonPath("$[3].eventName", is("ForthEvent")))
                .andExpect(jsonPath("$[3].keyWord", is("Entertainment")))
                .andExpect(jsonPath("$[3]", not(hasKey("user"))))
                .andExpect(status().isOk());
    }

    @DirtiesContext
    @Test
    public void should_add_rs_event_when_user_exist() throws Exception {
        RsEvent rsEvent = RsEvent.builder().eventName("ForthEvent").keyWord("Entertainment")
                .userId(userPO.getId()).voteNum(0).build();

        String jsonString = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/event").content(jsonString)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/rs/4"))
                .andExpect(jsonPath("$.eventName", is("ForthEvent")))
                .andExpect(jsonPath("$.keyWord", is("Entertainment")))
                .andExpect(status().isOk());
    }

    @DirtiesContext
    @Test
    public void should_add_rs_event_when_user_not_exist() throws Exception {
        RsEvent rsEvent = RsEvent.builder().eventName("ForthEvent").keyWord("Entertainment")
                .userId(100).voteNum(0).build();

        String jsonString = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/event").content(jsonString)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @DirtiesContext
    @Test
    public void should_not_update_rs_event_when_user_id_not_match() throws Exception {
        RsEvent updateRsEvent = RsEvent.builder().eventName("ThirdEvent").keyWord("Science")
                .userId(100).voteNum(0).build();

        String jsonString = objectMapper.writeValueAsString(updateRsEvent);
        mockMvc.perform(patch("/rs/" + rsEventPO.getId()).content(jsonString)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        mockMvc.perform(get("/rs/3"))
                .andExpect(jsonPath("$.eventName", is("ThirdEvent")))
                .andExpect(jsonPath("$.keyWord", is("Cultural")))
                .andExpect(status().isOk());
    }

    @DirtiesContext
    @Test
    public void should_update_rs_event_when_user_id_match() throws Exception {
        RsEvent updateRsEvent = RsEvent.builder().eventName("ThirdEvent").keyWord("Science")
                .userId(userPO.getId()).voteNum(0).build();

        String jsonString = objectMapper.writeValueAsString(updateRsEvent);
        mockMvc.perform(patch("/rs/" + rsEventPO.getId()).content(jsonString)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/rs/3"))
                .andExpect(jsonPath("$.eventName", is("ThirdEvent")))
                .andExpect(jsonPath("$.keyWord", is("Science")))
                .andExpect(status().isOk());
    }

    @DirtiesContext
    @Test
    public void should_delete_rs_event() throws Exception {
        mockMvc.perform(delete("/rs/" + rsEventPO.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(get("/rs/events"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].eventName", is("FirstEvent")))
                .andExpect(jsonPath("$[0].keyWord", is("Economy")))
                .andExpect(jsonPath("$[0]", not(hasKey("user"))))
                .andExpect(jsonPath("$[1].eventName", is("SecondEvent")))
                .andExpect(jsonPath("$[1].keyWord", is("Politics")))
                .andExpect(jsonPath("$[1]", not(hasKey("user"))))
                .andExpect(status().isOk());
    }

    @DirtiesContext
    @Test
    public void should_vote_event_when_vote_num_is_enough() throws Exception {
        Vote vote = Vote.builder().userId(userPO.getId()).rsEventId(rsEventPO.getId()).time(localDataTime).voteNum(10).build();
        String jsonString = objectMapper.writeValueAsString(vote);
        mockMvc.perform(post("/rs/vote/" + rsEventPO.getId()).content(jsonString)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @DirtiesContext
    @Test
    public void should_not_vote_event_when_vote_num_is_not_enough() throws Exception {
        Vote vote = Vote.builder().userId(userPO.getId()).rsEventId(rsEventPO.getId()).time(localDataTime).voteNum(25).build();
        String jsonString = objectMapper.writeValueAsString(vote);
        mockMvc.perform(post("/rs/vote/" + rsEventPO.getId()).content(jsonString)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @DirtiesContext
    @Test
    public void should_buy_event_when_rank_num_is_exist() throws Exception {
        RsEvent rsEvent = RsEvent.builder().eventName("ForthEvent").keyWord("Entertainment")
                .userId(userPO.getId()).voteNum(0).build();

        String jsonString = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/buy")
                .param("amount", String.valueOf(100))
                .param("rank", String.valueOf(1))
                .content(jsonString)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(get("/rs/events"))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].eventName", is("ForthEvent")))
                .andExpect(jsonPath("$[0].keyWord", is("Entertainment")))
                .andExpect(jsonPath("$[0].amount", is(100)))
                .andExpect(jsonPath("$[0].rank", is(1)))
                .andExpect(status().isOk());
    }

    @Test
    public void should_not_buy_event_when_rank_num_is_not_exist() throws Exception {
        RsEvent rsEvent = RsEvent.builder().eventName("ForthEvent").keyWord("Entertainment")
                .userId(userPO.getId()).voteNum(0).build();

        String jsonString = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/buy")
                .param("amount", String.valueOf(100))
                .param("rank", String.valueOf(7))
                .content(jsonString)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        mockMvc.perform(get("/rs/events"))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[2].eventName", is("ThirdEvent")))
                .andExpect(jsonPath("$[2].keyword", is("Cultural")))
                .andExpect(jsonPath("$[2].amount", is(0)))
                .andExpect(jsonPath("$[2].rank", is(3)))
                .andExpect(status().isOk());
    }

    @Test
    public void should_not_buy_event_when_amount_is_lower() throws Exception {
        RsEvent rsEvent = RsEvent.builder().eventName("ForthEvent").keyWord("Entertainment")
                .userId(userPO.getId()).voteNum(0).build();

        String jsonString = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/buy")
                .param("amount", String.valueOf(100))
                .param("rank", String.valueOf(1))
                .content(jsonString)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        rsEvent = RsEvent.builder().eventName("FifthEvent").keyWord("Science")
                .userId(userPO.getId()).voteNum(0).build();

        jsonString = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/buy")
                .param("amount", String.valueOf(50))
                .param("rank", String.valueOf(1))
                .content(jsonString)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @DirtiesContext
    @Test
    public void should_throw_rs_event_not_valid_exception() throws Exception {
        mockMvc.perform(get("/rs/0"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid index")));

        mockMvc.perform(get("/rs/4"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid index")));
    }

    @DirtiesContext
    @Test
    public void should_throw_method_argument_not_valid_exception() throws Exception {
        RsEvent rsEvent = new RsEvent();
        String jsonString = objectMapper.writeValueAsString(rsEvent);

        mockMvc.perform(post("/rs/event")
                .content(jsonString)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid param")));
    }

    @DirtiesContext
    @Test
    public void should_throw_rs_event_not_valid_request_param_exception() throws Exception {
        mockMvc.perform(get("/rs/events?start=0&end=2"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid request param")));

        mockMvc.perform(get("/rs/events?start=1&end=4"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid request param")));

        mockMvc.perform(get("/rs/events?start=0&end=4"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid request param")));
    }
}
