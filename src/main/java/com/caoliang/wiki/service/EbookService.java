package com.caoliang.wiki.service;

import com.caoliang.wiki.mapper.EbookMapper;
import com.caoliang.wiki.pojo.Ebook;
import com.caoliang.wiki.pojo.EbookExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EbookService {

    @Autowired(required = false)
    private EbookMapper ebookMapper;

    public List<Ebook> list(){
        return ebookMapper.selectByExample(new EbookExample());
    }
}
