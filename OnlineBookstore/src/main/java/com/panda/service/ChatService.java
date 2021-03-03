package com.panda.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.panda.domain.ChatRecord;
import com.panda.domain.User;
import com.panda.utils.TimeUtil;
import com.panda.utils.db.DBUtil;

import java.util.ArrayList;
import java.util.List;

public class ChatService {

    public static void addChatMsg(int userId, String text, int from) throws Exception {
        ChatRecord chatRecord = new ChatRecord();
        chatRecord.setContent(text);
        chatRecord.setCtime(TimeUtil.getNow());
        chatRecord.setFrom(from);
        chatRecord.setUserId(userId);
        DBUtil.create(chatRecord);
    }

    public static String getChatList() throws Exception {
        String sql = "select * from `chat_record` group by user_id";
        List<ChatRecord> chatRecords = DBUtil.readBySql(ChatRecord.class, sql);
        List<Integer> userIds = new ArrayList<>();
        for (ChatRecord chatRecord : chatRecords) {//获取和哪些人聊天
            userIds.add(chatRecord.getUserId());
        }
        System.out.println("===" + userIds.size());
        JSONArray result = new JSONArray();
        for (Integer userId : userIds) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("userId", userId);
            User user = UserService.getUserById(userId);
            jsonObject.put("userName", user.getAccount());
            List<ChatRecord> lastChatRecords = DBUtil.read(ChatRecord.class, "*", "user_id=?", "ctime desc", "1", userId);
            ChatRecord lastRecord = lastChatRecords.get(0);
            if (lastRecord.getFrom() == 1) {//代表最后一条是用户发的
                jsonObject.put("needReply", "YES");
            } else {//最后一条是自己发的
                jsonObject.put("needReply", "NO");
            }
            result.add(jsonObject);
        }
        System.out.println(result.toJSONString());
        return result.toJSONString();
    }

    public static List<String> getChatRecord(int userId) throws Exception {
        List<ChatRecord> chatRecords = DBUtil.read(ChatRecord.class, "*", "user_id = ?", userId);
        List<String> result = new ArrayList<>();
        for (ChatRecord chatRecord : chatRecords) {
            if (chatRecord.getFrom() == 1) {//用户发的
                result.add("用户:" + chatRecord.getContent());
            } else {//商家发的
                result.add("商家:" + chatRecord.getContent());
            }
        }
        return result;
    }
}
