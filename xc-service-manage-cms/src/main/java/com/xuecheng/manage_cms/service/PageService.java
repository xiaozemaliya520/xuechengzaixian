package com.xuecheng.manage_cms.service;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsTemplate;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_cms.dao.CmsConfigRepository;
import com.xuecheng.manage_cms.dao.CmsPageRepository;
import com.xuecheng.manage_cms.dao.CmsTemplateRepository;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;


@Service
public class PageService {
    @Autowired
    private CmsPageRepository cmsPageRepository;
    @Autowired
    private CmsConfigRepository cmsConfigRepository;
    @Autowired
    private CmsTemplateRepository cmsTemplateRepository;
    @Autowired
    private GridFsTemplate gridFsTemplate;
    @Autowired
    private GridFSBucket gridFSBucket;
    @Autowired
    private RestTemplate restTemplate;


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
        if (cm != null) {
            cmsPageRepository.deleteById(id);
            return new ResponseResult(CommonCode.SUCCESS);
        } else {
            return new ResponseResult(CommonCode.FAIL);
        }

    }

    public String getPageHtml(String pageId) throws IOException {
        //获取数据模型
        Map model = this.getModelByPageId(pageId);
        if (model == null) {
            throw new RuntimeException("数据不存在");
        }
        //获取页面模板
        String templateConent = this.getTemplateByPageId(pageId);
        if (StringUtils.isEmpty(templateConent)) {
            throw new RuntimeException("模板不存在");
        }
        //执行静态化
        String html = getHtml(model, templateConent);
        if (StringUtils.isEmpty(html)) {
            throw new RuntimeException("出现未知错误");
        }
        return html;
    }

    private String getHtml(Map model, String templateConent) {

        try {
            Configuration configuration = new Configuration(Configuration.getVersion());
            StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();
            stringTemplateLoader.putTemplate("template", templateConent);


            configuration.setTemplateLoader(stringTemplateLoader);
            Template template = configuration.getTemplate("template");

            String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
            return html;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        }
        return null;

    }

    private String getTemplateByPageId(String pageId) throws IOException {
        CmsPage cmsPage = this.getById(pageId);
        if (cmsPage == null) {
            throw new RuntimeException("该页面不存在");
        }
        String templateId = cmsPage.getTemplateId();
        if (StringUtils.isEmpty(templateId)) {
            throw new RuntimeException("该模板不存在");
        }
        Optional<CmsTemplate> optional = cmsTemplateRepository.findById(templateId);
        if (optional.isPresent()) {
            CmsTemplate cmsTemplate = optional.get();
            String templateFileId = cmsTemplate.getTemplateFileId();
            GridFSFile gridsf = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(templateFileId)));
            GridFSDownloadStream stream = gridFSBucket.openDownloadStream(gridsf.getObjectId());


            GridFsResource gridFsResource = new GridFsResource(gridsf, stream);
            String s = IOUtils.toString(gridFsResource.getInputStream(), "utf-8");
            return s;
        }
        return null;


    }

    private Map getModelByPageId(String pageId) throws IOException {

        CmsPage cmsPage = this.getById(pageId);
        if (cmsPage == null) {
            throw new RuntimeException("没找到");
        }
        String dataUrl = cmsPage.getDataUrl();
        if(StringUtils.isEmpty(dataUrl)){
            throw new RuntimeException("地址为空！！！");
        }
        ResponseEntity<Map> map = (ResponseEntity<Map>) restTemplate.getForObject(dataUrl, Map.class);
        Map body = map.getBody();
        return body;


        //获取模板文件Id


    }
}
