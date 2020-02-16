package com.springboot.demo;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Lists;
import com.springboot.demo.bean.User;
import com.springboot.demo.mapper.UserMapper;
import com.springboot.demo.service.UserService;
import io.swagger.models.auth.In;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserService userService;

    private QueryWrapper<User> queryWrapper;

    @Before
    public void bofore() {
        this.queryWrapper = new QueryWrapper<>();
        //QueryWrapper<User> queryWrapper = Wrappers<User>.query();
    }

    @Test
    public void test0() {
        User user = userService.getOne(Wrappers.<User>lambdaQuery().gt(User::getAge, 0), false);
        System.out.println(user);
    }

    @Test
    public void test1() {
        //名字中包含雨并且年龄小于40
        //name like '%雨%' and age<40
        this.queryWrapper.like("user_name", '雨').lt("age", 40);
        List<User> userList = userMapper.selectList(this.queryWrapper);
        userList.forEach(System.out::println);
    }

    @Test
    public void test2() {
        //名字中包含雨年并且龄大于等于20且小于等于40并且email不为空
        //name like '%雨%' and age between 20 and 40 and email is not null
        this.queryWrapper.like("user_name", "雨")
                .ge("age", 20)
                .le("age", 40)
                .isNotNull("email");
        List<User> userList = userMapper.selectList(this.queryWrapper);
        userList.forEach(System.out::println);
    }

    @Test
    public void test3() {
        //名字为王姓或者年龄大于等于25，按照年龄降序排列，年龄相同按照id升序排列
        //name like '王%' or age>=25 order by age desc,id asc
        queryWrapper.likeRight("user_name", "王")
                .or()
                .gt("age", 25)
                .orderByDesc("age").orderByAsc("id");
        userMapper.selectList(this.queryWrapper).forEach(System.out::println);
    }

    @Test
    public void test4() {
        //创建日期为2020年2月14日并且直属上级为名字为王姓
        //date_format(create_time, '%Y-%m-%d') = '2020-02-14' AND manager_id in (select user_id from mp_user where user_name like '王%')
        queryWrapper.apply("date_format(create_time, '%Y-%m-%d') = '2020-02-14'")
                  .inSql("manager_id", "select user_id from mp_user where user_name like '王%'");
        userMapper.selectList(this.queryWrapper);
    }

    @Test
    public void test5() {
        //名字为王姓并且（年龄小于40或邮箱不为空）
        //user_name LIKE '王%' AND ( (age < 40 OR email IS NOT NULL) )
        queryWrapper.likeRight("user_name", "王")
                .and(i -> i.lt("age", 20).or().isNotNull("email"));
        userMapper.selectList(this.queryWrapper);
    }

    @Test
    public void test6() {
        //名字为王姓或者（年龄小于40并且年龄大于20并且邮箱不为空）
        //user_name LIKE '王%' OR ( (age < 40 AND age > 20 AND email IS NOT NULL) )
        queryWrapper.likeRight("user_name", "王")
                .or(i -> i.lt("age", 40).gt("age", 20).isNotNull("email"));
        userMapper.selectList(queryWrapper);
    }

    @Test
    public void test7() {
        //（年龄小于40或邮箱不为空）并且名字为王姓
        //user_name LIKE ? AND ( (age < ? OR email IS NOT NULL)
        queryWrapper.likeRight("user_name", "王")
                .and(i -> i.lt("age", 40).or().isNotNull("email"));
        userMapper.selectList(queryWrapper);
    }

    @Test
    public void test8() {
        //年龄为30、31、34、35
        //ge IN (30, 31, 34, 35)
        queryWrapper.in("age", 30, 31, 34, 35);
        userMapper.selectList(queryWrapper);
    }

    @Test
    public void test9() {
        //只返回满足条件的其中一条语句即可
        //limit 1
        queryWrapper.last("limit 1");
        userMapper.selectList(queryWrapper);
    }

    @Test
    public void test10() {
        //select中字段不全部出现的查询
        queryWrapper.select("user_id", "user_name", "age");
        userMapper.selectList(queryWrapper);
    }

    @Test
    public void test11() {
        //按照直属上级分组，查询每组的平均年龄、最大年龄、最小年龄。并且只取年龄总和小于500的组
        //SELECT avg(age), max(age), min(age) FROM mp_user GROUP BY manager_id HAVING sum(age) < 500
        queryWrapper.select("avg(age), max(age), min(age)")
                .groupBy("manager_id")
                .having("sum(age) < 500");
        userMapper.selectList(queryWrapper);
    }

}
