package egovframework.com.devjitsu.service.menu;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import egovframework.com.cmm.ResponseCode;
import egovframework.com.cmm.service.ResultVO;
import egovframework.com.devjitsu.model.common.SearchDto;
import egovframework.com.devjitsu.model.menu.QTblAuthrtGroupMenu;
import egovframework.com.devjitsu.model.menu.QTblMenuAuthrtGroup;
import egovframework.com.devjitsu.model.menu.TblAuthrtGroupMenu;
import egovframework.com.devjitsu.model.menu.TblMenuAuthrtGroup;
import egovframework.com.devjitsu.repository.menu.TblAuthrtGroupMenuRepository;
import egovframework.com.devjitsu.repository.menu.TblMenuAuthrtGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class MenuAuthGroupApiService {

    private final EntityManager em;
    private final TblMenuAuthrtGroupRepository tblMenuAuthrtGroupRepository;
    private final TblAuthrtGroupMenuRepository tblAuthrtGroupMenuRepository;

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

    public ResultVO getMenuAuthGroup(TblMenuAuthrtGroup tblMenuAuthrtGroup) {
        ResultVO resultVO = new ResultVO();
        try {
            resultVO.putResult("menuAuthGroup", tblMenuAuthrtGroupRepository.findByAuthrtGroupSn(tblMenuAuthrtGroup.getAuthrtGroupSn()));
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
}
