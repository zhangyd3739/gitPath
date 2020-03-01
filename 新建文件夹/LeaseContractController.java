package com.zhaoshang800.web.manager.controller.branchmgr;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zhaoshang800.base.commons.result.Result;
import com.zhaoshang800.web.manager.controller.BaseController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * @author zhangyangdong
 * @create 2020-01-14
 * @desc 分行租赁合同
 **/
@RestController
@RequestMapping("/branchmgr/leaseContract")
public class LeaseContractController extends BaseController {

    @Reference
    private DubboLeaseContractService dubboLeaseContractService;

    /**
     * 根据分行id查询租赁合同
     * @param leaseContract
     * @return
     */
    @RequestMapping("/getByBranchId")
    public Result<Object> getByBranchId(@RequestBody LeaseContractDTO leaseContract) {
        Result result = new Result();
        List<LeaseContractVO> listLeaseContractVO = dubboLeaseContractService.listByBranchId(leaseContract.getBranchId());
        result.setData(listLeaseContractVO);
        return result;
    }

}
