package com.thoughtworks.rslist.api;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.domain.Vote;
import com.thoughtworks.rslist.po.RsEventPo;
import com.thoughtworks.rslist.po.UserPo;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.security.PublicKey;
import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc

class RsControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RsEventRepository rsEventRepository;
    @Autowired
    VoteRepository voteRepository;

    ObjectMapper objectMapper;
    UserPo userPo;

    @BeforeEach
    void setUp() throws Exception {
        voteRepository.deleteAll();
        rsEventRepository.deleteAll();
        userRepository.deleteAll();
        objectMapper = new ObjectMapper();
        userPo = UserPo.builder().phone("18888888888").name("thr").gender("male").email("a@b.com").age(18).voteNumber(10).build();
        userPo = userRepository.save(userPo);
    }

    @Test
    public void should_get_rs_event_list() throws Exception {

        RsEventPo rsEventPo = RsEventPo.builder().eventName("美国山火").keyWord("国际").userPo(userPo).build();
        RsEventPo savedRsEventPo = rsEventRepository.save(rsEventPo);
        mockMvc.perform(get("/rs/events"))
                .andExpect(jsonPath("$",hasSize(1)))
                .andExpect(jsonPath("$[0].eventName",is("美国山火")))
                .andExpect(jsonPath("$[0].keyWord",is("国际")))
                .andExpect(status().isOk());
    }

    @Test
    public  void  should_get_rs_event_list_between() throws Exception {
        RsEventPo rsEventPo1 = RsEventPo.builder().eventName("第一条事件").keyWord("无参数").userPo(userPo).build();
        RsEventPo savedRsEventPo1 = rsEventRepository.save(rsEventPo1);
        RsEventPo rsEventPo2 = RsEventPo.builder().eventName("第二条事件").keyWord("无参数").userPo(userPo).build();
        RsEventPo savedRsEventPo2 = rsEventRepository.save(rsEventPo2);

        mockMvc.perform(get("/rs/events/?start=1&end=2"))
                .andExpect(jsonPath("$",hasSize(2)))
                .andExpect(jsonPath("$[0].eventName",is("第一条事件")))
                .andExpect(jsonPath("$[0].keyWord",is("无参数")))
                .andExpect(jsonPath("$[1].eventName",is("第二条事件")))
                .andExpect(jsonPath("$[1].keyWord",is("无参数")))
                .andExpect(status().isOk());
    }

    @Test
    public  void should_get_one_rs_event() throws Exception {
        RsEventPo rsEventPo1 = RsEventPo.builder().eventName("第一条事件").keyWord("无参数").userPo(userPo).build();
        RsEventPo savedRsEventPo1 = rsEventRepository.save(rsEventPo1);
        RsEventPo rsEventPo2 = RsEventPo.builder().eventName("第二条事件").keyWord("无参数").userPo(userPo).build();
        RsEventPo savedRsEventPo2 = rsEventRepository.save(rsEventPo2);

        mockMvc.perform(get("/rs/event/{id}",savedRsEventPo1.getId()))
                .andExpect(jsonPath("$.eventName",is("第一条事件")))
                .andExpect(jsonPath("$.keyWord",is("无参数")))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/event/{id}",savedRsEventPo2.getId()))
                .andExpect(jsonPath("$.eventName",is("第二条事件")))
                .andExpect(jsonPath("$.keyWord",is("无参数")))
                .andExpect(status().isOk());

    }





    @Test
    public void should_throw_exception_add_rs_event_list_when_user_not_exist() throws Exception {
        RsEvent rsEvent = RsEvent.builder().eventName("猪肉涨价了").keyWord("经济").userId(100).build();
        String jsonString = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void should_delete_rs_event_list()throws Exception {
        RsEventPo rsEventPo = RsEventPo.builder().eventName("美国山火").keyWord("国际").userPo(userPo).build();
        RsEventPo savedRsEventPo = rsEventRepository.save(rsEventPo);
        assertEquals(1,rsEventRepository.findAll().size());
        mockMvc.perform(delete("/rs/event/{id}",savedRsEventPo.getId())).andExpect(status().isOk());
        assertEquals(0,rsEventRepository.findAll().size());


    }

    @Test
    public void should_change_rs_event_list_via_body()throws Exception{
        RsEventPo rsEventPo = RsEventPo.builder().eventName("美国山火").keyWord("国际").userPo(userPo).build();
        RsEventPo savedRsEventPo = rsEventRepository.save(rsEventPo);
        String jsonString = objectMapper.writeValueAsString(new RsEvent("改过的美国山火",null,userPo.getId(),savedRsEventPo.getId(),5));
        mockMvc.perform(patch("/rs/event/{id}",savedRsEventPo.getId()).content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        RsEventPo rsEventPoChanged = rsEventRepository.findById(savedRsEventPo.getId()).get();
        assertEquals("改过的美国山火",rsEventPoChanged.getEventName());
        assertEquals("国际",rsEventPoChanged.getKeyWord());

    }

    @Test
    public void should_throw_exception_change_rs_event_list_when_uerid_not_match()throws Exception{
        RsEventPo rsEventPo = RsEventPo.builder().eventName("美国山火").keyWord("国际").userPo(userPo).build();
        RsEventPo savedRsEventPo = rsEventRepository.save(rsEventPo);
        String jsonString = objectMapper.writeValueAsString(new RsEvent("改过的美国山火",null,100,savedRsEventPo.getId(),10));
        mockMvc.perform(patch("/rs/event/{id}",savedRsEventPo.getId()).content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void should_get_all_user() throws Exception {


        mockMvc.perform(get("/rs/users"))
                .andExpect(jsonPath("$[0].name",is("thr")))
                .andExpect(jsonPath("$[0].age",is(18)))
                .andExpect(jsonPath("$[0].gender",is("male")))
                .andExpect(jsonPath("$[0].email",is("a@b.com")))
                .andExpect(jsonPath("$[0].phone",is("18888888888")))
                .andExpect(status().isOk());
    }

    @Test
    public void should_throw_exceptin_when_index_out_of_range() throws Exception {
        mockMvc.perform(get("/rs/event/0"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error",is("invalid id")));

    }


    @Test
    public void should_throw_exception_when_given_not_valid_request_param() throws Exception {
        mockMvc.perform(get("/rs/events/?start=0&end=2"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error",is("invalid request param")));
        mockMvc.perform(get("/rs/events/?start=1&end=5"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error",is("invalid request param")));
        mockMvc.perform(get("/rs/events/?start=2&end=1"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error",is("invalid request param")));
    }

    @Test
    public void should_add_rs_event_list_when_user_exist() throws Exception {
        RsEvent rsEvent = RsEvent.builder().eventName("猪肉涨价了").keyWord("经济").userId(userPo.getId()).build();
        String jsonString = objectMapper.writeValueAsString(rsEvent);

        mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        List<RsEventPo> all = rsEventRepository.findAll();
        assertNotNull(all);
        assertEquals(1,all.size());
        assertEquals("猪肉涨价了",all.get(0).getEventName());
        assertEquals("经济",all.get(0).getKeyWord());
        assertEquals(userPo.getId(),all.get(0).getUserPo().getId());

    }

    @Test
    public void should_vote_to_rs_event() throws  Exception{
        RsEventPo rsEventPo = RsEventPo.builder().eventName("美国山火").keyWord("国际").userPo(userPo).voteNum(5).build();
        RsEventPo savedRsEventPo = rsEventRepository.save(rsEventPo);
        Vote vote = Vote.builder()
                .userId(userPo.getId())
                .voteNum(3)
                .build();
        String jsonString = objectMapper.writeValueAsString(vote);
        mockMvc.perform(post("/rs/vote/{eventId}",savedRsEventPo.getId()).content(jsonString).contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());

        assertEquals(8,rsEventRepository.findById(savedRsEventPo.getId()).get().getVoteNum());
        assertEquals(7,userRepository.findById(userPo.getId()).get().getVoteNumber());
    }

    @Test
    public void should_throw_exception_when_vote_number_invalid() throws  Exception{
        RsEventPo rsEventPo = RsEventPo.builder().eventName("美国山火").keyWord("国际").userPo(userPo).voteNum(5).build();
        RsEventPo savedRsEventPo = rsEventRepository.save(rsEventPo);
        Vote vote = Vote.builder()
                .userId(userPo.getId())
                .voteNum(11)
                .build();
        String jsonString = objectMapper.writeValueAsString(vote);
        mockMvc
                .perform(post("/rs/vote/{eventId}",savedRsEventPo.getId()).content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error",is("invalid vote number")));

    }



}