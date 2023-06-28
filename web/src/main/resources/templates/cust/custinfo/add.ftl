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
    <link rel="stylesheet" href="${request.contextPath}/layuiadmin/style/popup.css" media="all">
</head>
<body>

<div class="layui-card layui-content">
    <div class="layui-card-body">
        <form class="layui-form" action="" lay-filter="component-form-element">
            <div class="layui-row layui-form-item">

<#--                <div class="layui-col-lg6">
                        id
                </div>
                <div class="layui-input-line">
                    <input type="text"  name="id"  placeholder="自动生成"  autocomplete="off" class="layui-input" disabled>
                </div>-->

                <div class="layui-col-lg6">
                        企业名称
                </div>
                <div class="layui-input-line">
                    <input type="text"  name="customerName"  placeholder="请输入"  autocomplete="off" class="layui-input">
                </div>

                <div class="layui-col-lg6">
                        法定代表人
                </div>
                <div class="layui-input-line">
                    <input type="text"  name="legalLeader"  placeholder="请输入"  autocomplete="off" class="layui-input">
                </div>

                <div class="layui-col-lg6">
                        成立时间
                </div>
                <div class="layui-input-line">
                    <input type="text" name="registerDate" class="layui-input" id="ID-laydate-demo" placeholder="yyyy-MM-dd">
                </div>

                <div class="layui-col-lg6">
                        经营状态
                </div>
                <div class="layui-input-line">
                    <#--<input type="text"  name="openStatus"  placeholder="请输入"  autocomplete="off" class="layui-input">-->
                    <select name="openStatus">
                        <option value="0">开业</option>
                        <option value="1">注销</option>
                        <option value="2">破产</option>
                    </select>
                </div>

                <div class="layui-col-lg6">
                        所属地区省份
                </div>
                <div class="layui-input-line">
<#--
                    <input type="text"  name="province"  placeholder="请输入"  autocomplete="off" class="layui-input">
-->
                    <select name="province">
                        <#list cities as city>
                            <option value="${city.key}">${city.value}</option>
                        </#list>
                    </select>

                </div>

                <div class="layui-col-lg6">
                        注册资本(万元)
                </div>
                <div class="layui-input-line">
                    <input type="text"  name="regCapital"  placeholder="请输入"  autocomplete="off" class="layui-input">
                </div>

                <div class="layui-col-lg6">
                        所属行业
                </div>
                <div class="layui-input-line">
                    <input type="text"  name="industry"  placeholder="请输入"  autocomplete="off" class="layui-input">
                </div>

                <div class="layui-col-lg6">
                        经营范围
                </div>
                <div class="layui-input-line">
                    <input type="text"  name="scope"  placeholder="请输入"  autocomplete="off" class="layui-input">
                </div>

                <div class="layui-col-lg6">
                        注册地址
                </div>
                <div class="layui-input-line">
                    <input type="text"  name="regAddr"  placeholder="请输入"  autocomplete="off" class="layui-input">
                </div>

<#--
                <div class="layui-col-lg6">
                        录入时间
                </div>
                <div class="layui-input-line">
                    <input type="text"  name="inputTime"  placeholder="自动生成"  autocomplete="off" class="layui-input" disabled>
                </div>

                <div class="layui-col-lg6">
                        修改时间
                </div>
                <div class="layui-input-line">
                    <input type="text"  name="updateTime"  placeholder="自动生成"  autocomplete="off" class="layui-input" disabled>
                </div>

                <div class="layui-col-lg6">
                        录入人
                </div>
                <div class="layui-input-line">
                    <input type="text"  name="inputUserId"  placeholder="请输入"  autocomplete="off" class="layui-input">
                </div>
-->

            </div>
            <div class="layui-form-item">
                <div class="layui-input-block">
                    <button class="layui-btn layui-btn-normal" lay-submit lay-filter="Add-filter">新增</button>
                    <button type="reset" class="layui-btn layui-btn-primary">重置</button>
                </div>
            </div>
        </form>
    </div>
</div>

<script src="${request.contextPath}/layuiadmin/layui/layui.js"></script>
<script src="${request.contextPath}/layui-extend.js"></script>
<script type="text/javascript" src="${request.contextPath}/scripts/cust/custinfo/add.js?_=${randomNum}"></script>
</body>
