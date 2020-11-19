package com.htstar.ovms.enterprise.service.impl;

import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.common.security.util.SecurityUtils;
import com.htstar.ovms.enterprise.api.constant.CardCostInfoConstant;
import com.htstar.ovms.enterprise.api.entity.CarFuelCard;
import com.htstar.ovms.enterprise.api.entity.CardCostInfo;
import com.htstar.ovms.enterprise.api.req.CarFileManageReq;
import com.htstar.ovms.enterprise.api.req.RechargeReq;
import com.htstar.ovms.enterprise.api.vo.FuelCardPageVO;
import com.htstar.ovms.enterprise.mapper.CarFuelCardMapper;
import com.htstar.ovms.enterprise.service.CarFuelCardService;
import com.htstar.ovms.enterprise.service.CarInfoService;
import com.htstar.ovms.enterprise.service.CardCostInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static cn.hutool.core.util.NumberUtil.roundStr;

/**
 * 油卡
 *
 * @author lw
 * @date 2020-06-23 13:54:59
 */
@Service
@Slf4j
public class CarFuelCardServiceImpl extends ServiceImpl<CarFuelCardMapper, CarFuelCard> implements CarFuelCardService {
    @Autowired
    private CarInfoService carInfoService;
    @Autowired
    private CardCostInfoService cardCostInfoService;
    /**
     * 新增
     * @param carFuelCard
     * @return
     */
    @Override
    public R saveInfo(CarFuelCard carFuelCard) {
        String mobile = carFuelCard.getMobile();

        if (!NumberUtil.isInteger(carFuelCard.getCardBalance().toString())){
            return R.failed("请输入正确的余额");
        }
        if(StrUtil.isEmpty(carFuelCard.getCardName())){
            return R.failed("请输入卡名");
        }
        if(StrUtil.isEmpty(carFuelCard.getCardNo())){
            return R.failed("请输入卡号");
        }
        Integer count = baseMapper.selectCount(new QueryWrapper<CarFuelCard>()
                .eq("card_no", carFuelCard.getCardNo()
                ).eq("del_flag", 0));
        if (count>0){
            return R.failed("该卡号已存在，添加失败");
        }
        Integer etpId = SecurityUtils.getUser().getEtpId();
        carFuelCard.setEtpId(etpId);
        if (this.save(carFuelCard)){
            return R.ok("添加成功");
        }
        return R.failed("添加失败");
    }

    /**
     *批量删除
     * @param ids
     * @return
     */
    @Override
    public R removeByIds(String ids) {
        if (StrUtil.isBlank(ids)){
            return R.failed("请选择要删除的记录");
        }
        String[] split = ids.replace("\"", "").split(",");
        for (String s : split) {
            Integer id = Integer.valueOf(s);

            CarFuelCard carFuelCard = baseMapper.selectOne(new QueryWrapper<CarFuelCard>().eq("id", id));
            carFuelCard.setDelFlag(1);
            baseMapper.updateById(carFuelCard);
        }
        return R.ok("删除成功");
    }

    /**
     * 修改
     * @param carFuelCard
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R updateFuelById(CarFuelCard carFuelCard) {
        log.info("要修改的油卡信息为{}", JSONObject.toJSONString(carFuelCard));
        String mobile = carFuelCard.getMobile();
        if (StrUtil.isEmpty(mobile)&& !Validator.isMobile(mobile)){
            return R.failed("请输入正确的手机号码");
        }
        if (!NumberUtil.isInteger(carFuelCard.getCardBalance().toString())){
            return R.failed("请输入正确的余额");
        }
        if(StrUtil.isEmpty(carFuelCard.getCardName())){
            return R.failed("请输入卡名");
        }
        if(StrUtil.isEmpty(carFuelCard.getCardNo())){
            return R.failed("请输入卡号");
        }
        Integer id = carFuelCard.getId();
        CarFuelCard oldCarFuelCard = baseMapper.selectOne(new QueryWrapper<CarFuelCard>().eq("id",id )
                .eq("del_flag", 0));
        //油卡卡号新旧不同
        if (carFuelCard.getCardNo()!=null&&!oldCarFuelCard.getCardNo().equals(carFuelCard.getCardNo())){
            //判断新卡是否存在
            Integer count = baseMapper.selectCount(new QueryWrapper<CarFuelCard>().eq("card_no", carFuelCard.getCardNo())
                    .eq("del_flag", 0));
            if (count>0){
                return R.failed("修改的油卡卡号已经存在,修改失败");
            }
        }
        //原本余额
        Integer oldBalance = oldCarFuelCard.getCardBalance();
        //修改后的余额
        Integer cardBalance = carFuelCard.getCardBalance();
        if (cardBalance!=null){
            Integer margin=cardBalance-oldBalance;
            //新增流水
            if (0 != margin){
                CardCostInfo cardCostInfo = new CardCostInfo();
                cardCostInfo.setCardId(id);
                cardCostInfo.setCost(margin);
                cardCostInfo.setCardType(CardCostInfoConstant.FUEL_CARD);
                cardCostInfo.setActionType(CardCostInfoConstant.MODIFY);
                cardCostInfo.setRemark(carFuelCard.getRemark());
                cardCostInfo.setBalance(cardBalance);
                cardCostInfoService.saveInfo(cardCostInfo);
            }
        }
        this.updateById(carFuelCard);
        return  R.ok("修改成功");
    }

    /**
     * 分页
     * @param carFileManageReq
     * @return
     */
    @Override
    public IPage<FuelCardPageVO> queryPage(CarFileManageReq carFileManageReq) {
        if (carFileManageReq==null){
            carFileManageReq = new CarFileManageReq();
        }
        carFileManageReq.setEtpId(SecurityUtils.getUser().getEtpId());
        return baseMapper.queryPage(carFileManageReq);
    }


    /**
     * 导出
     * @param req
     */
    @Override
    public void exportExcel(CarFileManageReq req) {
        Integer etpId = SecurityUtils.getUser().getEtpId();
        req.setEtpId(etpId);
        req.setSize(Long.MAX_VALUE);
        //导出数据集合
        List<Map<String, Object>> rows = new ArrayList<>();
        //需要导出的数据
        List<FuelCardPageVO> fuelCardPageVos = baseMapper.exportExcel(req);
        if (fuelCardPageVos.size()>0){
            for (FuelCardPageVO fuelCardPageVo : fuelCardPageVos) {
                LinkedHashMap<String, Object> map = new LinkedHashMap<>();
                map.put("卡名", fuelCardPageVo.getCardName());
                map.put("卡号", fuelCardPageVo.getCardNo());
                map.put("持卡人", fuelCardPageVo.getCardUser());
                map.put("持卡人手机", fuelCardPageVo.getMobile());
                map.put("余额", fuelCardPageVo.getCardBalance()==null?"":roundStr(((double)(fuelCardPageVo.getCardBalance()) / 100), 2));
                String bindingType="";
                /*绑定类型0:未绑定 1:车辆 2:成员 */
                Integer type = fuelCardPageVo.getBindingType();

                if (type==0){
                    bindingType="未绑定";
                }else if (type==1){
                    bindingType="车辆";
                }else if (type==2){
                    bindingType="成员";
                }
                map.put("绑定类型",bindingType);
                map.put("绑定对象",fuelCardPageVo.getBindingUser());
                map.put("备注", fuelCardPageVo.getRemark());
                rows.add(map);
            }
        }
        carInfoService.carExportUtil(rows,"油卡信息");
    }

    /**
     * 油卡扣费 返回最新余额
     * @param cardId 卡号
     * @param cost  扣费金额
     */
    @Override
    public Integer fuelDeduction(Integer cardId, Integer cost) {
        CarFuelCard carFuelCard = baseMapper.selectOne(new QueryWrapper<CarFuelCard>().eq("id", cardId));
        //原本余额
        Integer cardBalance = carFuelCard.getCardBalance();
        carFuelCard.setCardBalance(cardBalance-cost);
        baseMapper.updateById(carFuelCard);
        return carFuelCard.getCardBalance();
    }

    /**
     * 检查能否绑定
     * @param cardId 编号
     * @param cost  待扣费金额
     * @return
     */
    @Override
    public boolean checkIsBinding(Integer cardId, Integer cost) {
        CarFuelCard carFuelCard = baseMapper.selectOne(new QueryWrapper<CarFuelCard>().eq("id", cardId));
        //原本余额
        Integer cardBalance = carFuelCard.getCardBalance();
        if (cost>cardBalance){
            return false;
        }
        return true;
    }

    /**
     * 油卡充值
     * @param req
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R recharge(RechargeReq req) {
        log.info("油卡充值{}",JSONObject.toJSONString(req) );
        if (req==null|| !NumberUtil.isInteger(req.getCost().toString())||req.getCost() <= 0) {
            return R.failed("充值金额不合理");
        }
        Integer id = req.getId();
        Integer cost = req.getCost();
        CarFuelCard carFuelCard = baseMapper.selectOne(new QueryWrapper<CarFuelCard>().eq("id", id));
        //余额
        Integer cardBalance = carFuelCard.getCardBalance();
        carFuelCard.setCardBalance(cardBalance+cost);
        baseMapper.updateById(carFuelCard);
        //新增流水
        CardCostInfo cardCostInfo = new CardCostInfo();
        cardCostInfo.setCardId(id);
        cardCostInfo.setCost(cost);
        cardCostInfo.setCardType(CardCostInfoConstant.FUEL_CARD);
        cardCostInfo.setActionType(CardCostInfoConstant.RECHARGE);
        //余额
        cardCostInfo.setBalance(carFuelCard.getCardBalance());
        cardCostInfo.setRemark(req.getRemark());
        cardCostInfoService.saveInfo(cardCostInfo);
        return R.ok("充值成功");
    }

}
