package egovframework.com.devjitsu.util;

import egovframework.com.devjitsu.model.common.TblComFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class FileUtil {

    @Value("#{properties['File.Server.Dir']}")
    private String serverDir;

    @Value("#{properties['File.Server.Path']}")
    private String serverPath;

    @Value("#{properties['File.Base.Directory']}")
    private String baseDir;

    public List<TblComFile> multiFileUpload(MultipartFile[] files, Map<String, Object> params) {
        List<TblComFile> list = new ArrayList();
        String filename = "";

        for(int i = 0; i < files.length; ++i) {
            TblComFile tblFile = new TblComFile();
            filename = files[i].getOriginalFilename();
            String fileUUID = randomUUID(filename);
            this.uploadService(files[i], filePath(params, serverDir), fileUUID);
            tblFile.setStrgFileNm(fileUUID);

            String[] fileNames = filename.split("[.]");
            if(fileNames.length > 2){
                String fileOrgName = "";
                for(int j = 0; j < fileNames.length - 1; j++){
                    fileOrgName += "_" + fileNames[j];
                }
                tblFile.setAtchFileNm(fileOrgName.substring(1));
            }else{
                tblFile.setAtchFileNm(filename.split("[.]")[0]);
            }
            tblFile.setAtchFilePathNm(filePath(params, baseDir));
            tblFile.setAtchFileSz((int) files[i].getSize());
            tblFile.setAtchFileExtnNm(filename.split("[.]")[fileNames.length-1]);
            tblFile.setCreatrSn(Integer.parseInt(params.get("regUserSn").toString()));
            tblFile.setPsnTblPk(params.get("psnCd") + "_" + params.get("key"));
            list.add(tblFile);
        }

        return list;
    }

    private void uploadService(MultipartFile file, String dir, String UUID) {
        try {
            File newPath = new File(dir);
            if (!newPath.exists()) {
                newPath.mkdirs();
            }

            Path path = Paths.get(dir + UUID);
            Files.copy(file.getInputStream(), path, new CopyOption[]{StandardCopyOption.REPLACE_EXISTING});
        } catch (IOException var6) {
            IOException e = var6;
            e.printStackTrace();
        }
    }

    public static String randomUUID(String str) {
        String extension = str.substring(str.lastIndexOf("."), str.length());
        UUID uuid = UUID.randomUUID();
        String strUUID = uuid.toString().toString().replaceAll("-", "") + extension;
        return strUUID;
    }

    private String filePath (Map<String, Object> params, String dir){
        String path = dir + "/" + params.get("psnCd") + "/" + params.get("key") + "/";
        return path;
    }
}
