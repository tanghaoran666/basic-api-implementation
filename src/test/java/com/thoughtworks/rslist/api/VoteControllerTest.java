package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.po.RsEventPo;
import com.thoughtworks.rslist.po.UserPo;
import com.thoughtworks.rslist.po.VotePo;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class VoteControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RsEventRepository rsEventRepository;
    @Autowired
    VoteRepository voteRepository;
    UserPo userPo;
    RsEventPo rsEventPo;

    @BeforeEach
    void setUp(){
        voteRepository.deleteAll();
        rsEventRepository.deleteAll();
        userRepository.deleteAll();
        userPo = UserPo.builder().phone("18888888888").name("thr").gender("male").email("a@b.com").age(18).voteNumber(10).build();
        userPo = userRepository.save(userPo);
        rsEventPo = RsEventPo.builder().eventName("美国山火").keyWord("国际").voteNum(0).userPo(userPo).build();
        rsEventPo = rsEventRepository.save(rsEventPo);

    }

    @Test
    public void should_get_vote_record() throws Exception{
        VotePo votePo = VotePo.builder()
                .localDateTime(LocalDateTime.now())
                .rsEvent(rsEventPo)
                .user(userPo)
                .voteNum(5)
                .build();
        voteRepository.save(votePo);
        String rsEventString = String.valueOf(rsEventPo.getId());
        String userString = String.valueOf(userPo.getId());
        mockMvc.perform(get("/voteRecord/").param("userId",userString)
        .param("rsEventId",rsEventString))
                .andExpect(jsonPath("$",hasSize(1)))
                .andExpect(jsonPath("$[0].userId",is(userPo.getId())))
                .andExpect(jsonPath("$[0].rsEventId",is(rsEventPo.getId())))
                .andExpect(jsonPath("$[0].voteNum",is(5)))
                .andExpect(status().isOk());
    }

}