package com.thoughtworks.rslist.po;

import com.fasterxml.jackson.annotation.JsonProperty;
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
@AllArgsConstructor
@NoArgsConstructor
public class UserPO {

    @Id
    @GeneratedValue
    private int id;

    @Column(name = "name")
    private String userName;

    private String gender;

    private int age;

    private String email;

    private String phone;

    private int voteNum = 10;

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "userPO")
    private List<RsEventPO> rsEventPOs;

}
