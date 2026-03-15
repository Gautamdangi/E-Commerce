package com.gautam.payment.dto;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
 private int status;
 private String message;
 private LocalDateTime time;
private String path;


        public ErrorResponse(int status, String message, String path){
            this(status, message, LocalDateTime.now(),path);
    }
}
