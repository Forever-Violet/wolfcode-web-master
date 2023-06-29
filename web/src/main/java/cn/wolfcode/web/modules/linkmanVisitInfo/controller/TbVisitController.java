package cn.wolfcode.web.modules.linkmanVisitInfo.controller;

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
import cn.wolfcode.web.modules.linkmanInfo.service.ITbCustLinkmanService;
import cn.wolfcode.web.modules.log.LogModules;
import cn.wolfcode.web.modules.sys.entity.SysUser;
import cn.wolfcode.web.modules.sys.form.LoginForm;
import cn.wolfcode.web.modules.sys.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;


import cn.wolfcode.web.modules.linkmanVisitInfo.entity.TbVisit;
import cn.wolfcode.web.modules.linkmanVisitInfo.service.ITbVisitService;


import link.ahsj.core.annotations.AddGroup;
import link.ahsj.core.annotations.SameUrlData;
import link.ahsj.core.annotations.SysLog;
import link.ahsj.core.annotations.UpdateGroup;
import link.ahsj.core.entitys.ApiModel;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author 吴彦祖
 * @since 2023-06-27
 */
@Controller
@RequestMapping("visitInfo")
public class TbVisitController extends BaseController {

    @Autowired
    private ITbVisitService entityService;

    @Autowired
    private UserService userService;

    @Autowired
    private ITbCustomerService tbCustomerService;

    @Autowired
    private ITbCustLinkmanService tbCustLinkmanService;

    private static final String LogModule = "TbVisit";

    @GetMapping("/list.html")
    public String list() {
        return "linkmanVisit/visitInfo/list";
    }

    @RequestMapping("/add.html")
    @PreAuthorize("hasAuthority('linkmanVisit:visitInfo:add')")
    public ModelAndView toAdd(ModelAndView mv) {
        List<TbCustomer> custs = tbCustomerService.list();
        // 企业用户列表
        mv.addObject("custs", custs);

/*        List<TbCustLinkman> linkmen = tbCustLinkmanService.list();
        // 企业联系人列表
        mv.addObject("linkmen", linkmen);*/
        mv.setViewName("linkmanVisit/visitInfo/add");
        return mv;
    }

    @RequestMapping("/getLinkmen")
    @ResponseBody
    public List<TbCustLinkman> getLinkmen(@RequestParam("custId") String custId) {
        //根据客户id获取对应的联系人列表
        List<TbCustLinkman> linkmen = tbCustLinkmanService.getByCustId(custId);
        return linkmen;
    }

    @GetMapping("/{id}.html")
    @PreAuthorize("hasAuthority('linkmanVisit:visitInfo:update')")
    public ModelAndView toUpdate(@PathVariable("id") String id, ModelAndView mv) {

        List<TbCustomer> custs = tbCustomerService.list();
        // 企业用户列表
        mv.addObject("custs", custs);

        // 根据拜访记录id获取拜访记录
        TbVisit obj = entityService.getById(id);
        // 根据拜访记录id获取联系人id, 然后根据联系人id获取联系人名称
        obj.setLinkmanName(tbCustLinkmanService.getById(obj.getLinkmanId()).getLinkman());

        mv.setViewName("linkmanVisit/visitInfo/update");
        mv.addObject("obj", obj);
        mv.addObject("id", id);
        return mv;
    }

    @RequestMapping("list")
    @PreAuthorize("hasAuthority('linkmanVisit:visitInfo:list')")
    public ResponseEntity page(LayuiPage layuiPage, String visitReason
            , Integer visitType, String startDate, String endDate) {
        SystemCheckUtils.getInstance().checkMaxPage(layuiPage);
        IPage page = new Page<>(layuiPage.getPage(), layuiPage.getLimit());


        // 获取具体数据
        page = entityService
                .lambdaQuery()
                .eq(!ObjectUtils.isEmpty(visitType), TbVisit::getVisitType, visitType) //拜访方式
                .like(!StringUtils.isEmpty(visitReason), TbVisit::getVisitReason, visitReason) //拜访原因 模糊查询
                .ge(!StringUtils.isEmpty(startDate), TbVisit::getVisitDate, startDate) // 起始日期
                .le(!StringUtils.isEmpty(endDate), TbVisit::getVisitDate, endDate) // 结束日期
                .page(page);

        // 获取所有拜访记录
        List<TbVisit> records = page.getRecords();
        // 获取拜访记录列表, 查询并填充联系人的名称
        for (TbVisit record : records) {

            // 根据联系人id获取联系人
            TbCustLinkman tbCustLinkman = tbCustLinkmanService.getById(record.getLinkmanId());
            if (tbCustLinkman != null) {
                String inputUserName = tbCustLinkman.getLinkman();
                // 设置联系人姓名
                record.setLinkmanName(inputUserName);
            } else { //此种情况是联系人的记录被删除, 将曾经保留的联系人id作为联系人姓名
                record.setLinkmanName(record.getLinkmanId());
            }

            // 根据企业客户的id获取企业客户
            TbCustomer tbCustomer = tbCustomerService.getById(record.getCustId());
            if (tbCustomer != null) {
                String custName = tbCustomer.getCustomerName();
                // 设置企业名称
                record.setCustName(custName);
            } else { //此种情况是企业用户的记录被删除, 将曾经保留的企业用户id作为企业名称
                record.setCustName(record.getCustId());
            }
        }

        return ResponseEntity.ok(LayuiTools.toLayuiTableModel(page));
    }

    @SameUrlData
    @PostMapping("save")
    @SysLog(value = LogModules.SAVE, module =LogModule)
    @PreAuthorize("hasAuthority('linkmanVisit:visitInfo:add')")
    public ResponseEntity<ApiModel> save(@Validated({AddGroup.class}) @RequestBody TbVisit entity, HttpServletRequest request) {
        // 从session域中获取当前登录的账户id, 获取账户名
        SysUser sysUser = (SysUser) request.getSession().getAttribute(LoginForm.LOGIN_USER_KEY);
        // 账户名不能重复, 因此将账户名作为录入人
        entity.setInputUser(userService.getById(sysUser.getUserId()).getUsername());

        // 永远不要信任前端传来的数据, 清空id, 防止用户私自定义id
        entity.setId(null);
        // 设置记录的录入时间
        entity.setInputTime(LocalDateTime.now());

        entityService.save(entity);
        return ResponseEntity.ok(ApiModel.ok());
    }

    @SameUrlData
    @SysLog(value = LogModules.UPDATE, module = LogModule)
    @PutMapping("update")
    @PreAuthorize("hasAuthority('linkmanVisit:visitInfo:update')")
    public ResponseEntity<ApiModel> update(@Validated({UpdateGroup.class}) @RequestBody TbVisit entity) {
        entityService.updateById(entity);
        return ResponseEntity.ok(ApiModel.ok());
    }

    @SysLog(value = LogModules.DELETE, module = LogModule)
    @DeleteMapping("delete/{id}")
    @PreAuthorize("hasAuthority('linkmanVisit:visitInfo:delete')")
    public ResponseEntity<ApiModel> delete(@PathVariable("id") String id) {
        entityService.removeById(id);
        return ResponseEntity.ok(ApiModel.ok());
    }

    @RequestMapping("export")
    @PreAuthorize("hasAuthority('linkmanVisit:visitInfo:export')")
    public void export(HttpServletResponse response, String startDate, String endDate,Integer visitType, String visitReason) throws Exception {

        System.out.println("后端");
        //1.导出的内容
        List<TbVisit> list = entityService
                .lambdaQuery()
                .eq(!ObjectUtils.isEmpty(visitType), TbVisit::getVisitType, visitType) //拜访方式
                .like(!StringUtils.isEmpty(visitReason), TbVisit::getVisitReason, visitReason) //拜访原因 模糊查询
                .ge(!StringUtils.isEmpty(startDate), TbVisit::getVisitDate, startDate) // 起始日期
                .le(!StringUtils.isEmpty(endDate), TbVisit::getVisitDate, endDate) // 结束日期
                .list();

        for (TbVisit record : list) {

            // 根据联系人id获取联系人
            TbCustLinkman tbCustLinkman = tbCustLinkmanService.getById(record.getLinkmanId());
            if (tbCustLinkman != null) {
                String inputUserName = tbCustLinkman.getLinkman();
                // 设置联系人姓名
                record.setLinkmanName(inputUserName);
            } else { //此种情况是联系人的记录被删除, 将曾经保留的联系人id作为联系人姓名
                record.setLinkmanName(record.getLinkmanId());
            }

            // 根据企业客户的id获取企业客户
            TbCustomer tbCustomer = tbCustomerService.getById(record.getCustId());
            if (tbCustomer != null) {
                String custName = tbCustomer.getCustomerName();
                // 设置企业名称
                record.setCustName(custName);
            } else { //此种情况是企业用户的记录被删除, 将曾经保留的企业用户id作为企业名称
                record.setCustName(record.getCustId());
            }
        }


        //2.导出前的准备，设置表格标题属性样式
        ExportParams exportParams = new ExportParams();

        /**
         * 参数1：表格标题属性
         * 参数2：导出的类的字节码， 配合注解 @Excel(name = "xxx")
         * 参数3：需要导出的数据
         *
         * 返回一个工作簿
         */
        Workbook workbook = ExcelExportUtil.exportExcel(exportParams, TbVisit.class, list);

        //3.导出 --> IO流 输出流 字节
        PoiExportHelper.exportExcel(response, "联系人走访管理", workbook);
    }

}
