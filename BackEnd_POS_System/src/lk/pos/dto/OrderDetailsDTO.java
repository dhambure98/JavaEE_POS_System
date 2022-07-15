package lk.pos.dto;

/**
 * @author : A.D.Liyanage
 * @since : 0.1.0
 **/

public class OrderDetailsDTO {
    private String oId;
    private String iCode;
    private int oQty;
    private double price;
    private double total;

    public OrderDetailsDTO() {
    }

    public OrderDetailsDTO(String oId, String iCode, int oQty, double price, double total) {
        this.oId = oId;
        this.iCode = iCode;
        this.oQty = oQty;
        this.price = price;
        this.total = total;
    }

    public String getoId() {
        return oId;
    }

    public void setoId(String oId) {
        this.oId = oId;
    }

    public String getiCode() {
        return iCode;
    }

    public void setiCode(String iCode) {
        this.iCode = iCode;
    }

    public int getoQty() {
        return oQty;
    }

    public void setoQty(int oQty) {
        this.oQty = oQty;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "OrderDetailsDTO{" +
                "oId='" + oId + '\'' +
                ", iCode='" + iCode + '\'' +
                ", oQty=" + oQty +
                ", price=" + price +
                ", total=" + total +
                '}';
    }
}
