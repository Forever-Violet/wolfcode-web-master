layui.extend({}).use(['form', 'layer', 'laydate'], function () {
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
            url: web.rootPath() + "custinfo/save",
            type: "post",
            contentType: "application/json",
            data: JSON.stringify(data.field),
            dataType: 'json',
            traditional: true,
            success: function (data) {
                layer.msg("操作成功", {
                    icon: 1,
                    success: function () {
                        reloadTb("Save-frame", "#SearchBtn");
                    }
                });
            },
            error: function (e) {
                //{"errCode":1003,"message":"数据校验失败","data":["请填写企业名称!"]}
                if (e.responseJSON.errCode === 1003){ //接收错误信息, 数据效验错误
                    layer.msg(e.responseJSON.data.toString(), {icon: 2});
                } else{
                    layer.msg(e.responseJSON.message, {icon: 2});
                }
            }

        });
        return false;
    });

});
