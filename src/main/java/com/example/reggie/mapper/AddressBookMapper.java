package com.example.reggie.mapper;

import com.example.reggie.entity.AddressBook;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 地址管理 Mapper 接口
 * </p>
 *
 * @author chy
 * @since 2022-07-21
 */
@Mapper
public interface AddressBookMapper extends BaseMapper<AddressBook> {

}
