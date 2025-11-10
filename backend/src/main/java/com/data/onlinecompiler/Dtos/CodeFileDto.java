package com.data.onlinecompiler.Dtos;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CodeFileDto {
    private String fileName;
    private String code;
    private String language;
    private List<String> inputs;

}
