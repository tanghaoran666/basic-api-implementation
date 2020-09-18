package com.thoughtworks.rslist.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Vote {
    @NotNull
    private int userId;
    @NotNull
    private int rsEventId;
    @NotNull
    private int voteNum;
    @NotNull
    private LocalDateTime localDateTime;
}
