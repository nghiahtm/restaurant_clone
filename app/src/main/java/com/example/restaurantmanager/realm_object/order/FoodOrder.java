package  com.example.restaurantmanager.realm_object.order;

import com.example.restaurantmanager.realm_object.Option;

import java.util.UUID;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.RealmClass;

@RealmClass(embedded=true)
public class FoodOrder extends RealmObject {
    UUID id;
    String name;
    Double price=0.0;
    OptionOrder option;
    RealmList<ToppingOrder> toppings;
    Double totalPrice = 0.0;
    boolean isPrint = false;


    int count = 1;

    public int getFoodType() {
        return foodType;
    }

    public void setFoodType(int foodType) {
        this.foodType = foodType;
    }

    int foodType;

    public Boolean getPrint() {
        return isPrint;
    }

    public void setPrint(Boolean print) {
        isPrint = print;
    }

    public FoodOrder() {
    }
    public Double getTotalPrice(){
        double total = price;
        if(option != null){
            total += option.getPrice();
        }
        for (ToppingOrder topping: toppings) {
            total += topping.getPrice();
        }
        total = total*count;
        totalPrice = total;
        return totalPrice;
    }
    public Double getTotalPrice2(){

        return totalPrice;
    }
    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
        getTotalPrice();
    }

    public OptionOrder getOption() {
        return option;
    }

    public void setOption(OptionOrder size) {
        this.option = size;
    }


    public RealmList<ToppingOrder> getToppings() {
        return toppings;
    }

    public void setToppings(RealmList<ToppingOrder> toppings) {
        this.toppings = toppings;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
