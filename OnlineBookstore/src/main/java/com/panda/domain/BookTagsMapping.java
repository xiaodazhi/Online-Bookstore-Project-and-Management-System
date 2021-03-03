package com.panda.domain;

import com.panda.utils.db.Column;
import com.panda.utils.db.ID;
import com.panda.utils.db.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@Table("book_tags_mapping")
public class BookTagsMapping {
    @ID
    @Column("id")
    private int id;
    @Column("book_id")
    private int bookId;
    @Column("tags_id")
    private int tagsId;
    @Column("ctime")
    private int ctime;
}
