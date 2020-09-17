package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.po.RsEventPO;
import com.thoughtworks.rslist.po.UserPO;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class RsControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RsEventRepository rsEventRepository;

    @DirtiesContext
    @Test
    public void should_get_rs_event_list() throws Exception {
        mockMvc.perform(get("/rs/list"))
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
        mockMvc.perform(get("/rs/list?start=1&end=2"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].eventName", is("FirstEvent")))
                .andExpect(jsonPath("$[0].keyWord", is("Economy")))
                .andExpect(jsonPath("$[0]", not(hasKey("user"))))
                .andExpect(jsonPath("$[1].eventName", is("SecondEvent")))
                .andExpect(jsonPath("$[1].keyWord", is("Politics")))
                .andExpect(jsonPath("$[1]", not(hasKey("user"))))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/list?start=2&end=3"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].eventName", is("SecondEvent")))
                .andExpect(jsonPath("$[0].keyWord", is("Politics")))
                .andExpect(jsonPath("$[0]", not(hasKey("user"))))
                .andExpect(jsonPath("$[1].eventName", is("ThirdEvent")))
                .andExpect(jsonPath("$[1].keyWord", is("Cultural")))
                .andExpect(jsonPath("$[1]", not(hasKey("user"))))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/list?start=1&end=3"))
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
        User user = new User("Mike", "male", 20, "a@thoughtworks.com", "13386688553");
        RsEvent rsEvent = new RsEvent("ForthEvent", "Entertainment", 4);
        String jsonString = rsEvent2Json(rsEvent);
        mockMvc.perform(post("/rs/event").content(jsonString)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().stringValues("index", "3"));

        mockMvc.perform(get("/rs/list"))
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
        UserPO userPO = userRepository.save(UserPO.builder().userName("Mike").age(20).phone("13386688553")
                .email("a@thoughtworks.com").gender("male").voteNum(20).build());
        RsEvent rsEvent = new RsEvent("ForthEvent", "Entertainment", userPO.getId());
        String jsonString = rsEvent2Json(rsEvent);
        mockMvc.perform(post("/rs/event").content(jsonString)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        List<RsEventPO> rsEventPOs = rsEventRepository.findAll();
        assertNotNull(rsEventPOs);
        assertEquals(1, rsEventPOs.size());
        assertEquals("ForthEvent", rsEventPOs.get(0).getEventName());
        assertEquals("Entertainment", rsEventPOs.get(0).getKeyWord());
        assertEquals(userPO.getId(), rsEventPOs.get(0).getUserPO().getId());
    }

    @DirtiesContext
    @Test
    public void should_add_rs_event_when_user_not_exist() throws Exception {
        RsEvent rsEvent = new RsEvent("ForthEvent", "Entertainment", 100);
        String jsonString = rsEvent2Json(rsEvent);
        mockMvc.perform(post("/rs/event").content(jsonString)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @DirtiesContext
    @Test
    public void should_modify_rs_event() throws Exception {
        RsEvent rsEvent = new RsEvent("ThirdEvent", "Science", 3);
        String jsonString = rsEvent2Json(rsEvent);
        mockMvc.perform(patch("/rs/3").content(jsonString)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(get("/rs/3"))
                .andExpect(jsonPath("$.eventName", is("ThirdEvent")))
                .andExpect(jsonPath("$.keyWord", is("Science")))
                .andExpect(status().isOk());
    }

    @DirtiesContext
    @Test
    public void should_delete_rs_event() throws Exception {
        mockMvc.perform(delete("/rs/2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].eventName", is("FirstEvent")))
                .andExpect(jsonPath("$[0].keyWord", is("Economy")))
                .andExpect(jsonPath("$[0]", not(hasKey("user"))))
                .andExpect(jsonPath("$[1].eventName", is("ThirdEvent")))
                .andExpect(jsonPath("$[1].keyWord", is("Cultural")))
                .andExpect(jsonPath("$[1]", not(hasKey("user"))))
                .andExpect(status().isOk());
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
        User user = new User("LeBronRaymoneJames", "male", 20, "a@thoughtworks.com", "13386688553");
        RsEvent rsEvent = new RsEvent("ForthEvent", "Entertainment", 4);
        String jsonString = rsEvent2Json(rsEvent);

        mockMvc.perform(post("/rs/event")
                .content(jsonString)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid param")));
    }

    @DirtiesContext
    @Test
    public void should_throw_rs_event_not_valid_request_param_exception() throws Exception {
        mockMvc.perform(get("/rs/list?start=0&end=2"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid request param")));

        mockMvc.perform(get("/rs/list?start=1&end=4"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid request param")));

        mockMvc.perform(get("/rs/list?start=0&end=4"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid request param")));
    }

    private String rsEvent2Json(RsEvent rsEvent) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(rsEvent);
    }
}
