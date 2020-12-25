package cn.edu.xmu.other.mapper;

import cn.edu.xmu.other.model.po.AdvertisementPo;
import cn.edu.xmu.other.model.po.AdvertisementPoExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface AdvertisementPoMapper {
    int countByExample(AdvertisementPoExample example);

    int deleteByExample(AdvertisementPoExample example);

    int deleteByPrimaryKey(Long id);

    int insert(AdvertisementPo record);

    int insertSelective(AdvertisementPo record);

    List<AdvertisementPo> selectByExample(AdvertisementPoExample example);

    AdvertisementPo selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") AdvertisementPo record, @Param("example") AdvertisementPoExample example);

    int updateByExample(@Param("record") AdvertisementPo record, @Param("example") AdvertisementPoExample example);

    int updateByPrimaryKeySelective(AdvertisementPo record);

    int updateByPrimaryKey(AdvertisementPo record);
}