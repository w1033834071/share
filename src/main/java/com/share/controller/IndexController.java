package com.share.controller;

import com.share.model.User;
import com.share.service.ShareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.websocket.server.PathParam;
import java.util.*;

//@Controller
public class IndexController {

    @Autowired  //通过依赖注入的方式  IOC
    ShareService shareService;


    @RequestMapping(path = {"/","/index"}, method = {RequestMethod.GET})
    @ResponseBody
    public String index(HttpSession httpSession){
        return shareService.getUserName(9527) + "   Hello World" + "   msg from session:"  + httpSession.getAttribute("msg");
    }

    @RequestMapping(path = {"/profile/{groupId}/{userId}"})
    @ResponseBody
    public String profile(@PathVariable("userId") int userId,
                          @PathVariable("groupId") String groupId,
                          @RequestParam(value = "type", defaultValue = "1") int type,
                          @RequestParam(value = "key", required = false) String key){
        return String.format("Profile Page of %s / %d, t:%d  k:%s",groupId,userId,type,key);
    }


    @RequestMapping(path = {"/vm"}, method = {RequestMethod.GET})
    public String template(Model model){
        model.addAttribute("value1","value1");
        List<String> colors = Arrays.asList(new String[]{"RED","GREEN","BLUE"});
        model.addAttribute("colors",colors);

        Map<String,String> map = new HashMap<>();
        for(int i = 0; i < 4; i ++){
            map.put(String.valueOf(i),String.valueOf(i * i));
        }
        model.addAttribute("map",map);
//        model.addAttribute("user",new User("Wang"));
        return "home";
    }

    @RequestMapping(path = {"/request"}, method = {RequestMethod.GET})
    @ResponseBody
    public String request(Model model, HttpServletResponse response,
                          HttpServletRequest request,
                          HttpSession httpSession,
                          //通过注解的方式获得Cookie中的JSESSIONID
                          @CookieValue("JSESSIONID") String sessionId){
        StringBuilder sb = new StringBuilder();
        if(!sessionId.isEmpty()){
            sb.append("COOKIE_VALUE:" + sessionId + "<br>");
        }

        Enumeration<String> headerNames = request.getHeaderNames();
        while(headerNames.hasMoreElements()){
            String name = headerNames.nextElement();
            sb.append(name + ":" + request.getHeader(name) + "<br>");
        }
        if(request.getCookies() != null){
            for(Cookie cookie : request.getCookies()){
                sb.append("Cookie:" + cookie.getName() + " values:" + cookie.getValue() + "<br>");
            }
        }
        sb.append(request.getMethod() + "<br>");
        sb.append(request.getQueryString() + "<br>");
        sb.append(request.getPathInfo() + "<br>");
        sb.append(request.getRequestURI() + "<br>");

        response.addHeader("shareId","hello world");
        response.addCookie(new Cookie("username","linkwang"));
        return sb.toString();
    }

    @RequestMapping(path = {"/redirect/{code}"}, method = {RequestMethod.GET})
    public RedirectView redirect(@PathVariable("code") int code, HttpSession httpSession){
        httpSession.setAttribute("msg","jump from redirect");
        RedirectView red = new RedirectView("/",true);
        if(code == 301){
            red.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
        }
        return red;
    }

    @RequestMapping(path = {"/admin"}, method = {RequestMethod.GET})
    @ResponseBody
    public String admin(@PathParam("key") String key){
        if(key.equals("admin")){
            return "Hello admin";
        }
        throw new IllegalArgumentException("不是管理员");
    }

    @ExceptionHandler()
    @ResponseBody
    public String error(Exception e){
        return "error:" + e.toString();
    }

}
