package  com.example.restaurantmanager.realm_object.order;

import com.example.restaurantmanager.realm_object.Table;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class Order extends RealmObject {
    @PrimaryKey
    UUID id;
    RealmList<SubOrder> subOrders;
    Table table;
    int status;
    Date orderTime;
    Date payTime;


    boolean isPay = false;

    public void setTotalBill(Double totalBill) {
        this.totalBill = totalBill;
    }

    Double totalBill = 0.0;

    public Order() {
    }
    public boolean isHaveFood(){
        for (SubOrder subOrder:subOrders) {
            if(subOrder.getFoodOrders().size() > 0){
                return true;
            }
        }
        return false;
    }

    public Double getTotalBill(){
        Double total = 0.0;
        for (SubOrder subOrder:subOrders) {
            total += subOrder.getTotalBill();
        }
        this.totalBill = total;
        return total;

    }

    public Double getTotalBill2(){

        return totalBill;

    }
    public RealmList<SubOrder> getSubOrders() {
        return subOrders;
    }

    public boolean isPay() {
        return isPay;
    }

    public void setPay(boolean pay) {
        isPay = pay;
    }
    public void setSubOrders(RealmList<SubOrder> subOrders) {
        this.subOrders = subOrders;
    }
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }
}
