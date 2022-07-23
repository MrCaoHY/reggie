package com.example.reggie.mapper;

import com.example.reggie.entity.Employee;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 员工信息 Mapper 接口
 * </p>
 *
 * @author chy
 * @since 2022-07-21
 */
@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {

}
