package com.resultCom.serviceImpl;

import com.resultCom.service.UserService;
import com.resultCom.vo.UserInfo;
import org.springframework.stereotype.Service;

/**
 * @author wxb
 * @date 2020-10-22 15:48
 */
@Service
public class UserServiceImpl implements UserService {
    @Override
    public UserInfo getByUserId(String userId) {
        return new UserInfo(userId,"wangxb");
    }
}
