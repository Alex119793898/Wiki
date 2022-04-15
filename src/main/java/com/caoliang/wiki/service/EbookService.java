package com.caoliang.wiki.service;

import com.caoliang.wiki.mapper.EbookMapper;
import com.caoliang.wiki.pojo.Ebook;
import com.caoliang.wiki.pojo.EbookExample;
import com.caoliang.wiki.req.EbookReq;
import com.caoliang.wiki.req.EbookResp;
import com.caoliang.wiki.util.CopyUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
public class EbookService {

    private static final Logger LOG = LoggerFactory.getLogger(EbookService.class);

    @Autowired(required = false)
    private EbookMapper ebookMapper;

    public List<EbookResp> list(EbookReq req){
        EbookExample ebookExample = new EbookExample();
        EbookExample.Criteria criteria = ebookExample.createCriteria();

        if(!ObjectUtils.isEmpty(req.getName())){
            criteria.andNameLike("%" + req.getName() + "%");
        }

        PageHelper.startPage(1,3);
        List<Ebook> ebookList = ebookMapper.selectByExample(ebookExample);

        PageInfo<Ebook> pageInfo = new PageInfo<>(ebookList);
        LOG.info("总行数:{}",pageInfo.getTotal());
        LOG.info("总页数:{}",pageInfo.getPages());

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
