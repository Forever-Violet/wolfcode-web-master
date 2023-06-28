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
<script>
</script>
<div class="layui-card layui-content">
    <div class="layui-card-body">
        <form class="layui-form"  action="" lay-filter="component-form-element">
            <input type="hidden" id="id" name="id" value="${id}">
            <div class="layui-row layui-col-space10 layui-form-item">


<#--                <div class="layui-col-lg6">
                        <label class="layui-form-label">唯一id</label>
                    <div class="layui-input-block">
                        <input type="text"
                               name="id"
                               value="${obj.id}"
                               autocomplete="off"
                               class="layui-input">
                    </div>
                </div>-->


                <div class="layui-col-lg6">
                        <label class="layui-form-label">客户id</label>
                    <div class="layui-input-block">
                        <select id="custId" lay-filter="custId" name="custId">
                            <#list custs as cust>
                                <#--<option value="">请选择</option>-->
                                <option <#if obj.custId==cust.id>selected</#if> value="${cust.id}">${cust.customerName}[${cust.legalLeader}](${cust.id})</option>
                            </#list>
                        </select>
                    </div>
                </div>


                <div class="layui-col-lg6">
                        <label class="layui-form-label">联系人</label>
                    <div class="layui-input-block">
                        <select id="linkmanId" name="linkmanId">
                            <option value="${obj.linkmanId}">${obj.linkmanName}</option>
                        </select>
                    </div>
                </div>


                <div class="layui-col-lg6">
                        <label class="layui-form-label">拜访方式</label>
                    <div class="layui-input-block">
<#--                        <input type="text"
                               name="visitType"
                               value="${obj.visitType}"
                               autocomplete="off"
                               class="layui-input">-->
                        <select name="visitType">
                            <option value="">请选择</option>
                            <option <#if obj.visitType==1>selected</#if> value="1">上门走访</option>
                            <option <#if obj.visitType==2>selected</#if> value="2">电话拜访</option>
                        </select>
                    </div>
                </div>


                <div class="layui-col-lg6">
                        <label class="layui-form-label">拜访原因</label>
                    <div class="layui-input-block">
                        <input type="text"
                               name="visitReason"
                               value="${obj.visitReason}"
                               autocomplete="off"
                               class="layui-input">
                    </div>
                </div>


                <div class="layui-col-lg6">
                        <label class="layui-form-label">交流内容</label>
                    <div class="layui-input-block">
                        <input type="text"
                               name="content"
                               value="${obj.content}"
                               autocomplete="off"
                               class="layui-input">
                    </div>
                </div>


                <div class="layui-col-lg6">
                        <label class="layui-form-label">拜访时间</label>
                    <div class="layui-input-block">
                        <input type="text"
                               name="visitDate"
                               value="${obj.visitDate}"
                               id="ID-laydate-demo"
                               placeholder="yyyy-MM-dd"
                               class="layui-input">
                    </div>
                </div>


<#--                <div class="layui-col-lg6">
                        <label class="layui-form-label">录入人</label>
                    <div class="layui-input-block">
                        <input type="text"
                               name="inputUser"
                               value="${obj.inputUser}"
                               autocomplete="off"
                               class="layui-input">
                    </div>
                </div>


                <div class="layui-col-lg6">
                        <label class="layui-form-label">录入时间</label>
                    <div class="layui-input-block">
                        <input type="text"
                               name="inputTime"
                               value="${obj.inputTime}"
                               autocomplete="off"
                               class="layui-input">
                    </div>
                </div>-->


                <div class="layui-form-item">
                    <div class="layui-input-block">
                        <button class="layui-btn layui-btn-normal" lay-submit lay-filter="Add-filter">修改</button>
                        <button type="reset" class="layui-btn layui-btn-primary">重置</button>
                    </div>
                </div>
            </div>
        </form>
    </div>
</div>

<script src="${request.contextPath}/layuiadmin/layui/layui.js"></script>
<script src="${request.contextPath}/layui-extend.js"></script>
<script src="${request.contextPath}/webjars/jquery/jquery.min.js"></script>
<script>

</script>
<script type="text/javascript" src="${request.contextPath}/scripts/linkmanVisit/visitInfo/update.js?_=${randomNum}"></script>
</body>
