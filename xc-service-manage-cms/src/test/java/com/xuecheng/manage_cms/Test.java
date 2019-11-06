package com.xuecheng.manage_cms;


import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.manage_cms.dao.CmsPageRepository;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
}
