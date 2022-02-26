package com.jh.stock.batch.slackdto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SlackBlock {
    @Builder.Default
    private String type = "section";
    private List<SlackField> fields;
}
