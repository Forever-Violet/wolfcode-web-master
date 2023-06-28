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
            <div class="layui-row layui-col-space10 layui-form-item">


                <div class="layui-col-lg6">
                        所属企业
                </div>
                <div class="layui-input-line">
                    <select name="custId" >
                        <option value="">请选择</option>
                        <#list custs as cust>
                            <option value="${cust.id}">${cust.customerName}<#--[${cust.legalLeader}](${cust.id})--></option>
                        </#list>
                    </select>
                </div>

                <div class="layui-col-lg6">
                        联系人名字
                </div>
                <div class="layui-input-line">
                    <input type="text"  name="linkman"  placeholder="请输入"  autocomplete="off" class="layui-input">
                </div>

                <div class="layui-col-lg6">
                        性别
                </div>
                <div class="layui-input-line">
                    <select name="sex">
                        <option value="1">男</option>
                        <option value="0">女</option>
                    </select>
                </div>

                <div class="layui-col-lg6">
                        年龄
                </div>
                <div class="layui-input-line">

                    <div id="ageSlider"></div>
                    <input type="hidden" name="age" value="">
                </div>

                <div class="layui-col-lg6">
                        联系人电话
                </div>
                <div class="layui-input-line">
                    <input type="text"  name="phone"  placeholder="请输入"  autocomplete="off" class="layui-input">
                </div>

                <div class="layui-col-lg6">
                        职位
                </div>
                <div class="layui-input-line">
                    <input type="text"  name="position"  placeholder="请输入"  autocomplete="off" class="layui-input">
                </div>

                <div class="layui-col-lg6">
                        部门
                </div>
                <div class="layui-input-line">
                    <input type="text"  name="department"  placeholder="请输入"  autocomplete="off" class="layui-input">
                </div>

                <div class="layui-col-lg6">
                    任职状态
                </div>
                <div class="layui-input-line">
                    <select name="jobStatus">
                        <option value="1">在职</option>
                        <option value="0">离职</option>
                    </select>
                </div>

                <div class="layui-col-lg6">
                        备注信息
                </div>
                <div class="layui-input-line">
                    <input type="text"  name="remark"  placeholder="请输入"  autocomplete="off" class="layui-input">
                </div>

<#--                <div class="layui-col-lg6">
                        录入人
                </div>
                <div class="layui-input-line">
                    <input type="text"  name="inputUser"  placeholder="请输入"  autocomplete="off" class="layui-input">
                </div>

                <div class="layui-col-lg6">
                        录入时间
                </div>
                <div class="layui-input-line">
                    <input type="text"  name="inputTime"  placeholder="自动生成"  autocomplete="off" class="layui-input" disabled>
                </div>-->

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
<script type="text/javascript" src="${request.contextPath}/scripts/custLinkman/linkmanInfo/add.js?_=${randomNum}"></script>
</body>
