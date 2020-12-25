package cn.edu.xmu.other.mapper;

import cn.edu.xmu.other.model.po.FootprintPo;
import cn.edu.xmu.other.model.po.FootprintPoExample;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface FootprintPoMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table foot_print
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table foot_print
     *
     * @mbg.generated
     */
    int insert(FootprintPo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table foot_print
     *
     * @mbg.generated
     */
    int insertSelective(FootprintPo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table foot_print
     *
     * @mbg.generated
     */
    List<FootprintPo> selectByExample(FootprintPoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table foot_print
     *
     * @mbg.generated
     */
    FootprintPo selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table foot_print
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(FootprintPo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table foot_print
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(FootprintPo record);
}