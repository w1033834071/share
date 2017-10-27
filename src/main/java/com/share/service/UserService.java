package com.share.service;

import com.share.dao.LoginTicketDAO;
import com.share.dao.UserDAO;
import com.share.model.LoginTicket;
import com.share.model.User;
import com.share.util.ShareUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    @Autowired
    UserDAO userDAO;

    @Autowired
    LoginTicketDAO loginTicketDAO;

    public Map<String, String> register (String username, String password){
        Map<String, String> map = new HashMap<>();

        if(StringUtils.isBlank(username)){
            map.put("msg","用户名不能为空");
            return map;
        }

        if(StringUtils.isBlank(password)){
            map.put("msg","密码不能为空");
            return map;
        }

        User user = userDAO.selectByName(username);
        if(user != null){
            map.put("msg","用户名被注册");
            return map;
        }

        user = new User();
        user.setName(username);
        user.setSalt(UUID.randomUUID().toString().substring(0,5));
        user.setPassword(ShareUtil.MD5(password + user.getSalt()));
        user.setHeadUrl("http://images.nowcoder.com/head/"+ new Random().nextInt(1000)+"t.png");
        userDAO.addUser(user);

        String ticket = addLoginTicket(user.getId());  //注册成功  也给用户下发ticket
        map.put("ticket", ticket);

        return map;
    }

    /**
     * 用户登录
     * @param username
     * @param password
     * @return
     */
    public Map<String, String> login (String username, String password){
        Map<String, String> map = new HashMap<>();

        if(StringUtils.isBlank(username)){
            map.put("msg","用户名不能为空");
            return map;
        }

        if(StringUtils.isBlank(password)){
            map.put("msg","密码不能为空");
            return map;
        }

        User user = userDAO.selectByName(username);
        if(user == null){
            map.put("msg","用户名不存在");
            return map;
        }

        if(!ShareUtil.MD5(password + user.getSalt()).equals(user.getPassword())){
            map.put("msg","密码错误");
            return map;
        }

        String ticket = addLoginTicket(user.getId());  //登陆成功 给用户下发ticket
        map.put("ticket", ticket);

        return map;
    }

    public String addLoginTicket(int userId){
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(userId);
        loginTicket.setStatus(0);
        loginTicket.setTicket(UUID.randomUUID().toString().replaceAll("-",""));
        Date now = new Date();
        now.setTime(3600*24*100 + now.getTime());  //设置有效期是100天
        loginTicket.setExpired(now);

        loginTicketDAO.addLoginTicket(loginTicket);
        return loginTicket.getTicket();
    }

    public void logout(String ticket){
        loginTicketDAO.updateStatus(ticket, 1);
    }

    public User getUser(int id){
        return userDAO.selectById(id);
    }
}
