package com.share;

import com.share.dao.QuestionDAO;
import com.share.dao.UserDAO;
import com.share.model.Question;
import com.share.model.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.Random;

@RunWith(SpringRunner.class)
@SpringBootTest
@Sql("/init-schema.sql")
public class InitDatabaseTests {

	@Autowired
	UserDAO userDAO;

	@Autowired
	QuestionDAO questionDAO;

	@Test
	public void initDatabase() {
		for(int i = 0; i < 11; ++i){
			Random random = new Random();
			User user = new User();
			user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png",random.nextInt(1000)));
			user.setName(String.format("USER%d",i+1));
			user.setPassword("");
			user.setSalt("");
			userDAO.addUser(user);

			Question question = new Question();
			question.setId(i + 1);
			question.setTitle(String.format("{TITLE%d}", i+1));
			question.setContent(String.format("Bababababababa Content %d",i+1));
			Date date = new Date();
			date.setTime(date.getTime() + 1000*3600*i);
			question.setCreatedDate(date);
			question.setCommentCount(i);
			question.setUserId(i+1);
			questionDAO.addQuestion(question);

		}

		Assert.assertEquals("",userDAO.selectById(1).getPassword());
		userDAO.updatePassword(1,"xx");
		Assert.assertEquals("xx",userDAO.selectById(1).getPassword());

		System.out.println(questionDAO.selectLatestQuestions(0,0,10).size());

	}

}
