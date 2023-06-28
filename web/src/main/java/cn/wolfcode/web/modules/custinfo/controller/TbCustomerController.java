package cn.wolfcode.web.modules.custinfo.controller;

import cn.wolfcode.web.commons.entity.LayuiPage;
import cn.wolfcode.web.commons.utils.CityUtils;
import cn.wolfcode.web.commons.utils.LayuiTools;
import cn.wolfcode.web.commons.utils.SystemCheckUtils;
import cn.wolfcode.web.modules.BaseController;
import cn.wolfcode.web.modules.linkmanInfo.entity.TbCustLinkman;
import cn.wolfcode.web.modules.linkmanInfo.mapper.TbCustLinkmanMapper;
import cn.wolfcode.web.modules.linkmanInfo.service.ITbCustLinkmanService;
import cn.wolfcode.web.modules.linkmanInfo.service.impl.TbCustLinkmanServiceImpl;
import cn.wolfcode.web.modules.log.LogModules;
import cn.wolfcode.web.modules.sys.entity.SysUser;
import cn.wolfcode.web.modules.sys.form.LoginForm;
import cn.wolfcode.web.modules.sys.service.SysUserService;
import cn.wolfcode.web.modules.sys.service.UserService;
import cn.wolfcode.web.modules.sys.service.impl.SysUserServiceImpl;
import cn.wolfcode.web.modules.sys.service.impl.UserServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;


import cn.wolfcode.web.modules.custinfo.entity.TbCustomer;
import cn.wolfcode.web.modules.custinfo.service.ITbCustomerService;


import link.ahsj.core.annotations.AddGroup;
import link.ahsj.core.annotations.SameUrlData;
import link.ahsj.core.annotations.SysLog;
import link.ahsj.core.annotations.UpdateGroup;
import link.ahsj.core.entitys.ApiModel;
import link.ahsj.core.exception.AppServerException;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.util.StringUtil;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.management.Query;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author 吴彦祖
 * @since 2023-06-25
 */
@Controller
@RequestMapping("custinfo")
public class TbCustomerController extends BaseController {

    @Autowired
    private ITbCustomerService entityService;

    @Autowired
    private UserService userService;

    @Autowired
    private ITbCustLinkmanService tbCustLinkmanService;


    private static final String LogModule = "TbCustomer";

    @GetMapping("/list.html")
    public ModelAndView list(ModelAndView mv) {
        mv.setViewName("cust/custinfo/list");
        mv.addObject("cities", CityUtils.citys);
        return mv;
    }

    @RequestMapping("/add.html")
    @PreAuthorize("hasAuthority('cust:custinfo:add')")
    public ModelAndView toAdd(ModelAndView mv) {

        //组装省份信息 将数据放在请求域
        mv.addObject("cities", CityUtils.citys);

        mv.setViewName("cust/custinfo/add");
        return mv;
    }

    @GetMapping("/{id}.html")
    @PreAuthorize("hasAuthority('cust:custinfo:update')")
    public ModelAndView toUpdate(@PathVariable("id") String id, ModelAndView mv) {
        mv.setViewName("cust/custinfo/update");

        //组装省份信息, 共享到请求域
        mv.addObject("cities", CityUtils.citys);

        mv.addObject("obj", entityService.getById(id));
        mv.addObject("id", id);
        return mv;
    }

    @RequestMapping("list")
    @PreAuthorize("hasAuthority('cust:custinfo:list')") //parameterName: 企业名称  province: 省份
    public ResponseEntity page(LayuiPage layuiPage, String parameterName, String province, Integer openStatus) {

        SystemCheckUtils.getInstance().checkMaxPage(layuiPage);
        //分页对象
        IPage page = new Page<>(layuiPage.getPage(), layuiPage.getLimit());
        //具体分页数据  获取
        page = entityService
                .lambdaQuery()
                .like(!StringUtils.isEmpty(parameterName), TbCustomer::getCustomerName, parameterName) //企业名称
                .eq(!StringUtils.isEmpty(province), TbCustomer::getProvince, province) //省份
                .eq(!ObjectUtils.isEmpty(openStatus), TbCustomer::getOpenStatus, openStatus) //经营状态
                .page(page);

        // 列表所属省份 查询 填充
        List<TbCustomer> records = page.getRecords();
        for (TbCustomer record : records) {
            // 根据省份编号获取省份名称
            String cityValue = CityUtils.getCityValue(record.getProvince());
            // 设置当前记录的省份名称
            record.setProvinceName(cityValue);

            // 根据录入人id获取录入人账号
            SysUser sysUser = userService.getById(record.getInputUserId());
            if (sysUser != null) {
                String inputUserName = sysUser.getUsername();
                // 设置录入人账号
                record.setInputUserName(inputUserName);
            } else { //此种情况是录入人的账号被删除, 将曾经保留的录入人id作为录入人账号
                record.setInputUserName(record.getInputUserId());
            }

        }

        return ResponseEntity.ok(LayuiTools.toLayuiTableModel(page));
    }

    @SameUrlData
    @PostMapping("save")
    @SysLog(value = LogModules.SAVE, module =LogModule)
    @PreAuthorize("hasAuthority('cust:custinfo:add')") //AddGroup.class组
    public ResponseEntity<ApiModel> save(@Validated({AddGroup.class}) @RequestBody TbCustomer entity, HttpServletRequest request) {
        // 永远不要信任前端传来的数据, 清空id, 防止用户私自定义id
        entity.setId(null);
        // 设置该记录的录入时间
        entity.setInputTime(LocalDateTime.now());
        // 从session域中获取当前登录用户, 即设置录入人id
        SysUser sysUser = (SysUser) request.getSession().getAttribute(LoginForm.LOGIN_USER_KEY);
        entity.setInputUserId(sysUser.getUserId());

        // 判断当前新增的企业客户名字是否已经存在
        // 查询数据库是否存在相同名字
        Integer count = entityService.lambdaQuery()
                .eq(TbCustomer::getCustomerName,entity.getCustomerName())
                .count();
        if (count > 0) {
            // 说明数据库已存在相同名字的企业用户
            throw new AppServerException("这个客户已经存在了!");
        }

        entityService.save(entity);
        return ResponseEntity.ok(ApiModel.ok());
    }

    @SameUrlData
    @SysLog(value = LogModules.UPDATE, module = LogModule)
    @PutMapping("update")
    @PreAuthorize("hasAuthority('cust:custinfo:update')") //UpdateGroup.class组
    public ResponseEntity<ApiModel> update(@Validated({UpdateGroup.class}) @RequestBody TbCustomer entity) {
        // 更新该记录的修改时间
        entity.setUpdateTime(LocalDateTime.now());
        // 重置记录插入时间, 防止被修改
        // entity.setInputTime(entityService.getById(entity.getId()).getInputTime());

        // 判断当前修改的企业客户名字是否已经存在
        // 查询数据库是否存在相同名字
        Integer count = entityService.lambdaQuery()
                .eq(TbCustomer::getCustomerName,entity.getCustomerName()) // 名字相同
                .ne(TbCustomer::getId, entity.getId()) // 当前修改的企业的id与相同名字的id不等
                .count();
        if (count > 0) { // 条数>0 说明存在重复企业名字
            // 说明数据库已存在相同名字的企业用户
            throw new AppServerException("这个客户已经存在了!");
        }

        // 根据id修改记录, 不改变id
        entityService.updateById(entity);
        return ResponseEntity.ok(ApiModel.ok());
    }

    @SysLog(value = LogModules.DELETE, module = LogModule)
    @DeleteMapping("delete/{id}")
    @PreAuthorize("hasAuthority('cust:custinfo:delete')")
    public ResponseEntity<ApiModel> delete(@PathVariable("id") String id) {
        entityService.removeById(id);

        // 根据要删除的企业用户id, 一并删除旗下的联系人
        tbCustLinkmanService.lambdaUpdate().eq(TbCustLinkman::getCustId, id).remove();

        return ResponseEntity.ok(ApiModel.ok());
    }

}
