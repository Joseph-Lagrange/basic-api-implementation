package com.thoughtworks.rslist.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RsEvent implements Serializable {

    @NotNull
    private String eventName;

    @NotNull
    private String keyWord;

    @NotNull
    private int userId;

    @NotNull
    private int voteNum;

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public int getVoteNum() {
        return voteNum;
    }

    public void setVoteNum(int voteNum) {
        this.voteNum = voteNum;
    }

    // @JsonIgnore
    public int getUserId() {
        return userId;
    }

    // @JsonProperty
    public void setUserId(int userId) {
        this.userId = userId;
    }
}
