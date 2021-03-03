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
@Table("now_chat")
public class NowChat {
    @ID
    @Column("id")
    private int id;
    @Column("user_id")
    private int userId;
    @Column("ctime")
    private int ctime;
    @Column("utime")
    private int utime;
    @Column("sign")
    private int sign;
}
