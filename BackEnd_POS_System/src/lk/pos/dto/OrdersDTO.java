package lk.pos.dto;

import java.util.ArrayList;
import java.util.Date;

/**
 * @author : A.D.Liyanage
 * @since : 0.1.0
 **/

public class OrdersDTO {
    private String orderId;
    private String cId;
    private Date orderDate;
    private double total;
    private double discount;
    private double subTotal;
    private ArrayList<OrderDetailsDTO> orderDetail;

    public OrdersDTO(String orderId, String cId, Date orderDate, double total, double discount, double subTotal, ArrayList<OrderDetailsDTO> orderDetail) {
        this.orderId = orderId;
        this.cId = cId;
        this.orderDate = orderDate;
        this.total = total;
        this.discount = discount;
        this.subTotal = subTotal;
        this.orderDetail = orderDetail;
    }

    public OrdersDTO(String orderId, String cId, Date orderDate, double total, double discount, double subTotal) {
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

    public ArrayList<OrderDetailsDTO> getOrderDetail() {
        return orderDetail;
    }

    public void setOrderDetail(ArrayList<OrderDetailsDTO> orderDetail) {
        this.orderDetail = orderDetail;
    }

    @Override
    public String toString() {
        return "OrdersDTO{" +
                "orderId='" + orderId + '\'' +
                ", cId='" + cId + '\'' +
                ", orderDate=" + orderDate +
                ", total=" + total +
                ", discount=" + discount +
                ", subTotal=" + subTotal +
                ", orderDetail=" + orderDetail +
                '}';
    }
}
