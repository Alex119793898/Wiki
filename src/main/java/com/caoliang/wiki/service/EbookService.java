package com.caoliang.wiki.service;

import com.caoliang.wiki.mapper.EbookMapper;
import com.caoliang.wiki.pojo.Ebook;
import com.caoliang.wiki.pojo.EbookExample;
import com.caoliang.wiki.req.EbookReq;
import com.caoliang.wiki.req.EbookResp;
import com.caoliang.wiki.util.CopyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EbookService {

    @Autowired(required = false)
    private EbookMapper ebookMapper;

    public List<EbookResp> list(EbookReq req){
        EbookExample ebookExample = new EbookExample();
        EbookExample.Criteria criteria = ebookExample.createCriteria();
        criteria.andNameLike("%" + req.getName() + "%");

        List<Ebook> ebookList = ebookMapper.selectByExample(ebookExample);

        /*ArrayList<EbookResp> respList = new ArrayList<>();

        for (Ebook ebook : ebookList) {
            *//*EbookResp ebookResp = new EbookResp();
            BeanUtils.copyProperties(ebook,ebookResp);*//*
            EbookResp copyResp = CopyUtil.copy(ebook, EbookResp.class);
            respList.add(copyResp);
        }*/

        /*
        * CopyUtil.copyList列表复制
        * */
        List<EbookResp> list = CopyUtil.copyList(ebookList, EbookResp.class);

        return list;
    }
}
