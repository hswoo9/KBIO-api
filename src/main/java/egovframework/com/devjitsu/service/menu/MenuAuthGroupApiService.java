package egovframework.com.devjitsu.service.menu;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import egovframework.com.cmm.ResponseCode;
import egovframework.com.cmm.service.ResultVO;
import egovframework.com.devjitsu.model.access.TblMngrAcsIp;
import egovframework.com.devjitsu.model.common.SearchDto;
import egovframework.com.devjitsu.model.menu.*;
import egovframework.com.devjitsu.model.menu.QTblAuthrtGroupMenu;
import egovframework.com.devjitsu.model.menu.QTblMenu;
import egovframework.com.devjitsu.model.menu.QTblMenuAuthrtGroup;
import egovframework.com.devjitsu.model.menu.QTblMenuAuthrtGroupUser;
import egovframework.com.devjitsu.repository.menu.TblAuthrtGroupMenuRepository;
import egovframework.com.devjitsu.repository.menu.TblMenuAuthrtGroupRepository;
import egovframework.com.devjitsu.repository.menu.TblMenuAuthrtGroupUserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections.bag.HashBag;
import org.egovframe.rte.fdl.property.EgovPropertyService;
import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MenuAuthGroupApiService {

    @Resource(name = "propertiesService")
    protected EgovPropertyService propertyService;

    private final EntityManager em;
    private final TblMenuAuthrtGroupRepository tblMenuAuthrtGroupRepository;
    private final TblAuthrtGroupMenuRepository tblAuthrtGroupMenuRepository;
    private final TblMenuAuthrtGroupUserRepository tblMenuAuthrtGroupUserRepository;

    public ResultVO getMenuAuthGroupList(SearchDto dto) {
        ResultVO resultVO = new ResultVO();

        try {
            QTblMenuAuthrtGroup qTblMenuAuthrtGroup = QTblMenuAuthrtGroup.tblMenuAuthrtGroup;
            JPAQueryFactory q = new JPAQueryFactory(em);

            /** query DSL 조건 추가하는 방법 */
            BooleanBuilder builder = new BooleanBuilder();
            if (!StringUtils.isEmpty(dto.get("authGroupName"))) {
                builder.and(qTblMenuAuthrtGroup.authrtGroupNm.contains((String) dto.get("authGroupName")));
            }

            List<TblMenuAuthrtGroup> authGroup = q.selectFrom(qTblMenuAuthrtGroup).where(builder).orderBy(qTblMenuAuthrtGroup.frstCrtDt.desc()).fetch();
            resultVO.putResult("menuAuthGroup", authGroup);
            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        }catch (Exception e) {
            resultVO.setResultCode(ResponseCode.SELECT_ERROR.getCode());
        }

        return resultVO;
    }

    public ResultVO getMenuAuthGroupListOnPage(SearchDto dto) {
        ResultVO resultVO = new ResultVO();
        PaginationInfo paginationInfo = new PaginationInfo();

        try {
            if (!StringUtils.isEmpty(dto.get("pageIndex"))) {
                paginationInfo.setCurrentPageNo(Integer.parseInt(dto.get("pageIndex").toString()));
            }
            paginationInfo.setRecordCountPerPage(propertyService.getInt("Globals.pageUnit"));
            paginationInfo.setPageSize(propertyService.getInt("Globals.pageSize"));

            QTblMenuAuthrtGroup qTblMenuAuthrtGroup = QTblMenuAuthrtGroup.tblMenuAuthrtGroup;
            JPAQueryFactory q = new JPAQueryFactory(em);

            /** query DSL 조건 추가하는 방법 */
            BooleanBuilder builder = new BooleanBuilder();
            if (!StringUtils.isEmpty(dto.get("authGroupName"))) {
                builder.and(qTblMenuAuthrtGroup.authrtGroupNm.contains((String) dto.get("authGroupName")));
            }

            List<TblMenuAuthrtGroup> authGroup = q.selectFrom(qTblMenuAuthrtGroup).where(builder).orderBy(qTblMenuAuthrtGroup.frstCrtDt.desc()).offset(paginationInfo.getFirstRecordIndex()).limit(paginationInfo.getRecordCountPerPage()).fetch();

            Long totCnt = q.select(qTblMenuAuthrtGroup.count())
                            .from(qTblMenuAuthrtGroup)
                    .where(builder).orderBy(qTblMenuAuthrtGroup.frstCrtDt.desc()).fetchOne();
            if(totCnt == null) totCnt = 0L;
            paginationInfo.setTotalRecordCount(totCnt.intValue());

            resultVO.putResult("authGroup", authGroup);
            resultVO.putPaginationInfo(paginationInfo);
            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        }catch (Exception e) {
            resultVO.setResultCode(ResponseCode.SELECT_ERROR.getCode());
        }

        return resultVO;
    }

    public ResultVO getMenuAuthGroup(TblMenuAuthrtGroup tblMenuAuthrtGroup) {
        ResultVO resultVO = new ResultVO();
        try {


            QTblAuthrtGroupMenu qtblAuthrtGroupMenu = QTblAuthrtGroupMenu.tblAuthrtGroupMenu;
            QTblMenu qtblMenu = QTblMenu.tblMenu;

            JPAQueryFactory q = new JPAQueryFactory(em);
            BooleanBuilder builder = new BooleanBuilder();

            TblMenuAuthrtGroup menuAuthGroup = tblMenuAuthrtGroupRepository.findByAuthrtGroupSn(tblMenuAuthrtGroup.getAuthrtGroupSn());
            if(!StringUtils.isEmpty(menuAuthGroup.getAuthrtGroupSn())){
                builder.and(qtblAuthrtGroupMenu.authrtGroupSn.eq(menuAuthGroup.getAuthrtGroupSn()));
                List<TblMenu> allowAccessMenus = q.selectFrom(qtblMenu).join(qtblAuthrtGroupMenu).on(qtblAuthrtGroupMenu.menuSn.eq(qtblMenu.menuSn)).where(builder).fetch();
                menuAuthGroup.setAllowAccessMenu(allowAccessMenus);
            }
            resultVO.putResult("menuAuthGroup", menuAuthGroup);
            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        }catch (Exception e){
            resultVO.setResultCode(ResponseCode.SELECT_ERROR.getCode());
        }

        return resultVO;
    }

    public ResultVO setMenuAuthGroup(TblMenuAuthrtGroup tblMenuAuthrtGroup) {
        ResultVO resultVO = new ResultVO();

        try {
            JPAQueryFactory q = new JPAQueryFactory(em);
            tblMenuAuthrtGroupRepository.save(tblMenuAuthrtGroup);

            QTblAuthrtGroupMenu qTblAuthrtGroupMenu = QTblAuthrtGroupMenu.tblAuthrtGroupMenu;
            q.delete(qTblAuthrtGroupMenu).where(qTblAuthrtGroupMenu.authrtGroupSn.eq(tblMenuAuthrtGroup.getAuthrtGroupSn())).execute();

            if(!StringUtils.isEmpty(tblMenuAuthrtGroup.getAllowAccessMenu())) {
                for(int i = 0; i < tblMenuAuthrtGroup.getAllowAccessMenu().size(); i++){

                    TblAuthrtGroupMenu tblAuthrtGroupMenu = new TblAuthrtGroupMenu();
                    tblAuthrtGroupMenu.setMenuSn(tblMenuAuthrtGroup.getAllowAccessMenu().get(i).getMenuSn());
                    tblAuthrtGroupMenu.setAuthrtGroupSn(tblMenuAuthrtGroup.getAuthrtGroupSn());
                    tblAuthrtGroupMenu.setCreatrSn(tblMenuAuthrtGroup.getCreatrSn());

                    long duplicateCnt = q
                            .selectFrom(qTblAuthrtGroupMenu)
                            .where(
                                qTblAuthrtGroupMenu.authrtGroupSn.eq(tblMenuAuthrtGroup.getAuthrtGroupSn())
                                    .and(qTblAuthrtGroupMenu.menuSn.eq(tblAuthrtGroupMenu.getMenuSn()))).fetchCount();
                    if(duplicateCnt == 0){
                        tblAuthrtGroupMenuRepository.save(tblAuthrtGroupMenu);
                    }
                }
            }

            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        }catch (Exception e){
            resultVO.setResultCode(ResponseCode.SAVE_ERROR.getCode());
        }

        return resultVO;
    }

    public ResultVO setMenuAuthGroupDel(SearchDto dto) {
        ResultVO resultVO = new ResultVO();

        try {
            JPAQueryFactory q = new JPAQueryFactory(em);
            QTblAuthrtGroupMenu qTblAuthrtGroupMenu = QTblAuthrtGroupMenu.tblAuthrtGroupMenu;
            QTblMenuAuthrtGroupUser qTblMenuAuthrtGroupUser = QTblMenuAuthrtGroupUser.tblMenuAuthrtGroupUser;

            String[] authrtGroupSns = dto.get("authrtGroupSns").toString().split(",");
            for(String authrtGroupSn : authrtGroupSns){
                TblMenuAuthrtGroup menuAuthrtGroup = tblMenuAuthrtGroupRepository.findByAuthrtGroupSn(Long.parseLong(authrtGroupSn));
                q.delete(qTblAuthrtGroupMenu).where(qTblAuthrtGroupMenu.authrtGroupSn.eq(menuAuthrtGroup.getAuthrtGroupSn())).execute();
                q.delete(qTblMenuAuthrtGroupUser).where(qTblMenuAuthrtGroupUser.authrtGroupSn.eq(menuAuthrtGroup.getAuthrtGroupSn())).execute();
                tblMenuAuthrtGroupRepository.delete(menuAuthrtGroup);
            }

            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        }catch (Exception e){
            resultVO.setResultCode(ResponseCode.DELETE_ERROR.getCode());
        }

        return resultVO;
    }

    public ResultVO getMenuAuthGroupUserList(SearchDto dto) {
        ResultVO resultVO = new ResultVO();

        try {
            QTblMenuAuthrtGroupUser qTblMenuAuthrtGroupUser = QTblMenuAuthrtGroupUser.tblMenuAuthrtGroupUser;
            JPAQueryFactory q = new JPAQueryFactory(em);

            BooleanBuilder builder = new BooleanBuilder();
            builder.and(qTblMenuAuthrtGroupUser.authrtGroupSn.eq(Long.parseLong((String) dto.get("authrtGroupSn"))));

            if (!StringUtils.isEmpty(dto.get("userId"))) {
                builder.and(qTblMenuAuthrtGroupUser.userId.contains((String) dto.get("userId")));
            }
            if (!StringUtils.isEmpty(dto.get("userNm"))) {
                builder.and(qTblMenuAuthrtGroupUser.userNm.contains((String) dto.get("userNm")));
            }

            List<TblMenuAuthrtGroupUser> menuAuthGroupUser = q.selectFrom(qTblMenuAuthrtGroupUser).where(builder).orderBy(qTblMenuAuthrtGroupUser.frstCrtDt.desc()).fetch();
            resultVO.putResult("menuAuthGroupUser",  menuAuthGroupUser);
            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        }catch (Exception e) {
            e.printStackTrace();
            resultVO.setResultCode(ResponseCode.SELECT_ERROR.getCode());
        }

        return resultVO;
    }

    public ResultVO setMenuAuthGroupUser(SearchDto dto) {
        ResultVO resultVO = new ResultVO();

        try {
            QTblMenuAuthrtGroupUser qTblMenuAuthrtGroupUser = QTblMenuAuthrtGroupUser.tblMenuAuthrtGroupUser;
            JPAQueryFactory q = new JPAQueryFactory(em);

            Gson gson = new Gson();
            List<TblMenuAuthrtGroupUser> tblMenuAuthrtGroupUsers = gson.fromJson(dto.get("authrtGroupUsers").toString(), new TypeToken<List<TblMenuAuthrtGroupUser>>() {}.getType());
            for(TblMenuAuthrtGroupUser t : tblMenuAuthrtGroupUsers){
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                if (StringUtils.isEmpty(t.getAuthrtGrntDt())) {
                    t.setAuthrtGrntDt(LocalDate.now().format(formatter));
                    t.setActvtnYn("Y");
                }else{
                    LocalDate authDt = LocalDate.parse(t.getAuthrtGrntDt(), formatter);
                    LocalDate currentDate = LocalDate.now();
                    if (authDt.isAfter(currentDate)) {
                        t.setActvtnYn("N");
                    } else {
                        t.setActvtnYn("Y");
                    }
                }

                long duplicateCnt = q
                        .selectFrom(qTblMenuAuthrtGroupUser)
                        .where(
                                qTblMenuAuthrtGroupUser.authrtGroupSn.eq(t.getAuthrtGroupSn())
                                        .and(qTblMenuAuthrtGroupUser.userSn.eq(t.getUserSn()))
                        ).fetchCount();

                if(duplicateCnt == 0){
                    tblMenuAuthrtGroupUserRepository.save(t);
                }else{
                    TblMenuAuthrtGroupUser existingAuthGrant = tblMenuAuthrtGroupUserRepository.findByAuthrtGroupSnAndUserSn(t.getAuthrtGroupSn(), t.getUserSn());
                    existingAuthGrant.setUserNm(t.getUserNm());
                    existingAuthGrant.setUserId(t.getUserId());
                    existingAuthGrant.setAuthrtGrntDt(t.getAuthrtGrntDt());
                    existingAuthGrant.setActvtnYn(t.getActvtnYn());
                    existingAuthGrant.setMdfrSn(t.getMdfrSn() == null ? t.getCreatrSn() : t.getMdfrSn());
                    tblMenuAuthrtGroupUserRepository.save(existingAuthGrant);
                }
            }

            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        }catch (Exception e) {
            e.printStackTrace();
            resultVO.setResultCode(ResponseCode.SELECT_ERROR.getCode());
        }


        return resultVO;
    }

    public ResultVO setMenuAuthGroupUserDel(SearchDto dto) {
        ResultVO resultVO = new ResultVO();

        try {
            JPAQueryFactory q = new JPAQueryFactory(em);
            QTblMenuAuthrtGroupUser qTblMenuAuthrtGroupUser = QTblMenuAuthrtGroupUser.tblMenuAuthrtGroupUser;

            String[] authrtGroupUserSns = dto.get("authrtGroupUserSns").toString().split(",");
            for(String authrtGroupUserSn : authrtGroupUserSns){
                q.delete(qTblMenuAuthrtGroupUser).where(qTblMenuAuthrtGroupUser.authrtGroupUserSn.eq(Long.valueOf(authrtGroupUserSn))).execute();

            }

            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        }catch (Exception e) {
            e.printStackTrace();
            resultVO.setResultCode(ResponseCode.SELECT_ERROR.getCode());
        }

        return resultVO;
    }

    public List<Long> getMenuAuthrtGroups(long userSn) {
        JPAQueryFactory q = new JPAQueryFactory(em);
        QTblMenuAuthrtGroupUser qTblMenuAuthrtGroupUser = QTblMenuAuthrtGroupUser.tblMenuAuthrtGroupUser;

        List<TblMenuAuthrtGroupUser> menuAuthrtGroupUsers = q.selectFrom(qTblMenuAuthrtGroupUser).where(qTblMenuAuthrtGroupUser.userSn.eq(userSn)).fetch();

        return menuAuthrtGroupUsers.stream()
                .map(TblMenuAuthrtGroupUser::getAuthrtGroupSn)
                .collect(Collectors.toList());
    }
}
