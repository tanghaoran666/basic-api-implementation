package com.thoughtworks.rslist.po;

import com.thoughtworks.rslist.domain.Vote;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.List;

@Entity
@Table(name = "user")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPo {


    @Id
    @GeneratedValue
    private int id;
    @NotNull
    @Size(max=8)
    private String name;
    @NotNull
    private String gender;
    @Max(100)
    @Min(18)
    private int age;
    @Email
    private String email;
    @Pattern(regexp = "^1\\d{10}")
    private String phone;
    private int voteNumber=10;

    @OneToMany(cascade = CascadeType.REMOVE,mappedBy = "userPo")
    private List<RsEventPo> rsEventPos;



}
