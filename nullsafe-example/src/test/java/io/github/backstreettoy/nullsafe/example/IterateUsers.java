package io.github.backstreettoy.nullsafe.example;

import io.github.backstreettoy.nullsafe.example.dataobject.User;
import io.github.backstreettoy.nullsafe.impl.NullSafeWrapper;
import io.github.backstreettoy.nullsafe.impl.field.fallback.handlers.AbstractFieldHandler;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static io.github.backstreettoy.nullsafe.impl.field.fallback.handlers.AbstractFieldHandler.FallbackResult.of;
import static io.github.backstreettoy.nullsafe.impl.field.fallback.handlers.FieldHandlers.custom;
import static io.github.backstreettoy.nullsafe.impl.field.fallback.handlers.FieldHandlers.value;
import static io.github.backstreettoy.nullsafe.impl.matchers.Matchers.property;
import static io.github.backstreettoy.nullsafe.impl.matchers.Matchers.propertyMatch;

public class IterateUsers {

    @Test
    public void testInterateUsers() {
        List<User> users = queryUsers();
        List<String> userDescription = new ArrayList<>();
        for (User user : users) {
            User readOnlyUser = NullSafeWrapper.wrapRecursively(user)
                    .fallback(property("optionalName"), value("Unknown"))
                    .fallback(property("level"), value(0))
                    // You could match a varity of properties by regex.
                    .fallback(propertyMatch(".+Description"),
                            custom((obj, method, field)
                                    -> of("No " + field.replace("Description", "") + " content")))
                    // TODO java.lang.ClassCastException: java.lang.Integer cannot be cast to java.lang.Long
                    .fallback(property("score"), value(100))
                    .fallback(property("age"), value(10))
                    .get();

//            userDescription.add(new StringBuilder()
//                    .append(readOnlyUser.getId()).append("\t")
//                    .append(readOnlyUser.getUserName()).append("\t")
//                    .append(readOnlyUser.getLevel()).append("\t")
//                    .append(readOnlyUser.getOptionalName()).append("\t")
//                    .append(readOnlyUser.getScore()).append("\t").toString());
            userDescription.add(readOnlyUser.toString());
        }

        userDescription.forEach(System.out::println);
    }

    private static List<User> queryUsers() {
        User userOne = new User();
        userOne.setId(1L);
        userOne.setUserName("userOne");
        // null indicates that this field has no valid of.
        userOne.setLevel(null);
        userOne.setScore(null);

        User userTwo = new User();
        userTwo.setId(2L);
        userTwo.setUserName("userTwo");
        userTwo.setLevel(2);
        userTwo.setOptionalName("Jack");
        userTwo.setScore(100L);

        return Arrays.asList(userOne, userTwo);
    }
}
