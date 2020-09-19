package com.thoughtworks.rslist.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "vote")
public class VotePO {

    @Id
    @GeneratedValue
    private int id;

    private int num;

    private Date localDateTime;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserPO user;

    @ManyToOne
    @JoinColumn(name = "rs_event_id")
    private RsEventPO rsEvent;

}
