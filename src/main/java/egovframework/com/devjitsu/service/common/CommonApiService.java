package egovframework.com.devjitsu.service.common;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.Expressions;
import egovframework.com.cmm.ResponseCode;
import egovframework.com.cmm.service.EgovFileMngUtil;
import egovframework.com.cmm.service.ResultVO;
import egovframework.com.cmm.util.AccessIP;
import egovframework.com.devjitsu.model.bbs.PstDto;
import egovframework.com.devjitsu.model.bbs.TblPst;
import egovframework.com.devjitsu.model.common.*;
import egovframework.com.devjitsu.model.menu.*;
import egovframework.com.devjitsu.model.menu.QTblAuthrtGroupMenu;
import egovframework.com.devjitsu.model.menu.QTblMenu;
import egovframework.com.devjitsu.model.user.TblUser;
import egovframework.com.devjitsu.repository.bbs.TblPstRepository;
import egovframework.com.devjitsu.repository.code.TblComCdGroupRepository;
import egovframework.com.devjitsu.repository.code.TblComCdRepository;
import egovframework.com.devjitsu.repository.common.TblAtchFileDwnldCntRepository;
import egovframework.com.devjitsu.repository.common.TblComFileRepository;
import egovframework.com.devjitsu.repository.menu.TblMenuAuthrtGroupRepository;
import egovframework.com.devjitsu.repository.menu.TblMenuRepository;
import egovframework.com.devjitsu.service.access.MngrAcsIpApiService;
import egovframework.com.devjitsu.service.menu.MenuAuthGroupApiService;
import lombok.RequiredArgsConstructor;
import org.egovframe.rte.fdl.property.EgovPropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.net.ssl.*;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static egovframework.com.devjitsu.model.common.QTblComFile.tblComFile;

@Service
@RequiredArgsConstructor
@Transactional
public class CommonApiService {

    private final EntityManager em;

    @Autowired
    private RedisApiService redisApiService;

    @Autowired
    private MenuAuthGroupApiService menuAuthGroupApiService;

    @Autowired
    private MngrAcsIpApiService mngrAcsIpApiService;

    @Resource(name = "propertiesService")
    protected EgovPropertyService propertyService;

    @Resource(name = "EgovFileMngUtil")
    private EgovFileMngUtil fileUtil;

    /**
     * jpa 부등호
     * gt : >
     * lt : <
     * goe : >=
     * loe : <=
     */
    /**
     * query DSL 조건 추가하는 방법
     * BooleanBuilder builder = new BooleanBuilder();
     * builder.and(qTblComCdGroup.actvtnYn.eq("Y"));
     */
    private final TblComCdRepository tblComCdRepository;
    private final TblComCdGroupRepository tblComCdGroupRepository;
    private final TblComFileRepository tblComFileRepository;
    private final TblAtchFileDwnldCntRepository tblAtchFileDwnldCntRepository;

    private final TblMenuAuthrtGroupRepository tblMenuAuthrtGroupRepository;
    private final TblMenuRepository tblMenuRepository;
    private final TblPstRepository tblPstRepository;

    public ResultVO getMenu(HttpServletRequest request) {
        ResultVO resultVO = new ResultVO();

        try {
            SearchDto dto = (SearchDto) request.getAttribute("searchDto");

            JPAQueryFactory q = new JPAQueryFactory(em);
            QTblMenu qTblMenu = QTblMenu.tblMenu;
            QTblAuthrtGroupMenu qTblAuthrtGroupMenu = QTblAuthrtGroupMenu.tblAuthrtGroupMenu;

            String menuAuthrtGroups = "1";

            if(!StringUtils.isEmpty(dto.get("userSn"))){
                menuAuthrtGroups = menuAuthGroupApiService.getMenuAuthrtGroups(Long.parseLong(String.valueOf(dto.get("userSn"))));
            }

            BooleanBuilder builder = new BooleanBuilder();
            builder.and(qTblMenu.actvtnYn.eq("Y"));
            builder.and(Expressions.booleanTemplate("FIND_IN_SET({0}, {1}) > 0", qTblAuthrtGroupMenu.authrtGroupSn, menuAuthrtGroups));

            if(!StringUtils.isEmpty(dto.get("menuSeq"))){
                builder.and(qTblMenu.menuSeq.eq(Long.valueOf(dto.get("menuSeq").toString())));
            }

            if(!StringUtils.isEmpty(dto.get("upperMenuSn"))){
                builder.and(qTblMenu.upperMenuSn.eq(Long.valueOf(dto.get("upperMenuSn").toString())));
            }

            List<TblMenu> menuList = 
                    q.selectFrom(qTblMenu)
                        .join(qTblAuthrtGroupMenu).on(qTblMenu.menuSn.eq(qTblAuthrtGroupMenu.menuSn))
                        .where(builder).groupBy(qTblMenu.menuSn).orderBy(qTblMenu.upperMenuSn.asc(), qTblMenu.menuSortSeq.asc()).fetch();

            resultVO.putResult("menuList", menuList);
            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        } catch (Exception e) {
            e.printStackTrace();
            resultVO.setResultCode(ResponseCode.SELECT_ERROR.getCode());
        }

        return resultVO;
    }


    public ResultVO getLeftMenu(HttpServletRequest request) {
        ResultVO resultVO = new ResultVO();

        try {
            SearchDto dto = (SearchDto) request.getAttribute("searchDto");

            String menuAuthrtGroups = "1";
            if(!StringUtils.isEmpty(dto.get("userSn"))){
                menuAuthrtGroups = menuAuthGroupApiService.getMenuAuthrtGroups(Long.parseLong(String.valueOf(dto.get("userSn"))));
            }
            resultVO.putResult("leftMenuList", getLeftMenuList(Long.parseLong(dto.get("menuSn").toString()), menuAuthrtGroups));
            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        } catch (Exception e) {
            e.printStackTrace();
            resultVO.setResultCode(ResponseCode.SELECT_ERROR.getCode());
        }

        return resultVO;
    }

    private List<MenuDto> getLeftMenuList(long menuSn, String menuAuthrtGroups){
        List<String[]> results = tblMenuRepository.getLeftMenu(menuSn, menuAuthrtGroups);
        List<MenuDto> leftMenuList = new ArrayList<>();
        for (String[] result : results) {

            Long bbsSn = null;
            if (result[8] != null) { // null 체크 먼저 수행
                bbsSn = Long.parseLong(result[8].toString());
            }

            MenuDto menuDto = new MenuDto(
                    Long.valueOf(result[0]),
                    Long.valueOf(result[1]),
                    Long.valueOf(result[2]),
                    result[3],
                    result[4],
                    result[5],
                    result[6],
                    result[7],
                    bbsSn,
                    result[9],
                    result[10]
            );

            leftMenuList.add(menuDto);
        }

        return leftMenuList;
    }

    public ResultVO getMngrAcsIpChk(HttpServletRequest request) {
        ResultVO resultVO = new ResultVO();

        try {
            String clientIp = request.getRemoteAddr();
            System.out.println("clientIp = " + clientIp);
            List<String> mngrAcsIps = mngrAcsIpApiService.getMngrIps();

            if (!mngrAcsIps.contains(clientIp)) {
                resultVO.setResultCode(ResponseCode.AUTH_IP_ERROR.getCode());
                resultVO.setResultMessage(ResponseCode.AUTH_IP_ERROR.getMessage());
            }else{
                resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultVO.setResultCode(ResponseCode.AUTH_IP_ERROR.getCode());
            resultVO.setResultMessage(ResponseCode.AUTH_IP_ERROR.getMessage());
        }

        return resultVO;
    }

    public ResultVO getComCdGroupList(Map<String, Object> params) {
        ResultVO resultVO = new ResultVO();

        QTblComCdGroup qTblComCdGroup = QTblComCdGroup.tblComCdGroup;
        JPAQueryFactory q = new JPAQueryFactory(em);

        /** query DSL 조건 추가하는 방법 */
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qTblComCdGroup.actvtnYn.eq("Y"));

        resultVO.putResult("rs", q.selectFrom(qTblComCdGroup).where(builder).orderBy(qTblComCdGroup.frstCrtDt.desc()).fetch());
        resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        return resultVO;
    }

    public ResultVO getRedisUserInfo(SearchDto dto) {
        ResultVO resultVO = new ResultVO();
        resultVO.putResult("rs", redisApiService.getRedis(0, String.valueOf(dto.get("userSn"))));
        resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        resultVO.setResultMessage(ResponseCode.SUCCESS.getMessage());
        return resultVO;
    }

    public ResultVO getFile(TblComFile tblComFile) {
        ResultVO resultVO = new ResultVO();

        try {
            if(!StringUtils.isEmpty(tblComFile.getAtchFileSn())){
                tblComFile = tblComFileRepository.findByAtchFileSn(tblComFile.getAtchFileSn());
            }else{
                tblComFile = tblComFileRepository.findByPsnTblSn(tblComFile.getPsnTblSn());
            }

            resultVO.putResult("file", tblComFile);
            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        } catch (Exception e) {
            e.printStackTrace();
            resultVO.setResultCode(ResponseCode.DELETE_ERROR.getCode());
        }

        return resultVO;
    }

    public ResultVO setCkEditorFiles(List<MultipartFile> files){
        ResultVO resultVO = new ResultVO();
        List<TblComFile> filesList = new ArrayList<>();
        try{
            if(files != null){
                filesList = fileUtil.devFileInf(
                        files,
                        "/ckeditor/",
                        "ckeditor",
                        0
                );
            }
            resultVO.putResult("files", filesList);
            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        }catch (Exception e) {
            e.printStackTrace();
            resultVO.setResultCode(ResponseCode.DELETE_ERROR.getCode());
        }
        return resultVO;
    }

    public ResponseEntity<org.springframework.core.io.Resource> getFileDownLoad(SearchDto dto) throws IOException {
        TblComFile tblComFile = tblComFileRepository.findByAtchFileSn(Long.valueOf(dto.get("atchFileSn").toString()));
        String fileNm = tblComFile.getAtchFileNm();
        Path filePath = Paths.get(tblComFile.getAtchFilePathNm() + "/" + tblComFile.getStrgFileNm() + "." + tblComFile.getAtchFileExtnNm());
        org.springframework.core.io.Resource resource = new UrlResource(filePath.toUri());

        if (!resource.exists() || !resource.isReadable()) {
            throw new FileNotFoundException("파일을 찾을 수 없습니다: " + fileNm);
        }

        String contentType = Files.probeContentType(filePath);
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        if(!StringUtils.isEmpty(dto.get("trgtTblNm"))){
            QTblAtchFileDwnldCnt qTblAtchFileDwnldCnt = QTblAtchFileDwnldCnt.tblAtchFileDwnldCnt;
            JPAQueryFactory q = new JPAQueryFactory(em);
            BooleanBuilder builder = new BooleanBuilder();

            builder.and(
                Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m-%d')", qTblAtchFileDwnldCnt.frstCrtDt).eq(
                    Expressions.stringTemplate("DATE_FORMAT(NOW(), '%Y-%m-%d')")
                )
            );

            if(tblComFile.getPsnTblSn().startsWith("pst_")){
                builder.and(qTblAtchFileDwnldCnt.trgtTblNm.eq(dto.get("trgtTblNm").toString())
                    .and(qTblAtchFileDwnldCnt.trgtSn.eq(Long.valueOf(dto.get("trgtSn").toString())))
                );
            }else{
                builder.and(qTblAtchFileDwnldCnt.trgtTblNm.eq(dto.get("trgtTblNm").toString()));
            }

            TblAtchFileDwnldCnt tblAtchFileDwnldCnt = q
                    .selectFrom(qTblAtchFileDwnldCnt)
                    .where(builder).fetchFirst();

            if(tblAtchFileDwnldCnt != null){
                tblAtchFileDwnldCnt.setAtchFileDwnldCnt(tblAtchFileDwnldCnt.getAtchFileDwnldCnt() + 1);
            }else{
                tblAtchFileDwnldCnt = new TblAtchFileDwnldCnt();
                tblAtchFileDwnldCnt.setTrgtTblNm(dto.get("trgtTblNm").toString());
                tblAtchFileDwnldCnt.setTrgtSn(dto.get("trgtSn") == null ? null : Long.valueOf(dto.get("trgtSn").toString()));
                tblAtchFileDwnldCnt.setAtchFileDwnldCnt(1);
            }
            tblAtchFileDwnldCntRepository.save(tblAtchFileDwnldCnt);
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + URLEncoder.encode(fileNm, StandardCharsets.UTF_8.toString()) + "\"")
                .body(resource);
    }

    public ResponseEntity<org.springframework.core.io.Resource> getFileZipDownLoad(SearchDto dto) throws IOException {
        List<TblComFile> tblComFiles = tblComFileRepository.findAllByPsnTblSn((String) dto.get("psnTblSn"));

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream);

        for (TblComFile tblComFile : tblComFiles) {
            String fileNm = tblComFile.getAtchFileNm();
            Path filePath = Paths.get(tblComFile.getAtchFilePathNm() + "/" + tblComFile.getStrgFileNm() + "." + tblComFile.getAtchFileExtnNm());

            org.springframework.core.io.Resource resource = new UrlResource(filePath.toUri());
            if (!resource.exists() || !resource.isReadable()) {
                continue;
            }

            try (FileInputStream fileInputStream = new FileInputStream(filePath.toFile())) {
                ZipEntry zipEntry = new ZipEntry(fileNm);
                zipOutputStream.putNextEntry(zipEntry);

                byte[] buffer = new byte[1024];
                int length;
                while ((length = fileInputStream.read(buffer)) > 0) {
                    zipOutputStream.write(buffer, 0, length);
                }
                zipOutputStream.closeEntry();
            }
        }

        zipOutputStream.close();
        byte[] zipBytes = byteArrayOutputStream.toByteArray();

        ByteArrayResource resource = new ByteArrayResource(zipBytes);

        String zipFileName = dto.get("zipFileName") + ".zip";
        String encodedFileName = URLEncoder.encode(zipFileName, StandardCharsets.UTF_8.toString());

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + encodedFileName + "\"")
                .body(resource);
    }

    public ResultVO setFileDel(TblComFile tblComFile) {
        ResultVO resultVO = new ResultVO();

        try {
            boolean isDelete = fileUtil.deleteFile(new String[]{tblComFile.getStrgFileNm()}, tblComFile.getAtchFilePathNm());
            if (isDelete) {
                tblComFileRepository.delete(tblComFile);
            } else {
                throw new Exception();
            }
            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        } catch (Exception e) {
            e.printStackTrace();
            resultVO.setResultCode(ResponseCode.DELETE_ERROR.getCode());
        }

        return resultVO;
    }
}
