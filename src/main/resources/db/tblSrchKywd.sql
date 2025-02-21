create table tbl_srch_kywd
(
    SRCH_KYWD_SN int(22) auto_increment
        primary key,
    KYWD         VARCHAR(256)                         null comment '검색키워드',
    SRCH_NMTM    int(22)                              null comment '검색횟수',
    FRST_CRT_DT  DATETIME default current_timestamp() null comment '최초생성일시',
    MDFCN_DT     DATETIME default current_timestamp() null on update current_timestamp() comment '수정일시'
)
    comment '검색키워드' collate = utf8mb4_general_ci;