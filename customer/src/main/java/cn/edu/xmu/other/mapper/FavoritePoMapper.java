package cn.edu.xmu.other.mapper;

import cn.edu.xmu.other.model.po.FavoritePo;
import cn.edu.xmu.other.model.po.FavoritePoExample;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface FavoritePoMapper {
    int deleteByExample(FavoritePoExample example);

    int deleteByPrimaryKey(Long id);

    int insert(FavoritePo record);

    int insertSelective(FavoritePo record);

    List<FavoritePo> selectByExample(FavoritePoExample example);

    FavoritePo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(FavoritePo record);

    int updateByPrimaryKey(FavoritePo record);
}