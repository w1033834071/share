package com.share.dao;

import com.share.model.Question;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
@Mapper
public interface QuestionDAO {

    String TABLE_NAME = " question ";
    String INSERT_FIELDS = " title, content, userId, createdDate, commentCount ";
    String SELECT_FIELDS = "id," + INSERT_FIELDS;


    // 通过MyBatis方式进行插入
    @Insert({"insert into ",TABLE_NAME, "(", INSERT_FIELDS, ") values(#{title}, #{content}, #{userId}, #{createdDate}, #{commentCount})"})
    int addQuestion(Question question);

    // 通过XML方式进行查询
    public List<Question> selectLatestQuestions(@Param("userId") int userId,
                                         @Param("offset") int offset,
                                         @Param("limit") int limit);
}
