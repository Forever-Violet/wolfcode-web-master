layui.use(['form', 'layer'], function () {
    var form = layui.form,
        layer = layui.layer,
        $ = layui.jquery;


    form.on('submit(Add-filter)', function (data) {
        $.ajax({
            url: web.rootPath() + "custOrderInfo/update",
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
            error: function (e) {//注意是errCode  别搞错了
                if (e.responseJSON.errCode === 1003) {//接收错误信息, 数据效验错误
                    layer.msg(e.responseJSON.data.toString(), {icon: 2});
                } else{
                    layer.msg(e.responseJSON.message, {icon: 2});
                }
            }

        })
        return false;
    });

});
