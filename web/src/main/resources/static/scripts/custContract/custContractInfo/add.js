layui.use(['form', 'layer', 'laydate'], function () {
    var form = layui.form,
        layer = layui.layer,
        laydate = layui.laydate,
        $ = layui.jquery;

    //起始日期  渲染
    var start = laydate.render({
        elem: '#startDate',
        type: 'date',
        trigger: 'click', //小屏幕一闪而过 可以加上这个
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
        trigger: 'click', //小屏幕一闪而过 可以加上这个
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

    // 监听盖章确认状态下拉框的选择变化事件
    form.on('select(affixSealStatus)', function (data) {
        // 获取选中的盖章确认状态
        var selectedAffixSealStatus = data.value;
        // 获取是否作废下拉框
        var selectNullifyStatus = $('#nullifyStatus');

        if (selectedAffixSealStatus == '0') { // 未盖章确认的合同可以作废, 加上选项
            var option = $('<option></option>');
            option.val('1'); // 1 作废选项
            option.text("是");

            selectNullifyStatus.append(option);

            // 重新渲染下拉框
            form.render('select');
        } else { // 已经盖章确认的合同不能作废, 去除选项
            // 清空选项框
            selectNullifyStatus.empty();

            var option = $('<option></option>');
            option.val('0'); // 0 否选项
            option.text("否");

            selectNullifyStatus.append(option);
            // 重新渲染下拉框
            form.render('select');
        }

    });

    form.on('submit(Add-filter)', function (data) {
        $.ajax({
            url: web.rootPath() + "custContractInfo/save",
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
                if (e.responseJSON.errCode === 1003) {
                    layer.msg(e.responseJSON.data.toString(), {icon: 2});
                } else {
                    layer.msg(e.responseJSON.message, {icon: 2});
                }
            }

        });
        return false;
    });

});
