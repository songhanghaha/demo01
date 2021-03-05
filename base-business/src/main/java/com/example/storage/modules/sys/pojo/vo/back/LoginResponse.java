package com.example.storage.modules.sys.pojo.vo.back;

import lombok.Data;

@Data
public class LoginResponse {
    private String accessToken;

    private String email;

    private String loginName;
    
    //add
    private String permissions;
}
