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
                        <label>企业名称</label>
                </div>
                <div class="layui-input-line">
<#--
                    <input type="text"  name="custId"  placeholder="请输入"  autocomplete="off" class="layui-input">
-->
                    <select name="custId">
                        <option value="">请选择</option>
                        <#list custList as cust>
                            <option value="${cust.id}">${cust.customerName}</option>
                        </#list>
                    </select>
                </div>

                <div class="layui-col-lg6">
                        <label>合同名称</label>
                </div>
                <div class="layui-input-line">
                    <input type="text"  name="contractName"  placeholder="请输入"  autocomplete="off" class="layui-input">
                </div>

                <div class="layui-col-lg6">
                        <label>合同编码</label>
                </div>
                <div class="layui-input-line">
                    <input type="text"  name="contractCode"  placeholder="请输入"  autocomplete="off" class="layui-input">
                </div>

                <div class="layui-col-lg6">
                        <label>合同金额(元)</label>
                </div>
                <div class="layui-input-line">
                    <input type="text"  name="amounts"  placeholder="请输入"  autocomplete="off" class="layui-input">
                </div>

                <div class="layui-col-lg6">
                        <label>合同开始时间</label>
                </div>
                <div class="layui-input-line">
                    <#--<input type="text"  name="startDate"  placeholder="请输入"  autocomplete="off" class="layui-input">-->
                    <input class="layui-input" id="startDate" name="startDate" placeholder="请选择合同生效开始时间" />
                </div>

                <div class="layui-col-lg6">
                        <label>合同终止时间</label>
                </div>
                <div class="layui-input-line">
                    <#--<input type="text"  name="endDate"  placeholder="请输入"  autocomplete="off" class="layui-input">-->
                    <input class="layui-input" id="endDate" name="endDate" placeholder="请选择合同生效结束时间" />
                </div>


                <div class="layui-col-lg6">
                        <label>合同内容</label>
                </div>
                <div class="layui-input-line">
                    <input type="text"  name="content"  placeholder="请输入"  autocomplete="off" class="layui-input">
                </div>

<#--                <div class="layui-col-lg6">
                        <label>是否盖章确认</label>
                </div>
                <div class="layui-input-line">

                    <select name="affixSealStatus" id="affixSealStatus" lay-filter="affixSealStatus">
                        <option value="0">否</option>
                        <option value="1">是</option>
                    </select>
                </div>

                <div class="layui-col-lg6">
                        <label>审核状态</label>
                </div>
                <div class="layui-input-line">

                    <select name="auditStatus">
                        <option value="0">未审核</option>
                        <option value="1">审核通过</option>
                        <option value="-1">审核不通过</option>
                    </select>
                </div>

                <div class="layui-col-lg6">
                        <label>是否作废</label>
                </div>
                <div class="layui-input-line">

                    <select name="nullifyStatus" id="nullifyStatus">
                        <option value="0">否</option>
                        <option value="1">是</option>
                    </select>
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
<script type="text/javascript" src="${request.contextPath}/scripts/custContract/custContractInfo/add.js?_=${randomNum}"></script>
</body>
