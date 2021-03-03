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
@Table("cart")
public class Cart {
    @ID
    @Column("id")
    private int id;
    @Column("user_id")
    private int userId;
    @Column("book_id")
    private int bookId;
    @Column("count")
    private int count;
    @Column("ctime")
    private int ctime;
}
