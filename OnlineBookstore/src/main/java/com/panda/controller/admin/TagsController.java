package com.panda.controller.admin;

import com.alibaba.fastjson.JSON;
import com.panda.domain.Tags;
import com.panda.service.TagsService;
import com.panda.utils.ResponseUtil;
import com.panda.utils.TimeUtil;
import com.panda.utils.web.Controller;
import com.panda.utils.web.Path;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
public class TagsController {

    @Path("/admin/tags/add.api")
    public void addTags(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String tag = request.getParameter("tag");
        if (tag == null || "".equals(tag)) {
            ResponseUtil.writeJSON(response, ResponseUtil.ResponseEnum.FAIL, "tag不能为空", "");
            return;
        }
        Tags tags = new Tags();
        tags.setName(tag);
        tags.setCtime(TimeUtil.getNow());
        try {
            TagsService.addTags(tags);
        } catch (Exception e) {
            ResponseUtil.writeJSON(response, ResponseUtil.ResponseEnum.FAIL, "标签已存在", "");
            return;
        }
        ResponseUtil.writeJSON(response, ResponseUtil.ResponseEnum.OK, "添加成功", "");
        return;
    }

    @Path("/admin/tags/all.api")
    public void getAllTags(HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<Tags> tagsList = TagsService.getAllTags();
        ResponseUtil.writeJSON(response, ResponseUtil.ResponseEnum.OK, "查询成功", JSON.toJSONString(tagsList));
    }

    @Path("/admin/tags/delete.api")
    public void deleteTags(HttpServletRequest request, HttpServletResponse response) throws Exception {
        int id = Integer.parseInt(request.getParameter("id"));
        Tags tags = new Tags();
        tags.setId(id);
        TagsService.deleteTagsById(tags);
    }

}
