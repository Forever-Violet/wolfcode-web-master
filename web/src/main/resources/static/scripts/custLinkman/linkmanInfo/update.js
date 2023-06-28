layui.use(['form', 'layer', 'slider'], function () {
    var form = layui.form,
        layer = layui.layer,
        slider = layui.slider,
        $ = layui.jquery;


    // 渲染滑块
    slider.render({
        elem: '#ageSlider',
        value: $('input[name="age"]').val(), // 在隐藏输入框中获取age的值, 并设置为默认值
        min: 0, //最小 0
        max: 100, //最大 100
        input: true, //输入框
        theme: '#16b777',  //换个色
        change: function (value) { // 当滑块变动时
            // 将age的值设置到隐藏输入框
            $('input[name="age"]').val(value);
        }
    });

    form.on('submit(Add-filter)', function (data) {
        $.ajax({
            url: web.rootPath() + "linkmanInfo/update",
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
            error: function (e) { //注意是errCode  别搞错了
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
