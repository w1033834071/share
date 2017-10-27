package com.share.service;

import org.springframework.stereotype.Service;

@Service
public class ShareService {
    public String getUserName(int userId){
        return "ShareService :" + String.valueOf(userId);
    }
}
