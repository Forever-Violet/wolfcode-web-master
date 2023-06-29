package cn.wolfcode.web.modules.custOrderInfo.controller;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.wolfcode.web.commons.entity.LayuiPage;
import cn.wolfcode.web.commons.utils.LayuiTools;
import cn.wolfcode.web.commons.utils.PoiExportHelper;
import cn.wolfcode.web.commons.utils.SystemCheckUtils;
import cn.wolfcode.web.modules.BaseController;
import cn.wolfcode.web.modules.custinfo.entity.TbCustomer;
import cn.wolfcode.web.modules.custinfo.service.ITbCustomerService;
import cn.wolfcode.web.modules.linkmanInfo.entity.TbCustLinkman;
import cn.wolfcode.web.modules.linkmanVisitInfo.entity.TbVisit;
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
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
        // 获取企业用户列表
        List<TbCustomer> custList = tbCustomerService.list();
        // 共享至请求域
        mv.addObject("custList", custList);

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
    public ResponseEntity page(LayuiPage layuiPage, String custId, String startDate, String endDate) {
        SystemCheckUtils.getInstance().checkMaxPage(layuiPage);
        IPage page = new Page<>(layuiPage.getPage(), layuiPage.getLimit());

        page = entityService
                .lambdaQuery()
                .eq(!StringUtils.isEmpty(custId), TbOrderInfo::getCustId, custId) //企业id
                //.between(!StringUtils.isEmpty(startDate)||!StringUtils.isEmpty(endDate), TbOrderInfo::getInputTime, startDate, endDate)
                .ge(!StringUtils.isEmpty(startDate), TbOrderInfo::getInputTime, startDate) // 起始日期
                .le(!StringUtils.isEmpty(endDate), TbOrderInfo::getInputTime, endDate) // 结束日期
                .page(page);

        // 获取所有订货单记录
        List<TbOrderInfo> orderList = page.getRecords();
        // 获取订货单记录列表, 查询并填充企业的名称
        orderList.forEach(item ->{
            String id = item.getCustId(); //拿到客户id
            //根据客户id查询客户对象
            TbCustomer tbCustomer = tbCustomerService.getById(id);
            if (tbCustomer != null){
                // 设置企业客户的名称
                item.setCustName(tbCustomer.getCustomerName());
            } else { // 若企业客户的记录被删除, 则将订货单记录中保存的企业客户的id作为企业名称
                item.setCustName(id);
            }

            // 格式化产品单价, 显示两位小数
            String price = item.getPrice();
            BigDecimal priceValue = new BigDecimal(price);
            item.setPrice(String.format("%.2f", priceValue));

        });


        return ResponseEntity.ok(LayuiTools.toLayuiTableModel(page));
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

    /**
     * 发货
     * @param id 订货单id
     * @return
     */
    @SysLog(value = LogModules.DELIVER, module = LogModule)
    @RequestMapping("deliver/{id}")
    @PreAuthorize("hasAuthority('custOrder:custOrderInfo:deliver')")
    public ResponseEntity<ApiModel> deliver(@PathVariable("id") String id) {
        // 根据id获取订单
        TbOrderInfo order = entityService.getById(id);
        // 设置发货时间
        order.setDeliverTime(LocalDateTime.now());
        // 将状态更改为已发货
        order.setStatus(1);
        // 更新
        entityService.updateById(order);
        return ResponseEntity.ok(ApiModel.ok());
    }

    @SysLog(value = LogModules.EXPORT, module = LogModule)
    @RequestMapping("export")
    @PreAuthorize("hasAuthority('custOrder:custOrderInfo:export')")
    public void export(HttpServletResponse response, String custId, String startDate, String endDate) {

        //要把什么数据导出到表格当中
        List<TbOrderInfo> list = entityService.lambdaQuery()
                .eq(!StringUtils.isEmpty(custId), TbOrderInfo::getCustId, custId) //企业id
                .ge(!StringUtils.isEmpty(startDate), TbOrderInfo::getInputTime, startDate) // 起始日期
                .le(!StringUtils.isEmpty(endDate), TbOrderInfo::getInputTime, endDate) // 结束日期
                .list();

        // 为所有订单记录设置企业名称
        list.forEach(item->{
            // 设置企业名称
            String custName = tbCustomerService.getById(item.getCustId()).getCustomerName();
            item.setCustName(custName);

            // 格式化合产品价格, 显示两位小数
            String price = item.getPrice();
            BigDecimal priceValue = new BigDecimal(price);
            item.setPrice(String.format("%.2f", priceValue));
        });

        //执行文件导出 准备工作
        ExportParams exportParams = new ExportParams();
        /**
         * 参数一： 样式
         * 参数二：导出的实体类的字节码  实际上来解析我们的导出的注释列的
         * 参数三：导出的内容
         */
        Workbook workbook = ExcelExportUtil.exportExcel(exportParams, TbOrderInfo.class, list);
        //导出
        try {
            PoiExportHelper.exportExcel(response, "客户订单管理", workbook);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    /**
     * 确认收货
     * @param id 订货单id
     * @return
     */            // logModules里面去自定义常量
    @SysLog(value = LogModules.RECEIVE, module = LogModule)
    @RequestMapping("receive/{id}")
    @PreAuthorize("hasAuthority('custOrder:custOrderInfo:receive')")
    public ResponseEntity<ApiModel> receive(@PathVariable("id") String id) {
        // 根据id获取订单
        TbOrderInfo order = entityService.getById(id);
        // 设置收货时间
        order.setReceiveTime(LocalDateTime.now());
        // 将状态更改为已收货
        order.setStatus(2);
        // 更新
        entityService.updateById(order);
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
