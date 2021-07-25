package io.github.backstreettoy.nullsafe.example.safecall;

/**
 * @author backstreettoy
 */
public class MyObject {

    private String name;
    private int type;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
