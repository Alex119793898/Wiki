package com.caoliang.wiki.service;

import com.caoliang.wiki.mapper.DemoMapper;
import com.caoliang.wiki.pojo.Demo;
import com.caoliang.wiki.pojo.DemoExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DemoService {

    @Autowired(required = false)
    private DemoMapper demoMapper;

    public List<Demo> list(){
        return demoMapper.selectByExample(new DemoExample());
    }
}
