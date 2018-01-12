package org.hibernate.bugs;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class MyMethodEntity {

    private String id;

    private String myfield;

    @Id
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMyfield() {
        return myfield;
    }

    public void setMyfield(String myfield) {
        this.myfield = myfield;
    }
}
