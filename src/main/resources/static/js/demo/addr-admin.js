function addAddrAdmin() {
    let type = $("#type").val();
    let address = $("#address").val();
    let chainType = $("#chainType").val();
    if (!type) {
        alert("请选择地址类型");
        return;
    }
    if (!address) {
        alert("请输入地址");
        return;
    }
    if (!chainType) {
        alert("请选择链类型");
        return;
    }
    $.ajax({
        url: baseUrl + "/admin/add_addr_admin",
        type: 'POST',
        dataType: "json",
        data: {type: type, address: address, chainType: chainType},
        headers: {access_token: $.cookie("access_token")},
        async: true,
        success: function (data) {
            if (data["code"] !== 200) {
                handleErrorData(data);
            } else {
                alert("添加成功");
                location.reload();
            }
        }
    });
}

function infoAddrAdmin(address, chainType,walletCode) {
    $.ajax({
        url: baseUrl + "/wallet/v1/export_wallet",
        type: 'GET',
        dataType: "json",
        data: {walletCode: walletCode, type: 100},
        headers: {access_token: $.cookie("access_token")},
        async: true,
        success: function (data) {
            if (data["code"] !== 200) {
                handleErrorData(data);
            } else {
                let d=data["data"];
                let html = "<div>地址:" + address + "</div><div>链类型:" + chainType + "</div><div>私钥文件号:"+walletCode+"</div><div>私钥:"+d+"</div>"
                $("#infoAddrAdmin").html(html);
            }
        }
    });

}

function delAddrAdmin(id) {
    $.ajax({
        url: baseUrl + "/admin/del_addr_admin",
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
        url: baseUrl + "/admin/get_addr_admin_list",
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
                    e["buttons"] = "<a href='#' data-toggle='modal' data-target='#info' onclick='infoAddrAdmin(\""+e["address"]+"\",\""+e["chainType"]+"\",\""+e["walletCode"]+"\")' class='btn btn-info btn-circle btn-sm'><i class='fas fa-info-circle'></i></a><a href='#' onclick='delAddrAdmin(" + e["id"] + ")' class='btn btn-danger btn-circle btn-sm'><i class='fas fa-trash'></i></a>";
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
                        }, {
                            field: 'address',
                            title: '地址',
                            editable: {
                                type: 'text',
                                validate: function (v) {
                                    if (!v) return '不能为空';
                                },
                            }
                        },
                        {
                            field: 'chainType',
                            title: '链类型',
                            sortable: true,
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
                            field: 'addressType',
                            title: '地址类型',
                            sortable: true,
                            editable: {
                                type: 'select',
                                source: [
                                    {value: "100", text: "热钱包"},
                                    {value: "200", text: "归集钱包"},
                                    {value: "300", text: "gas钱包"},],
                                validate: function (v) {
                                    if (!v) return '不能为空';
                                }
                            }
                        },
                        {
                            field: 'walletCode',
                            title: '私钥文件号',
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
                            sortable: true
                        },
                        {
                            field: 'buttons',
                            title: '按钮'
                        }
                    ],
                    onEditableSave: function (field, row, oldValue, $el) {
                        let time = row["createdAt"];
                        delete row["createdAt"];
                        delete row["updatedAt"];
                        $.ajax({
                            url: baseUrl + "/admin/edit_addr_admin",
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
                        row["createdAt"] = time;
                    },
                });
            }
        }
    });
});
