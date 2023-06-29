package cn.wolfcode.web.modules.custOrderInfo.controller;

import cn.wolfcode.web.commons.entity.LayuiPage;
import cn.wolfcode.web.commons.utils.LayuiTools;
import cn.wolfcode.web.commons.utils.SystemCheckUtils;
import cn.wolfcode.web.modules.BaseController;
import cn.wolfcode.web.modules.custinfo.entity.TbCustomer;
import cn.wolfcode.web.modules.custinfo.service.ITbCustomerService;
import cn.wolfcode.web.modules.log.LogModules;
import cn.wolfcode.web.modules.sys.entity.SysUser;
import cn.wolfcode.web.modules.sys.form.LoginForm;
import cn.wolfcode.web.modules.sys.service.UserService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import cn.wolfcode.web.modules.custOrderInfo.entity.TbOrderInfo;
import cn.wolfcode.web.modules.custOrderInfo.service.ITbOrderInfoService;


import link.ahsj.core.annotations.AddGroup;
import link.ahsj.core.annotations.SameUrlData;
import link.ahsj.core.annotations.SysLog;
import link.ahsj.core.annotations.UpdateGroup;
import link.ahsj.core.entitys.ApiModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author 吴彦祖
 * @since 2023-06-29
 */
@Controller
@RequestMapping("custOrderInfo")
public class TbOrderInfoController extends BaseController {

    @Autowired
    private ITbOrderInfoService entityService;

    @Autowired
    private UserService userService;

    @Autowired
    private ITbCustomerService tbCustomerService;

    private static final String LogModule = "TbOrderInfo";

    @GetMapping("/list.html")
    public ModelAndView list(ModelAndView mv) {

        mv.setViewName("custOrder/custOrderInfo/list");
        return mv;
    }

    @RequestMapping("/add.html")
    @PreAuthorize("hasAuthority('custOrder:custOrderInfo:add')")
    public ModelAndView toAdd(ModelAndView mv) {

        // 获取企业客户列表
        List<TbCustomer> custList = tbCustomerService.list();
        // 共享到请求域
        mv.addObject("custList", custList);

        mv.setViewName("custOrder/custOrderInfo/add");
        return mv;
    }

    @GetMapping("/{id}.html")
    @PreAuthorize("hasAuthority('custOrder:custOrderInfo:update')")
    public ModelAndView toUpdate(@PathVariable("id") String id, ModelAndView mv) {

        // 获取企业客户列表
        List<TbCustomer> custList = tbCustomerService.list();
        // 共享到请求域
        mv.addObject("custList", custList);

        mv.setViewName("custOrder/custOrderInfo/update");
        mv.addObject("obj", entityService.getById(id));
        mv.addObject("id", id);
        return mv;
    }

    @RequestMapping("list")
    @PreAuthorize("hasAuthority('custOrder:custOrderInfo:list')")
    public ResponseEntity page(LayuiPage layuiPage) {
        SystemCheckUtils.getInstance().checkMaxPage(layuiPage);
        IPage page = new Page<>(layuiPage.getPage(), layuiPage.getLimit());
        return ResponseEntity.ok(LayuiTools.toLayuiTableModel(entityService.page(page)));
    }

    @SameUrlData
    @PostMapping("save")
    @SysLog(value = LogModules.SAVE, module =LogModule)
    @PreAuthorize("hasAuthority('custOrder:custOrderInfo:add')")
    public ResponseEntity<ApiModel> save(@Validated({AddGroup.class}) @RequestBody TbOrderInfo entity, HttpServletRequest request) {
        // 清空id, 防止用户自定义
        entity.setId(null);

        // 获取当前登录用户的账号名
        SysUser sysUser = (SysUser) request.getSession().getAttribute(LoginForm.LOGIN_USER_KEY);
        String username = userService.getById(sysUser.getUserId()).getUsername();
        // 设置录入人
        entity.setInputUser(username);

        // 设置录入时间
        entity.setInputTime(LocalDateTime.now());


        entityService.save(entity);
        return ResponseEntity.ok(ApiModel.ok());
    }

    @SameUrlData
    @SysLog(value = LogModules.UPDATE, module = LogModule)
    @PutMapping("update")
    @PreAuthorize("hasAuthority('custOrder:custOrderInfo:update')")
    public ResponseEntity<ApiModel> update(@Validated({UpdateGroup.class}) @RequestBody TbOrderInfo entity) {
        entityService.updateById(entity);
        return ResponseEntity.ok(ApiModel.ok());
    }

    @SysLog(value = LogModules.DELETE, module = LogModule)
    @DeleteMapping("delete/{id}")
    @PreAuthorize("hasAuthority('custOrder:custOrderInfo:delete')")
    public ResponseEntity<ApiModel> delete(@PathVariable("id") String id) {
        entityService.removeById(id);
        return ResponseEntity.ok(ApiModel.ok());
    }

}
