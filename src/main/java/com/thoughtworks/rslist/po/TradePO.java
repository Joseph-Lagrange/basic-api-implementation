package com.thoughtworks.rslist.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "trade")
public class TradePO {

    @Id
    @GeneratedValue
    private int id;

    private int amount;

    private int rankNum;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserPO user;

    @ManyToOne
    @JoinColumn(name = "rs_event_id")
    private RsEventPO rsEvent;
}
