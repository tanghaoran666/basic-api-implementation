package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public RsService rsService(RsEventRepository rsEventRepository, UserRepository userRepository, VoteRepository voteRepository){
        return new RsService(rsEventRepository,userRepository,voteRepository);
    }
}
