package org.hibernate.bugs;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class MyFieldEntity {

    @Id
    public String id;

    public String myfield;
}
