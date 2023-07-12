package  com.example.restaurantmanager.realm_object;

import java.util.UUID;

import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.LinkingObjects;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

@RealmClass(embedded=true)
public class SubCategory extends RealmObject {
    UUID id;
    String name;
    int color;
    Boolean status;
    @LinkingObjects("subCategory")
    private final RealmResults<Food> foods = null;

    public SubCategory() {
    }
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public RealmResults<Food> getFoods() {
        return foods;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }



}
