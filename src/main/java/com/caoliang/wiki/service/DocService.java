package com.caoliang.wiki.service;

import com.caoliang.wiki.mapper.DocMapper;
import com.caoliang.wiki.pojo.Doc;
import com.caoliang.wiki.pojo.DocExample;
import com.caoliang.wiki.req.DocQueryReq;
import com.caoliang.wiki.req.DocSaveReq;
import com.caoliang.wiki.resp.DocQueryResp;
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
public class DocService {

    private static final Logger LOG = LoggerFactory.getLogger(DocService.class);

    @Autowired(required = false)
    private DocMapper docMapper;

    @Resource
    private SnowFlake snowFlake;

    public List<DocQueryResp> all(){
        DocExample docExample = new DocExample();
        docExample.setOrderByClause("sort asc");
        List<Doc> docList = docMapper.selectByExample(docExample);

        List<DocQueryResp> list = CopyUtil.copyList(docList, DocQueryResp.class);

        return list;
    }

    public PageResp<DocQueryResp> list(DocQueryReq req){
        DocExample docExample = new DocExample();
        docExample.setOrderByClause("sort asc");

        DocExample.Criteria criteria = docExample.createCriteria();

        PageHelper.startPage(req.getPage(),req.getSize());
        List<Doc> docList = docMapper.selectByExample(docExample);

        PageInfo<Doc> pageInfo = new PageInfo<>(docList);
        LOG.info("总行数:{}",pageInfo.getTotal());
        LOG.info("总页数:{}",pageInfo.getPages());

        /*ArrayList<DocResp> respList = new ArrayList<>();

        for (Doc doc : docList) {
            *//*DocResp docResp = new DocResp();
            BeanUtils.copyProperties(doc,docResp);*//*
            DocResp copyResp = CopyUtil.copy(doc, DocResp.class);
            respList.add(copyResp);
        }*/

        /*
        * CopyUtil.copyList列表复制
        * */
        List<DocQueryResp> list = CopyUtil.copyList(docList, DocQueryResp.class);

        PageResp<DocQueryResp> pageResp = new PageResp<>();
        pageResp.setList(list);
        pageResp.setTotal(pageInfo.getTotal());

        return pageResp;
    }


    public void save(DocSaveReq req) {

        Doc copy = CopyUtil.copy(req, Doc.class);
        if(ObjectUtils.isEmpty(req.getId())){
            //新增
            long id = snowFlake.nextId();
            copy.setId(id);
            docMapper.insert(copy);
        }else{
            //更新
            docMapper.updateByPrimaryKey(copy);
        }
    }

    public void delete(Long id) {
        docMapper.deleteByPrimaryKey(id);
    }
}
