function delToken(id) {
    $.ajax({
        url: baseUrl + "/admin/del_token",
        type: 'POST',
        dataType: "json",
        data: {id: id},
        headers: {access_token: $.cookie("access_token")},
        async: true,
        success: function (data) {
            if (data["code"] !== 200) {
                handleErrorData(data);
            } else {
                alert("删除成功");
            }
        }
    });
}

// Call the dataTables jQuery plugin
$(document).ready(function () {
    $.ajax({
        url: baseUrl + "/admin/get_token_list",
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
                    e["buttons"] = "<a href='#' class='btn btn-danger btn-circle btn-sm' onclick='delToken("+e["id"]+")'><i class='fas fa-trash'></i></a>";
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
                            sortable:true
                        },
                        {
                            field: 'chainType',
                            title: '链类型',
                            sortable:true,
                            editable: {
                                type: 'select',
                                source: function () {
                                    let result = [];
                                    supportChain.forEach(chain => {
                                        result.push({value: chain, text: chain});
                                    })
                                    return result
                                },
                                validate: function (v) {
                                    if (!v) return '不能为空';
                                }
                            }
                        },
                        {
                            field: 'tokenSymbol',
                            title: '代币类型',
                            sortable:true,
                            editable: {
                                type: 'select',
                                source: function () {
                                    let result = [];
                                    supportSymbol.forEach(chain => {
                                        result.push({value: chain, text: chain});
                                    })
                                    return result
                                },
                                validate: function (v) {
                                    if (!v) return '不能为空';
                                }
                            }
                        },
                        {
                            field: 'tokenAddress',
                            title: '代币地址',
                            editable: {
                                type: 'text',
                                validate: function (v) {
                                    if (!v) return '不能为空';
                                }
                            }
                        },
                        {
                            field: 'minCollect',
                            title: '最小归集数量',
                            sortable:true,
                            editable: {
                                type: 'text',
                                validate: function (v) {
                                    if (!v) return '不能为空';
                                }
                            }
                        },
                        {
                            field: 'createdAt',
                            title: '创建时间',
                            sortable:true
                        },
                        {
                            field: 'buttons',
                            title: '按钮'
                        },
                    ],
                    onEditableSave: function (field, row, oldValue, $el) {
                        let time=row["createdAt"];
                        delete row["createdAt"];
                        delete row["updatedAt"];
                        $.ajax({
                            url: baseUrl + "/admin/edit_token",
                            type: 'POST',
                            dataType: "json",
                            data: row,
                            headers: {access_token: $.cookie("access_token")},
                            async: true,
                            success: function (data) {
                                if (data["code"] !== 200) {
                                    handleErrorData(data);
                                } else {

                                    alert("编辑成功");
                                }
                            }
                        });
                        row["createdAt"]=time;
                    },
                });
            }
        }
    });

});
