package io.github.backstreettoy.nullsafe.example.safecall;

import org.junit.Test;

import io.github.backstreettoy.nullsafe.impl.SafeCallWrapper;
import static io.github.backstreettoy.nullsafe.impl.SafeCallWrapper.eval;

/**
 * @author backstreettoy
 */
public class ListObjects {

    @Test
    public void testListObjects() {

        MyObject upperObject = new MyObject();
        upperObject.setName("Upper");
        MyObject bottomObject = new MyObject();
        bottomObject.setName("Bottom");
        MyObject leftObject = new MyObject();
        leftObject.setName("");

        ObjectRelation relationOfUpper = new ObjectRelation();
        relationOfUpper.setBottom(bottomObject);
        relationOfUpper.setLeft(leftObject);
        upperObject.setRelation(relationOfUpper);
        ObjectRelation relationOfBottom = new ObjectRelation();
        relationOfBottom.setUpper(upperObject);
        relationOfBottom.setLeft(leftObject);
        bottomObject.setRelation(relationOfBottom);

        SafeCallWrapper<MyObject> wrapper = new SafeCallWrapper<>(upperObject);
        wrapper.throwExceptionWhenWrapMethodFail();
        MyObject upperObjectProxy = wrapper.get();
        System.out.println(eval(upperObjectProxy.getRelation().getLeft().getName()));
        System.out.println(eval(upperObjectProxy.getRelation().getLeft().getRelation()));
        System.out.println(eval(upperObjectProxy.getRelation().getBottom().getName()));
        System.out.println(eval(upperObjectProxy.getRelation().getRight().getName()));
    }
}
