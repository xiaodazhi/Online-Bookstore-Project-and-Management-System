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
@Table("tags")
public class Tags {
    @ID
    @Column("id")
    private int id;
    @Column("name")
    private String name;
    @Column("ctime")
    private int ctime;
}
