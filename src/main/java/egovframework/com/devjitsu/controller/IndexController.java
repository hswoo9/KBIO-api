package egovframework.com.devjitsu.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
public class IndexController {

    @PostMapping("/callback")
    public ResponseEntity<Map<String, Object>> callback(@RequestBody Map<String, Object> params) {
        Map<String, Object> s = new HashMap<>();

        System.out.println("params = " + params);

        if(Integer.parseInt(params.get("code").toString()) > 200){
            return new ResponseEntity<>(s, HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            return new ResponseEntity<>(s, HttpStatus.OK);
        }
    }
}
