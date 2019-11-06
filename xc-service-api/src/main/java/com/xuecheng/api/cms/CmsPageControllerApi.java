package com.xuecheng.api.cms;

import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.model.response.QueryResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "cms页面管理接口",description = "t提供页面的增删改查啦！！")
public interface CmsPageControllerApi {
    @ApiOperation(value = "分页查询列表")
    public QueryResponseResult  findList(int page, int size, QueryPageRequest queryPageRequest);
}
