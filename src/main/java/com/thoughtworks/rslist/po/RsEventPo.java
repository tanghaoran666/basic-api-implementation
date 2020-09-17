package com.thoughtworks.rslist.po;

import com.thoughtworks.rslist.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "rsEvent")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RsEventPo {

    @Id
    @GeneratedValue
    private int id;
    @NotNull
    private String eventName;
    @NotNull
    private String keyWord;
    @Valid
    private int userId;
}
