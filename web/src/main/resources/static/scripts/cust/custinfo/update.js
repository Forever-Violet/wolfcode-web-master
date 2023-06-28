layui.use(['form', 'layer', 'laydate'], function () {
    var form = layui.form,
        layer = layui.layer,
        laydate = layui.laydate, //日期时间插件
        $ = layui.jquery;

    // 渲染
    laydate.render({
        elem: '#ID-laydate-demo',
        trigger: 'click', //小屏幕一闪而过 可以加上这个
    });

    form.on('submit(Add-filter)', function (data) {
        $.ajax({
            url: web.rootPath() + "custinfo/update",
            contentType: "application/json",
            type: "put",
            data: JSON.stringify(data.field),
            dataType: 'json',
            success: function (data) {
                layer.msg("操作成功", {
                    icon: 1,
                    success: function () {
                        reloadTb("Update-frame", "#SearchBtn");
                    }
                });
            },
            error: function (e) {
                if (e.responseJSON.errCode === 1003){ //接受错误信息, 数据效验错误
                    layer.msg(e.responseJSON.data.toString(), {icon: 2});
                } else{
                    layer.msg(e.responseJSON.message, {icon: 2});
                }
            }

        })
        return false;
    });

});
