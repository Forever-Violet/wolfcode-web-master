layui.use(['form', 'layer', 'table', 'laytpl', 'laydate'], function () {
    var form = layui.form,
        layer = layui.layer,
        $ = layui.jquery,
        laytpl = layui.laytpl,
        laydate = layui.laydate,
        table = layui.table;

    //起始日期  渲染
    var start = laydate.render({
        elem: '#startDate',
        type: 'date',
        done: function (value, date) { // 日期选择后的回调函数
            endMax = end.config.max; // 存储结束日期的 最大日期
            end.config.min = date; // 设置结束日期的 最小能选择的日期 为 起始日期
            end.config.min.month = date.month - 1; // 设置 结束日期的 最小能选择的月份
        }
    });
    //结束日期 渲染
    var end = laydate.render({
        elem: '#endDate',
        type: 'date', //日期选择器的类型为 日期
        done: function (value, date) { // 日期选择后的回调函数
            if ($.trim(value) == '') { // 检查结束日期是否为空
                var curDate = new Date(); // 将当日日期设置为默认日期
                date = {
                    'date': curDate.getDate(),
                    'month': curDate.getMonth() + 1,
                    'year': curDate.getFullYear()
                };
            }
            start.config.max = date;  // 将起始日期的最大能设置的日期 设置为 结束日期
            start.config.max.month = date.month - 1; // 设置起始日期的 最小能选择的 最大月份
        }
    });


    //用户列表
    var tableIns = table.render({
        elem: '#List',
        url: web.rootPath() + 'visitInfo/list',
        cellMinWidth: 95,
        page: true,
        height: "full-" + Math.round(Number($('.layui-card-header').height()) + 44),
        limits: [10, 13, 15, 20, 30, 50, 100, 200],
        limit: 10,
        toolbar: '#List-toolbar',
        id: "ListTable",
        cols: [[
            {type: "checkbox", fixed: "left", width: 50},
                    /*{field: 'id', title: '唯一id', minWidth: 100, align: "center"},*/
                    {field: 'custName', title: '所属企业', minWidth: 100, align: "center"},
                    {field: 'linkmanName', title: '联系人名称', minWidth: 100, align: "center"},
                    {
                        field: 'visitType', title: '拜访方式', minWidth: 100, align: "center", templet: function(data){
                            if (data.visitType == '1'){
                                return "<button class=\"layui-btn layui-btn-normal layui-btn-xs\" style='background-color: #6ee175'>上门走访</button>";
                            } else {
                                return "<button class=\"layui-btn layui-btn-normal layui-btn-xs\" style='background-color: #2a75ed'>电话拜访</button>";
                            }
                        }
                    },
                    {field: 'visitReason', title: '拜访原因', minWidth: 100, align: "center"},
                    {field: 'content', title: '交流内容', minWidth: 100, align: "center"},
                    {field: 'visitDate', title: '拜访时间', minWidth: 100, align: "center"},
                    {field: 'inputUser', title: '录入人', minWidth: 100, align: "center"},
                    {field: 'inputTime', title: '录入时间', minWidth: 100, align: "center"},

            {title: '操作', width: 160, templet: '#List-editBar', fixed: "right", align: "center"}
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
        }
        ;
    });

    var $ = layui.$, active = {
        reload: function () {
            //获取搜索条件值
            var visitReason = $("#searchForm").find("input[name='visitReason']").val().trim();
            var visitType = $("#searchForm").find("select[name='visitType']").val().trim();
            var startDate = $("#searchForm").find("input[name='startDate']").val().trim();
            var endDate = $("#searchForm").find("input[name='endDate']").val().trim();

            //表格重载
            tableIns.reload({
                where: { //设定异步数据接口的额外参数，任意设
                    visitReason: visitReason, // 拜访原因模糊查询
                    visitType: visitType, // 拜访方式
                    startDate: startDate, // 开始日期
                    endDate: endDate, // 结束日期
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
                    content: web.rootPath() + 'visitInfo/add.html'
                });
                break;
        }
    });
    //监听工具条
    table.on('tool(List-toolbar)', function (obj) {
        var data = obj.data;
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
                    content: web.rootPath() + "visitInfo/" + data.id + ".html?_"
                });
                break;
            case 'delete':
                var index = layer.confirm('确定要删除?', {
                    btn: ['确定', '取消'] //按钮
                }, function () {
                    $.ajax({
                        url: web.rootPath() + "visitInfo/delete/" + data.id,
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
                            layer.msg(e.responseJSON.data.toString(), {icon: 2});
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
