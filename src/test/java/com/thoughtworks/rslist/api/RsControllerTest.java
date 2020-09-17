package com.thoughtworks.rslist.api;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.po.RsEventPo;
import com.thoughtworks.rslist.po.UserPo;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

class RsControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RsEventRepository rsEventRepository;

    ObjectMapper objectMapper;
    @BeforeEach
    void setUp() throws Exception {
        userRepository.deleteAll();
        rsEventRepository.deleteAll();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void should_get_rs_event_list() throws Exception {
        mockMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$",hasSize(3)))
                .andExpect(jsonPath("$[0].eventName",is("第一条事件")))
                .andExpect(jsonPath("$[0].keyWord",is("无参数")))
//                .andExpect(jsonPath("$[0]",not(hasKey("user"))))
                .andExpect(jsonPath("$[1].eventName",is("第二条事件")))
                .andExpect(jsonPath("$[1].keyWord",is("无参数")))
//                .andExpect(jsonPath("$[1]",not(hasKey("user"))))
                .andExpect(jsonPath("$[2].eventName",is("第三条事件")))
                .andExpect(jsonPath("$[2].keyWord",is("无参数")))
//                .andExpect(jsonPath("$[2]",not(hasKey("user"))))
                .andExpect(status().isOk());
    }

    @Test
    public  void should_get_one_rs_event() throws Exception {
        mockMvc.perform(get("/rs/1"))
                .andExpect(jsonPath("$.eventName",is("第一条事件")))
                .andExpect(jsonPath("$.keyWord",is("无参数")))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/2"))
                .andExpect(jsonPath("$.eventName",is("第二条事件")))
                .andExpect(jsonPath("$.keyWord",is("无参数")))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/3"))
                .andExpect(jsonPath("$.eventName",is("第三条事件")))
                .andExpect(jsonPath("$.keyWord",is("无参数")))
                .andExpect(status().isOk());
    }

    @Test
    public  void  should_get_rs_event_list_between() throws Exception {
        mockMvc.perform(get("/rs/list/?start=1&end=2"))
                .andExpect(jsonPath("$",hasSize(2)))
                .andExpect(jsonPath("$[0].eventName",is("第一条事件")))
                .andExpect(jsonPath("$[0].keyWord",is("无参数")))
                .andExpect(jsonPath("$[1].eventName",is("第二条事件")))
                .andExpect(jsonPath("$[1].keyWord",is("无参数")))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/list/?start=2&end=3"))
                .andExpect(jsonPath("$",hasSize(2)))
                .andExpect(jsonPath("$[0].eventName",is("第二条事件")))
                .andExpect(jsonPath("$[0].keyWord",is("无参数")))
                .andExpect(jsonPath("$[1].eventName",is("第三条事件")))
                .andExpect(jsonPath("$[1].keyWord",is("无参数")))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/list/?start=1&end=3"))
                .andExpect(jsonPath("$",hasSize(3)))
                .andExpect(jsonPath("$[0].eventName",is("第一条事件")))
                .andExpect(jsonPath("$[0].keyWord",is("无参数")))
                .andExpect(jsonPath("$[1].eventName",is("第二条事件")))
                .andExpect(jsonPath("$[1].keyWord",is("无参数")))
                .andExpect(jsonPath("$[2].eventName",is("第三条事件")))
                .andExpect(jsonPath("$[2].keyWord",is("无参数")))
                .andExpect(status().isOk());
    }

    @Test
    public void should_add_rs_event_list_when_user_exist() throws Exception {
        UserPo userPo = UserPo.builder().phone("18888888888").name("thr").gender("male").email("a@b.com").age(18).voteNumber(10).build();
        UserPo savedUserPo = userRepository.save(userPo);
        RsEventPo rsEventPo = RsEventPo.builder().eventName("猪肉涨价了").keyWord("经济").userPo(savedUserPo).build();
        String jsonString = objectMapper.writeValueAsString(rsEventPo);

        mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        List<RsEventPo> all = rsEventRepository.findAll();
        assertNotNull(all);
        assertEquals(1,all.size());
        assertEquals("猪肉涨价了",all.get(0).getEventName());
        assertEquals("经济",all.get(0).getKeyWord());
        assertEquals(savedUserPo.getId(),all.get(0).getUserPo().getId());

    }

    @Test
    public void should_throw_exception_add_rs_event_list_when_user_not_exist() throws Exception {
        UserPo userPo = UserPo.builder().phone("18888888888").name("thr").gender("male").email("a@b.com").age(18).voteNumber(10).id(100).build();
        RsEventPo rsEventPo = RsEventPo.builder().eventName("猪肉涨价了").keyWord("经济").userPo(userPo).build();
        String jsonString = objectMapper.writeValueAsString(rsEventPo);
        mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void should_delete_rs_event_list()throws Exception {
        mockMvc.perform(delete("/rs/3")).andExpect(status().isOk());
        mockMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$",hasSize(2)))
                .andExpect(jsonPath("$[0].eventName",is("第一条事件")))
                .andExpect(jsonPath("$[0].keyWord",is("无参数")))
                .andExpect(jsonPath("$[1].eventName",is("第二条事件")))
                .andExpect(jsonPath("$[1].keyWord",is("无参数")))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/rs/2")).andExpect(status().isOk());
        mockMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$",hasSize(1)))
                .andExpect(jsonPath("$[0].eventName",is("第一条事件")))
                .andExpect(jsonPath("$[0].keyWord",is("无参数")))
                .andExpect(status().isOk());
    }

    //refactory 9.17
    @Test
    public void should_change_rs_event_list_via_body()throws Exception{

        ObjectMapper objectMapper = new ObjectMapper();
        User user = new User("thr","male",19,"a@b.com","18888888888");
        String jsonString1 = objectMapper.writeValueAsString(new RsEvent("美国山火","国际",1));
        String jsonString2 = objectMapper.writeValueAsString(new RsEvent("新冠疫苗",null,1));
        String jsonString3 = objectMapper.writeValueAsString(new RsEvent(null,"神秘",1));

        mockMvc.perform(patch("/rs/1").content(jsonString1).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        mockMvc.perform(patch("/rs/2").content(jsonString2).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        mockMvc.perform(patch("/rs/3").content(jsonString3).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$",hasSize(3)))
                .andExpect(jsonPath("$[0].eventName",is("美国山火")))
                .andExpect(jsonPath("$[0].keyWord",is("国际")))
                .andExpect(jsonPath("$[1].eventName",is("新冠疫苗")))
                .andExpect(jsonPath("$[1].keyWord",is("无参数")))
                .andExpect(jsonPath("$[2].eventName",is("第三条事件")))
                .andExpect(jsonPath("$[2].keyWord",is("神秘")))
                .andExpect(status().isOk());
    }

    @Test
    public void should_get_all_user() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(jsonPath("$[0].name",is("thr")))
                .andExpect(jsonPath("$[0].age",is(19)))
                .andExpect(jsonPath("$[0].gender",is("male")))
                .andExpect(jsonPath("$[0].email",is("a@b.com")))
                .andExpect(jsonPath("$[0].phone",is("18888888888")))
                .andExpect(status().isOk());
    }

    @Test
    public void should_throw_exceptin_when_index_out_of_range() throws Exception {
        mockMvc.perform(get("/rs/0"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error",is("invalid index")));

    }

    @Test
    public  void should_throw_exception_when_method_not_valid_param() throws Exception {
        User user = new User("thrxxxxxx","male",18,"a@b.com","18888888888");
        RsEvent rsEvent = new RsEvent("猪肉涨价了","经济",1);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error",is("invalid param")))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void should_throw_exception_when_given_not_valid_request_param() throws Exception {
        mockMvc.perform(get("/rs/list/?start=0&end=2"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error",is("invalid request param")));
        mockMvc.perform(get("/rs/list/?start=1&end=5"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error",is("invalid request param")));
        mockMvc.perform(get("/rs/list/?start=2&end=1"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error",is("invalid request param")));
    }

}