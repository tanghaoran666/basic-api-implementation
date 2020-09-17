package com.thoughtworks.rslist.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.*;

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
    private String name;
    private String gender;
    private int age;
    private String email;
    private String phone;
    private int voteNumber =10;
}
