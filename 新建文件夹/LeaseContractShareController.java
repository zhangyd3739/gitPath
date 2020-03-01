package com.zhaoshang800.web.manager.controller.branchmgr;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zhaoshang800.base.commons.result.Result;
import com.zhaoshang800.base.commons.tool.ChineseNumber;
import com.zhaoshang800.web.manager.controller.BaseController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author zhangyangdong
 * @create 2020-01-14
 * @desc 分行租赁合同分摊信息
 **/
@RestController
@RequestMapping("/branchmgr/leaseContractShare")
public class LeaseContractShareController extends BaseController {

    @Reference
    private DubboLeaseContractShareService dubboLeaseContractShareService;

    /**
     * 获取分摊信息
     *
     * @param arrayLeaseContractShareDetailDTO
     * @return
     */
    @RequestMapping("/listShareDetail")
    public Result<Object> listShareDetail(@RequestBody LeaseContractShareDetailDTO[] arrayLeaseContractShareDetailDTO) {
        Result result = new Result();
        Map map = new HashMap<>();
        List<LeaseContractShareDetailVO> leaseContractShareDetail = dubboLeaseContractShareService.listShareDetail(arrayLeaseContractShareDetailDTO);
        map.put("leaseContractShareDetail", leaseContractShareDetail);
        BigDecimal totalAmount = this.getTotalAmount(arrayLeaseContractShareDetailDTO);
        DecimalFormat df = new DecimalFormat("###,##0.00");
        String amountStr = df.format(totalAmount);
        map.put("amount", amountStr);
        map.put("amountChineseNumber", ChineseNumber.toChineseNumber(totalAmount) + "元");
        result.setData(map);
        return result;
    }

    /**
     * 计算总数
     *
     * @param arrayLeaseContractShareDetailDTO
     * @return
     */
    private BigDecimal getTotalAmount(LeaseContractShareDetailDTO[] arrayLeaseContractShareDetailDTO) {
        BigDecimal totalAmount = new BigDecimal("0");
        if (arrayLeaseContractShareDetailDTO != null) {
            for (LeaseContractShareDetailDTO leaseContractShareDetailDTO : arrayLeaseContractShareDetailDTO) {
                BigDecimal amount = leaseContractShareDetailDTO.getAmount();
                totalAmount = totalAmount.add(amount);
            }
        }
        return totalAmount;
    }

}
