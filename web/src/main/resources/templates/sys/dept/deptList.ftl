<#assign sec=JspTaglibs["http://http://www.ahsj.link/security/tags"]/>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>通用后台管理模板系统</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, dept-scalable=0">
    <link rel="stylesheet" href="${request.contextPath}/layuiadmin/layui/css/layui.css" media="all">
    <link rel="stylesheet" href="${request.contextPath}/layuiadmin/layui/css/modules/formSelects/formSelects-v4.css" media="all">
    <link rel="stylesheet" href="${request.contextPath}/MultipleTreeSelect/css/metroStyle/metroStyle.css" media="all">
    <link rel="stylesheet" href="${request.contextPath}/layuiadmin/style/admin.css" media="all">
    <link rel="stylesheet" href="${request.contextPath}/layuiadmin/style/common.css" media="all">
</head>
<body>
<div class="left-page">
    <div class="show-hide">
        <i class="layui-icon hide">&#xe65a;</i>
        <i class="layui-icon show" style="display: none">&#xe65b;</i>
    </div>
    <div class="header-page">
        <span>组织结构</span>
        <i class="layui-icon retract" title="收起">&#xe619;</i>
        <i class="layui-icon refresh" title="刷新">&#xe9aa;</i>
    </div>
    <div class="content-page">
        <div id="organization-tree" class="ztree"></div>
    </div>
</div>
<div class="layui-fluid right-page">
    <div class="layui-row layui-col-space15">
        <div class="layui-col-md12">
            <div class="layui-card">
                <@sec.authenticate grants="dept:deptList">
                    <!-- 搜索条件start -->
                    <form class="layui-form layui-card-header layuiadmin-card-header-auto" id="searchForm">
                        <div class="layui-form-item">
                            <div class="layui-inline">
                                <label class="layui-form-label">架构名称</label>
                                <div class="layui-input-block input-box">
                                    <input type="text" name="deptName" placeholder="请输入" autocomplete="off"
                                           class="layui-input">
                                </div>
                            </div>
                            <div class="layui-inline">
                                <label class="layui-form-label">状态</label>
                                <div class="layui-input-block input-box">
                                    <select name="disable">
                                        <option value="">请选择</option>
                                        <option value="NORMAL">正常</option>
                                        <option value="DISABLE">已删除</option>
                                    </select>
                                </div>
                            </div>

                            <div class="layui-inline">
                                <button type="button" class="layui-btn layui-btn-normal" id="SearchBtn" data-type="reload">查询
                                </button>
                                <button type="reset" class="layui-btn layui-btn-primary">重置</button>
                            </div>
                        </div>
                    </form>
                    <!-- 搜索条件end -->
                </@sec.authenticate>

                <!-- 数据表格start -->
                <div class="layui-card-body">
                    <table class="layui-hide" id="deptList" lay-filter="deptList-toolbar"></table>

                    <script type="text/html" id="deptList-toolbar">
                        <div class="layui-btn-container">
                            <@sec.authenticate grants="dept:add">
                                <button class="layui-btn layui-btn-sm layui-btn-primary" lay-event="add"><i
                                            class="layui-icon">&#xe654;</i>新增
                                </button>
                            </@sec.authenticate>

                            <@sec.authenticate grants="dept:delete">
                                <button class="layui-btn layui-btn-sm layui-btn-primary" lay-event="delete"><i
                                            class="layui-icon">&#xe640;</i>删除
                                </button>
                            </@sec.authenticate>

                            <@sec.authenticate grants="dept:import">
                                <button class="layui-btn layui-btn-sm layui-btn-primary" lay-event="import"><i
                                            class="layui-icon">&#xe67c;</i>导入
                                </button>
                            </@sec.authenticate>

                            <@sec.authenticate grants="dept:export">
                                <button class="layui-btn layui-btn-sm layui-btn-primary" lay-tips="导出"
                                        lay-event="export">
                                    <i class="layui-icon layui-icon-export"></i>导出
                                </button>
                            </@sec.authenticate>
                        </div>
                    </script>

                    <script type="text/html" id="deptList-editBar">
                        {{#  if(d.disable == '0'){
                        }}
                        <@sec.authenticate grants="dept:update">
                            <a class="layui-btn layui-btn-xs layui-btn-primary" lay-event="update"><i class="layui-icon">&#xe642;</i>修改</a>
                        </@sec.authenticate>
                        <@sec.authenticate grants="dept:delete">
                            <a class="layui-btn layui-btn-xs layui-btn-primary" lay-event="delete"><i class="layui-icon">&#xe640;</i>删除</a>
                        </@sec.authenticate>
                        {{#  } else { }}
                        <a class="layui-btn layui-btn-xs layui-btn-primary" lay-event="recovery"><i class="layui-icon">&#xe642;</i>恢复</a>
                        {{#  } }}
                    </script>
                </div>
                <!-- 数据表格end -->
            </div>
        </div>
    </div>
</div>
<script src="${request.contextPath}/layuiadmin/layui/layui.js"></script>
<script src="${request.contextPath}/webjars/jquery/jquery.min.js"></script>
<script src="${request.contextPath}/fileDownload/jquery.fileDownload.min.js"></script>
<script type="text/javascript" src="${request.contextPath}/MultipleTreeSelect/js/jquery.ztree.all.js"></script>
<script type="text/javascript" src="${request.contextPath}/MultipleTreeSelect/MultipleTreeSelect.js"></script>
<script src="${request.contextPath}/layui-extend.js"></script>
<script type="application/javascript">
    layui.config({
        base: '${request.contextPath}/layuiadmin/'
    }).extend({
        formSelects: 'lib/formSelects-v4.min'
    });
</script>
<script type="text/javascript" src="${request.contextPath}/static/scripts/sys/dept/deptList.js?_=${randomNum}"></script>
<script type="text/javascript" src="${request.contextPath}/static/scripts/newForm.js?_=${randomNum}"></script>
</body>
</html>