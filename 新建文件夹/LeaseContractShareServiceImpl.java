package com.zhaoshang800.service.branchmgr.contract.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.zhaoshang800.commons.tk.mybatis.service.BaseService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.zhaoshang800.commons.tk.mybatis.service.impl.AbstractBaseServiceImpl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 租赁费用分摊业务接口实现类
 * @author zhangyangdong
 * @date 2020-01-13 16:29:34
 * @version v4.0.8
 */ 
@Service(interfaceClass = DubboLeaseContractShareService.class, delay = -1, retries = 0)
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class LeaseContractShareServiceImpl extends AbstractBaseServiceImpl<LeaseContractShare, LeaseContractShareMapper> implements DubboLeaseContractShareService, BaseService<LeaseContractShare> {

    @Autowired
    private LeaseContractShareMapper leaseContractShareMapper;

    /**
     * 计算各分行分摊
     * @param arrayLeaseContractShareDetailDTO
     * @return
     */
    @Override
    public List<LeaseContractShareDetailVO> listShareDetail(LeaseContractShareDetailDTO[] arrayLeaseContractShareDetailDTO) {
        List<LeaseContractShareDetailVO> listLeaseContractShareDetailVO = new ArrayList<>();
        if (arrayLeaseContractShareDetailDTO != null) {
            for (LeaseContractShareDetailDTO leaseContractShareDetailDTO : arrayLeaseContractShareDetailDTO) {
                // 计算后的分摊明细
                LeaseContractShareDetailVO leaseContractShareDetailVO = new LeaseContractShareDetailVO();
                BeanUtils.copyProperties(leaseContractShareDetailDTO, leaseContractShareDetailVO);
                List<LeaseContractShareVO> listLeaseContractShareVO = this.getShareDetail(leaseContractShareDetailDTO);
                leaseContractShareDetailVO.setShare(listLeaseContractShareVO);
                listLeaseContractShareDetailVO.add(leaseContractShareDetailVO);
            }
        }
        return listLeaseContractShareDetailVO;
    }

    /**
     * 计算各分行分摊明细
     * @param leaseContractShareDetailDTO
     * @return
     */
    private List<LeaseContractShareVO> getShareDetail(LeaseContractShareDetailDTO leaseContractShareDetailDTO) {
        // 分摊总金额
        Float amount = 0f;
        BigDecimal amountBigDecimal = leaseContractShareDetailDTO.getAmount();
        if (amountBigDecimal != null) {
            amount = amountBigDecimal.floatValue();
        }
        LeaseContractShareDTO leaseContractShareDTO = new LeaseContractShareDTO();
        BeanUtils.copyProperties(leaseContractShareDetailDTO, leaseContractShareDTO);
        // 获取分摊记录
        List<LeaseContractShareVO> listLeaseContractShareVO = leaseContractShareMapper.listByFeeType(leaseContractShareDTO);
        if (CollectionUtils.isNotEmpty(listLeaseContractShareVO)) {
            // 固定分摊金额
            Float fixed = 0f;
            for (LeaseContractShareVO leaseContractShareVO : listLeaseContractShareVO) {
                if (leaseContractShareVO.getShareAmount() != null) {
                    fixed = fixed + leaseContractShareVO.getShareAmount();
                }
            }
            for (LeaseContractShareVO leaseContractShareVO : listLeaseContractShareVO) {
                if (leaseContractShareVO.getShareAmount() == null || (leaseContractShareVO.getShareAmount() != null && leaseContractShareVO.getShareAmount() <= 0)) {
                    Float shareAmount = (amount - fixed) * leaseContractShareVO.getRatioAmount();
                    BigDecimal bigDecimal = new BigDecimal(shareAmount);
                    float shareAmountFloat = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
                    leaseContractShareVO.setShareAmount(shareAmountFloat);
                }
            }
        }
        return listLeaseContractShareVO;
    }
}
