package com.panda.domain;

import com.panda.utils.db.Column;
import com.panda.utils.db.ID;
import com.panda.utils.db.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Table("chat_record")
public class ChatRecord {
    @ID
    @Column("id")
    private int id;
    @Column("user_id")
    private int userId;
    @Column("content")
    private String content;
    @Column("from")
    private int from;
    @Column("ctime")
    private int ctime;
}
