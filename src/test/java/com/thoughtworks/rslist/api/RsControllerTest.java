package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.RsEvent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class RsControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    public void should_get_rs_event_list() throws Exception {
        mockMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].eventName", is("FirstEvent")))
                .andExpect(jsonPath("$[0].keyWord", is("Economy")))
                .andExpect(jsonPath("$[1].eventName", is("SecondEvent")))
                .andExpect(jsonPath("$[1].keyWord", is("Politics")))
                .andExpect(jsonPath("$[2].eventName", is("ThirdEvent")))
                .andExpect(jsonPath("$[2].keyWord", is("Cultural")))
                .andExpect(status().isOk());
    }

    @Test
    public void should_get_one_rs_event() throws Exception {
        mockMvc.perform(get("/rs/1"))
                .andExpect(jsonPath("$.eventName", is("FirstEvent")))
                .andExpect(jsonPath("$.keyWord", is("Economy")))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/2"))
                .andExpect(jsonPath("$.eventName", is("SecondEvent")))
                .andExpect(jsonPath("$.keyWord", is("Politics")))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/3"))
                .andExpect(jsonPath("$.eventName", is("ThirdEvent")))
                .andExpect(jsonPath("$.keyWord", is("Cultural")))
                .andExpect(status().isOk());
    }

    @Test
    public void should_rs_event_between() throws Exception {
        mockMvc.perform(get("/rs/list?start=1&end=2"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].eventName", is("FirstEvent")))
                .andExpect(jsonPath("$[0].keyWord", is("Economy")))
                .andExpect(jsonPath("$[1].eventName", is("SecondEvent")))
                .andExpect(jsonPath("$[1].keyWord", is("Politics")))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/list?start=2&end=3"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].eventName", is("SecondEvent")))
                .andExpect(jsonPath("$[0].keyWord", is("Politics")))
                .andExpect(jsonPath("$[1].eventName", is("ThirdEvent")))
                .andExpect(jsonPath("$[1].keyWord", is("Cultural")))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/list?start=1&end=3"))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].eventName", is("FirstEvent")))
                .andExpect(jsonPath("$[0].keyWord", is("Economy")))
                .andExpect(jsonPath("$[1].eventName", is("SecondEvent")))
                .andExpect(jsonPath("$[1].keyWord", is("Politics")))
                .andExpect(jsonPath("$[2].eventName", is("ThirdEvent")))
                .andExpect(jsonPath("$[2].keyWord", is("Cultural")))
                .andExpect(status().isOk());
    }

    @Test
    public void should_add_rs_event() throws Exception {
        RsEvent rsEvent = new RsEvent("ForthEvent", "Entertainment");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/add").content(jsonString)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[0].eventName", is("FirstEvent")))
                .andExpect(jsonPath("$[0].keyWord", is("Economy")))
                .andExpect(jsonPath("$[1].eventName", is("SecondEvent")))
                .andExpect(jsonPath("$[1].keyWord", is("Politics")))
                .andExpect(jsonPath("$[2].eventName", is("ThirdEvent")))
                .andExpect(jsonPath("$[2].keyWord", is("Cultural")))
                .andExpect(jsonPath("$[3].eventName", is("ForthEvent")))
                .andExpect(jsonPath("$[3].keyWord", is("Entertainment")))
                .andExpect(status().isOk());
    }

    @Test
    public void should_modify_rs_event() throws Exception {
        RsEvent rsEvent = new RsEvent("ThirdEvent", "Science");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/modify?index=3").content(jsonString)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(get("/rs/3"))
                .andExpect(jsonPath("$.eventName", is("ThirdEvent")))
                .andExpect(jsonPath("$.keyWord", is("Science")))
                .andExpect(status().isOk());
    }

}
