package cn.edu.xmu.other.mapper;

import cn.edu.xmu.other.model.po.RegionPo;
import cn.edu.xmu.other.model.po.RegionPoExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface RegionPoMapper {
    int deleteByPrimaryKey(Long id);

    int insert(RegionPo record);

    int insertSelective(RegionPo record);

    List<RegionPo> selectByExample(RegionPoExample example);

    RegionPo selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") RegionPo record, @Param("example") RegionPoExample example);

    int updateByExample(@Param("record") RegionPo record, @Param("example") RegionPoExample example);

    int updateByPrimaryKeySelective(RegionPo record);

    int updateByPrimaryKey(RegionPo record);
}