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
@Table("comments")
public class Comments {
    @ID
    @Column("id")
    private int id;
    @Column("book_id")
    private int bookId;
    @Column("star")
    private int star;
    @Column("content")
    private String content;
    @Column("reply")
    private String reply;
    @Column("ctime")
    private int ctime;
}
