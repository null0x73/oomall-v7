package cn.edu.xmu.other.mapper;

import cn.edu.xmu.other.model.po.AddressPo;
import cn.edu.xmu.other.model.po.AddressPoExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Mapper
@Repository
public interface AddressPoMapper {
    int deleteByPrimaryKey(Long id);

    int insert(AddressPo record);

    int insertSelective(AddressPo record);

    List<AddressPo> selectByExample(AddressPoExample example);

    AddressPo selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") AddressPo record, @Param("example") AddressPoExample example);

    int updateByExample(@Param("record") AddressPo record, @Param("example") AddressPoExample example);

    int updateByPrimaryKeySelective(AddressPo record);

    int updateByPrimaryKey(AddressPo record);
}