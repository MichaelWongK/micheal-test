package com.micheal.wang.transtractiondemo.service;

import com.micheal.wang.transtractiondemo.dao.OperationLogDao;
import com.micheal.wang.transtractiondemo.dao.UserDao;
import com.micheal.wang.transtractiondemo.domain.OperationLog;
import com.micheal.wang.transtractiondemo.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
public class DemoService {
    @Autowired
    UserDao userDao;

    @Autowired
    OperationLogDao operationLogDao;

    @Transactional
    public void addUser(String name) {
        OperationLog log = new OperationLog();
        log.setContent("create user:" + name);
        operationLogDao.save(log);

        User user = new User();
        user.setName(name);
        userDao.save(user);
    }
}
