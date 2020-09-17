package com.thoughtworks.rslist.api;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.po.UserPo;
import com.thoughtworks.rslist.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserControllerTest {
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;
    @Autowired
    UserRepository userRepository;
    @BeforeEach
    public void setUp(){
        userRepository.deleteAll();
        objectMapper = new ObjectMapper();
    }


    @Test
    @Order(1)
    public void should_add_user() throws Exception {
        User user = new User("thr","male",18,"a@b.com","18888888888");
        String jsonString = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user/").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        List<UserPo> all = userRepository.findAll();
        assertEquals(1,all.size());
        assertEquals("thr",all.get(0).getName());
        assertEquals("a@b.com",all.get(0).getEmail());
    }
    @Test
    @Order(2)
    public void name_should_less_than_8() throws Exception {
        User user = new User("thasddssr","male",18,"a@b.com","18888888888");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user/").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error",is("invalid user")))
                .andExpect(status().isBadRequest());

    }

    @Test
    @Order(3)
    public void age_should_less_100_and_more_18() throws Exception {
        User user = new User("thr","male",15,"a@b.com","18888888888");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user/").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error",is("invalid user")))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(4)
    public void email_should_format_pattern() throws Exception {
        User user = new User("thr","male",19,"ab.com","18888888888");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user/").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error",is("invalid user")))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(5)
    public void phone_should_format_pattern() throws Exception {
        User user = new User("thr","male",19,"a@b.com","188888888881");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user/").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error",is("invalid user")))
                .andExpect(status().isBadRequest());
    }
}