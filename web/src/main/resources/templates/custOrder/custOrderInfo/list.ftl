<#assign sec=JspTaglibs["http://http://www.ahsj.link/security/tags"]/>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>通用后台管理模板系统</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0">
    <link rel="stylesheet" href="${request.contextPath}/layuiadmin/layui/css/layui.css" media="all">
    <link rel="stylesheet" href="${request.contextPath}/layuiadmin/style/admin.css" media="all">
    <link rel="stylesheet" href="${request.contextPath}/layuiadmin/style/common.css" media="all">
</head>
<body>
<style>
    .layui-form-label {
        width: 100px;
    }

    .layui-input-block {
        margin-left: 100px;
    }
</style>

<div class="layui-fluid" >
    <div class="layui-row layui-col-space15">
        <div class="layui-col-md12">
            <div class="layui-card">
                <@sec.authenticate grants="custOrder:custOrderInfo:list">

                    <!-- 搜索条件start -->
                    <form class="layui-form layui-card-header layuiadmin-card-header-auto"
                         id="searchForm">
                        <div class="layui-form-item">
<#--                            <div class="layui-inline">
                                <label class="layui-form-label">参数</label>
                                <div class="layui-input-block input-box">
                                    <input type="text" name="parameterName" placeholder="请输入"
                                           autocomplete="off"
                                           class="layui-input">
                                </div>
                            </div>-->

                            <div class="layui-inline">
                                <label class="layui-form-label">所属企业</label>
                                <div class="layui-input-block input-box">
                                    <select name="custId">
                                        <option value="">请选择</option>
                                        <#list custList as cust>
                                            <option value="${cust.id}">${cust.customerName}</option>
                                        </#list>
                                    </select>
                                </div>
                            </div>

                            <div class="layui-inline">
                                <label class="layui-form-label">录入时间范围</label>
                                <div class="layui-input-block input-box">
                                    <input class="layui-input" id="startDate" name="startDate" placeholder="请选择起始时间" />
                                </div>
                            </div>
                            <span style="margin-right: 5px">到</span>
                            <div class="layui-inline">
                                <div class="">
                                    <input class="layui-input" id="endDate" name="endDate" placeholder="请选择结束时间" />
                                </div>
                            </div>

                            <div class="layui-inline">
                                <button type="button" class="layui-btn layui-btn-normal" id="SearchBtn"
                                        data-type="reload">搜索
                                </button>
                                <button type="reset" class="layui-btn layui-btn-primary">重置</button>
                            </div>
                        </div>
                    </form>
                    <!-- 搜索条件end -->
                </@sec.authenticate>


                <!-- 数据表格start -->
                <div class="layui-card-body">
                    <table class="layui-hide" id="List" lay-filter="List-toolbar"></table>

                    <script type="text/html" id="List-toolbar">
                        <div class="layui-btn-container">
                            <@sec.authenticate grants="custOrder:custOrderInfo:add">
                                <button class="layui-btn layui-btn-sm layui-btn-primary"
                                        lay-event="add"><i class="layui-icon">&#xe654;</i>新增
                                </button>
                            </@sec.authenticate>
                        </div>
                    </script>

                    <@sec.authenticate grants="custOrder:custOrderInfo:export">
                        <button class="layui-btn layui-btn-sm layui-btn-primary" lay-tips="导出" lay-event="export">
                            <i class="layui-icon layui-icon-export"></i>导出
                        </button>
                    </@sec.authenticate>

                    <script type="text/html" id="List-editBar">
                        {{#  if(d.status == '0'){ }} <#--未发货可以修改-->
                        <@sec.authenticate grants="custOrder:custOrderInfo:deliver"> <#--记得在菜单表中加入对应按钮, 把权限录入-->
                            <a class="layui-btn layui-btn-xs layui-btn-primary" lay-event="deliver"><i
                                        class="layui-icon">&#xe609;</i>发货</a>
                        </@sec.authenticate>
                        <@sec.authenticate grants="custOrder:custOrderInfo:update">
                            <a class="layui-btn layui-btn-xs layui-btn-primary" lay-event="update"><i
                                        class="layui-icon">&#xe642;</i>修改</a>
                        </@sec.authenticate>
                        <@sec.authenticate grants="custOrder:custOrderInfo:delete">
                            <a class="layui-btn layui-btn-xs layui-btn-primary" lay-event="delete"><i
                                        class="layui-icon">&#xe640;</i>删除</a>
                        </@sec.authenticate>
                        {{# }else if(d.status == '1'){ }}  <#--已发货可以确认收货-->
                        <@sec.authenticate grants="custOrder:custOrderInfo:receive">
                            <a class="layui-btn layui-btn-xs layui-btn-primary" lay-event="receive"><i
                                        class="layui-icon">&#xe605;</i>确认收货</a>
                        </@sec.authenticate>
                        <@sec.authenticate grants="custOrder:custOrderInfo:delete">
                            <a class="layui-btn layui-btn-xs layui-btn-primary" lay-event="delete"><i
                                        class="layui-icon">&#xe640;</i>删除</a>
                        </@sec.authenticate>
                        {{# }else{ }}
                        <@sec.authenticate grants="custOrder:custOrderInfo:delete">
                            <a class="layui-btn layui-btn-xs layui-btn-primary" lay-event="delete"><i
                                        class="layui-icon">&#xe640;</i>删除</a>
                        </@sec.authenticate>
                        {{# } }}
                    </script>
                </div>
                <!-- 数据表格end -->
            </div>
        </div>
    </div>
</div>

<script src="${request.contextPath}/layuiadmin/layui/layui.js"></script>
<script src="${request.contextPath}/layui-extend.js"></script>
<script src="${request.contextPath}/webjars/jquery/jquery.min.js"></script>
<script type="text/javascript" src="${request.contextPath}/scripts/custOrder/custOrderInfo/list.js?_=${randomNum}"></script>
</body>
</html>
