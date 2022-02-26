package com.jh.stock.batch.slackdto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SlackField {
    private String type;
    private String text;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean emoji;
}
