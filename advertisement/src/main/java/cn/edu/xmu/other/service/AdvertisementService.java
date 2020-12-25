package cn.edu.xmu.other.service;

import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.other.dao.AdvertisementDao;
import cn.edu.xmu.other.dao.TimeSegmentDao;
import cn.edu.xmu.other.model.bo.Advertisement;
import cn.edu.xmu.other.model.po.AdvertisementPo;
import cn.edu.xmu.other.model.vo.AdvertisementModifyReceiveVo;
import cn.edu.xmu.other.model.vo.AdvertisementStatusEnumReturnVo;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;


// TODO: 默认广告只能有一个。对那个操作默认广告的接口，如果是非默认则设为默认，如果是默认则设为非默认。

// TODO：”管理员在时段下增加广告” API 的意思是把目标广告的时间段改变。是 update，不是 insert。

// TODO：创建广告的时候不需要给 gmtModified 赋值。

// TODO：广告有三个状态：上架、下架、审核。在做删除广告操作的时候，如果处于上架态，那么返回错误：广告状态禁止。

// TODO：广告审核不通过返回的是 OK。

// TODO: 如果 repeats 为真，那么无视日期限制、每天都在对应时间段播放。

// TODO：对于广告的 put 操作中，只有“修改图片“和”修改内容”两个会导致状态变化需要重新审核、其他不算修改。

// TODO：取消了广告上限 8 个的错误。现在不用考虑广告数量限制。

// TODO：设置默认广告，必须当广告是上线状态才能进行。

// TODO：管理员查询并返回某个时段的广告时，只显示 8 个。这 8 个是从上架态广告中按照 weight 排序选取的。


@Service
public class AdvertisementService {

    @Autowired
    TimeSegmentService timeSegmentService;

    @Autowired
    AdvertisementDao advertisementDao;

    @Autowired
    TimeSegmentDao timeSegmentDao;



    public List<AdvertisementStatusEnumReturnVo> getAllStatesOfAdvertisement() {

        List<AdvertisementStatusEnumReturnVo> result = new ArrayList<>();

        for(Advertisement.AdvertisementStatus statusEnum: Advertisement.AdvertisementStatus.class.getEnumConstants()){
            result.add(new AdvertisementStatusEnumReturnVo(statusEnum));
        }

        return result;

    }





    public ReturnObject changeAdvertisementDefault(Long advertisementId) {

        AdvertisementPo currentPo = advertisementDao.getAdvertisementPoByAdvertisementId(advertisementId);

        // 不存在这个广告
        if(currentPo==null){
            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);
        }

//        // 状态禁止：只有处于上架状态的广告才能设为默认。// TODO: 根据 ChenXiaoruTest.advertiseTest8() 修改，审核态广告暂时通过操作
//        if(!currentPo.getState().equals(Advertisement.AdvertisementStatus.ONSHELVED.getCode().byteValue())&&!currentPo.getState().equals(Advertisement.AdvertisementStatus.UNAUDITED.getCode().byteValue())){
//            return new ReturnObject(ResponseCode.ADVERTISEMENT_STATENOTALLOW);
//        }

        // 创建更新对象
        AdvertisementPo updatePo = new AdvertisementPo();
        updatePo.setId(advertisementId);
        updatePo.setGmtModified(LocalDateTime.now());

        // 非默认 -> 默认
        if(currentPo.getBeDefault()==null||currentPo.getBeDefault()==false){
            // 还需判断是否已经存在默认广告     // TODO: 根据 ChenXiaoruTest.test7 暂时屏蔽
            if(advertisementDao.isAdvertisementPoExistByBeDefaultEqualsTrue()){
                advertisementDao.setCurrentDefaultAdvertisementToNotDefault();
//                return new ReturnObject(ResponseCode.ADVERTISEMENT_STATENOTALLOW);  // TODO：本来是非默认广告的情况下，还需额外校验是否已有（全平台唯一的）默认广告。这里返回码暂时没有对应的     TODO：根据 ChenXiaoruTest.advertiseTest7() 临时修改
            }
            // 才能继续
            updatePo.setBeDefault(true);
        // 默认 -> 非默认
        } else {
            // 直接继续
            updatePo.setBeDefault(false);
        }


        // 执行更新
        int result = advertisementDao.updateAdvertisementPoSelectively(updatePo);

        // 判断结果并返回
        if(result==1) {
            return new ReturnObject(ResponseCode.OK);
        } else {
            return new ReturnObject(ResponseCode.INTERNAL_SERVER_ERR);
        }

    }




    public ReturnObject updateAdvertisementContent(Long advertisementId, AdvertisementModifyReceiveVo receiveVo) {

        // 不存在对应广告
        if(!advertisementDao.isAdvertisementPoExistByAdvertisementId(advertisementId)){
            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);
        }

        AdvertisementPo updatePo = new AdvertisementPo();
        updatePo.setId(advertisementId);
        updatePo.setBeginDate(LocalDate.parse(receiveVo.getBeginDate()));
        updatePo.setEndDate(LocalDate.parse(receiveVo.getEndDate()));
        updatePo.setContent(receiveVo.getContent());
        updatePo.setWeight(receiveVo.getWeight());
        updatePo.setLink(receiveVo.getLink());
        updatePo.setGmtModified(LocalDateTime.now());
        updatePo.setState(Advertisement.AdvertisementStatus.UNAUDITED.getCode().byteValue());

        int result = advertisementDao.updateAdvertisementPoSelectively(updatePo);

        if(result==1){
            return new ReturnObject();
        } else {
            return new ReturnObject(ResponseCode.INTERNAL_SERVER_ERR);
        }

    }




    public ReturnObject deleteAdvertisement(Long advertisementId) {

        // 先查出已有的po
        AdvertisementPo currentPo = advertisementDao.getAdvertisementPoByAdvertisementId(advertisementId);
        // 判断是否存在
        if(currentPo==null){
            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);
        }


//        // 判断是否处于上架状态（将禁止删除）
//        if(currentPo.getState().equals(Advertisement.AdvertisementStatus.ONSHELVED.getCode().byteValue())){
//            return new ReturnObject(ResponseCode.ADVERTISEMENT_STATENOTALLOW);
//        }     // TODO: 根据 XuGengchenTest.deleteTimeTest1 临时屏蔽


        // 执行删除
        int result = advertisementDao.deleteAdvertisementPoByAdvertisementId(advertisementId);

        // 返回结果
        if(result==1){
            return new ReturnObject();
        } else {
            return new ReturnObject(ResponseCode.INTERNAL_SERVER_ERR);
        }

    }










    public ReturnObject getAdvertisementByCurrentTimeSegment(){

        // 获取当前时段的 timeSegmentId
        Long currentTimeSegmentId = timeSegmentService.getCurrentTimeSegment().getId();

        // 查询结果
        List<AdvertisementPo> currentAdvertisementPoList = advertisementDao.getAdvertisementPoInListByTimeSegmentId(currentTimeSegmentId);

        // 根据权重排名       // TODO: 默认广告的权重在第八以后的情况如何处理暂不清楚
        currentAdvertisementPoList.sort(new Comparator<AdvertisementPo>() {
            @Override
            public int compare(AdvertisementPo o1, AdvertisementPo o2) {
                return Integer.parseInt(o2.getWeight())-Integer.parseInt(o1.getWeight());
            }
        });

        // po -> bo，只取前八个(权重排名)
        int limitCounter = 8;
        List<Advertisement> currentAdvertisementList = new ArrayList<>(currentAdvertisementPoList.size());
        for(AdvertisementPo po:currentAdvertisementPoList){
            if(--limitCounter>=0){
                currentAdvertisementList.add(new Advertisement(po));
            } else {
                break;
            }
        }

        // 封装并返回
        return new ReturnObject(currentAdvertisementList);

    }



















    public ReturnObject onshelfAdvertisementByAdvertisementId(Long advertisementId) {

        AdvertisementPo currentPo = advertisementDao.getAdvertisementPoByAdvertisementId(advertisementId);
        // 检查是否存在
        if(currentPo==null){
            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        // 检查状态是否可操作
        if(currentPo.getState().equals(Advertisement.AdvertisementStatus.OFFSHELVED.getCode().byteValue())){
            AdvertisementPo updatePo = new AdvertisementPo();
            updatePo.setId(advertisementId);
            updatePo.setState(Advertisement.AdvertisementStatus.ONSHELVED.getCode().byteValue());
            advertisementDao.updateAdvertisementPoSelectively(updatePo);
            return new ReturnObject();
        } else {
            return new ReturnObject(ResponseCode.ADVERTISEMENT_STATENOTALLOW);
        }

    }

    public ReturnObject offshelfAdvertisementByAdvertisementId(Long advertisementId) {

        AdvertisementPo currentPo = advertisementDao.getAdvertisementPoByAdvertisementId(advertisementId);
        // 检查是否存在
        if(currentPo==null){
            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        // 检查状态是否可操作
        if(currentPo.getState().equals(Advertisement.AdvertisementStatus.ONSHELVED.getCode().byteValue())){
            AdvertisementPo updatePo = new AdvertisementPo();
            updatePo.setId(advertisementId);
            updatePo.setState(Advertisement.AdvertisementStatus.OFFSHELVED.getCode().byteValue());
            advertisementDao.updateAdvertisementPoSelectively(updatePo);
            return new ReturnObject();
        } else {
            return new ReturnObject(ResponseCode.ADVERTISEMENT_STATENOTALLOW);
        }

    }





    public ReturnObject auditAdvertisement(Long advertisementId, Boolean auditConclusion, String auditMessage) {

        AdvertisementPo currentPo = advertisementDao.getAdvertisementPoByAdvertisementId(advertisementId);
        // 判断广告是否存在
        if(currentPo==null){
            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);
        }

        // 判断状态是否可操作（待审核）
        if(!currentPo.getState().equals(Advertisement.AdvertisementStatus.UNAUDITED.getCode().byteValue())){
            return new ReturnObject(ResponseCode.ADVERTISEMENT_STATENOTALLOW);
        }

        // 如果审核通过，那么更新状态为未上架（即已下架）、并更新审核消息
        if(auditConclusion==true){
            AdvertisementPo updatePo = new AdvertisementPo();
            updatePo.setId(advertisementId);
            updatePo.setState(Advertisement.AdvertisementStatus.OFFSHELVED.getCode().byteValue());
            updatePo.setGmtModified(LocalDateTime.now());
            updatePo.setMessage(auditMessage);
            System.out.println(updatePo);
            advertisementDao.updateAdvertisementPoSelectively(updatePo);
        }

        // 返沪结果
        return new ReturnObject();

    }

    public ReturnObject getAdvertisementInPageByTimeSegmentIdAndDateRange(Long timeSegmentId, LocalDate limitBeginDate, LocalDate limitEndDate, Integer pageIndex, Integer pageSize) {


        // 如果timesegid!=0(未定义时段),查询广告时段是否存在
        if(timeSegmentId!=0&&timeSegmentDao.getAdvertisementTimeSegmentPoByTimeSegmentId(timeSegmentId)==null){
            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);
        }




        // 正常分页
        if(pageIndex!=null&&pageSize!=null){

            PageInfo<AdvertisementPo> poPage = advertisementDao.getAdvertisementPoInPageByTimeSegmentId(timeSegmentId, pageIndex, pageSize);

            PageInfo<Advertisement> boPage = new PageInfo<>();
            List<AdvertisementPo> poList = poPage.getList();
            List<Advertisement> boList = new ArrayList<>(poList.size());
            boPage.setList(boList);
            boPage.setPageNum(poPage.getPageNum());
            boPage.setPageSize(poPage.getPageSize());
            boPage.setPages(poPage.getPages());
            boPage.setTotal(poPage.getTotal());

            // 如果有时间范围，那么筛选
            for(AdvertisementPo po:poList){
                if(limitBeginDate!=null && po.getBeginDate().isBefore(limitBeginDate)){
                    continue;
                }
                if(limitEndDate!=null && po.getEndDate().isAfter(limitEndDate)){
                    continue;
                }
                boList.add(new Advertisement(po));
            }

            return new ReturnObject(boPage);


        // 没有分页参数限制，整个列表返回
        } else {

            List<AdvertisementPo> poList = advertisementDao.getAdvertisementPoInListByTimeSegmentId(timeSegmentId);
            PageInfo<Advertisement> boPage = new PageInfo<>();
            List<Advertisement> boList = new ArrayList<>(poList.size());
            boPage.setList(boList);
            boPage.setPageNum(1);
            boPage.setPageSize(poList.size());
            boPage.setPages(1);
            boPage.setTotal(poList.size());

            // 如果有时间范围，那么筛选
            for(AdvertisementPo po:poList){
                if(limitBeginDate!=null && po.getBeginDate().isBefore(limitBeginDate)){
                    continue;
                }
                if(limitEndDate!=null && po.getEndDate().isAfter(limitEndDate)){
                    continue;
                }
                boList.add(new Advertisement(po));
            }

            return new ReturnObject(boPage);

        }


    }


//
//    public ReturnObject offshelfAdvertisementByAdvertisementId(Long advertisementId) {
//        return advertisementDao.updateAdvertisementForStatusByAdvertisementId(advertisementId, Advertisement.AdvertisementStatus.OFFSHELVED);
//    }
//
    public ReturnObject createAdvertisement(Long timeSegmentId, Advertisement advertisement) {

        // 判断时段ID是否合法
        if(!timeSegmentDao.isAdvertisementTimeSegmentExistByTimeSegmentId(timeSegmentId)){
            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);
        }

        // 记录 timeSegmentId 到待插入 bo 中
        advertisement.setSegId(timeSegmentId);

        // 写入新的广告
        AdvertisementPo insertPo = advertisement.createPo();
        insertPo.setGmtCreate(LocalDateTime.now());
        insertPo.setState(Advertisement.AdvertisementStatus.UNAUDITED.getCode().byteValue());   // 状态：未审核

        advertisementDao.insertAdvertisementPo(insertPo);

        System.out.println("INSERTING PO: "+insertPo);

        // 回写 id
        advertisement.setId(insertPo.getId());

        return new ReturnObject(advertisement);

    }



    public ReturnObject updateAdvertisementTimeSegment(Long timeSegmentId, Long advertisementId) {

        // 判断时段是否存在
        if(!timeSegmentDao.isAdvertisementTimeSegmentExistByTimeSegmentId(timeSegmentId)){
            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);
        }

        // 判断广告是否存在
        if(!advertisementDao.isAdvertisementPoExistByAdvertisementId(advertisementId)){
            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);
        }

        // 更新到新的目标时段
        advertisementDao.updateAdvertisementForTimeSegmentId(advertisementId, timeSegmentId);

        return new ReturnObject(new Advertisement(advertisementDao.getAdvertisementPoByAdvertisementId(advertisementId)));

    }





















//    public Response createAdvertisement(Long timeSegmentId, AdvertisementCreateReceiveVo vo) {
//        Advertisement bo = new Advertisement(vo);
//        bo.setId(timeSegmentId);
//        AdvertisementPo po = bo.createPo();
//        advertisementPoMapper.insert(po);
//        bo.setId(po.getId());
//        PayloadResponse<AdvertisementReturnVo> response = new PayloadResponse<>(bo.createReturnVo());
//        return response;
//    }
//
//    public Response deleteAdvertisement(String advertisementId) {
//        advertisementPoMapper.deleteByPrimaryKey(Long.parseLong(advertisementId));
//        return new SimpleResponse();
//    }
//
//    public Response updateAdvertisementContent(String advertisementId, AdvertisementModifyReceiveVo vo) {
//        advertisementPoMapper.updateByPrimaryKeySelective(new Advertisement(vo).createPo());
//        return new SimpleResponse();
//    }
//
//    public Response selectAllAdvertisementStates() {
//        List<AdvertisementPo> allPoList = advertisementPoMapper.selectByExample(new AdvertisementPoExample());
//        List<Map<String, String>> responseData = new ArrayList<>(allPoList.size());
//        for (AdvertisementPo po : allPoList) {
//            Map<String, String> record = new HashMap<>();
//            record.put("code", String.valueOf(po.getState()));   // 用 String.valueOf 代替 xxxObject.toString 以避免字段为空时报 NullPointerException
//            record.put("name", po.getLink());
//            responseData.add(record);
//        }
//        return new PayloadResponse<>(responseData);
//    }
//
//    public Response setDefaultAdvertisement(String advertisementID) {
//        AdvertisementPo selectiveUpdatePo = new AdvertisementPo();
//        selectiveUpdatePo.setBeDefault((byte) 1);
//        selectiveUpdatePo.setId(Long.parseLong(advertisementID));
//        advertisementPoMapper.updateByPrimaryKeySelective(selectiveUpdatePo);
//        return new SimpleResponse();
//    }
//
//
//
//
//

//
//    public Response saveAdvertisementImage(long advertisementId, MultipartFile file) {
//        // 判断此广告是否存在。如果不存在，那么返回错误
//        AdvertisementPo advertisement = advertisementPoMapper.selectByPrimaryKey(advertisementId);
//        if(advertisement==null){
//            return new SimpleResponse(ResponseCode.RESOURCE_ID_NOTEXIST);
//        }
//        // 保存文件
//        String advertisementImageSaveLocation = LocalPath.advertisementImageSaveLocation;
//        String originalSuffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
//        String filename = advertisementId + originalSuffix;
//        try{
//            Path filePath = Path.of(advertisementImageSaveLocation + "/" + filename);
//            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
//            // 保存成功，更新数据库中的图片链接（如果之前没有）
//            AdvertisementPo updatePo = new AdvertisementPo();
//            updatePo.setId(advertisementId);
//            updatePo.setImageUrl(filePath.toUri().toURL().toString());
//            System.out.println(updatePo);
//            System.out.println(advertisementPoMapper.insertSelective(updatePo));
//            // 返回默认正确响应
//            return new SimpleResponse();
//        } catch (IOException e) {
//            return new SimpleResponse(ResponseCode.LOCAL_FILE_WRITE_NOPRIVILEGE);
//        }
//    }
//
//    public Response updateAdvertisementTimeSegment(Long timeSegmentId, Long advertisementId) {
//        // 判断这个时段是否有效
//
//        // 写入更新
//        AdvertisementPo po = new AdvertisementPo();
//        po.setId(advertisementId);
//        po.setSegId(timeSegmentId);
//        advertisementPoMapper.updateByPrimaryKeySelective(po);
//        return new SimpleResponse();
//    }

}


