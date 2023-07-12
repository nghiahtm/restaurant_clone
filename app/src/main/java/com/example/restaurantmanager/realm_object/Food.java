package  com.example.restaurantmanager.realm_object;
import java.util.UUID;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmField;

public class Food extends RealmObject {
    @PrimaryKey
    UUID id;
    String name;
    Double price;
    Boolean status;
    SubCategory subCategory;
    RealmList<Option> options;
    int foodType;

    public int getFoodType() {
        return foodType;
    }

    public void setFoodType(int foodType) {
        this.foodType = foodType;
    }
    public int getOptionsCount(){
        return  options.size();
    }

    public RealmList<Option> getOptions() {
        return options;
    }

    public void setOptions(RealmList<Option> options) {
        this.options = options;
    }

    public Food() {
        this.status = true;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public SubCategory getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(SubCategory subCategory) {
        this.subCategory = subCategory;
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
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }


    public void setStatus(Boolean status) {
        this.status = status;
    }

}
