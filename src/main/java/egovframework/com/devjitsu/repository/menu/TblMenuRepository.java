package egovframework.com.devjitsu.repository.menu;

import egovframework.com.devjitsu.model.consult.TblCnsltDtl;
import egovframework.com.devjitsu.model.menu.MenuDto;
import egovframework.com.devjitsu.model.menu.TblMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TblMenuRepository extends JpaRepository<TblMenu, String> {

    TblMenu findByMenuSn(long menuSn);

    @Query(value =
        "WITH RECURSIVE CTE AS (" +
        "SELECT " +
            "A.MENU_SN," +
            "A.UPPER_MENU_SN," +
            "A.MENU_SEQ," +
            "MENU_NM," +
            "MENU_NM_PATH," +
            "CAST(CONCAT(A.MENU_SN, '|') AS CHAR(100) CHARACTER SET UTF8) AS MENU_SN_PATH," +
            "CAST(CONCAT('000', MENU_SORT_SEQ, '|') AS CHAR(100) CHARACTER SET UTF8) AS MENU_WHOL_PATH," +
            "A.MENU_TYPE," +
            "A.BBS_SN," +
            "A.MENU_PATH_NM," +
            "A.LWR_MENU_EN " +
        "FROM " +
            "SCHM_BIO_CMS.TBL_MENU A " +
        "JOIN " +
            "SCHM_BIO_CMS.TBL_AUTHRT_GROUP_MENU B " +
        "ON A.MENU_SN = B.MENU_SN " +
        "WHERE " +
        "   A.ACTVTN_YN = 'Y' " +
        "AND " +
            "A.UPPER_MENU_SN = 0 " +
        "AND " +
            "A.MENU_SN = (SELECT SUBSTRING_INDEX(MENU_SN_PATH, '|', 1) FROM SCHM_BIO_CMS.TBL_MENU WHERE MENU_SN = :menuSn) " +
        "UNION ALL " +
        "SELECT " +
            "P.MENU_SN," +
            "P.UPPER_MENU_SN," +
            "P.MENU_SEQ," +
            "P.MENU_NM," +
            "CONCAT(C.MENU_NM_PATH, ' > ', P.MENU_NM) AS MENU_NM_PATH," +
            "CONCAT(C.MENU_SN_PATH, CONCAT(P.MENU_SN, '|')) AS MENU_SN_PATH," +
            "CONCAT(C.MENU_WHOL_PATH, RIGHT(CONCAT('000', P.MENU_SORT_SEQ), 4), '|') AS MENU_WHOL_PATH," +
            "P.MENU_TYPE," +
            "P.BBS_SN," +
            "P.MENU_PATH_NM," +
            "P.LWR_MENU_EN " +
        "FROM " +
            "SCHM_BIO_CMS.TBL_MENU P " +
        "INNER JOIN " +
            "CTE C " +
        "ON C.MENU_SN = P.UPPER_MENU_SN " +
        "WHERE " +
            "P.ACTVTN_YN = 'Y'" +
        ") " +
        "SELECT " +
            "A.MENU_SN," +
            "UPPER_MENU_SN," +
            "MENU_SEQ," +
            "MENU_NM," +
            "MENU_NM_PATH," +
            "MENU_SN_PATH," +
            "MENU_WHOL_PATH," +
            "MENU_TYPE," +
            "BBS_SN," +
            "MENU_PATH_NM," +
            "LWR_MENU_EN " +
        "FROM CTE A " +
        "JOIN " +
            "SCHM_BIO_CMS.TBL_AUTHRT_GROUP_MENU B " +
        "ON A.MENU_SN = B.MENU_SN " +
        "WHERE " +
            "FIND_IN_SET(B.AUTHRT_GROUP_SN, :menuAuthrtGroupSn) > 0 " +
        "GROUP BY MENU_SN " +
        "ORDER BY MENU_WHOL_PATH", nativeQuery = true)
    List<String[]> getLeftMenu(@Param("menuSn") long menuSn, @Param("menuAuthrtGroupSn") String menuAuthrtGroupSn);


    @Query(value =
            "UPDATE " +
                "SCHM_BIO_CMS.TBL_MENU M, " +
                "(" +
                    "WITH RECURSIVE CTE AS (" +
                        "SELECT " +
                            "MENU_SN," +
                            "MENU_NM," +
                            "MENU_NM AS MENU_NM_PATH," +
                            "CAST(CONCAT(MENU_SN, '|') AS CHAR(100) CHARACTER SET UTF8) AS MENU_SN_PATH," +
                            "CAST(CONCAT(RIGHT(CONCAT('000', MENU_SORT_SEQ), 4), '|') AS CHAR(100) CHARACTER SET UTF8) AS MENU_WHOL_PATH " +
                        "FROM " +
                            "SCHM_BIO_CMS.TBL_MENU " +
                        "WHERE " +
                            "UPPER_MENU_SN = 0 " +
                        "UNION ALL " +
                        "SELECT " +
                            "P.MENU_SN," +
                            "P.MENU_NM," +
                            "CONCAT(C.MENU_NM_PATH, ' > ', P.MENU_NM) AS MENU_NM_PATH," +
                            "CONCAT(C.MENU_SN_PATH, CONCAT(P.MENU_SN, '|')) AS MENU_SN_PATH," +
                            "CONCAT(C.MENU_WHOL_PATH, RIGHT(CONCAT('000', P.MENU_SORT_SEQ), 4), '|') AS MENU_WHOL_PATH " +
                        "FROM " +
                            "SCHM_BIO_CMS.TBL_MENU P " +
                        "INNER JOIN " +
                            "CTE C " +
                        "ON C.MENU_SN = P.UPPER_MENU_SN" +
                    ")" +
                    "SELECT " +
                        "MENU_SN," +
                        "MENU_NM_PATH," +
                        "MENU_SN_PATH," +
                        "MENU_WHOL_PATH " +
                    "FROM CTE" +
                ")b " +
                "SET " +
                    "M.MENU_NM_PATH = b.MENU_NM_PATH," +
                    "M.MENU_SN_PATH = b.MENU_SN_PATH," +
                    "M.MENU_WHOL_PATH = b.MENU_WHOL_PATH " +
                "WHERE " +
                    "M.MENU_SN = b.MENU_SN", nativeQuery = true)
    void setMenuPathAllUpd();
}
