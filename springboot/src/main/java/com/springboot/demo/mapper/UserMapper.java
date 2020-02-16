package com.springboot.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.springboot.demo.bean.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper extends BaseMapper<User> {



}
