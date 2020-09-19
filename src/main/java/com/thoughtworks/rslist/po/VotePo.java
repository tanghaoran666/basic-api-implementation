package com.thoughtworks.rslist.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "vote")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VotePo {
    @Id
    @GeneratedValue
    private int id;
    private LocalDateTime localDateTime;
    private int voteNum;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserPo user;

    @ManyToOne
    @JoinColumn(name = "rs_event_id")
    private RsEventPo rsEvent;


}
