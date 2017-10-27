package com.share.controller;

import com.share.model.HostHolder;
import com.share.model.Question;
import com.share.service.QuestionService;
import com.share.util.ShareUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Controller
public class QuestionController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    QuestionService questionService;

    @Autowired
    HostHolder hostHolder;

    @RequestMapping(value = "/question/add", method = RequestMethod.POST)
    @ResponseBody
    public String addQuestion(@RequestParam("title") String title, @RequestParam("content") String content){
        try{
            Question question = new Question();
            question.setTitle(title);
            question.setContent(content);
            question.setCreatedDate(new Date());
            if(hostHolder.getUser() == null){  //判断用户是否登陆
                //默认给匿名用户
//                question.setUserId(ShareUtil.ANONYMOUS_USERID);
                return ShareUtil.getJSONString(999);
            }else{
                question.setUserId(hostHolder.getUser().getId());
            }

            if(questionService.addQuestion(question) > 0){  //添加问题成功  code=0
                return ShareUtil.getJSONString(0);
            }
        }catch (Exception e){
            logger.error("问题添加异常" + e.getMessage());
        }

        return ShareUtil.getJSONString(1,"失败");
    }
}
