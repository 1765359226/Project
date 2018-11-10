package com.taotao.service.impl;

import com.taotao.service.TestService;
import com.taotao.mapper.TestMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestServiceImpl implements TestService {
    //注入TestService接口
    @Autowired
    private TestMapper testMapper;
    @Override
    public String queryNow() {
        return testMapper.queryNow();
    }

}
