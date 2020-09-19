package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.po.RsEventPO;
import com.thoughtworks.rslist.po.UserPO;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RsEventRepository rsEventRepository;

    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        rsEventRepository.deleteAll();
        userRepository.deleteAll();
        objectMapper = new ObjectMapper();
    }

    @Test
    @Order(1)
    public void shoule_register_user() throws Exception {
        User user = new User("Mike", "male", 20, "mike@thoughtworks.com", "13386688553");
        String jsonString = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/user")
                .content(jsonString)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        List<UserPO> userPOs = userRepository.findAll();
        assertEquals(1, userPOs.size());
        assertEquals("Mike", userPOs.get(0).getUserName());
        assertEquals("mike@thoughtworks.com", userPOs.get(0).getEmail());
    }

    @Test
    @Order(2)
    public void name_shoule_less_than_8() throws Exception {
        User user = new User("LeBronRaymoneJames", "male", 20, "mike@thoughtworks.com", "13386688553");
        String jsonString = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/user")
                .content(jsonString)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(3)
    public void age_shoule_between_18_and_100() throws Exception {
        User user = new User("Mike", "male", 15, "mike@thoughtworks.com", "13386688553");
        String jsonString = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/user")
                .content(jsonString)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                // .andExpect(jsonPath("$.error", is("invalid user")));
                .andExpect(jsonPath("$.error", is("invalid param")));
    }

    @Test
    @Order(4)
    public void email_shoule_suit_format() throws Exception {
        User user = new User("Mike", "male", 20, "ab.com", "13386688553");
        String jsonString = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/user")
                .content(jsonString)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                // .andExpect(jsonPath("$.error", is("invalid user")));
                .andExpect(jsonPath("$.error", is("invalid param")));
    }

    @Test
    @Order(5)
    public void phone_shoule_suit_format() throws Exception {
        User user = new User("Mike", "male", 20, "mike@thoughtworks.com", "13386688553987");
        String jsonString = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/user")
                .content(jsonString)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                // .andExpect(jsonPath("$.error", is("invalid user")));
                .andExpect(jsonPath("$.error", is("invalid param")));
    }

    @Test
    @Order(6)
    public void should_get_all_users() throws Exception {
        User user = new User("Mike", "male", 20, "mike@thoughtworks.com", "13386688553");
        String jsonString = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/user")
                .content(jsonString)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/users"))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].userName", is("Mike")))
                .andExpect(jsonPath("$[0].gender", is("male")))
                .andExpect(jsonPath("$[0].age", is(20)))
                .andExpect(jsonPath("$[0].email", is("mike@thoughtworks.com")))
                .andExpect(jsonPath("$[0].phone", is("13386688553")))
                .andExpect(status().isOk());
    }

    @Test
    @Order(7)
    public void should_delete_user() throws Exception {
        UserPO userPO = UserPO.builder().voteNum(10).phone("13386688553").userName("Mike")
                .age(20).gender("male").email("mike@thoughtworks.com").build();
        userRepository.save(userPO);
        RsEventPO rsEventPO = RsEventPO.builder().eventName("FifthEvent").keyWord("photograph")
                .userPO(userPO).build();
        rsEventRepository.save(rsEventPO);
        mockMvc.perform(delete("/user/{id}", userPO.getId())).andExpect(status().isOk());
        assertEquals(0, userRepository.findAll().size());
        assertEquals(0, rsEventRepository.findAll().size());
    }

    @Test
    @Order(8)
    public void should_get_one_user() throws Exception {
        UserPO userPO = userRepository.save(UserPO.builder().userName("Mike").age(20).phone("13386688553")
                .email("mike@thoughtworks.com").gender("male").voteNum(20).build());

        mockMvc.perform(get("/user/{id}", userPO.getId()))
                .andExpect(jsonPath("$.userName", is(userPO.getUserName())))
                .andExpect(jsonPath("$.age", is(userPO.getAge())))
                .andExpect(jsonPath("$.phone", is(userPO.getPhone())))
                .andExpect(jsonPath("$.email", is(userPO.getEmail())))
                .andExpect(jsonPath("$.gender", is(userPO.getGender())))
                .andExpect(jsonPath("$.voteNum", is(userPO.getVoteNum())))
                .andExpect(status().isOk());
    }
}
