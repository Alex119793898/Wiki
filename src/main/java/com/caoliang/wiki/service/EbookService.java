package com.caoliang.wiki.service;

import com.caoliang.wiki.mapper.EbookMapper;
import com.caoliang.wiki.pojo.Ebook;
import com.caoliang.wiki.pojo.EbookExample;
import com.caoliang.wiki.req.EbookReq;
import com.caoliang.wiki.resp.EbookResp;
import com.caoliang.wiki.resp.PageResp;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class EbookService {

    private static final Logger LOG = LoggerFactory.getLogger(EbookService.class);

    @Autowired(required = false)
    private EbookMapper ebookMapper;

    public PageResp<EbookResp> list(EbookReq req){
        EbookExample ebookExample = new EbookExample();
        EbookExample.Criteria criteria = ebookExample.createCriteria();

        if(!ObjectUtils.isEmpty(req.getName())){
            criteria.andNameLike("%" + req.getName() + "%");
        }

        PageHelper.startPage(req.getPage(),req.getSize());
        List<Ebook> ebookList = ebookMapper.selectByExample(ebookExample);

        PageInfo<Ebook> pageInfo = new PageInfo<>(ebookList);
        LOG.info("总行数:{}",pageInfo.getTotal());
        LOG.info("总页数:{}",pageInfo.getPages());

        ArrayList<EbookResp> list = new ArrayList<>();

        for (Ebook ebook : ebookList) {
            EbookResp ebookResp = new EbookResp();
            BeanUtils.copyProperties(ebook,ebookResp);
            //EbookResp ebookResp = CopyUtil.copy(ebook, EbookResp.class);
            list.add(ebookResp);
        }

        /*
        * CopyUtil.copyList列表复制
        * */
        PageResp<EbookResp> resp = new PageResp<>();
        //List<EbookResp> list = CopyUtil.copyList(ebookList, EbookResp.class);
        resp.setList(list);
        resp.setTotal(pageInfo.getTotal());

        return resp;
    }
}
