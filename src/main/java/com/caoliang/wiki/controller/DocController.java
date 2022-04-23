package com.caoliang.wiki.controller;

import com.caoliang.wiki.req.DocQueryReq;
import com.caoliang.wiki.req.DocSaveReq;
import com.caoliang.wiki.resp.DocQueryResp;
import com.caoliang.wiki.resp.CommonResp;
import com.caoliang.wiki.resp.PageResp;
import com.caoliang.wiki.service.DocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/doc")
public class DocController {

    @Autowired
    private DocService docService;

    @GetMapping("/all")
    public CommonResp all(){
        CommonResp<List<DocQueryResp>> resp = new CommonResp<>();
        List<DocQueryResp> list = docService.all();
        resp.setContent(list);
        return resp;
    }

    @GetMapping("/list")
    public CommonResp list(@Valid DocQueryReq req){
        CommonResp<PageResp<DocQueryResp>> resp = new CommonResp<>();
        PageResp<DocQueryResp> list = docService.list(req);
        resp.setContent(list);
        return resp;
    }

    @PostMapping("/save")
    public CommonResp save(@Valid @RequestBody DocSaveReq req){
        CommonResp resp = new CommonResp<>();
        docService.save(req);
        return resp;
    }

    @DeleteMapping("/delete/{idStr}")
    public CommonResp save(@PathVariable String idStr){
        CommonResp resp = new CommonResp<>();
        List<String> list = Arrays.asList(idStr.split(","));
        docService.delete(list);
        return resp;
    }
}
