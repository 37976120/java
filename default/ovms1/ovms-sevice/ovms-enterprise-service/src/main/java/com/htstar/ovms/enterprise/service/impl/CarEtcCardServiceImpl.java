package com.htstar.ovms.enterprise.service.impl;

import cn.afterturn.easypoi.cache.manager.IFileLoader;
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
import com.htstar.ovms.enterprise.api.entity.CarEtcCard;
import com.htstar.ovms.enterprise.api.entity.CardCostInfo;
import com.htstar.ovms.enterprise.api.req.CarFileManageReq;
import com.htstar.ovms.enterprise.api.vo.EtcCardPageVO;
import com.htstar.ovms.enterprise.mapper.CarEtcCardMapper;
import com.htstar.ovms.enterprise.service.CarEtcCardService;
import com.htstar.ovms.enterprise.service.CarInfoService;
import com.htstar.ovms.enterprise.service.CardCostInfoService;
import com.htstar.ovms.enterprise.api.req.RechargeReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.Callable;

import static cn.hutool.core.util.NumberUtil.roundStr;

/**
 * etc卡
 *
 * @author lw
 * @date 2020-06-23 13:54:59
 */
@Service
@Slf4j
public class CarEtcCardServiceImpl extends ServiceImpl<CarEtcCardMapper, CarEtcCard> implements CarEtcCardService {
    @Autowired
    private CarInfoService carInfoService;
    @Autowired
    private CardCostInfoService cardCostInfoService;

    /**
     * 新增
     *
     * @param carEtcCard
     * @return
     */
    @Override
    public R saveInfo(CarEtcCard carEtcCard) {
        String mobile = carEtcCard.getMobile();

        if (!NumberUtil.isInteger(carEtcCard.getCardBalance().toString())){
            return R.failed("请输入正确的余额");
        }
        if(StrUtil.isEmpty(carEtcCard.getCardName())){
            return R.failed("请输入卡名");
        }
        if(StrUtil.isEmpty(carEtcCard.getCardNo())){
            return R.failed("请输入卡号");
        }
        Integer count = baseMapper.selectCount(new QueryWrapper<CarEtcCard>()
                .eq("card_no", carEtcCard.getCardNo()
                ).eq("del_flag", 0));
        if (count > 0) {
            return R.failed("该etc卡号已存在，添加失败");
        }
        Integer etpId = SecurityUtils.getUser().getEtpId();
        carEtcCard.setEtpId(etpId);
        if (this.save(carEtcCard)) {
            return R.ok("添加成功");
        }
        return R.failed("添加失败");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @Override
    public R removeByIds(String ids) {
        if (StrUtil.isBlank(ids)) {
            return R.failed("请选择要删除的数据");
        }
        String[] split = ids.replace("\"", "").split(",");
        for (String s : split) {
            Integer id = Integer.valueOf(s);
            CarEtcCard carEtcCard = baseMapper.selectOne(new QueryWrapper<CarEtcCard>().eq("id", id));
            carEtcCard.setDelFlag(1);
            baseMapper.updateById(carEtcCard);
        }
        return R.ok("删除成功");
    }

    /**
     * 修改
     *
     * @param carEtcCard
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R updateEtcById(CarEtcCard carEtcCard) {
        log.info("要修改的etc卡信息为{}", JSONObject.toJSONString(carEtcCard));
        String mobile = carEtcCard.getMobile();
        if (StrUtil.isEmpty(mobile)&& !Validator.isMobile(mobile)){
            return R.failed("请输入正确的手机号码");
        }
        if (!NumberUtil.isInteger(carEtcCard.getCardBalance().toString())){
            return R.failed("请输入正确的余额");
        }
        if(StrUtil.isEmpty(carEtcCard.getCardName())){
            return R.failed("请输入卡名");
        }
        if(StrUtil.isEmpty(carEtcCard.getCardNo())){
            return R.failed("请输入卡号");
        }
        Integer id = carEtcCard.getId();
        Integer cardType = carEtcCard.getCardType();
        if (cardType==CardCostInfoConstant.CREDIT_CARD){
            carEtcCard.setCardBalance(0);
        }
        CarEtcCard oldCarEtc = baseMapper.selectOne(new QueryWrapper<CarEtcCard>().eq("id", id)
                .eq("del_flag", 0));
        //etc卡号新旧不同
        if (!oldCarEtc.getCardNo().equals(carEtcCard.getCardNo())) {
            //判断新卡是否存在
            Integer count = baseMapper.selectCount(new QueryWrapper<CarEtcCard>().eq("card_no", carEtcCard.getCardNo())
                    .eq("del_flag", 0));
            if (count > 0) {
                return R.failed("修改的etc卡号已经存在,修改失败");
            }
        }
        this.updateById(carEtcCard);
        //信用卡不修改流水
        if (cardType==CardCostInfoConstant.CREDIT_CARD){
            return R.ok("修改成功");
        }
        //原本余额
        Integer oldBalance = oldCarEtc.getCardBalance();
        //修改后的余额
        Integer balance = carEtcCard.getCardBalance();
        Integer margin=balance-oldBalance;
        //新增流水
        if (0 != margin){
            CardCostInfo cardCostInfo = new CardCostInfo();
            cardCostInfo.setCardId(id);
            cardCostInfo.setCost(margin);
            cardCostInfo.setCardType(CardCostInfoConstant.ETC_CARD);
            cardCostInfo.setActionType(CardCostInfoConstant.MODIFY);
            cardCostInfo.setRemark(carEtcCard.getRemark());
            cardCostInfo.setBalance(balance);
            cardCostInfoService.saveInfo(cardCostInfo);
        }

        return R.ok("修改成功");

    }

    /**
     * 分页
     *
     * @param carFileManageReq
     * @return
     */
    @Override
    public IPage<EtcCardPageVO> queryPage(CarFileManageReq carFileManageReq) {
        if (carFileManageReq == null) {
            carFileManageReq = new CarFileManageReq();
        }
        carFileManageReq.setEtpId(SecurityUtils.getUser().getEtpId());
        return baseMapper.queryPage(carFileManageReq);
    }

    /**
     * 导出
     *
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
        List<EtcCardPageVO> etcCardPageVos = baseMapper.exportExcel(req);
        if (etcCardPageVos.size() > 0) {
            for (EtcCardPageVO etcCardPageVo : etcCardPageVos) {
                LinkedHashMap<String, Object> map = new LinkedHashMap<>();
                map.put("卡名", etcCardPageVo.getCardName());
                map.put("卡号", etcCardPageVo.getCardNo());
                String cardType=etcCardPageVo.getCardType()==0?"借记卡":"信用卡";
                map.put("卡类型", cardType);
                map.put("持卡人", etcCardPageVo.getCardUser());
                map.put("持卡人手机", etcCardPageVo.getMobile());
                map.put("余额", etcCardPageVo.getCardBalance() == null ? "" : roundStr(((double) (etcCardPageVo.getCardBalance()) / 100), 2));
                map.put("绑定对象", etcCardPageVo.getBindingUser());
                map.put("备注", etcCardPageVo.getRemark());
                rows.add(map);
            }
        }
        carInfoService.carExportUtil(rows, "etc卡信息");
    }

    /**
     * etc扣费
     *返回最新余额
     * @param cardId etc卡
     * @param cost   扣费金额
     */
    @Override
    public Integer etcDeduction(Integer cardId, Integer cost) {
        CarEtcCard carEtcCard = baseMapper.selectOne(new QueryWrapper<CarEtcCard>().eq("id", cardId));
        //信用卡直接跳过
        if (carEtcCard.getCardType()==1){
           return 0;
        }
        //原本余额
        Integer cardBalance = carEtcCard.getCardBalance();
        carEtcCard.setCardBalance(cardBalance - cost);
        baseMapper.updateById(carEtcCard);
        return carEtcCard.getCardBalance();
    }

    /**
     * 检查是否能绑定
     *
     * @param cardId 卡id
     * @param cost   待扣费金额
     * @return
     */
    @Override
    public boolean checkIsBinding(Integer cardId, Integer cost) {
        CarEtcCard carEtcCard = baseMapper.selectOne(new QueryWrapper<CarEtcCard>().eq("id", cardId));
        //借记卡
        if (carEtcCard.getCardType()==0){
            Integer cardBalance = carEtcCard.getCardBalance();
            if (cardBalance == null || cost > cardBalance) {
                return false;
            }
        }
        return true;
    }

    /**
     * 费用充值
     *
     * @param req
     * @return
     */
    @Override
    public R recharge(RechargeReq req) {
        if (req==null|| !NumberUtil.isInteger(req.getCost().toString())||req.getCost() <= 0) {
            return R.failed("充值金额不合理");
        }
        Integer id = req.getId();
        Integer cost = req.getCost();
        CarEtcCard carEtcCard = baseMapper.selectOne(new QueryWrapper<CarEtcCard>().eq("id", id));
        if (carEtcCard.getCardType() == CardCostInfoConstant.CREDIT_CARD) {
            return R.failed("信用卡不可充值");
        }
        //余额
        Integer cardBalance = carEtcCard.getCardBalance();
        carEtcCard.setCardBalance(cardBalance + cost);
        baseMapper.updateById(carEtcCard);
        //新增流水
        CardCostInfo cardCostInfo = new CardCostInfo();
        cardCostInfo.setCardId(id);
        cardCostInfo.setCost(cost);
        cardCostInfo.setCardType(CardCostInfoConstant.ETC_CARD);
        cardCostInfo.setActionType(CardCostInfoConstant.RECHARGE);
        cardCostInfo.setRemark(req.getRemark());
        cardCostInfo.setCostTime(LocalDateTime.now());
        //操作后余额
        cardCostInfo.setBalance(carEtcCard.getCardBalance());
        cardCostInfoService.saveInfo(cardCostInfo);
        return R.ok("充值成功");
    }
}
