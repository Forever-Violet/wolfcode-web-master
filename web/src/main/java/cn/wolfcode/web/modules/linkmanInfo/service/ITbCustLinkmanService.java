package cn.wolfcode.web.modules.linkmanInfo.service;

import cn.wolfcode.web.modules.linkmanInfo.entity.TbCustLinkman;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 客户联系人 服务类
 * </p>
 *
 * @author 吴彦祖
 * @since 2023-06-25
 */
public interface ITbCustLinkmanService extends IService<TbCustLinkman> {

    /**
     * 根据企业用户id获取对应的联系人列表
     * @param custId
     * @return
     */
    List<TbCustLinkman> getByCustId(String custId);

}
