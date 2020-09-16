package com.thoughtworks.rslist.api;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.RsEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc

class RsControllerTest {
    @Autowired
    MockMvc mockMvc;

    @BeforeEach
    void setUp() throws Exception {
        mockMvc.perform(delete("/rs/reStart")).andExpect(status().isOk());
    }

    @Test
    public void should_get_rs_event_list() throws Exception {
        mockMvc.perform(get("/rs/list"))
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
    public void should_add_rs_event_list() throws Exception {
//        String jsonString = "{\"eventName\":\"猪肉涨价了\",\"keyWord\":\"经济\"}";
        RsEvent rsEvent = new RsEvent("猪肉涨价了","经济");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$",hasSize(4)))
                .andExpect(jsonPath("$[0].eventName",is("第一条事件")))
                .andExpect(jsonPath("$[0].keyWord",is("无参数")))
                .andExpect(jsonPath("$[1].eventName",is("第二条事件")))
                .andExpect(jsonPath("$[1].keyWord",is("无参数")))
                .andExpect(jsonPath("$[2].eventName",is("第三条事件")))
                .andExpect(jsonPath("$[2].keyWord",is("无参数")))
                .andExpect(jsonPath("$[3].eventName",is("猪肉涨价了")))
                .andExpect(jsonPath("$[3].keyWord",is("经济")))
                .andExpect(status().isOk());
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

//    @Test
//    public void should_change_rs_event_list()throws Exception{
//        mockMvc.perform(patch("/rs/1/?eventName=美国山火&keyWord=国际"))
//                .andExpect(status().isOk());
//        mockMvc.perform(get("/rs/1"))
//                .andExpect(jsonPath("$.eventName",is("美国山火")))
//                .andExpect(jsonPath("$.keyWord",is("国际")))
//                .andExpect(status().isOk());
//
//
//        mockMvc.perform(patch("/rs/2/?eventName=新冠疫苗"))
//                .andExpect(status().isOk());
//        mockMvc.perform(patch("/rs/3/?keyWord=神秘"))
//                .andExpect(status().isOk());
//
//        mockMvc.perform(get("/rs/list"))
//                .andExpect(jsonPath("$",hasSize(3)))
//                .andExpect(jsonPath("$[0].eventName",is("美国山火")))
//                .andExpect(jsonPath("$[0].keyWord",is("国际")))
//                .andExpect(jsonPath("$[1].eventName",is("新冠疫苗")))
//                .andExpect(jsonPath("$[1].keyWord",is("无参数")))
//                .andExpect(jsonPath("$[2].eventName",is("第三条事件")))
//                .andExpect(jsonPath("$[2].keyWord",is("神秘")))
//                .andExpect(status().isOk());
//
//    }

    @Test
    public void should_change_rs_event_list_via_body()throws Exception{

        ObjectMapper objectMapper = new ObjectMapper();

        String jsonString1 = objectMapper.writeValueAsString(new RsEvent("美国山火","国际"));
        String jsonString2 = objectMapper.writeValueAsString(new RsEvent("新冠疫苗",null));
        String jsonString3 = objectMapper.writeValueAsString(new RsEvent(null,"神秘"));

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
}