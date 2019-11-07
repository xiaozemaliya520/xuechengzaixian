package com.xuecheng.manage_cms.web;

import com.xuecheng.api.cms.CmsPageControllerApi;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_cms.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class CmsPageController implements CmsPageControllerApi {
    @Autowired
    PageService pageService;
    @Override
    @GetMapping("/list/{page}/{size}")
    public QueryResponseResult findList(@PathVariable("page") int page, @PathVariable("size") int size, QueryPageRequest queryPageRequest) {

        return pageService.findList(page,size,queryPageRequest);
    }

    @Override
    @PostMapping("/add")
    public CmsPageResult add(CmsPage cmsPage) {
        CmsPageResult result = pageService.add(cmsPage);
        System.out.println(result);
        return result;
    }

    @Override
    @GetMapping("/getById/{id}")
    public CmsPage getById(@PathVariable("id") String id) {

        return pageService.getById(id);
    }

    @Override
    @PutMapping("/eidt/{id}")
    public CmsPageResult edit(@PathVariable("id") String id, CmsPage cmsPage) {
        return  edit(id,cmsPage);
    }

    @Override
    @DeleteMapping("/delete/{id}")
    public ResponseResult deleteById(@PathVariable("id") String id) {
        return  pageService.deleteById(id);

    }
}
