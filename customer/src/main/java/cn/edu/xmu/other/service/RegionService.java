package cn.edu.xmu.other.service;


import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.other.dao.RegionDao;
import cn.edu.xmu.other.model.bo.Region;
import cn.edu.xmu.other.model.vo.RegionRecieveVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author 何明祥 24320182203193
 * createBy 何明祥 2020/12/5 14:33
 */
@Service
public class RegionService {

    private Logger logger = LoggerFactory.getLogger(RegionService.class);
    @Autowired
    private RegionDao regionDao;

    /**
     * 查询某个地区的所有上级地区
     *
     * @author 何明祥 24320182203193
     * @param id 地区id
     * @return ReturnObject<Region>
     * createBy 何明祥 2020/12/5 13:31
     */
    @Transactional
    public ReturnObject<List> getAncestor(Long id) {
        ReturnObject<List> returnObject = regionDao.getAncestor(id);
        return returnObject;
    }

    /**
     * 管理员在地区下新增子地区
     *
     * @author 24320182203193 何明祥
     * @param region 地区bo
     * @return ReturnObject<Region> 地区返回视图
     * createBy 何明祥 2020/12/5 19:31
     */
    @Transactional
    public ReturnObject insertSubregion(Region region) {
        ReturnObject<Region> retObj = regionDao.insertRegion(region);
        return retObj;
    }

    /**
     * 管理员修改某个地区
     *
     * @author 24320182203193 何明祥
     * @param regionRecieveVo 地区vo
     * @return ReturnObject<Region> 地区返回视图
     * createBy 何明祥 2020/12/5 21:25
     */
    @Transactional
    public ReturnObject updateRegion(Long id, RegionRecieveVo regionRecieveVo) {
        ReturnObject<Region> retObj = regionDao.updateRegion(id, regionRecieveVo);
        return retObj;
    }

    /**
     * 管理员使某个地区无效
     *
     * @author 24320182203193 何明祥
     * @param id 地区id
     * @return ReturnObject<Region> 地区返回视图
     * createBy 何明祥 2020/12/6 10:02
     */
    @Transactional
    public ReturnObject invalidateRegion(Long id) {
        ReturnObject<Region> retObj = regionDao.changeRegionState(id, Region.State.INVALID);
        return retObj;
    }



}
