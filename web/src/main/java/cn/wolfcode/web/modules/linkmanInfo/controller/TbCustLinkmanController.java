package cn.wolfcode.web.modules.linkmanInfo.controller;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.wolfcode.web.commons.entity.LayuiPage;
import cn.wolfcode.web.commons.utils.LayuiTools;
import cn.wolfcode.web.commons.utils.PoiExportHelper;
import cn.wolfcode.web.commons.utils.SystemCheckUtils;
import cn.wolfcode.web.modules.BaseController;
import cn.wolfcode.web.modules.custinfo.entity.TbCustomer;
import cn.wolfcode.web.modules.custinfo.service.ITbCustomerService;
import cn.wolfcode.web.modules.log.LogModules;
import cn.wolfcode.web.modules.sys.entity.SysUser;
import cn.wolfcode.web.modules.sys.form.LoginForm;
import cn.wolfcode.web.modules.sys.service.SysUserService;
import cn.wolfcode.web.modules.sys.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;


import cn.wolfcode.web.modules.linkmanInfo.entity.TbCustLinkman;
import cn.wolfcode.web.modules.linkmanInfo.service.ITbCustLinkmanService;

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
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author 吴彦祖
 * @since 2023-06-25
 */
@Controller
@RequestMapping("linkmanInfo")
public class TbCustLinkmanController extends BaseController {

    @Autowired
    private ITbCustLinkmanService entityService;

    @Autowired
    private UserService userService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private ITbCustomerService tbCustomerService;

    private static final String LogModule = "TbCustLinkman";

    @GetMapping("/list.html")
    public ModelAndView list(ModelAndView mv) {
        // 获取企业用户列表
        List<TbCustomer> custList = tbCustomerService.list();
        // 共享至请求域
        mv.addObject("custList", custList);
        mv.setViewName("custLinkman/linkmanInfo/list");

        return mv;
    }

    @RequestMapping("/add.html")
    @PreAuthorize("hasAuthority('custLinkman:linkmanInfo:add')")
    public ModelAndView toAdd(ModelAndView mv) {
        //List<String> custIds = tbCustomerService.getUserIds(); //获取企业用户id列表
        //mv.addObject("custIds", custIds);
        //System.out.println(custIds);
        // 获取企业用户列表
        List<TbCustomer> custs = tbCustomerService.list();
        // 共享企业用户列表至请求域
        mv.addObject("custs", custs);
        mv.setViewName("custLinkman/linkmanInfo/add");
        return mv;
    }

    @GetMapping("/{id}.html")
    @PreAuthorize("hasAuthority('custLinkman:linkmanInfo:update')")
    public ModelAndView toUpdate(@PathVariable("id") String id, ModelAndView mv) {
        // 获取企业用户列表
        List<TbCustomer> custs = tbCustomerService.list();
        // 共享企业用户列表至请求域
        mv.addObject("custs", custs);

        mv.setViewName("custLinkman/linkmanInfo/update");
        mv.addObject("obj", entityService.getById(id));
        mv.addObject("id", id);
        return mv;
    }

    /**
     * 导出
     *
     * @param parameterName 联系人名称 或 电话
     * @param custId 所属企业id
     */
    @SysLog(value = LogModules.EXPORT, module = LogModule)
    @RequestMapping("export")
    @PreAuthorize("hasAuthority('user:export')")
    public void export(HttpServletResponse response, String parameterName, String custId) {

        //要把什么数据导出到表格当中
        List<TbCustLinkman> list = entityService.lambdaQuery().
                eq(!StringUtils.isEmpty(custId), TbCustLinkman::getCustId, custId) //所属企业
                .like(!StringUtils.isEmpty(parameterName), TbCustLinkman::getLinkman, parameterName) //联系人
                .or()
                .like(!StringUtils.isEmpty(parameterName), TbCustLinkman::getPhone, parameterName)//电话
                .list();

        // 为所有联系人记录设置企业名称
        list.forEach(item->{
            // 设置企业名称
            String custName = tbCustomerService.getById(item.getCustId()).getCustomerName();
            item.setCustName(custName);
        });

        //执行文件导出 准备工作
        ExportParams exportParams = new ExportParams();
        /**
         * 参数一： 样式
         * 参数二：导出的实体类的字节码  实际上来解析我们的导出的注释列的
         * 参数三：导出的内容
         */
        Workbook workbook = ExcelExportUtil.exportExcel(exportParams, TbCustLinkman.class, list);
        //导出
        try {
            PoiExportHelper.exportExcel(response, "联系人管理", workbook);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }


    @RequestMapping("list")
    @PreAuthorize("hasAuthority('custLinkman:linkmanInfo:list')") //parameterName: 联系人名字 或 电话
    public ResponseEntity page(LayuiPage layuiPage, String parameterName, String custId) {
        SystemCheckUtils.getInstance().checkMaxPage(layuiPage);
        IPage page = new Page<>(layuiPage.getPage(), layuiPage.getLimit());
        //具体分页数据
        page = entityService
                .lambdaQuery()
                .eq(!StringUtils.isEmpty(custId), TbCustLinkman::getCustId, custId) //企业id
                .like(!StringUtils.isEmpty(parameterName), TbCustLinkman::getLinkman, parameterName) //联系人名称
                .or()
                .like(!StringUtils.isEmpty(parameterName), TbCustLinkman::getPhone, parameterName) //联系人电话
                .page(page);

        List<TbCustLinkman> records = page.getRecords();
        records.forEach(item ->{
            String id = item.getCustId(); //拿到客户id
            //根据客户id查询客户对象
            TbCustomer tbCustomer = tbCustomerService.getById(id);
            if (tbCustomer != null){
                // 设置企业客户的名称
                item.setCustName(tbCustomer.getCustomerName());
            } else { // 若企业客户的记录被删除, 则将联系人记录中保存的企业客户的id作为企业名称
                item.setCustName(id);
            }
        });

        return ResponseEntity.ok(LayuiTools.toLayuiTableModel(page));
    }

    @SameUrlData
    @PostMapping("save")
    @SysLog(value = LogModules.SAVE, module =LogModule)
    @PreAuthorize("hasAuthority('custLinkman:linkmanInfo:add')")
    public ResponseEntity<ApiModel> save(@Validated({AddGroup.class}) @RequestBody TbCustLinkman entity
            , HttpServletRequest request) {
        // 永远不要信任前端传来的数据, 清空id, 防止用户私自定义id
        entity.setId(null);
        // 设置该记录的插入时间
        entity.setInputTime(LocalDateTime.now());

        //从session域中获取当前登录用户的id, 根据id获取其账户名, 设置录入人的账户名.(账户名是不能重复的, 且更具有辨识度)
        SysUser sysUser = (SysUser) request.getSession().getAttribute(LoginForm.LOGIN_USER_KEY);
        String inputUser = userService.getById(sysUser.getUserId()).getUsername();
        entity.setInputUser(inputUser);

        entityService.save(entity);
        return ResponseEntity.ok(ApiModel.ok());
    }

    @SameUrlData
    @SysLog(value = LogModules.UPDATE, module = LogModule)
    @PutMapping("update")
    @PreAuthorize("hasAuthority('custLinkman:linkmanInfo:update')")
    public ResponseEntity<ApiModel> update(@Validated({UpdateGroup.class}) @RequestBody TbCustLinkman entity) {
        // 重置记录插入时间, 防止被修改
        // entity.setInputTime(entityService.getById(entity.getId()).getInputTime());
        // 根据id修改记录, 不改变id
        entityService.updateById(entity);
        return ResponseEntity.ok(ApiModel.ok());
    }

    @SysLog(value = LogModules.DELETE, module = LogModule)
    @DeleteMapping("delete/{id}")
    @PreAuthorize("hasAuthority('custLinkman:linkmanInfo:delete')")
    public ResponseEntity<ApiModel> delete(@PathVariable("id") String id) {
        entityService.removeById(id);
        return ResponseEntity.ok(ApiModel.ok());
    }

}
