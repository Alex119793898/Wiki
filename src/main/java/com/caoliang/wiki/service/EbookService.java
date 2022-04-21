package com.caoliang.wiki.service;

import com.caoliang.wiki.mapper.EbookMapper;
import com.caoliang.wiki.pojo.Ebook;
import com.caoliang.wiki.pojo.EbookExample;
import com.caoliang.wiki.req.EbookQueryReq;
import com.caoliang.wiki.req.EbookSaveReq;
import com.caoliang.wiki.resp.EbookQueryResp;
import com.caoliang.wiki.resp.PageResp;
import com.caoliang.wiki.util.CopyUtil;
import com.caoliang.wiki.util.SnowFlake;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class EbookService {

    private static final Logger LOG = LoggerFactory.getLogger(EbookService.class);

    @Autowired(required = false)
    private EbookMapper ebookMapper;

    @Resource
    private SnowFlake snowFlake;

    public PageResp<EbookQueryResp> list(EbookQueryReq req){
        EbookExample ebookExample = new EbookExample();
        EbookExample.Criteria criteria = ebookExample.createCriteria();

        if(!ObjectUtils.isEmpty(req.getName())){
            criteria.andNameLike("%" + req.getName() + "%");
        }

        if(!ObjectUtils.isEmpty(req.getCategory2Id())){
            criteria.andCategory2IdEqualTo(req.getCategory2Id());
        }

        PageHelper.startPage(req.getPage(),req.getSize());
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
        List<EbookQueryResp> list = CopyUtil.copyList(ebookList, EbookQueryResp.class);

        PageResp<EbookQueryResp> pageResp = new PageResp<>();
        pageResp.setList(list);
        pageResp.setTotal(pageInfo.getTotal());

        return pageResp;
    }


    public void save(EbookSaveReq req) {

        Ebook copy = CopyUtil.copy(req, Ebook.class);
        if(ObjectUtils.isEmpty(req.getId())){
            //新增
            long id = snowFlake.nextId();
            copy.setId(id);
            ebookMapper.insert(copy);
        }else{
            //更新
            ebookMapper.updateByPrimaryKey(copy);
        }
    }

    public void delete(Long id) {
        ebookMapper.deleteByPrimaryKey(id);
    }
}
