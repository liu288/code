package com.springboot.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Maps;
import com.springboot.demo.bean.User;
import com.springboot.demo.mapper.UserMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Api(value = "mybatisplus测试类")
public class UserController {

    @Autowired
    private UserMapper userMapper;

    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    @ApiOperation(value = "增加用户信息", notes = "mybatis增加用户信息测试")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "Long", name = "id", value = "用户id", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "age", value = "用户age", required = false, defaultValue = "25"),
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "name", value = "用户name", required = false, defaultValue = "liuxiao"),
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "email", value = "用户email", required = false)
    })
    public int insert(@RequestParam Long id,
                      @RequestParam(value = "age", required = false, defaultValue = "25") Integer age,
                      @RequestParam(value = "name", required = false, defaultValue = "liuxiao") String name,
                      String email,
                      User user2) {
        User user = new User();
        user.setId(id);
        user.setAge(age);
        user.setName(name);
        user.setEmail(email);
        return userMapper.insert(user);
    }

    @PostMapping("/deleteById")
    public int deleteById(@RequestParam Long id) {
        return userMapper.deleteById(id);
    }

    @PostMapping("/deleteByMap")
    public int deleteByMap(@RequestParam(required = false) String name, @RequestParam(required = false) Integer age) {
        Map<String, Object> queryMap = Maps.newHashMap();
        if (!StringUtils.isEmpty(name)) {
            queryMap.put("user_name", name);
        }
        if (age != null) {
            queryMap.put("age", age);
        }
        return queryMap.size() == 0 ? 0 : userMapper.deleteByMap(queryMap);
    }

    @PostMapping("/delete")
    public int delete(@RequestParam(required = false) String name, @RequestParam(required = false) Integer age){
        QueryWrapper<User> queryWrapper = Wrappers.<User>query();
//        QueryWrapper<User> queryWrapper = new QueryWrapper<>();

        if (!StringUtils.isEmpty(name)) {
            queryWrapper.eq("user_name", name);
        }

        if (age != null) {
           queryWrapper.eq("age", age);
        }
        return queryWrapper.isEmptyOfWhere() ? 0 : userMapper.delete(queryWrapper);
    }

    @PostMapping("/selectByIds")
    @ApiOperation(value = "根据用户ids查询找用户信息", notes = "查询用户信息")
    @ApiImplicitParam(paramType = "query", dataType = "Long", allowMultiple = true, name = "idsList", value = "用户ids", required = true)
    public List<User> selectBatchIds(@RequestParam List<Long> idsList) {
        return userMapper.selectBatchIds(idsList);
    }

    @PostMapping("/updateById")
    public int updateById(@RequestBody User user){
        return userMapper.updateById(user);
    }

    @PostMapping("/update")
    public int update(@RequestBody User user, @RequestParam Integer updateUserAge){
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();


        int res = userMapper.update(user, queryWrapper);
        return 0;
    }

//    @ApiOperation(value = "查询用户信息", notes = "查询用户信息")
//    @RequestMapping(value = "/list", method = RequestMethod.POST)
//    public List<User> listUser() {
//        List<User> users = userMapper.selectList(null);
//        return users;
//    }

    @GetMapping("/selectById")  //这里用增强swaggerUI发送请求时@PostMapping接受不到id参数
    @ApiOperation(value = "根据用户id查询找用户信息", notes = "查询用户信息")
    @ApiImplicitParam(paramType = "query", dataType = "Long", name = "id", value = "用户id", required = true)
    public User selectById(@RequestParam Long id) {
        return userMapper.selectById(id);
    }




}
