package cn.edu.xmu.other.mapper;

import cn.edu.xmu.other.model.po.ShareActivityPo;
import cn.edu.xmu.other.model.po.ShareActivityPoExample;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ShareActivityPoMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table share_activity
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table share_activity
     *
     * @mbg.generated
     */
    int insert(ShareActivityPo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table share_activity
     *
     * @mbg.generated
     */
    int insertSelective(ShareActivityPo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table share_activity
     *
     * @mbg.generated
     */
    List<ShareActivityPo> selectByExample(ShareActivityPoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table share_activity
     *
     * @mbg.generated
     */
    ShareActivityPo selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table share_activity
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(ShareActivityPo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table share_activity
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(ShareActivityPo record);
}