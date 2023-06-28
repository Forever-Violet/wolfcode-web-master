package cn.wolfcode.web.modules.linkmanInfo.service.impl;

import cn.wolfcode.web.modules.linkmanInfo.entity.TbCustLinkman;
import cn.wolfcode.web.modules.linkmanInfo.mapper.TbCustLinkmanMapper;
import cn.wolfcode.web.modules.linkmanInfo.service.ITbCustLinkmanService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 客户联系人 服务实现类
 * </p>
 *
 * @author 吴彦祖
 * @since 2023-06-25
 */
@Service
public class TbCustLinkmanServiceImpl extends ServiceImpl<TbCustLinkmanMapper, TbCustLinkman> implements ITbCustLinkmanService {

    @Autowired
    private TbCustLinkmanMapper tbCustLinkmanMapper;
    @Override
    public List<TbCustLinkman> getByCustId(String custId) {
        //根据企业客户id查询联系人列表
        QueryWrapper<TbCustLinkman> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("cust_id", custId);
        return tbCustLinkmanMapper.selectList(queryWrapper);
    }
}
