package egovframework.com.devjitsu.service.menu;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.Expressions;
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
import java.util.*;

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
            if (!StringUtils.isEmpty(dto.get("actvtnYn"))) {
                builder.and(tblMenu.actvtnYn.eq((String) dto.get("actvtnYn")));
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

    public ResultVO getMenu(TblMenu tblMenu) {
        ResultVO resultVO = new ResultVO();
        try {
            resultVO.putResult("menu", tblMenuRepository.findByMenuSn(tblMenu.getMenuSn()));
            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        }catch (Exception e){
            resultVO.setResultCode(ResponseCode.SELECT_ERROR.getCode());
        }

        return resultVO;
    }

    public ResultVO setMenu(TblMenu tblMenu){
        ResultVO resultVO = new ResultVO();
        try {
            QTblMenu qTblMenu = QTblMenu.tblMenu;
            QTblMenu qTblMenu1 = new QTblMenu("qTblMenu1");
            QTblMenu qTblMenu2 = new QTblMenu("qTblMenu2");
            JPAQueryFactory q = new JPAQueryFactory(em);
            BooleanBuilder builder = new BooleanBuilder();
            builder.and(qTblMenu.actvtnYn.eq(tblMenu.getActvtnYn()));

            List<TblMenu> menus = q
                    .selectFrom(qTblMenu)
                    .where(
                        qTblMenu.upperMenuSn.eq(tblMenu.getUpperMenuSn())
                            .and(qTblMenu.menuSortSeq.goe(tblMenu.getMenuSortSeq()))
                    )
                    .fetch();

            for(TblMenu menu : menus){
                String menuFullPath = q
                        .select(qTblMenu2.menuWholPath.concat(
                            Expressions.stringTemplate("SUBSTRING(CONCAT('000', {0}), LENGTH(CONCAT('000', {0})) - 3, 4)", qTblMenu1.menuSortSeq.add(1)).concat("|")
                        ))
                        .from(qTblMenu1)
                        .join(qTblMenu2)
                        .on(qTblMenu1.upperMenuSn.eq(qTblMenu2.menuSn))
                        .where(qTblMenu1.menuSn.eq(menu.getMenuSn()))
                        .fetchFirst();

                q.update(qTblMenu)
                    .set(qTblMenu.menuSortSeq, qTblMenu.menuSortSeq.add(1))
                    .set(qTblMenu.menuWholPath, menuFullPath)
                    .where(
                            qTblMenu.menuSn.eq(menu.getMenuSn())
                    )
                    .execute();
            }


            tblMenu.setMenuWholPath(
                q.select(
                    qTblMenu.menuWholPath.concat(
                        Expressions.stringTemplate("SUBSTRING(CONCAT('000', {0}), LENGTH(CONCAT('000', {0})) - 3, 4)", tblMenu.getMenuSortSeq()).concat("|")
                ))
                .from(qTblMenu)
                .where(qTblMenu.menuSn.eq(tblMenu.getUpperMenuSn()))
                .fetchFirst()
            );

            tblMenuRepository.save(tblMenu);
            q.update(qTblMenu)
                .set(qTblMenu.lwrMenuEn, "Y")
                .where(
                        qTblMenu.menuSn.eq(tblMenu.getUpperMenuSn())
                )
                .execute();

            tblMenuRepository.setMenuPathAllUpd();
            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        }catch (Exception e){
            resultVO.setResultCode(ResponseCode.SAVE_ERROR.getCode());
        }

        return resultVO;
    }

    public ResultVO setMenuDel(SearchDto dto){
        ResultVO resultVO = new ResultVO();

        try {
            String[] menuSns = dto.get("menuSns").toString().split(",");
            for(String menuSn : menuSns){
                deleteMenuRecursively(tblMenuRepository.findByMenuSn(Integer.parseInt(menuSn)));
            }
            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        }catch (Exception e){
            resultVO.setResultCode(ResponseCode.DELETE_ERROR.getCode());
        }

        return resultVO;
    }

    private void deleteMenuRecursively(TblMenu tblMenu) {
        QTblMenu qTblMenu = QTblMenu.tblMenu;
        JPAQueryFactory q = new JPAQueryFactory(em);
        List<TblMenu> subMenus = q.selectFrom(qTblMenu).where(qTblMenu.upperMenuSn.eq(tblMenu.getMenuSn())).fetch();
        for (TblMenu subMenu : subMenus) {
            deleteMenuRecursively(subMenu);
        }
        tblMenuRepository.delete(tblMenu);
    }
}
