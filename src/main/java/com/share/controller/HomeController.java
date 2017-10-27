package com.share.controller;

import com.share.dao.QuestionDAO;
import com.share.dao.UserDAO;
import com.share.model.Question;
import com.share.model.ViewObject;
import com.share.service.QuestionService;
import com.share.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    UserService userService;

    @Autowired
    QuestionService questionService;


    @RequestMapping(path = {"/", "/index"}, method = {RequestMethod.GET})
    public String index(Model model){

        List<ViewObject> vos = getQuestions(0,0,10);

        model.addAttribute("vos", vos);

        return "index";
    }

    private List<ViewObject> getQuestions(int userId, int offset, int limit){
        List<Question> questions = questionService.getLatestQuestions(userId, offset, limit);

        List<ViewObject> vos = new ArrayList<>();
        for(int i = 0; i < questions.size(); i ++){
            ViewObject viewObject = new ViewObject();
            viewObject.set("question",questions.get(i));
            viewObject.set("user",userService.getUser(questions.get(i).getUserId()));
            vos.add(viewObject);
        }

        return vos;
    }

    @RequestMapping(path = {"/user/{userId}"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String userIndex(Model model, @PathVariable("userId") int userId) {
        model.addAttribute("vos", getQuestions(userId, 0, 10));
        return "index";
    }

}
