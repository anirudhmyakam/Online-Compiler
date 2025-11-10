package com.data.onlinecompiler.Dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OutputDto {
    private String output;
    private String errorMessage;
    private int exitCode;

}
