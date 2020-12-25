package cn.edu.xmu.other.mapper;

import cn.edu.xmu.other.model.po.CartItemPo;
import cn.edu.xmu.other.model.po.CartItemPoExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface CartItemPoMapper {
    int countByExample(CartItemPoExample example);

    int deleteByExample(CartItemPoExample example);

    int deleteByPrimaryKey(Long id);

    int insert(CartItemPo record);

    int insertSelective(CartItemPo record);

    List<CartItemPo> selectByExample(CartItemPoExample example);

    CartItemPo selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") CartItemPo record, @Param("example") CartItemPoExample example);

    int updateByExample(@Param("record") CartItemPo record, @Param("example") CartItemPoExample example);

    int updateByPrimaryKeySelective(CartItemPo record);

    int updateByPrimaryKey(CartItemPo record);
}