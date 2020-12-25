package cn.edu.xmu.other.mapper;

import cn.edu.xmu.other.model.po.CustomerPo;
import cn.edu.xmu.other.model.po.CustomerPoExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface CustomerPoMapper {
    int deleteByPrimaryKey(Long id);

    int insert(CustomerPo record);

    int insertSelective(CustomerPo record);

    List<CustomerPo> selectByExample(CustomerPoExample example);

    CustomerPo selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") CustomerPo record, @Param("example") CustomerPoExample example);

    int updateByExample(@Param("record") CustomerPo record, @Param("example") CustomerPoExample example);

    int updateByPrimaryKeySelective(CustomerPo record);

    int updateByPrimaryKey(CustomerPo record);
}