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
@Table("order")
public class Order {
    @ID
    @Column("id")
    private int id;
    @Column("order_num")
    private String orderNum;
    @Column("total_price")
    private double totalPrice;
    @Column("user_id")
    private int userId;
    @Column("address")
    private String address;
    @Column("status")
    private int status;
    @Column("ctime")
    private int ctime;
}
