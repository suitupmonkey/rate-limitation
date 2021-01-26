package com.example.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.response.ResponseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: demo
 * @description: home page
 * @author: louweiwei
 * @create: 2021-01-24 15:42
 */
@RequestMapping("/")
@Controller
public class FrontDoorController {
    private static Logger log = LoggerFactory.getLogger(FrontDoorController.class);

    private String BOARD = "[{\"themeId\":\"1\",\"theme\":\"music\",\"comment\":53476},{\"themeId\":\"2\",\"theme\":\"politics\",\"comment\":66736}]";

    @GetMapping({"/",""})
    public String frontDoor(){
        return "/frontDoor/frontDoor";
    }

    @GetMapping("/initializeData")
    @ResponseBody
    public ResponseResult initializeData(){
        ResponseResult result = new ResponseResult<>();
        List commentList = new ArrayList();

        // transfer json to a list
        try {
            commentList = JSONObject.parseObject(BOARD, ArrayList.class);
            if(commentList.size() < 1) return result;
        }catch (Exception e){
            log.error("There is a class cast exception-> {}",e);
        }
        return new ResponseResult(commentList);
    }
}
