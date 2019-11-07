package com.xuecheng.manage_cms.service;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_cms.dao.CmsPageRepository;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class PageService {
    @Autowired
    private CmsPageRepository cmsPageRepository;


    public QueryResponseResult findList(int page, int size, QueryPageRequest queryPageRequest) {
        ExampleMatcher exampleMatcher = ExampleMatcher.matching().withMatcher("pageAliase", ExampleMatcher.GenericPropertyMatchers.contains());
        CmsPage cmsPage = new CmsPage();
        if (StringUtils.isNotEmpty(queryPageRequest.getSiteId())) {
            cmsPage.setSiteId(queryPageRequest.getSiteId());
        }
        if (StringUtils.isNotEmpty(queryPageRequest.getPageAliase())) {
            cmsPage.setSiteId(queryPageRequest.getPageAliase());
        }
        Example<CmsPage> example = Example.of(cmsPage, exampleMatcher);
        page = page - 1;

        Pageable pageable = new PageRequest(page, size);
        Page<CmsPage> list = cmsPageRepository.findAll(example, pageable);
        QueryResult<CmsPage> queryResult = new QueryResult<>();
        queryResult.setTotal(list.getTotalElements());
        queryResult.setList(list.getContent());
        System.out.println(queryResult);
        return new QueryResponseResult(CommonCode.SUCCESS, queryResult);


//        if (queryPageRequest==null){
//            queryPageRequest=new QueryPageRequest();
//        }
//        if (page<0){
//            page=1;
//        }
//        if(size<0){
//            size=22;
//        }
//        Pageable pageable= PageRequest.of(page,size);
//        Page<CmsPage> list = cmsPageRepository.findAll(pageable);
//        QueryResult<CmsPage> queryResult=new QueryResult<>();
//        queryResult.setList(list.getContent());
//        queryResult.setTotal(list.getTotalElements());
//        return new QueryResponseResult(CommonCode.SUCCESS,queryResult);


    }

    public CmsPageResult add(CmsPage cmsPage) {
        CmsPage cmsPage1 = cmsPageRepository.findByPageNameAndSiteIdAndPageWebPath(cmsPage.getPageName(), cmsPage.getSiteId(), cmsPage.getPageWebPath());
        if (cmsPage1 == null) {
            cmsPage1.setPageId(null);
            cmsPageRepository.save(cmsPage1);
            return new CmsPageResult(CommonCode.SUCCESS, cmsPage1);
        } else {
            return new CmsPageResult(CommonCode.FAIL, null);
        }


    }

    public CmsPage getById(String id) {
        Optional<CmsPage> optional = cmsPageRepository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        }
        return null;
    }

    public CmsPageResult edit(String id, CmsPage cmsPage) {
        CmsPage c = this.getById(id);
        if (c != null) {
            c.setTemplateId(cmsPage.getTemplateId());
            c.setSiteId(cmsPage.getSiteId());
            c.setPageAliase(cmsPage.getPageAliase());
            c.setPageName(cmsPage.getPageName());
            c.setPageWebPath(cmsPage.getPageWebPath());
            c.setPagePhysicalPath(cmsPage.getPagePhysicalPath());
            CmsPage save = cmsPageRepository.save(c);
            System.out.println(save);
            if (save != null) {
                return new CmsPageResult(CommonCode.SUCCESS, save);
            }

        }

        return new CmsPageResult(CommonCode.FAIL, null);

    }


    public ResponseResult deleteById(String id) {
        CmsPage cm = this.getById(id);
        if(cm!=null){
            cmsPageRepository.deleteById(id);
            return new ResponseResult(CommonCode.SUCCESS);
        }else {
            return new ResponseResult(CommonCode.FAIL);
        }

    }
}
