// Call the dataTables jQuery plugin
$(document).ready(function () {
    $.ajax({
        url: baseUrl + "/admin/get_block_height_list",
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
                            sortable:true
                        },
                        {
                            field: 'chainType',
                            title: '链类型',
                            // editable: {
                            //     type: 'select',
                            //     source: function () {
                            //         let result = [];
                            //         supportChain.forEach(chain => {
                            //             result.push({value: chain, text: chain});
                            //         })
                            //         result.push({value:"OMNI",text:"OMNI"});
                            //         return result
                            //     },
                            //     validate: function (v) {
                            //         if (!v) return '不能为空';
                            //     }
                            // }
                        },
                        {
                            field: 'height',
                            title: '高度',
                            editable: {
                                type: 'number',
                                validate: function (v) {
                                    if (!v) return '不能为空';
                                }
                            }
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
                            url: baseUrl + "/admin/edit_block_height",
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
