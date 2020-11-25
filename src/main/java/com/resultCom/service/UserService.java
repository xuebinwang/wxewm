package com.resultCom.service;

import com.resultCom.vo.UserInfo;

public interface UserService {
    UserInfo getByUserId(String userId);
}
