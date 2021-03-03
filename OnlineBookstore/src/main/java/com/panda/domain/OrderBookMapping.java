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
@Table("order_book_mapping")
public class OrderBookMapping {
    @ID
    @Column("id")
    private int id;
    @Column("order_id")
    private int orderId;
    @Column("book_id")
    private int bookId;
    @Column("count")
    private int count;
    @Column("price")
    private double price;
    @Column("ctime")
    private int ctime;
}
