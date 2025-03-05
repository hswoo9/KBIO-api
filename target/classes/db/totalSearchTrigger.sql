drop trigger `schm_bio_cms`.`pst_after_insert`;
drop trigger `schm_bio_cms`.`pst_after_update`;
drop trigger `schm_bio_cms`.`pst_after_delete`;

drop trigger `schm_bio_com`.`file_after_insert`;
drop trigger `schm_bio_com`.`file_after_delete`;

/* 게시글 트리거 */
create trigger `schm_bio_cms`.`pst_after_insert`
    after insert on `schm_bio_cms`.`tbl_pst`
    for each row
begin
    insert into `schm_bio_com`.`tbl_intg_srch` (PST_SN, MENU_SN, KND, URL, TTL, CN, FRST_CRT_DT, CREATR_SN)
    SELECT
        NEW.PST_SN,
        menu.MENU_SN,
        'pst',
        menu.MENU_PATH_NM,
        NEW.PST_TTL,
        NEW.PST_CN,
        NEW.FRST_CRT_DT,
        NEW.CREATR_SN
    FROM
        `schm_bio_cms`.`tbl_menu` menu
    WHERE menu.BBS_SN = NEW.BBS_SN;
end;

/* 게시글 수정 트리거 */
create trigger `schm_bio_cms`.`pst_after_update`
    after update on `schm_bio_cms`.`tbl_pst`
    for each row
begin
    IF OLD.PST_CN <> NEW.PST_CN OR OLD.PST_TTL <> NEW.PST_TTL THEN
    update `schm_bio_com`.`tbl_intg_srch`
    set TTL = NEW.PST_TTL,
        CN = NEW.PST_CN,
        MDFCN_DT = NEW.MDFCN_DT,
        MDFR_SN = NEW.MDFR_SN
    WHERE PST_SN = NEW.PST_SN;
END IF;
end;

/* 게시글 삭제 트리거 */
create trigger `schm_bio_cms`.`pst_after_delete`
    after delete on `schm_bio_cms`.`tbl_pst`
    for each row
begin
    delete from `schm_bio_com`.`tbl_intg_srch` where PST_SN = OLD.PST_SN;
end;


/* 파일 트리거 */
create trigger `schm_bio_com`.`file_after_insert`
    after insert on `schm_bio_com`.`tbl_com_file`
    for each row
begin
    IF NEW.PSN_TBL_SN LIKE 'pst_%' THEN
    insert into `schm_bio_com`.`tbl_intg_srch` (PST_SN, MENU_SN, ATCH_FILE_SN, KND, URL, ATCH_FILE_NM, ATCH_FILE_EXTN_NM, FRST_CRT_DT, CREATR_SN)
    SELECT
        pst.PST_SN,
        menu.MENU_SN,
        NEW.ATCH_FILE_SN,
        'file',
        menu.MENU_PATH_NM,
        NEW.ATCH_FILE_NM,
        NEW.ATCH_FILE_EXTN_NM,
        NEW.FRST_CRT_DT,
        NEW.CREATR_SN
    FROM
        `schm_bio_cms`.`tbl_pst` pst
            JOIN `schm_bio_cms`.`tbl_menu` menu
                 on pst.BBS_SN = menu.BBS_SN
    WHERE pst.PST_SN = REPLACE(NEW.PSN_TBL_SN, 'pst_', '');
END IF;
end;


/* 파일 삭제 트리거 */
create trigger `schm_bio_com`.`file_after_delete`
    after delete on `schm_bio_com`.`tbl_com_file`
    for each row
begin
    IF OLD.PSN_TBL_SN LIKE 'pst_%' THEN
    delete from `schm_bio_com`.`tbl_intg_srch` where ATCH_FILE_SN = OLD.ATCH_FILE_SN;
END IF;
end;


