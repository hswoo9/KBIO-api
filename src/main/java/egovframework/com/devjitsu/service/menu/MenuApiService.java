package egovframework.com.devjitsu.service.menu;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import egovframework.com.cmm.LoginVO;
import egovframework.com.cmm.ResponseCode;
import egovframework.com.cmm.service.ResultVO;
import egovframework.com.devjitsu.model.common.SearchDto;
import egovframework.com.devjitsu.model.login.LettnemplyrinfoVO;
import egovframework.com.devjitsu.model.login.QLettnemplyrinfoVO;
import egovframework.com.devjitsu.model.menu.QTblMenu;
import egovframework.com.devjitsu.model.menu.TblMenu;
import egovframework.com.devjitsu.repository.login.LettnemplyrinfoRepository;
import egovframework.com.devjitsu.repository.menu.TblMenuRepository;
import egovframework.let.utl.sim.service.EgovFileScrty;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class MenuApiService {

    private final EntityManager em;
    private final TblMenuRepository tblMenuRepository;
    /**
     * jpa 부등호
     * gt : >
     * lt : <
     * goe : >=
     * loe : <=
     */
    /**
     *  query DSL 조건 추가하는 방법
     *  BooleanBuilder builder = new BooleanBuilder();
     *  builder.and(qTblComCdGroup.actvtnYn.eq("Y"));
     * */

    public ResultVO getMenuTreeList(SearchDto dto) {
        ResultVO resultVO = new ResultVO();

        try {
            QTblMenu tblMenu = QTblMenu.tblMenu;
            JPAQueryFactory q = new JPAQueryFactory(em);

            /** query DSL 조건 추가하는 방법 */
            BooleanBuilder builder = new BooleanBuilder();
            if (!StringUtils.isEmpty(dto.get("active"))) {
                builder.and(tblMenu.actvtnYn.eq((String) dto.get("active")));
            }

            List<TblMenu> menus = q.selectFrom(tblMenu).where(builder).orderBy(tblMenu.menuWholPath.asc()).fetch();
            List<TblMenu> returnMenus = new ArrayList<>();

            for (TblMenu menu : menus) {
                for (TblMenu menu2 : menus) {
                    if(menu2.getUpperMenuSn() == menu.getMenuSn()){
                        List<TblMenu> subMenu = new ArrayList();
                        if(menu.getChildTblMenu().size() > 0){
                            subMenu = menu.getChildTblMenu();
                            subMenu.add(menu2);
                            menu.setChildTblMenu(subMenu);
                        }else{
                            subMenu.add(menu2);
                            menu.setChildTblMenu(subMenu);
                        }
                    }
                }

                if(menu.getMenuSeq() == 0){
                    returnMenus.add(menu);
                }
            }
            resultVO.putResult("menus", returnMenus);
            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        }catch (Exception e) {
            resultVO.setResultCode(ResponseCode.SELECT_ERROR.getCode());
        }

        return resultVO;
    }

    public ResultVO getMenu(SearchDto dto) {
        ResultVO resultVO = new ResultVO();
        if(StringUtils.isEmpty(dto.get("menuId"))) {
            resultVO.setResultCode(ResponseCode.SELECT_REQUIRE_ERROR.getCode());
            resultVO.setResultMessage(ResponseCode.SELECT_REQUIRE_ERROR.getMessage());

            return resultVO;
        }

//        resultVO.putResult("menu", tblMenuRepository.findById(dto.get("menuId")));
        resultVO.setResultCode(ResponseCode.SUCCESS.getCode());


        return resultVO;
    }

    public ResultVO setMenu(Map<String, Object> params){
        ResultVO resultVO = new ResultVO();

        QTblMenu qTblMenu = QTblMenu.tblMenu;
        QTblMenu qTblMenu1 = new QTblMenu("qTblMenu1");
        QTblMenu qTblMenu2 = new QTblMenu("qTblMenu2");
        JPAQueryFactory q = new JPAQueryFactory(em);
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qTblMenu.actvtnYn.eq((String) params.get("active")));

        List<TblMenu> menus = q
                .selectFrom(qTblMenu)
                .where(
                        qTblMenu.upperMenuSn.eq((int) Long.parseLong((String) params.get("upperMenuSn")))
                                .and(qTblMenu.menuSortseq.goe(Long.parseLong((String) params.get("menuSortseq"))))
                )
                .fetch();

        return resultVO;
    }
}
