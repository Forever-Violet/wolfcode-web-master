layui.use(['form', 'layer', 'laydate'], function () {
    var form = layui.form,
        layer = layui.layer,
        laydate = layui.laydate,
        $ = layui.jquery;

    // 渲染 日期选择
    laydate.render({
        elem: '#ID-laydate-demo',
        trigger: 'click', //小屏幕一闪而过 可以加上这个
        max: 0, // 设置最大日期为当前日期
    });

    // 监听企业用户下拉框的选择变化事件
    form.on('select(custId)', function(data){
       // 获取选中的企业客户id
       var selectedCustId = data.value;

       // 发送Ajax请求, 获取联系人列表
        $.ajax({
            url: web.rootPath() + 'visitInfo/getLinkmen',
            type: 'GET',
            data: {
                custId: selectedCustId
            },
            success: function(response) {
                // 请求成功, 获取联系人列表数据
                var linkmen = response;

                console.log(linkmen);

                // 清空企业联系人下拉框的选项
                var linkmanSelect = $('#linkmanId');
                linkmanSelect.empty();

                // 添加新的选项
                linkmen.forEach(function(linkman){
                    var option = $('<option></option>');
                    option.val(linkman.id);
                    option.text(linkman.linkman);
                    linkmanSelect.append(option);
                });

                // 重新渲染下拉框
                form.render('select');
            },
            error: function (xhr, status, error) {
                console.error(error);
            }
        })
    });

    form.on('submit(Add-filter)', function (data) {
        $.ajax({
            url: web.rootPath() + "visitInfo/save",
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
            error: function (e) {//注意是errCode  别搞错了
                if (e.responseJSON.errCode === 1003) {//接收错误信息, 数据效验错误
                    layer.msg(e.responseJSON.data.toString(), {icon: 2});
                } else{
                    layer.msg(e.responseJSON.message, {icon: 2});
                }
            }

        });
        return false;
    });

});
