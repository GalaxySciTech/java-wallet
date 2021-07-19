// Call the dataTables jQuery plugin
$(document).ready(function () {
    $.ajax({
        url: baseUrl + "/admin/get_withdraw_list",
        type: 'GET',
        dataType: "json",
        headers: {access_token: $.cookie("access_token")},
        async: true,
        success: function (data) {
            if (data["code"] !== 200) {
                handleErrorData(data);
            } else {
                let d = data["data"];
                d.forEach(e => {
                    e["buttons"] = "<a href='#' class='btn btn-danger btn-circle btn-sm'><i class='fas fa-trash'></i></a>";
                })
                $('#dataTable').bootstrapTable({
                    toolbar:'#toolbar',
                    showToggle:true,
                    showFullscreen:true, //显示全屏按钮
                    pagination: true,
                    search: true,
                    showColumns: true,
                    cache: false,
                    data: d,
                    columns: [
                        {
                            field: 'id',
                            title: '#',
                            sortable: true
                        },
                        {
                            field: 'hash',
                            title: '交易hash'
                        },
                        {
                            field: 'fromAddress',
                            title: '转出地址'
                        },
                        {
                            field: 'toAddress',
                            title: '转入地址'
                        },
                        {
                            field: 'amount',
                            title: '数量',
                            sortable: true
                        },
                        {
                            field: 'chainType',
                            title: '链类型',
                            sortable:true
                        },
                        {
                            field: 'tokenSymbol',
                            title: '代币类型',
                            sortable:true
                        },
                        {
                            field: 'withdrawType',
                            title: '交易类型',
                            sortable: true,
                            editable: {
                                type: 'select',
                                source: [
                                    {value: "100", text: "提现"},
                                    {value: "200", text: "归集"},
                                    {value: "300", text: "发送gas手续费"}]
                            }
                        },
                        {
                            field: 'createdAt',
                            title: '创建时间',
                            sortable: true
                        },
                        {
                            field: 'buttons',
                            title: '按钮'
                        },
                    ]
                });
            }
        }
    });

});
