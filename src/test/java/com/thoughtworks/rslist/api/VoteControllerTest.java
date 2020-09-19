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
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

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

        for (int i = 0; i < 8; i++) {
            VotePo votePo = VotePo.builder()
                    .localDateTime(LocalDateTime.now())
                    .rsEvent(rsEventPo)
                    .user(userPo)
                    .voteNum(i+1)
                    .build();
            voteRepository.save(votePo);
        }


        mockMvc.perform(get("/voteRecord/byId/").param("userId",String.valueOf(userPo.getId()))
        .param("rsEventId",String.valueOf(rsEventPo.getId()))
        .param("pageIndex","1"))
                .andExpect(jsonPath("$",hasSize(5)))
                .andExpect(jsonPath("$[0].userId",is(userPo.getId())))
                .andExpect(jsonPath("$[0].rsEventId",is(rsEventPo.getId())))
                .andExpect(jsonPath("$[0].voteNum",is(1)))
                .andExpect(jsonPath("$[1].voteNum",is(2)))
                .andExpect(jsonPath("$[2].voteNum",is(3)))
                .andExpect(jsonPath("$[3].voteNum",is(4)))
                .andExpect(jsonPath("$[4].voteNum",is(5)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/voteRecord/byId/").param("userId",String.valueOf(userPo.getId()))
                .param("rsEventId",String.valueOf(rsEventPo.getId()))
                .param("pageIndex","2"))
                .andExpect(jsonPath("$",hasSize(3)))
                .andExpect(jsonPath("$[0].userId",is(userPo.getId())))
                .andExpect(jsonPath("$[0].rsEventId",is(rsEventPo.getId())))
                .andExpect(jsonPath("$[0].voteNum",is(6)))
                .andExpect(jsonPath("$[1].voteNum",is(7)))
                .andExpect(jsonPath("$[2].voteNum",is(8)))
                .andExpect(status().isOk());
    }

    @Test
    public void should_get_vote_record_between_start_time_and_end_time() throws Exception{
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = startTime.plusMinutes(1);
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String startTimeString = df.format(startTime);
        String endTimeString = df.format(endTime);
        TimeUnit.SECONDS.sleep(1);
        for (int i = 0; i < 5; i++) {
            VotePo votePo = VotePo.builder()
                    .localDateTime(LocalDateTime.now())
                    .rsEvent(rsEventPo)
                    .user(userPo)
                    .voteNum(i+1)
                    .build();
            voteRepository.save(votePo);
        }
        TimeUnit.MINUTES.sleep(1);

        for (int i = 0; i < 3; i++) {
            VotePo votePo = VotePo.builder()
                    .localDateTime(LocalDateTime.now())
                    .rsEvent(rsEventPo)
                    .user(userPo)
                    .voteNum(i+1)
                    .build();
            voteRepository.save(votePo);
        }
        mockMvc.perform(get("/voteRecord/byTime/").param("startTimeString",startTimeString)
                .param("endTimeString",endTimeString))
                .andExpect(jsonPath("$",hasSize(5)))
                .andExpect(jsonPath("$[0].userId",is(userPo.getId())))
                .andExpect(jsonPath("$[0].rsEventId",is(rsEventPo.getId())))
                .andExpect(jsonPath("$[0].voteNum",is(1)))
                .andExpect(jsonPath("$[1].voteNum",is(2)))
                .andExpect(jsonPath("$[2].voteNum",is(3)))
                .andExpect(jsonPath("$[3].voteNum",is(4)))
                .andExpect(jsonPath("$[4].voteNum",is(5)))
                .andExpect(status().isOk());
    }

}