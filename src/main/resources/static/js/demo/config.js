function delConfig(id){
    $.ajax({
        url: baseUrl + "/admin/del_config",
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
                location.reload();
            }
        }
    });
}
// Call the dataTables jQuery plugin
$(document).ready(function () {
    $.ajax({
        url: baseUrl + "/admin/get_sys_config_list",
        type: 'GET',
        dataType: "json",
        headers:{access_token:$.cookie("access_token")},
        async: true,
        success: function (data) {
            if (data["code"] !== 200) {
                handleErrorData(data);
            }else{
                let d = data["data"];
                d.forEach(e => {
                    // e["buttons"] = "<a href='#' onclick='delConfig("+e["id"]+")' class='btn btn-danger btn-circle btn-sm'><i class='fas fa-trash'></i></a>";
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
                            field: 'configKey',
                            title: '键',
                            // editable: {
                            //     type: 'text',
                            //     validate: function (v) {
                            //         if (!v) return '不能为空';
                            //     }
                            // }
                        },
                        {
                            field: 'configValue',
                            title: '值',
                            editable: {
                                type: 'text',
                                validate: function (v) {
                                    if (!v) return '不能为空';
                                }
                            }
                        },
                        {
                            field: 'configGroup',
                            title: '组',
                            editable: {
                                type: 'text',
                                validate: function (v) {
                                    if (!v) return '不能为空';
                                }
                            }
                        },
                        {
                            field: 'description',
                            title: '描述',
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
                            url: baseUrl + "/admin/edit_config",
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
