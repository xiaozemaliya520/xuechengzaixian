package com.xuecheng.api.cms;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;

@Api(value = "cms页面管理接口",description = "t提供页面的增删改查啦！！")
public interface CmsPageControllerApi {
    @ApiOperation(value = "分页查询列表")
    public QueryResponseResult  findList(int page, int size, QueryPageRequest queryPageRequest);
    @ApiOperation(value = "添加页面思密达")
    public CmsPageResult add(CmsPage cmsPage);
    @ApiOperation(value = "根据Id查找页面")
    public CmsPage getById(String id);
    @ApiOperation(value = "修改页面")
    public CmsPageResult edit(String id,CmsPage cmsPage);
    @ApiOperation("根据id删除页面思密达")
    public ResponseResult deleteById(String id);
    @ApiOperation(value = "根据pageId静态化页面")
    public String getPageHtml( String pageId) throws IOException;
}
