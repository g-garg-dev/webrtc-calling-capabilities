package com.mettl.videoapp.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Message {
    private String from;
    private String to;
    private String message;
    private String msgType;
}
