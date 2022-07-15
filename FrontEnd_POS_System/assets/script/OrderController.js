/**
 * @ author : A.D.Liyanage
 * @ since : 0.1.0
 **/

generateOrderId();
setCurrentDate();
loadCustomerIds();
loadItemCodes();

/* ------------------- Generate Order Id ------------------- */
function generateOrderId() {

    $("#txtOrderID").val("O-0001");
    $.ajax({
        url: "http://localhost:8080/pos/orders?option=GETID",
        method: "GET",
        success: function (resp) {
            let orderId = resp.orderId;
            let tempId = parseInt(orderId.split("-")[1]);
            if (tempId != 0){
                tempId = tempId+1;
                if (tempId <= 9){
                    $("#txtOrderID").val("O-000"+tempId);
                }else if (tempId <= 99) {
                    $("#txtOrderID").val("O-00" + tempId);
                }else if (tempId <= 999){
                    $("#txtOrderID").val("O-0" + tempId);
                }else {
                    $("#txtOrderID").val("O-"+tempId);
                }
            }else {
                $("#txtOrderID").val("O-0001");
            }

        },
        error: function (ob, statusText, error) {

        }
    });

}

/* ------------------- Generate Current Date ------------------- */

function setCurrentDate() {
    let orderDate = $('#txtOrderDate');
    let today = new Date();
    let dd = String(today.getDate()).padStart(2, '0');
    let mm = String(today.getMonth() + 1).padStart(2, '0');
    let yyyy = today.getFullYear();
    today = yyyy + '-' + mm + '-' + dd;
    orderDate.val(today);
}

/* ------------------- Customer Segment ------------------- */

/*  Load all customer Id  */
function loadCustomerIds() {
    $("#txtOrderCusID").empty();
    $("#txtOrderCusID").append($("<option></option>").attr("value", 0).text("Select Customer"));
    let count = 0;
    $.ajax({
        url: "http://localhost:8080/pos/customer?option=GETALL",
        method: "GET",
        success: function (res) {
            for (const customer of res.data) {
                $("#txtOrderCusID").append($("<option></option>").attr("value", count).text(customer.id));
                count++;
            }
        },
        error: function (ob, textStatus, error) {
            alert(textStatus);
        }
    });

}

/*  Select customer details load text field  */
$("#txtOrderCusID").click(function () {

    let id = $("#txtOrderCusID option:selected").text();
    let name = $("#txtOrderCusName").val();
    let address = $("#txtOrderCusAddress").val();
    let contact = $("#txtOrderCusContact").val();

    $.ajax({
        url: "http://localhost:8080/pos/customer?option=GETALL",
        method: "GET",
        success: function (resp) {
            for (const customer of resp.data) {

                if (customer.id == id) {

                    name = customer.name;
                    address = customer.address;
                    contact = customer.contact;

                    $("#txtOrderCusName").val(name);
                    $("#txtOrderCusAddress").val(address);
                    $("#txtOrderCusContact").val(contact);
                }

            }
        }
    });

});

/* ------------------- Item Segment ------------------- */

/*  Load all Item codes  */
function loadItemCodes() {
    $("#txtOrderItemCode").empty();
    $("#txtOrderItemCode").append($("<option></option>").attr("value", 0).text("Select Item"));
    let count = 0;
    $.ajax({
        url: "http://localhost:8080/pos/item?option=GETALL",
        method: "GET",
        success: function (res) {
            for (const item of res.data) {
                $("#txtOrderItemCode").append($("<option></option>").attr("value", count).text(item.itemCode));
                count++;
            }
        },
        error: function (ob, textStatus, error) {
            alert(textStatus);
        }
    });

}

/*  Select Item details load text field  */
$("#txtOrderItemCode").click(function () {

    let id = $("#txtOrderItemCode option:selected").text();
    let name = $("#txtOrderItemName").val();
    let qtyOnHand = $("#txtOrderItemQtyOnHand").val();
    let price = $("#txtOrderItemPrice").val();

    $.ajax({
        url: "http://localhost:8080/pos/item?option=GETALL",
        method: "GET",
        success: function (resp) {
            for (const item of resp.data) {
                if (item.itemCode == id) {

                    name = item.name;
                    qtyOnHand = item.qtyOnHand;
                    price = item.price;

                    $("#txtOrderItemName").val(name);
                    $("#txtOrderItemQtyOnHand").val(qtyOnHand);
                    $("#txtOrderItemPrice").val(price);
                }
            }
        }
    });

});

/* ------------------- Order qty validation ------------------- */

var regExSellQuantity=/^[0-9]{1,20}$/;

$("#txtOrderQty").keyup(function (event) {

    let sellQty = $("#txtOrderQty").val();
    if (regExSellQuantity.test(sellQty)){
        $("#txtOrderQty").css('border','1px solid blue');
        $("#errorOrderQty").text("");
        if (event.key=="Enter"){
            $("#btnAddToTable").focus();
        }
    }else {
        $("#txtOrderQty").css('border','2px solid red');
        $("#errorOrderQty").text("Quantity is a required field: Pattern 00");
    }
});

/* ------------------- Order Table ------------------- */

var tableRow;

/*  Order details add to cart table for add item button click  */
$("#btnAddToCart").click(function () {

    $("#btnPurchase").attr('disabled', true);

    if ($("#txtOrderCusName").val() == '') {
        alert("Please Select Customer ....");
    } else if ($("#txtOrderItemName").val() == '') {
        alert("Please Select Item ....");
    } else if ($("#txtOrderQty").val() == "") {
        alert("Please Enter Quantity ....");
    } else if ($("#errorOrderQty").text() != "") {
        alert("Please Enter Valid Quantity ....");
    } else if (parseInt($("#txtOrderQty").val()) > parseInt($("#txtOrderItemQtyOnHand").val())){
        alert("Please Check Stock ....");
    }else {

        let text = "Do you really want to add to cart this Item ? ";
        $("#btnPurchase").attr('disabled', false);

        if (confirm(text) == true) {

            let duplicate = false;

            for (let i = 0; i < $("#addToCartTable tr").length; i++) {
                console.log($("#addToCartTable tr").children(':nth-child(1)')[i].innerText);
                if ($("#txtOrderItemCode option:selected").text() == $("#addToCartTable tr").children(':nth-child(1)')[i].innerText) {
                    duplicate = true;
                }
            }

            if (duplicate != true) {

                loadOrderDetail();
                minusQty($("#txtOrderQty").val());
                manageTotal($("#txtOrderQty").val() * $("#txtOrderItemPrice").val());
                manageDiscount();

                itemTextFieldClear();
                //bindOrderClickEvent();

            } else if (duplicate == true) {

            }
            //bindOrderClickEvent();
        }

    }

});

var itemCode;
var itemName;
var itemPrice;
var itemQtyOnHand;
var itemOrderQty;

$("#addToCartTable").empty();

/*  Order details load the cart table  */
function loadOrderDetail() {

    itemCode = $("#txtOrderItemCode option:selected").text();
    itemName = $("#txtOrderItemName").val();
    itemPrice = $("#txtOrderItemPrice").val();
    itemQtyOnHand = $("#txtOrderItemQtyOnHand").val();
    itemOrderQty = $("#txtOrderQty").val();

    let total = itemPrice * itemOrderQty;

    let raw = `<tr><td> ${itemCode} </td><td> ${itemName} </td><td> ${itemPrice} </td><td> ${itemOrderQty} </td><td> ${total} </td><td> <input id='btnEdit' class='btn btn-success btn-sm' value='Update' style="width: 75px"/> </td><td> <input id='btnDelete' class='btn btn-danger btn-sm' value='Delete' style="width: 75px"/> </td></tr>`;

    $("#addToCartTable").append(raw);

    manageDiscount();
    btnDeleteItemFromCart();
    //bindOrderClickEvent();

}

/* ------------------- Manage QTY ------------------- */
function minusQty(orderQty) {
    var minusQty = parseInt(orderQty);
    var manageQty = parseInt($("#txtOrderItemQtyOnHand").val());

    manageQty = manageQty - minusQty;

    $("#txtOrderItemQtyOnHand").val(manageQty);
    //bindOrderClickEvent();
}

var total = 0;

/* ------------------- Calculate Total ------------------- */
function manageTotal(amount) {
    total += amount;
    parseInt($("#total").text(total));

    manageDiscount();
}

/* ------------------- After Update Order Manage Total ------------------- */
function updateManageTotal(prvTotal, nowTotal) {
    total -= prvTotal;
    total += nowTotal;

    parseInt($("#total").text(total));

    manageDiscount();
}

/* ------------------- After Delete Order Manage Total ------------------- */
function deleteManageTotal(deleteTotal) {
    total -= deleteTotal;

    parseInt($("#total").text(total));

    manageDiscount();
}

/* ------------------- Calculate Discount ------------------- */
function manageDiscount() {
    var net = parseInt($("#total").text());
    var discount = 0;
    parseInt($("#txtDiscount").val(discount));

    if (net > 500 && net < 999) {
        discount = 2;
        parseInt($("#txtDiscount").val(discount));
    } else if (net >= 1000 && net < 2999) {
        discount = 4;
        parseInt($("#txtDiscount").val(discount));
    } else if (net >= 3000 && net < 4999) {
        discount = 5;
        parseInt($("#txtDiscount").val(discount));
    } else if (net >= 5000 && net < 9999) {
        discount = 8;
        parseInt($("#txtDiscount").val(discount));
    } else if (net >= 10000) {
        discount = 10;
        parseInt($("#txtDiscount").val(discount));
    }

    var subTotal = (net * discount) / 100;
    subTotal = net - subTotal;
    parseInt($("#subtotal").text(subTotal));

}

/* ------------------- Manage Quantity ------------------- */
function manageQuantity(prevQty, nowQty) {
    var prevQty = parseInt(prevQty);
    var nowQty = parseInt(nowQty);
    var availableQty = parseInt($("#txtOrderItemQtyOnHand").val());

    availableQty += prevQty;
    availableQty -= nowQty;

    $("#txtOrderItemQtyOnHand").val(availableQty);
}

/* ------------------- Delete Order at Cart ------------------- */
function btnDeleteItemFromCart() {
    $("#tblOrder tbody tr").off("click");
    $("#tblOrder tbody tr").on('click','#btnDelete',function () {
        let text = "Are you sure you want to remove this Item from cart?";

        if (confirm(text) == true) {
            $(this).parent().parent().remove();

            var delItemsTotal=parseInt($(this).parent().parent().children(':nth-child(5)').text());
            deleteManageTotal(delItemsTotal);

        } else {

        }
    });
}

/* ------------------- Clear Item Details Fields ------------------- */
function itemTextFieldClear() {
    loadItemCodes();
    $("#txtOrderItemQtyOnHand").val("");
    $("#txtOrderItemPrice").val("");
    $("#txtOrderItemName").val("");
    $("#txtOrderQty").val("");
    $("#txtOrderQty").css('border','1px solid gray');
    $("#errorOrderQty").text("");
}

/* ------------------- Clear Customer Details Fields ------------------- */
function customerTextFieldClear() {
    loadCustomerIds();
    $("#txtOrderCusName").val("");
    $("#txtOrderCusContact").val("");
    $("#txtOrderCusAddress").val("");
}

/* ------------------- Add New Order ------------------- */
$("#btnNew").click(function () {

    generateOrderId();
    itemTextFieldClear();
    customerTextFieldClear();

    $("#tblOrder tbody").empty();

    total = 0;
    parseInt($("#total").text("00.0"));
    parseInt($("#subtotal").text("00.0"));

    $("#cash").css('border', '1px solid gray');
    $("#cash").val("");

    $("#txtBalance").val("");
    $("#txtDiscount").val("");

});

/* ------------------- Calculate Balance When Cash Paid ------------------- */
$("#cash").keyup(function (event) {
    let balance = 0;
    let subtotal = parseInt($("#subtotal").text());
    let cash = parseInt($("#cash").val());

    balance = cash - subtotal;

    parseInt($("#txtBalance").val(balance));

});

/* ------------------- Place Order ------------------- */
$("#btnSubmitOrder").click(function () {

    var orderDetails = [];

    if (parseInt($("#subtotal").text()) > parseInt($("#cash").val())){
        alert("Please need more money");
        $("#txtCash").val('');
    }else{
        var discount = parseInt($("#total").text()) - parseInt($("#subtotal").text());


        for (let i = 0; i < $("#addToCartTable > tr").length; i++) {
            var OrderDetail = {
                oId : $("#txtOrderID").val(),
                itemCode : $("#addToCartTable > tr").children(':nth-child(1)')[i].innerText,
                qty : $("#addToCartTable > tr").children(':nth-child(4)')[i].innerText,
                price : $("#addToCartTable > tr").children(':nth-child(3)')[i].innerText,
                total : $("#addToCartTable > tr").children(':nth-child(5)')[i].innerText

            }
            orderDetails.push(OrderDetail);
        }

        var orderOb = {
            orderID:$("#txtOrderID").val(),
            cId:$("#txtOrderCusID option:selected").text(),
            orderDate:$("#txtOrderDate").val(),
            total:$("#total").text(),
            discount:discount.toString(),
            subTotal:$("#subtotal").text(),
            ODetail : orderDetails
        };

        if ($("#txtCash").val() == '') {
            alert("Please Enter Cash");
        }else {
            $.ajax({
                url: "http://localhost:8080/pos/orders",
                method: "POST",
                contentType: "application/json",
                data: JSON.stringify(orderOb),
                success: function (resp) {
                    itemTextFieldClear();
                    customerTextFieldClear();
                    $("#addToCartTable").empty();
                    alert("Successfully Added");

                },
                error: function (ob, textStatus, error) {
                    alert(textStatus);
                }
            });

        }
    }

});

