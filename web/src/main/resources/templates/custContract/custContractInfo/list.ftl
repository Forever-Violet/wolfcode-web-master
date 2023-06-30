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

<div class="layui-fluid">
    <div class="layui-row layui-col-space15">
        <div class="layui-col-md12">
            <div class="layui-card">
                <@sec.authenticate grants="custContract:custContractInfo:list">

                    <!-- 搜索条件start -->
                    <form class="layui-form layui-card-header layuiadmin-card-header-auto"
                         id="searchForm">
                        <div class="layui-form-item">
                            <div class="layui-inline">
                                <label class="layui-form-label">合同信息</label>
                                <div class="layui-input-block input-box">
                                    <input type="text" name="contractInfo" placeholder="合同名称/编码"
                                           autocomplete="off"
                                           class="layui-input">
                                </div>
                            </div>

                            <div class="layui-inline">
                                <label class="layui-form-label">审核状态</label>
                                <div class="layui-input-block input-box">
                                    <select name="auditStatus">
                                        <option value="">请选择</option>
                                        <option value="0">未审核</option>
                                        <option value="1">审核通过</option>
                                        <option value="-1">审核不通过</option>
                                    </select>
                                </div>
                            </div>

                            <div class="layui-inline">
                                <label class="layui-form-label">是否盖章</label>
                                <div class="layui-input-block input-box">
                                    <select name="affixSealStatus">
                                        <option value="">请选择</option>
                                        <option value="0">否</option>
                                        <option value="1">是</option>
                                    </select>
                                </div>
                            </div>

                            <div class="layui-inline">
                                <label class="layui-form-label">是否作废</label>
                                <div class="layui-input-block input-box">
                                    <select name="nullifyStatus">
                                        <option value="">请选择</option>
                                        <option value="0">否</option>
                                        <option value="1">是</option>
                                    </select>
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
                            <@sec.authenticate grants="custContract:custContractInfo:add">
                                <button class="layui-btn layui-btn-sm layui-btn-primary"
                                        lay-event="add"><i class="layui-icon">&#xe654;</i>新增
                                </button>
                            </@sec.authenticate>
                            <@sec.authenticate grants="custContract:custContractInfo:export">
                                <button class="layui-btn layui-btn-sm layui-btn-primary" lay-tips="导出" lay-event="export">
                                    <i class="layui-icon layui-icon-export"></i>导出
                                </button>
                            </@sec.authenticate>
                        </div>
                    </script>

                    <script type="text/html" id="List-editBar">
                        {{#  if(d.nullifyStatus == '1'){ <#--若作废-->
                        }}
                        <@sec.authenticate grants="custContract:custContractInfo:delete">
                            <a class="layui-btn layui-btn-xs layui-btn-primary" lay-event="delete"><i
                                        class="layui-icon">&#xe640;</i>删除</a>
                        </@sec.authenticate>
                        {{# }else if(d.auditStatus == '0'){ <#--未审核 & 未作废-->
                        }}
                        <@sec.authenticate grants="custContract:custContractInfo:audit">
                            <a class="layui-btn layui-btn-xs layui-btn-primary" lay-event="audit"><i
                                        class="layui-icon">&#xe6b2;</i>审核</a>
                        </@sec.authenticate>
                        <@sec.authenticate grants="custContract:custContractInfo:delete">
                            <a class="layui-btn layui-btn-xs layui-btn-primary" lay-event="delete"><i
                                        class="layui-icon">&#xe640;</i>删除</a>
                        </@sec.authenticate>
                        <@sec.authenticate grants="custContract:custContractInfo:nullify">
                            <a class="layui-btn layui-btn-xs layui-btn-primary" lay-event="nullify"><i
                                        class="layui-icon">&#x1007;</i>作废</a>
                        </@sec.authenticate>
                        {{#  } else if(d.auditStatus == '-1'){ }}  <#-- 审核不通过可以修改 未作废 -->
                        <@sec.authenticate grants="custContract:custContractInfo:update">
                            <a class="layui-btn layui-btn-xs layui-btn-primary" lay-event="update"><i
                                        class="layui-icon">&#xe642;</i>修改</a>
                        </@sec.authenticate>
                        <@sec.authenticate grants="custContract:custContractInfo:delete">
                            <a class="layui-btn layui-btn-xs layui-btn-primary" lay-event="delete"><i
                                        class="layui-icon">&#xe640;</i>删除</a>
                        </@sec.authenticate>
                        <@sec.authenticate grants="custContract:custContractInfo:nullify">
                            <a class="layui-btn layui-btn-xs layui-btn-primary" lay-event="nullify"><i
                                        class="layui-icon">&#x1007;</i>作废</a>
                        </@sec.authenticate>
                        {{#  } else if(d.affixSealStatus == '1'){ }} <#-- 盖章确认-->
                        <@sec.authenticate grants="custContract:custContractInfo:delete">
                            <a class="layui-btn layui-btn-xs layui-btn-primary" lay-event="delete"><i
                                        class="layui-icon">&#xe640;</i>删除</a>
                        </@sec.authenticate>
                        {{# } else if(d.auditStatus == '1'){ }} <#--未盖章确认-->
                        <@sec.authenticate grants="custContract:custContractInfo:affixSeal">
                            <a class="layui-btn layui-btn-xs layui-btn-primary" lay-event="affixSeal"><i
                                        class="layui-icon">&#xe672;</i>盖章</a>
                        </@sec.authenticate>
                        <@sec.authenticate grants="custContract:custContractInfo:delete">
                            <a class="layui-btn layui-btn-xs layui-btn-primary" lay-event="delete"><i
                                        class="layui-icon">&#xe640;</i>删除</a>
                        </@sec.authenticate>
                        <@sec.authenticate grants="custContract:custContractInfo:nullify">
                            <a class="layui-btn layui-btn-xs layui-btn-primary" lay-event="nullify"><i
                                        class="layui-icon">&#x1007;</i>作废</a>
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
<!--务必要引用  而且是在我们的 jquery.js后面 -->
<script src="${request.contextPath}/fileDownload/jquery.fileDownload.min.js"></script>
<script type="text/javascript" src="${request.contextPath}/scripts/custContract/custContractInfo/list.js?_=${randomNum}"></script>
</body>
</html>
