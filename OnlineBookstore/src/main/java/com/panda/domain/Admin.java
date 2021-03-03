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
@Table("admin")
public class Admin {

    @ID
    @Column("id")
    private int id;
    @Column("account")
    private String account;
    @Column("password")
    private String password;

}
