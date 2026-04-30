// Call the dataTables jQuery plugin
$(document).ready(function () {
    $.ajax({
        url: baseUrl + "/admin/get_dashboard",
        type: 'GET',
        dataType: "json",
        headers:{access_token:$.cookie("access_token")},
        async: true,
        success: function (data) {
            if (data["code"] !== 200) {
                handleErrorData(data);
            }else{
                data=data["data"];
                let totalAddress=data["totalAddress"];
                let totalDeposit=data["totalDeposit"];
                let totalWithdraw=data["totalWithdraw"];
                let totalTx=data["totalTx"];
                let pieChart=data["pieChart"];
                let areaChart=data["areaChart"];
                $(".totalAddress").html(totalAddress);
                $(".totalDeposit").html(totalDeposit);
                $(".totalWithdraw").html(totalWithdraw);
                $(".totalTx").html(totalTx);
                createPieChart(pieChart["labels"],pieChart["data"]);
                // createAreaChart(areaChart["labels"],areaChart["data"])
            }
        }
    });

});
