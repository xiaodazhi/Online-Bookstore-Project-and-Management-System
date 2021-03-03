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
@Table("book")
public class Book {

    @ID
    @Column("id")
    private int id;
    @Column("name")
    private String name;
    @Column("author")
    private String author;
    @Column("count")
    private int count;
    @Column("price")
    private double price;
    @Column("detail")
    private String detail;
    @Column("ctime")
    private int ctime;
    @Column("pic_url")
    private String picUrl;
}
