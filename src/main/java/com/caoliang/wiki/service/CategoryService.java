package com.caoliang.wiki.service;

import com.caoliang.wiki.mapper.CategoryMapper;
import com.caoliang.wiki.pojo.Category;
import com.caoliang.wiki.pojo.CategoryExample;
import com.caoliang.wiki.req.CategoryQueryReq;
import com.caoliang.wiki.req.CategorySaveReq;
import com.caoliang.wiki.resp.CategoryQueryResp;
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
public class CategoryService {

    private static final Logger LOG = LoggerFactory.getLogger(CategoryService.class);

    @Autowired(required = false)
    private CategoryMapper categoryMapper;


    @Resource
    private SnowFlake snowFlake;

    public List<CategoryQueryResp> all(){
        CategoryExample categoryExample = new CategoryExample();
        categoryExample.setOrderByClause("sort asc");
        List<Category> categoryList = categoryMapper.selectByExample(categoryExample);

        List<CategoryQueryResp> list = CopyUtil.copyList(categoryList, CategoryQueryResp.class);

        return list;
    }

    public PageResp<CategoryQueryResp> list(CategoryQueryReq req){
        CategoryExample categoryExample = new CategoryExample();
        categoryExample.setOrderByClause("sort asc");

        CategoryExample.Criteria criteria = categoryExample.createCriteria();

        PageHelper.startPage(req.getPage(),req.getSize());
        List<Category> categoryList = categoryMapper.selectByExample(categoryExample);

        PageInfo<Category> pageInfo = new PageInfo<>(categoryList);
        LOG.info("总行数:{}",pageInfo.getTotal());
        LOG.info("总页数:{}",pageInfo.getPages());

        /*ArrayList<CategoryResp> respList = new ArrayList<>();

        for (Category category : categoryList) {
            *//*CategoryResp categoryResp = new CategoryResp();
            BeanUtils.copyProperties(category,categoryResp);*//*
            CategoryResp copyResp = CopyUtil.copy(category, CategoryResp.class);
            respList.add(copyResp);
        }*/

        /*
        * CopyUtil.copyList列表复制
        * */
        List<CategoryQueryResp> list = CopyUtil.copyList(categoryList, CategoryQueryResp.class);

        PageResp<CategoryQueryResp> pageResp = new PageResp<>();
        pageResp.setList(list);
        pageResp.setTotal(pageInfo.getTotal());

        return pageResp;
    }


    public void save(CategorySaveReq req) {

        Category copy = CopyUtil.copy(req, Category.class);
        if(ObjectUtils.isEmpty(req.getId())){
            //新增
            long id = snowFlake.nextId();
            copy.setId(id);
            categoryMapper.insert(copy);
        }else{
            //更新
            categoryMapper.updateByPrimaryKey(copy);
        }
    }

    public void delete(Long id) {
        categoryMapper.deleteByPrimaryKey(id);
    }
}
