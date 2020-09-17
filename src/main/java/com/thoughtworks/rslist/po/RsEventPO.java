package com.thoughtworks.rslist.po;

import com.thoughtworks.rslist.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "rsEvent")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RsEventPO {

    @Id
    @GeneratedValue
    private int id;

    private String eventName;

    private String keyWord;

    @ManyToOne
    private UserPO userPO;
}
