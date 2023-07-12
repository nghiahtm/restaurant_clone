package  com.example.restaurantmanager.realm_object;

import java.util.UUID;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Category  extends RealmObject {
    @PrimaryKey
    UUID id;
    String name;
    Boolean status;
    RealmList<SubCategory> subCategories=null;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
    public Category() {
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RealmList<SubCategory> getSubCategories() {
        return subCategories;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public void setSubCategories(RealmList<SubCategory> subCategories) {
        this.subCategories = subCategories;
    }


}
