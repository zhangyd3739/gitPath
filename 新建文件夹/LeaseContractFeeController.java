package com.zhaoshang800.web.manager.controller.branchmgr;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zhaoshang800.base.commons.result.Result;
import com.zhaoshang800.web.manager.controller.BaseController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author zhangyangdong
 * @create 2020-01-14
 * @desc 分行租赁合同租金信息
 **/
@RestController
@RequestMapping("/branchmgr/leaseContractFee")
public class LeaseContractFeeController extends BaseController {

    @Reference
    private DubboLeaseContractFeeService dubboLeaseContractFeeService;

    /**
     * 根据租赁合同获取租金
     * @param leaseContractFeeDTO
     * @return
     */
    @RequestMapping("/getByFeeType")
    public Result<Object> getByFeeType(@RequestBody LeaseContractFeeDTO leaseContractFeeDTO) {
        Result result = new Result();
        LeaseContractFeeVO leaseContractFeeVO = dubboLeaseContractFeeService.getByFeeType(leaseContractFeeDTO);
        result.setData(leaseContractFeeVO);
        return result;
    }

}
