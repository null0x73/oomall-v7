package cn.edu.xmu.other.mapper;

import cn.edu.xmu.other.model.po.TimeSegmentPo;
import cn.edu.xmu.other.model.po.TimeSegmentPoExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface TimeSegmentPoMapper {
    int countByExample(TimeSegmentPoExample example);

    int deleteByExample(TimeSegmentPoExample example);

    int deleteByPrimaryKey(Long id);

    int insert(TimeSegmentPo record);

    int insertSelective(TimeSegmentPo record);

    List<TimeSegmentPo> selectByExample(TimeSegmentPoExample example);

    TimeSegmentPo selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") TimeSegmentPo record, @Param("example") TimeSegmentPoExample example);

    int updateByExample(@Param("record") TimeSegmentPo record, @Param("example") TimeSegmentPoExample example);

    int updateByPrimaryKeySelective(TimeSegmentPo record);

    int updateByPrimaryKey(TimeSegmentPo record);
}