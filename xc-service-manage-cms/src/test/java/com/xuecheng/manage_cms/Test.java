package com.xuecheng.manage_cms;


import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.manage_cms.dao.CmsPageRepository;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.data.domain.*;
import org.springframework.test.context.junit4.SpringRunner;


import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Test {
    @Autowired
    private CmsPageRepository cmsPageRepository;


    @org.junit.Test
    public  void Test01(){
        int page=0;
        int size=10;
        Pageable pageable=  PageRequest.of(page,size);
        Page<CmsPage> list = cmsPageRepository.findAll(pageable);
        System.out.println(list);
    }
    @org.junit.Test
    public void Test02(){
        ExampleMatcher exampleMatcher=ExampleMatcher.matching();
     exampleMatcher = exampleMatcher.withMatcher("pageAliase", ExampleMatcher.GenericPropertyMatchers.contains());
     CmsPage cmsPage=new CmsPage();
     cmsPage.setSiteId("5a751fab6abb5044e0d19ea1");
     cmsPage.setTemplateId("5a962c16b00ffc514038fafd");
     cmsPage.setPageAliase("分类导航");
        Example<CmsPage> example=Example.of(cmsPage,exampleMatcher);
        Pageable pageable=new PageRequest(0,10);
        Page<CmsPage> list = cmsPageRepository.findAll(example, pageable);
        System.out.println(list);


    }
}
