package com.dj.digitalplatform.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dj.digitalplatform.model.entity.User;
import org.apache.ibatis.annotations.Mapper;


/**
* @author XLW200420
* @description 针对表【user(用户)】的数据库操作Mapper
* @createDate 2025-03-27 20:29:34
* @Entity generator.domain.User
*/
@Mapper
public interface UserMapper extends BaseMapper<User> {

}




