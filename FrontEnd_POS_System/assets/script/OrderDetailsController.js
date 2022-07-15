/**
 * @ author : A.D.Liyanage
 * @ since : 0.1.0
 **/

loadAllOrders();
loadOrderDetailTable();
bindOrderDetailsClickEvent();

/* ------------------- Load All Orders to Order Table ------------------- */
function loadAllOrders(){
    $("#orderTable").empty();
    $.ajax({
        url: "http://localhost:8080/pos/orders?option=GETALL",
        method: "GET",
        success: function (resp) {
            for (const orders of resp.data) {

                let row = `<tr><td>${orders.orderId}</td><td>${orders.cid}</td><td>${orders.orderDate}</td><td>
                ${orders.total}</td><td>${orders.discount}</td><td>${orders.subTotal}</td></tr>`;
                $("#orderTable").append(row);

            }
            bindOrderDetailsClickEvent();
        }
    });
}

/* ------------------- Load Order Details Table ------------------- */
function loadOrderDetailTable() {
    $("#orderDetailTable").empty();
    $.ajax({
        url: "http://localhost:8080/pos/orders?option=GETALLORDERDETAILS",
        method: "GET",
        success: function (res) {
            console.log(res.data);
            for (let orderDetail of res) {
                let tableRow = `<tr><td>${orderDetail.oId}</td><td>${orderDetail.iCode}</td><td>${orderDetail.qty}</td><td>${orderDetail.price}</td><td>${orderDetail.total}</td></tr>`;
                $("#orderDetailTable").append(tableRow);
            }
        }
    })
}

function bindOrderDetailsClickEvent(){
    $("#orderTable > tr").click('click', function () {

        tableRow = $(this);
        let oid = $(this).children(":eq(0)").text();

        $("#orderDetailTable").empty();
        $.ajax({
            url: "http://localhost:8080/pos/orders?option=SEARCH&orderId=" + oid,
            method: "GET",
            success: function (resp) {
                for (const orders of resp) {

                    let row = `<tr><td>${orders.oId}</td><td>${orders.iCode}</td><td>${orders.qty}</td><td>
                    ${orders.price}</td><td>${orders.total}</td></tr>`;
                    $("#orderDetailTable").append(row);

                }
            }

        });
    });
}

/* ------------------- Search Order ------------------- */
let regOrderId = /^(O-)[0-9]{4}$/;

/* ------------------- add validation to search order text field ------------------- */
$("#txtSearchOrderId").keyup(function (event) {
    let searchOid = $("#txtSearchOrderId").val();
    if (regOrderId.test(searchOid)) {
        $("#txtSearchOrderId").css('border', '2px solid green');
        if (event.key == "Enter") {
            searchOrderByOrderDetailTable(searchOid);
            searchOrderByOrderTable(searchOid);
        }
    } else {
        $("#txtSearchOrderId").css('border', '2px solid red');
    }
});

$("#searchOrder").on('shown.bs.modal', function () {
    $(this).find("#txtSearchOrderId").focus();
});

$("#btnSearchOrder").click(function () {
    let searchOid = $("#txtSearchOrderId").val();
    searchOrderByOrderTable(searchOid);
    searchOrderByOrderDetailTable(searchOid);
});

/* ------------------- Search Order from Order Table ------------------- */
function searchOrderByOrderTable(orderId) {

    $.ajax({
        url: "http://localhost:8080/pos/orders?option=SEARCHORDER&orderId=" + orderId,
        method: "GET",
        success: function (res) {
            if (res.status == 200) {
                $("#orderTable").empty();
                let tableRow = `<tr><td>${res.orderId}</td><td>${res.cid}</td><td>${res.orderDate}</td><td>${res.total}</td><td>${res.discount}</td><td>${res.subTotal}</td></tr>`;
                $("#orderTable").append(tableRow);
            } else {
                loadAllOrders();
                loadOrderDetailTable();

                alert("Error! Order Not Found");

            }
        },
        error: function (ob, textStatus, error) {
            alert(textStatus);
        }
    });
}

/* ------------------- Search Order details from Order Detail Table ------------------- */
function searchOrderByOrderDetailTable(orderId) {
    $("#orderDetailTable").empty();
    $.ajax({
        url: "http://localhost:8080/pos/orders?option=SEARCH&orderId=" + orderId,
        method: "GET",
        success: function (res) {
            for (let orderDetail of res) {
                let tableRow = `<tr><td>${orderDetail.oId}</td><td>${orderDetail.iCode}</td><td>${orderDetail.qty}</td><td>${orderDetail.price}</td><td>${orderDetail.total}</td></tr>`;
                $("#orderDetailTable").append(tableRow);
            }
        }
    });
}

/* ------------------- btn clear search field function ------------------- */
$("#btnClearSearchOrderField").click(function () {
    $("#txtSearchOrderId").val("");
    $("#txtSearchOrderId").css('border', '1px solid #ced4da');
    $("#txtSearchOrderId").focus();
    loadAllOrders();
    loadOrderDetailTable();
    bindOrderDetailsClickEvent()
});