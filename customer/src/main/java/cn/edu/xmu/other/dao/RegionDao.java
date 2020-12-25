package cn.edu.xmu.other.dao;

import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.other.mapper.RegionPoMapper;
import cn.edu.xmu.other.model.bo.Region;
import cn.edu.xmu.other.model.po.RegionPo;
import cn.edu.xmu.other.model.vo.RegionRecieveVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author 何明祥 24320182203193
 * createBy 何明祥 2020/12/5 14:33
 */
@Repository
public class RegionDao {

    private static final Logger logger = LoggerFactory.getLogger(RegionDao.class);

    @Autowired
    private RegionPoMapper regionPoMapper;

    /**
     * 查询某个地区的所有上级地区
     *
     * @author 何明祥 24320182203193
     * @param id 地区id
     * @return ReturnObject<Region>
     * createBy 何明祥 2020/12/5 13:31
     */
    public ReturnObject <List> getAncestor(Long id) {
        RegionPo po = regionPoMapper.selectByPrimaryKey(id);
        if(po==null)
        {
            StringBuilder message = new StringBuilder().append("getAncestor: ").append(ResponseCode.RESOURCE_ID_NOTEXIST.getMessage());
            logger.error(message.toString());
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        if(po.getState().intValue()==1){
            //地区废弃插入失败
            return new ReturnObject<>(ResponseCode.REGION_OBSOLETE, String.format("新增失败：" + id));
        }

        List<Region> regions=new ArrayList<>();
        Long pid=po.getPid();
        RegionPo regionPo = regionPoMapper.selectByPrimaryKey(pid);
        for(;pid>0;)
        {
            regionPo=regionPoMapper.selectByPrimaryKey(pid);
            if(regionPo!=null)regions.add(new Region((regionPo)));
            pid=regionPo.getPid();
        }

        return new ReturnObject<>(regions);

    }

    /**
     /**
     * 管理员在地区下新增子地区
     *
     * @author 24320182203193 何明祥
     * @param region 地区bo
     * @return ReturnObject<Region> 新增结果
     * createBy 何明祥 2020/12/5 19:31
     */
    public ReturnObject<Region> insertRegion(Region region)
    {
        if(regionPoMapper.selectByPrimaryKey(region.getPid())==null){
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("新增失败：" + region.getName()));
        }
        if(regionPoMapper.selectByPrimaryKey(region.getPid()).getState().intValue()==1){
            //地区废弃插入失败
            logger.debug("insertRegion: insert region fail " + region.toString());
            return new ReturnObject<>(ResponseCode.REGION_OBSOLETE, String.format("新增失败：" + region.getName()));
        }
        RegionPo regionPo = region.gotRegionPo();
        ReturnObject<Region> retObj = null;
        int ret = regionPoMapper.insertSelective(regionPo);
        if (ret == 0) {
            //插入失败
            logger.debug("insertRegion: insert region fail " + regionPo.toString());
            retObj = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("新增失败：" + regionPo.getName()));
        }
        else {
            //插入成功
            logger.debug("insertRegion: insert region = " + regionPo.toString());
            region.setId(regionPo.getId());
            retObj = new ReturnObject<>(region);
        }
        return retObj;

    }


    /**
     * 管理员修改某个地区
     *
     * @author 24320182203193 何明祥
     * @param regionRecieveVo 地区bo
     * @return ReturnObject<Region> 修改结果
     * createBy 何明祥 2020/12/5 21:25
     */
    public ReturnObject<Region> updateRegion(Long id, RegionRecieveVo regionRecieveVo) {
        if(regionPoMapper.selectByPrimaryKey(id)==null){
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("修改失败：" + id));
        }
        if(regionPoMapper.selectByPrimaryKey(id).getState().intValue()==1){
            //地区废弃插入失败
            return new ReturnObject<>(ResponseCode.REGION_OBSOLETE, String.format("修改失败：" + id));
        }
        RegionPo regionPo = regionPoMapper.selectByPrimaryKey(id);
        ReturnObject<Region> retObj = null;
        regionPo.setGmtModified(LocalDateTime.now());
        regionPo.setName(regionRecieveVo.getName());
        regionPo.setPostalCode(regionRecieveVo.getPostalCode());

        try{
            int ret = regionPoMapper.updateByPrimaryKeySelective(regionPo);
            if (ret == 0) {
                //修改失败
                logger.debug("updateRegion: update region fail : " + regionPo.toString());
                retObj = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("地区id不存在：" + regionPo.getId()));
            } else {
                //修改成功
                logger.debug("updateRegion: update region = " + regionPo.toString());
                retObj = new ReturnObject<>();
            }
        }
        catch (DataAccessException e) {
            if (Objects.requireNonNull(e.getMessage()).contains("region.name")) {
                //若有重复的地区名则修改失败
                logger.debug("updateRegion: have same region name = " + regionPo.getName());
                retObj = new ReturnObject<>(ResponseCode.ROLE_REGISTERED, String.format("角色名重复：" + regionPo.getName()));
            } else {
                // 其他数据库错误
                logger.debug("other sql exception : " + e.getMessage());
                retObj = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
            }
        }
        catch (Exception e) {
            // 其他Exception错误
            logger.error("other exception : " + e.getMessage());
            retObj = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("其他错误：%s", e.getMessage()));
        }
        return retObj;
    }

    /**
     * 管理员修改某个地区的状态
     *
     * @author 24320182203193 何明祥
     * @param id 地区id
     * @param state 状态
     * @return ReturnObject<Region> 修改结果
     * createBy 何明祥 2020/12/6 10:02
     */
    public ReturnObject<Region> changeRegionState(Long id, Region.State state) {
        ReturnObject<Region> retObj = null;
        RegionPo regionPo = regionPoMapper.selectByPrimaryKey(id);
        if(regionPo==null) return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("地区id不存在：" ));

        regionPo.setState(state.getCode().byteValue());
        int ret = regionPoMapper.updateByPrimaryKey(regionPo);
        //int ret = regionPoMapper.updateByPrimaryKeySelective(regionPo);
        if (ret == 0) {
            //修改失败
            logger.debug("updateRegion: update region fail : " + regionPo.toString());
            retObj = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("地区id不存在：" + regionPo.getId()));
        } else {
            //修改成功
            logger.debug("updateRegion: update region = " + regionPo.toString());
            retObj = new ReturnObject<>();
        }
        return retObj;
    }

}
