package egovframework.com.devjitsu.controller.user;

import egovframework.com.cmm.service.ResultVO;
import egovframework.com.devjitsu.model.user.TblMvnEnt;
import egovframework.com.devjitsu.service.user.MvnEntApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
public class MvnEntApiController {

    @Autowired
    private MvnEntApiService mvnEntApiService;

    @PostMapping("/mvnEntApi/setMvnEnt")
    public ResultVO setMvnEnt(@RequestBody TblMvnEnt tblMvnEnt){
        return mvnEntApiService.setMvnEnt(tblMvnEnt);
    }
}
