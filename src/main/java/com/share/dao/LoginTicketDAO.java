package com.share.dao;

import com.share.model.LoginTicket;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface LoginTicketDAO {

    String TABLE_NAME = " login_ticket ";
    String INSTER_FIELDS = "userId, ticket, expired, status";
    String SELECT_FIELDS = "id, " + INSTER_FIELDS;

    @Insert({"insert into ", TABLE_NAME, "( ", INSTER_FIELDS, ") values(#{userId},#{ticket},#{expired},#{status})" })
    int addLoginTicket(LoginTicket loginTicket);

    @Select({"select ", SELECT_FIELDS, "from ", TABLE_NAME, "where ticket = #{ticket}"})
    LoginTicket selelctByTicket(@Param("ticket") String ticket);

    @Update({"update ", TABLE_NAME, "set status = #{status} where ticket = #{ticket}"})
    void updateStatus(@Param("ticket") String ticket, @Param("status") int status);
}
