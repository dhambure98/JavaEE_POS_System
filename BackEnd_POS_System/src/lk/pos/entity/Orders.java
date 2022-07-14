package lk.pos.entity;

import java.util.Date;

/**
 * @author : A.D.Liyanage
 * @since : 0.1.0
 **/

public class Orders {
    private String orderId;
    private String cId;
    private Date orderDate;
    private double total;
    private double discount;
    private double subTotal;

    public Orders() {
    }

    public Orders(String orderId, String cId, Date orderDate, double total, double discount, double subTotal) {
        this.orderId = orderId;
        this.cId = cId;
        this.orderDate = orderDate;
        this.total = total;
        this.discount = discount;
        this.subTotal = subTotal;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getcId() {
        return cId;
    }

    public void setcId(String cId) {
        this.cId = cId;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(double subTotal) {
        this.subTotal = subTotal;
    }
}
