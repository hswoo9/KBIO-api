package egovframework.com.devjitsu.repository.bbs;

import com.querydsl.core.Tuple;
import egovframework.com.devjitsu.model.bbs.PstDto;
import egovframework.com.devjitsu.model.bbs.TblBbs;
import egovframework.com.devjitsu.model.bbs.TblPst;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TblPstRepository extends JpaRepository<TblPst, String> {

    TblPst findByPstSn(long pstSn);
    
    @Query(value =
            "SELECT " +
                "* " +
            "FROM ( " +
                "SELECT " +
                    "PST.PST_SN," +
                    "PST.PST_TTL," +
                    "CASE WHEN PST.PST_SN < :pstSn THEN 'PREV'" +
                         "WHEN PST.PST_SN > :pstSn THEN 'NEXT'" +
                    "END AS POSITION " +
                "FROM KBIO.TBL_PST PST " +
                "WHERE " +
                    "PST.BBS_SN = :bbsSn " +
                "ORDER BY " +
                    "CASE WHEN PST.UPEND_NTC_YN = 'Y' " +
                            "AND DATE_FORMAT(PST.NTC_BGNG_DT, '%Y-%m-%d') <= DATE_FORMAT(NOW(), '%Y-%m-%d') " +
                            "AND DATE_FORMAT(PST.NTC_END_DATE, '%Y-%m-%d') >= DATE_FORMAT(NOW(), '%Y-%m-%d') THEN 0 " +
                    "ELSE 1 " +
                    "END ASC," +
                    "PST.PST_GROUP DESC," +
                    "PST.CMNT_LEVEL ASC," +
                    "PST.FRST_CRT_DT DESC" +
            ") subquery " +
            "WHERE " +
                "(POSITION = 'PREV' AND PST_SN = (SELECT MAX(PST_SN) FROM KBIO.TBL_PST WHERE PST_SN < :pstSn AND BBS_SN = :bbsSn)) OR " +
                "(POSITION = 'NEXT' AND PST_SN = (SELECT MIN(PST_SN) FROM KBIO.TBL_PST WHERE PST_SN > :pstSn AND BBS_SN = :bbsSn));"
            , nativeQuery = true)
    List<Object[]> getPrevNextPSt(@Param("bbsSn") long bbsSn, @Param("pstSn") long pstSn);
}
