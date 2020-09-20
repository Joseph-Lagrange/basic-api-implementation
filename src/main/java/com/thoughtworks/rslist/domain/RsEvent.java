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

    private int rank;

    private int amount;

}
