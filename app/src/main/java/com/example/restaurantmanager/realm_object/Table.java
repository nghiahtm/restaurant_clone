package  com.example.restaurantmanager.realm_object;

import com.example.restaurantmanager.realm_object.order.Order;

import java.util.UUID;

import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.LinkingObjects;
import io.realm.annotations.PrimaryKey;

public class Table extends RealmObject {
    String name;
    boolean status;
    boolean isHaveOrder = false;
    String note="";
    Double totalBill = 0.0;
    @PrimaryKey
    UUID id;
    @LinkingObjects("table")
    private final RealmResults<Order> orders = null;

    public Double getTotalBill() {
        return totalBill;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setTotalBill(Double totalBill) {
        this.totalBill = totalBill;
    }

    public boolean isHaveOrder() {
        return isHaveOrder;
    }

    public void setHaveOrder(boolean haveOrder) {
        isHaveOrder = haveOrder;
    }


    public boolean isStatus() {
        return status;
    }

    public RealmResults<Order> getOrders() {
        return orders;
    }

    public Table() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
