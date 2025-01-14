package egovframework.com.devjitsu.repository.menu;

import egovframework.com.devjitsu.model.consult.TblCnsltDtl;
import egovframework.com.devjitsu.model.menu.TblMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface TblMenuRepository extends JpaRepository<TblMenu, String> {

    TblMenu findByMenuSn(int menuSn);

    @Query(value =
            "UPDATE " +
                "KBIO.TBL_MENU M, " +
                "(" +
                    "WITH RECURSIVE CTE AS (" +
                        "SELECT " +
                            "MENU_SN," +
                            "MENU_NM," +
                            "MENU_NM AS MENU_NM_PATH," +
                            "CAST(CONCAT(MENU_SN, '|') AS CHAR(100) CHARACTER SET UTF8) AS MENU_SN_PATH," +
                            "CAST(CONCAT(RIGHT(CONCAT('000', MENU_SORT_SEQ), 4), '|') AS CHAR(100) CHARACTER SET UTF8) AS MENU_WHOL_PATH " +
                        "FROM " +
                            "KBIO.TBL_MENU " +
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
                            "KBIO.TBL_MENU P " +
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
