package com.caoliang.wiki.controller;

import com.caoliang.wiki.req.EbookReq;
import com.caoliang.wiki.resp.CommonResp;
import com.caoliang.wiki.resp.EbookResp;
import com.caoliang.wiki.resp.PageResp;
import com.caoliang.wiki.service.EbookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ebook")
public class EbookController {

    @Autowired
    private EbookService ebookService;

    @GetMapping("/list")
    public CommonResp list(EbookReq req){
        CommonResp<PageResp<EbookResp>> resp = new CommonResp<>();
        PageResp<EbookResp> pageResq= ebookService.list(req);
        resp.setContent(pageResq);
        return resp;
    }
}
