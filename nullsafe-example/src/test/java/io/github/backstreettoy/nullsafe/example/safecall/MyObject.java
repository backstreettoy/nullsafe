package io.github.backstreettoy.nullsafe.example.safecall;

/**
 * @author backstreettoy
 */
public class MyObject {

    private String name;
    private ObjectRelation relation;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ObjectRelation getRelation() {
        return relation;
    }

    public void setRelation(ObjectRelation relation) {
        this.relation = relation;
    }
}
