package com.thoughtworks.rslist.api;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.po.RsEventPo;
import com.thoughtworks.rslist.po.UserPo;
import com.thoughtworks.rslist.repository.RsEventRepository;
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
    @Autowired
    RsEventRepository rsEventRepository;
    @BeforeEach
    public void setUp(){
        userRepository.deleteAll();
        rsEventRepository.deleteAll();
        objectMapper = new ObjectMapper();
    }


    @Test
    @Order(1)
    public void should_add_user() throws Exception {
        UserPo userPo = UserPo.builder().phone("18888888888").name("thr").gender("male").email("a@b.com").age(18).build();
        String jsonString = objectMapper.writeValueAsString(userPo);
        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        List<UserPo> all = userRepository.findAll();
        assertEquals(1,all.size());
        assertEquals("thr",all.get(0).getName());
        assertEquals("a@b.com",all.get(0).getEmail());
    }
    @Test
    @Order(2)
    public void name_should_less_than_8() throws Exception {
        UserPo userPo = UserPo.builder().phone("18888888888").name("thrxxxxxx").gender("male").email("a@b.com").age(18).build();
        String jsonString = objectMapper.writeValueAsString(userPo);
        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error",is("invalid user")))
                .andExpect(status().isBadRequest());

    }

    @Test
    @Order(3)
    public void age_should_less_100_and_more_18() throws Exception {
        UserPo userPo = UserPo.builder().phone("18888888888").name("thr").gender("male").email("a@b.com").age(15).build();
        String jsonString = objectMapper.writeValueAsString(userPo);
        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error",is("invalid user")))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(4)
    public void email_should_format_pattern() throws Exception {
        UserPo userPo = UserPo.builder().phone("18888888888").name("thr").gender("male").email("ab.com").age(18).build();
        String jsonString = objectMapper.writeValueAsString(userPo);
        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error",is("invalid user")))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(5)
    public void phone_should_format_pattern() throws Exception {
        UserPo userPo = UserPo.builder().phone("188888888881").name("thr").gender("male").email("a@b.com").age(18).build();
        String jsonString = objectMapper.writeValueAsString(userPo);
        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error",is("invalid user")))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(6)
    public void should_get_user_in_data_base() throws Exception {
        UserPo savedUserPo = userRepository.save(UserPo.builder().phone("18888888888").name("thr").gender("male").email("a@b.com").age(18).build());
        mockMvc.perform(get("/user/{id}",savedUserPo.getId()))
                .andExpect(jsonPath("$.name",is("thr")))
                .andExpect(jsonPath("$.gender",is("male")))
                .andExpect(status().isOk());
    }

    @Test
    @Order(7)
    public void should_delete_user_in_database() throws Exception {
        UserPo savedUserPo = userRepository.save(UserPo.builder().phone("18888888888").name("thr").gender("male").email("a@b.com").age(18).voteNumber(10).build());
        RsEventPo rsEventPo = RsEventPo.builder().eventName("涨工资了").keyWord("经济")
                .userPo(savedUserPo).build();
        rsEventRepository.save(rsEventPo);
        assertEquals(1,rsEventRepository.findAll().size());
        mockMvc.perform(delete("/user/{id}",savedUserPo.getId()))
                .andExpect(status().isOk());
        assertEquals(0,userRepository.findAll().size());
        assertEquals(0,rsEventRepository.findAll().size());
    }
}