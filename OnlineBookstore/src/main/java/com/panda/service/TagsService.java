package com.panda.service;

import com.panda.domain.Tags;
import com.panda.utils.db.DBUtil;

import java.util.List;

public class TagsService {

    public static int addTags(Tags tags) throws Exception {
        return DBUtil.create(tags);
    }

    public static List<Tags> getAllTags() throws Exception {
        return DBUtil.readAll(Tags.class);
    }

    public static void deleteTagsById(Tags tags) throws Exception {
        DBUtil.delete(tags);
    }

    public static List<Tags> getTagsByPage(int offset, int limit) throws Exception {
        return DBUtil.read(Tags.class, "*", "", "", "?,?", offset, limit);
    }
}
