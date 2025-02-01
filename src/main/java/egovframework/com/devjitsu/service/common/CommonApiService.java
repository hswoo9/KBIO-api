package egovframework.com.devjitsu.service.common;

import com.querydsl.core.BooleanBuilder;
import egovframework.com.cmm.ResponseCode;
import egovframework.com.cmm.service.EgovFileMngUtil;
import egovframework.com.cmm.service.ResultVO;
import egovframework.com.cmm.util.AccessIP;
import egovframework.com.devjitsu.model.common.QTblComCdGroup;
import egovframework.com.devjitsu.model.common.SearchDto;
import egovframework.com.devjitsu.model.common.TblComFile;
import egovframework.com.devjitsu.repository.code.TblComCdGroupRepository;
import egovframework.com.devjitsu.repository.code.TblComCdRepository;
import egovframework.com.devjitsu.repository.common.TblComFileRepository;
import egovframework.com.devjitsu.service.access.MngrAcsIpApiService;
import lombok.RequiredArgsConstructor;
import org.egovframe.rte.fdl.property.EgovPropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.annotation.Resource;
import javax.net.ssl.*;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class CommonApiService {

    private final EntityManager em;

    @Autowired
    private RedisApiService redisApiService;

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

    public ResponseEntity<org.springframework.core.io.Resource> getFileDownLoad(TblComFile tblComFile, HttpServletRequest request, HttpServletResponse response) throws IOException {
        tblComFile = tblComFileRepository.findByAtchFileSn(tblComFile.getAtchFileSn());
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

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + URLEncoder.encode(fileNm, StandardCharsets.UTF_8.toString()) + "\"")
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
