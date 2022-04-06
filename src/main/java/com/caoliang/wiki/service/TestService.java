package com.caoliang.wiki.service;

import com.caoliang.wiki.mapper.TestMapper;
import com.caoliang.wiki.pojo.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestService {

    @Autowired(required = false)
    private TestMapper testMapper;

    public List<Test> list(){
        return testMapper.list();
    }
}
