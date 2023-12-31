package cn.wolfcode.web.modules.custContractInfo.controller;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.wolfcode.web.commons.entity.LayuiPage;
import cn.wolfcode.web.commons.utils.LayuiTools;
import cn.wolfcode.web.commons.utils.PoiExportHelper;
import cn.wolfcode.web.commons.utils.SystemCheckUtils;
import cn.wolfcode.web.modules.BaseController;
import cn.wolfcode.web.modules.custOrderInfo.entity.TbOrderInfo;
import cn.wolfcode.web.modules.custinfo.entity.TbCustomer;
import cn.wolfcode.web.modules.custinfo.service.ITbCustomerService;
import cn.wolfcode.web.modules.linkmanInfo.entity.TbCustLinkman;
import cn.wolfcode.web.modules.log.LogModules;
import cn.wolfcode.web.modules.sys.entity.SysUser;
import cn.wolfcode.web.modules.sys.form.LoginForm;
import cn.wolfcode.web.modules.sys.service.UserService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;


import cn.wolfcode.web.modules.custContractInfo.entity.TbContract;
import cn.wolfcode.web.modules.custContractInfo.service.ITbContractService;


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
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author 吴彦祖
 * @since 2023-06-28
 */
@Controller
@RequestMapping("custContractInfo")
public class TbContractController extends BaseController {

    @Autowired
    private ITbContractService entityService;

    @Autowired
    private UserService userService;

    @Autowired
    private ITbCustomerService tbCustomerService;

    private static final String LogModule = "TbContract";

    @GetMapping("/list.html")
    public String list() {
        return "custContract/custContractInfo/list";
    }

    @RequestMapping("/add.html")
    @PreAuthorize("hasAuthority('custContract:custContractInfo:add')")
    public ModelAndView toAdd(ModelAndView mv) {

        // 获取企业用户列表
        List<TbCustomer> custList = tbCustomerService.list();
        // 传递列表数据
        mv.addObject("custList", custList);

        mv.setViewName("custContract/custContractInfo/add");
        return mv;
    }

    @GetMapping("/{id}.html")
    @PreAuthorize("hasAuthority('custContract:custContractInfo:update')")
    public ModelAndView toUpdate(@PathVariable("id") String id, ModelAndView mv) {

        // 获取企业用户列表
        List<TbCustomer> custList = tbCustomerService.list();
        // 传递列表数据
        mv.addObject("custList", custList);

        mv.setViewName("custContract/custContractInfo/update");
        mv.addObject("obj", entityService.getById(id));
        mv.addObject("id", id);
        return mv;
    }

    @RequestMapping("list")
    @PreAuthorize("hasAuthority('custContract:custContractInfo:list')")
    public ResponseEntity page(LayuiPage layuiPage, String contractInfo, Integer auditStatus
            , Integer affixSealStatus, Integer nullifyStatus) {
        SystemCheckUtils.getInstance().checkMaxPage(layuiPage);

        IPage page = new Page<>(layuiPage.getPage(), layuiPage.getLimit());
        page = entityService
                .lambdaQuery()
                .eq(ObjectUtils.isNotEmpty(auditStatus), TbContract::getAuditStatus, auditStatus) // 审核状态
                .eq(ObjectUtils.isNotEmpty(affixSealStatus), TbContract::getAffixSealStatus, affixSealStatus) // 是否盖章
                .eq(ObjectUtils.isNotEmpty(nullifyStatus), TbContract::getNullifyStatus, nullifyStatus) // 是否作废
                .like(StringUtils.isNotEmpty(contractInfo), TbContract::getContractName, contractInfo) // 合同名称模糊查询
                .or()
                .like(StringUtils.isNotEmpty(contractInfo), TbContract::getContractCode, contractInfo) // 合同编码模糊查询
                .page(page);

        // 获取所有合同记录 , 设置合同记录的企业名称
        List<TbContract> contractList = page.getRecords();
        contractList.forEach(item ->{
            String id = item.getCustId(); //拿到客户id
            //根据客户id查询客户对象
            TbCustomer tbCustomer = tbCustomerService.getById(id);
            if (tbCustomer != null){
                // 设置企业客户的名称
                item.setCustName(tbCustomer.getCustomerName());
            } else { // 若企业客户的记录被删除, 则将合同记录中保存的企业客户的id作为企业名称
                item.setCustName(id);
            }

            // 格式化合同金额, 显示两位小数
            String amounts = item.getAmounts();
            BigDecimal amountsValue = new BigDecimal(amounts);
            item.setAmounts(String.format("%.2f", amountsValue));
        });

        return ResponseEntity.ok(LayuiTools.toLayuiTableModel(page));
    }

    @SameUrlData
    @PostMapping("save")
    @SysLog(value = LogModules.SAVE, module =LogModule)
    @PreAuthorize("hasAuthority('custContract:custContractInfo:add')")
    public ResponseEntity<ApiModel> save(@Validated({AddGroup.class}) @RequestBody TbContract entity, HttpServletRequest request) {
        // 永远不要信任前端传来的数据, 清空id, 防止用户私自定义id
        entity.setId(null);
        // 设置该记录的插入时间
        entity.setInputTime(LocalDateTime.now());

        // 获取当前登录的用户
        SysUser sysUser = (SysUser) request.getSession().getAttribute(LoginForm.LOGIN_USER_KEY);
        // 设置录入人为当前登录用户的账户名, 账户名不能重复
        String inputUser = userService.getById(sysUser.getUserId()).getUsername();
        entity.setInputUser(inputUser);

        entityService.save(entity);
        return ResponseEntity.ok(ApiModel.ok());
    }

    @SameUrlData
    @SysLog(value = LogModules.UPDATE, module = LogModule)
    @PutMapping("update")
    @PreAuthorize("hasAuthority('custContract:custContractInfo:update')")
    public ResponseEntity<ApiModel> update(@Validated({UpdateGroup.class}) @RequestBody TbContract entity) {

        // 设置该记录的修改时间
        entity.setUpdateTime(LocalDateTime.now());
        // 重置审核状态
        entity.setAuditStatus(0);

        entityService.updateById(entity);
        return ResponseEntity.ok(ApiModel.ok());
    }

    /**
     * 导出
     */
    @SysLog(value = LogModules.EXPORT, module = LogModule)
    @RequestMapping("export")
    @PreAuthorize("hasAuthority('custContract:custContractInfo:export')")
    public void export(HttpServletResponse response, String contractInfo, Integer auditStatus
            , Integer affixSealStatus, Integer nullifyStatus) {

        //要把什么数据导出到表格当中
        List<TbContract> list = entityService
                .lambdaQuery()
                .eq(ObjectUtils.isNotEmpty(auditStatus), TbContract::getAuditStatus, auditStatus) // 审核状态
                .eq(ObjectUtils.isNotEmpty(affixSealStatus), TbContract::getAffixSealStatus, affixSealStatus) // 是否盖章
                .eq(ObjectUtils.isNotEmpty(nullifyStatus), TbContract::getNullifyStatus, nullifyStatus) // 是否作废
                .like(StringUtils.isNotEmpty(contractInfo), TbContract::getContractName, contractInfo) // 合同名称模糊查询
                .or()
                .like(StringUtils.isNotEmpty(contractInfo), TbContract::getContractCode, contractInfo) // 合同编码模糊查询
                .list();

        // 为所有联系人记录设置企业名称
        list.forEach(item->{
            String id = item.getCustId(); //拿到客户id
            //根据客户id查询客户对象
            TbCustomer tbCustomer = tbCustomerService.getById(id);
            if (tbCustomer != null){
                // 设置企业客户的名称
                item.setCustName(tbCustomer.getCustomerName());
            } else { // 若企业客户的记录被删除, 则将合同记录中保存的企业客户的id作为企业名称
                item.setCustName(id);
            }

            // 格式化合同金额, 显示两位小数
            String amounts = item.getAmounts();
            BigDecimal amountsValue = new BigDecimal(amounts);
            item.setAmounts(String.format("%.2f", amountsValue));
        });

        //执行文件导出 准备工作
        ExportParams exportParams = new ExportParams();
        /**
         * 参数一： 样式
         * 参数二：导出的实体类的字节码  实际上来解析我们的导出的注释列的
         * 参数三：导出的内容
         */
        Workbook workbook = ExcelExportUtil.exportExcel(exportParams, TbContract.class, list);
        //导出
        try {
            PoiExportHelper.exportExcel(response, "客户合同管理", workbook);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }


    /**
     * 审核
     * @param id 合同id
     * @return
     */            // logModules里面去自定义常量
    @SysLog(value = LogModules.RECEIVE, module = LogModule)
    @RequestMapping("audit/{id}")
    @PreAuthorize("hasAuthority('custContract:custContractInfo:audit')")
    public ResponseEntity<ApiModel> audit(@PathVariable("id") String id, @RequestParam("userChoice") Boolean userChoice) {
        // 根据id获取订单
        TbContract contract = entityService.getById(id);
        if (userChoice){
            // 审核通过
            contract.setAuditStatus(1);
        } else {
            // 审核不通过
            contract.setAuditStatus(-1);
        }
        // 更新
        entityService.updateById(contract);
        return ResponseEntity.ok(ApiModel.ok());
    }

    /**
     * 盖章确认
     * @param id 合同id
     * @return
     */            // logModules里面去自定义常量
    @SysLog(value = LogModules.RECEIVE, module = LogModule)
    @RequestMapping("affixSeal/{id}")
    @PreAuthorize("hasAuthority('custContract:custContractInfo:affixSeal')")
    public ResponseEntity<ApiModel> affixSeal(@PathVariable("id") String id) {
        // 根据id获取订单
        TbContract contract = entityService.getById(id);
        // 确认盖章
        contract.setAffixSealStatus(1);
        // 更新
        entityService.updateById(contract);
        return ResponseEntity.ok(ApiModel.ok());
    }

    /**
     * 是否作废
     * @param id 合同id
     * @return
     */            // logModules里面去自定义常量
    @SysLog(value = LogModules.RECEIVE, module = LogModule)
    @RequestMapping("nullify/{id}")
    @PreAuthorize("hasAuthority('custContract:custContractInfo:nullify')")
    public ResponseEntity<ApiModel> nullify(@PathVariable("id") String id) {
        // 根据id获取订单
        TbContract contract = entityService.getById(id);
        contract.setNullifyStatus(1);
        // 更新
        entityService.updateById(contract);
        return ResponseEntity.ok(ApiModel.ok());
    }

    @SysLog(value = LogModules.DELETE, module = LogModule)
    @DeleteMapping("delete/{id}")
    @PreAuthorize("hasAuthority('custContract:custContractInfo:delete')")
    public ResponseEntity<ApiModel> delete(@PathVariable("id") String id) {
        entityService.removeById(id);
        return ResponseEntity.ok(ApiModel.ok());
    }

}
