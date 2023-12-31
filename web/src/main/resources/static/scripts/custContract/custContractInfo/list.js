layui.use(['form', 'layer', 'table', 'laytpl', 'laydate'], function () {
    var form = layui.form,
        layer = layui.layer,
        $ = layui.jquery,
        laytpl = layui.laytpl,
        laydate = layui.laydate,
        table = layui.table;


    //用户列表
    var tableIns = table.render({
        elem: '#List',
        url: web.rootPath() + 'custContractInfo/list',
        cellMinWidth: 95,
        page: true,
        height: "full-" + Math.round(Number($('.layui-card-header').height()) + 44),
        limits: [10, 13, 15, 20, 30, 50, 100, 200],
        limit: 10,
        toolbar: '#List-toolbar',
        id: "ListTable",
        cols: [[
            {type: "checkbox", fixed: "left", width: 50},
                    /*{field: 'id', title:  'id', minWidth: 100, align: "center"},*/
                    {field: 'custName', title: '企业名称', minWidth: 100, align: "center"},
                    {field: 'contractName', title: '合同名称', minWidth: 100, align: "center"},
                    {field: 'contractCode', title: '合同编码', minWidth: 100, align: "center"},
                    {field: 'amounts', title: '合同金额(元)', minWidth: 110, align: "center"},
                    {field: 'startDate', title: '合同开始时间', minWidth: 125, align: "center"},
                    {field: 'endDate', title: '合同终止时间', minWidth: 125, align: "center"},
                    {field: 'content', title: '合同内容', minWidth: 100, align: "center"},
                    {
                        field: 'auditStatus', title: '审核状态', minWidth: 110, align: "center"
                        , templet: function (data) {
                            if (data.auditStatus == '0') {
                                return "<button class=\"layui-btn layui-btn-normal layui-btn-xs\" style='background-color: #387ef3; width: 80px'>未审核</button>";
                            } else if (data.auditStatus == '1') {
                                return "<button class=\"layui-btn layui-btn-normal layui-btn-xs\" style='background-color: #91d756; width: 80px'>审核通过</button>";
                            } else {
                                return "<button class=\"layui-btn layui-btn-normal layui-btn-xs\" style='background-color: #ca5e50; width: 80px'>审核不通过</button>";
                            }
                        }
                    },
                    {
                        field: 'affixSealStatus', title: '是否盖章确认', minWidth: 100, align: "center"
                        , templet: function (data) {
                            if (data.affixSealStatus == '0') {
                                return "<button class=\"layui-btn layui-btn-normal layui-btn-xs\" style='background-color: #387ef3'>否</button>";
                            } else {
                                return "<button class=\"layui-btn layui-btn-normal layui-btn-xs\" style='background-color: #91d756'>是</button>";
                            }
                        }
                    },
                    {
                        field: 'nullifyStatus', title: '是否作废', minWidth: 100, align: "center"
                        , templet: function (data) {
                            if (data.nullifyStatus == '0') {
                                return "<button class=\"layui-btn layui-btn-normal layui-btn-xs\" style='background-color: #91d756'>否</button>";
                            } else {
                                return "<button class=\"layui-btn layui-btn-normal layui-btn-xs\" style='background-color: #ca5e50'>是</button>";
                            }
                        }
                    },
                    {field: 'inputUser', title: '录入人', minWidth: 100, align: "center"},
                    {field: 'inputTime', title: '录入时间', minWidth: 100, align: "center"},
                    {field: 'updateTime', title: '修改时间', minWidth: 100, align: "center"},

            {title: '操作', width: 220, templet: '#List-editBar', fixed: "right", align: "center"}
        ]],

    });

    //头工具栏事件
    table.on('toolbar(List-toolbar)', function (obj) {
        switch (obj.event) {
            case 'add':
                layer.msg("add");
                break;
            case 'update':
                layer.msg("update");
                break;
            case 'delete':
                layer.msg("delete");
                break;
            case 'export':
                layer.msg("export");
                break;
            case 'audit':
                layer.msg("audit");
                break;
            case 'affixSeal':
                layer.msg("affixSeal");
                break;
            case 'nullify':
                layer.msg("nullify");
                break;
        }

    });

    var $ = layui.$, active = {
        reload: function () {
            //获取搜索条件值
            var contractInfo = $("#searchForm").find("input[name='contractInfo']").val().trim();
            var auditStatus = $("#searchForm").find("select[name='auditStatus']").val().trim();
            var affixSealStatus = $("#searchForm").find("select[name='affixSealStatus']").val().trim();
            var nullifyStatus = $("#searchForm").find("select[name='nullifyStatus']").val().trim();
            //表格重载
            tableIns.reload({
                where: { //设定异步数据接口的额外参数，任意设
                    contractInfo: contractInfo, // 合同名称 / 合同编码
                    auditStatus: auditStatus,  // 审核状态
                    affixSealStatus: affixSealStatus, // 是否盖章
                    nullifyStatus: nullifyStatus, // 是否作废
                }
            });
        }
    };

    $('#SearchBtn').on('click', function () {
        var type = $(this).data('type');
        active[type] ? active[type].call(this) : '';
    });


    //头工具栏事件
    table.on('toolbar(List-toolbar)', function (obj) {
        switch (obj.event) {
            case 'add':
                layer.open({
                    id: "Save-frame",
                    type: 2,
                    area: ['600px', '501px'],
                    title: '新增',
                    fixed: false,
                    maxmin: true,
                    content: web.rootPath() + 'custContractInfo/add.html'
                });
                break;
            case 'export':
                var eix;
                //获取搜索条件值
                var contractInfo = $("#searchForm").find("input[name='contractInfo']").val().trim();
                var auditStatus = $("#searchForm").find("select[name='auditStatus']").val().trim();
                var affixSealStatus = $("#searchForm").find("select[name='affixSealStatus']").val().trim();
                var nullifyStatus = $("#searchForm").find("select[name='nullifyStatus']").val().trim();

                var url = web.rootPath() + 'custContractInfo/export?contractInfo=' + contractInfo + '&auditStatus='
                    + auditStatus + '&affixSealStatus=' + affixSealStatus + '&nullifyStatus=' + nullifyStatus;
                $.fileDownload(url, {
                    httpMethod: 'POST',
                    prepareCallback: function (url) {
                        eix = layer.load(2);
                    },
                    successCallback: function (url) {
                        layer.close(eix)
                    },
                    failCallback: function (html, url) {
                        layer.close(eix)
                        layer.msg("导出失败", {icon: 2});
                    }
                });
                break;
        }
    });
    //监听工具条
    table.on('tool(List-toolbar)', function (obj) {
        var data = obj.data;
        console.log(obj.event);
        switch (obj.event) {
            case 'update':
                layer.open({
                    id: "Update-frame",
                    type: 2,
                    resize: false,
                    area: ['550px', '500px'],
                    title: '修改',
                    fixed: false,
                    maxmin: true,
                    content: web.rootPath() + "custContractInfo/" + data.id + ".html?_"
                });
                break;
            case 'delete':
                var index = layer.confirm('确定要删除?', {
                    btn: ['确定', '取消'] //按钮
                }, function () {
                    $.ajax({
                        url: web.rootPath() + "custContractInfo/delete/" + data.id,
                        type: "delete",
                        contentType: "application/json;charset=utf-8",
                        data: JSON.stringify(data.field),
                        dataType: 'json',
                        success: function (data) {
                            layer.msg("操作成功", {
                                icon: 1,
                                success: function () {
                                    $('#SearchBtn').trigger("click");
                                }
                            });
                        },
                        error: function (e) {
                            layer.msg(e.responseJSON.message, {icon: 2});
                        }
                    })
                }, function () {
                });
                break;
            case 'audit':
                var index = layer.confirm('审核是否通过?', {
                    btn: ['是', '否'] //按钮
                }, function () {
                    $.ajax({
                        url: web.rootPath() + "custContractInfo/audit/" + data.id,
                        type: "get",
                        contentType: "application/json;charset=utf-8",
                        data: { userChoice: true }, // 用户选择是
                        dataType: 'json',
                        success: function (data) {
                            layer.msg("操作成功", {
                                icon: 1,
                                success: function () {
                                    $('#SearchBtn').trigger("click");
                                }
                            });
                        },
                        error: function (e) {
                            layer.msg(e.responseJSON.message, {icon: 2});
                        }
                    })
                }, function () {
                    // 点击“否”按钮
                    $.ajax({
                        url: web.rootPath() + "custContractInfo/audit/" + data.id,
                        type: "get",
                        data: { userChoice: false }, // 用户选择否
                        success: function (response) {
                            layer.msg("操作成功", {
                                icon: 1,
                                success: function () {
                                    $('#SearchBtn').trigger("click");
                                }
                            });
                        },
                        error: function (e) {
                            layer.msg(e.responseJSON.message, {icon: 2});
                        }
                    });
                });
                break;
            case 'affixSeal':
                var index = layer.confirm('是否盖章确认?', {
                    btn: ['是', '否'] //按钮
                }, function () {
                    $.ajax({
                        url: web.rootPath() + "custContractInfo/affixSeal/" + data.id,
                        type: "get",
                        contentType: "application/json;charset=utf-8",
                        data: { userChoice: true }, // 用户选择是
                        dataType: 'json',
                        success: function (data) {
                            layer.msg("操作成功", {
                                icon: 1,
                                success: function () {
                                    $('#SearchBtn').trigger("click");
                                }
                            });
                        },
                        error: function (e) {
                            layer.msg(e.responseJSON.message, {icon: 2});
                        }
                    })
                }, function () {

                });
                break;
            case 'nullify':
                var index = layer.confirm('是否作废合同?', {
                    btn: ['是', '否'] //按钮
                }, function () {
                    $.ajax({
                        url: web.rootPath() + "custContractInfo/nullify/" + data.id,
                        type: "get",
                        contentType: "application/json;charset=utf-8",
                        data: { userChoice: true }, // 用户选择是
                        dataType: 'json',
                        success: function (data) {
                            layer.msg("操作成功", {
                                icon: 1,
                                success: function () {
                                    $('#SearchBtn').trigger("click");
                                }
                            });
                        },
                        error: function (e) {
                            layer.msg(e.responseJSON.message, {icon: 2});
                        }
                    })
                }, function () {

                });
                break;
        };
    });

    $(window).resize(function () {
        $('div[lay-id="ListTable"]').height(document.body.offsetHeight - Math.round(Number($('.layui-card-header').height()) + 47));
    });
});
