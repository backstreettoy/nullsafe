package io.github.backstreettoy.nullsafe.example;

import io.github.backstreettoy.nullsafe.example.dataobject.User;
import io.github.backstreettoy.nullsafe.impl.NullSafeWrapper;
import io.github.backstreettoy.nullsafe.impl.matchers.Matchers;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static io.github.backstreettoy.nullsafe.impl.matchers.Matchers.property;

public class IterateUsers {

    @Test
    public void testInterateUsers() {
        List<User> users = queryUsers();
        List<String> userDescription = new ArrayList<>();
        for (User user : users) {
            User readOnlyUser = NullSafeWrapper.wrapRecursively(user)
                    .fallback(property("optionalName"), "unknown")
                    .fallback(property("level"), 0)
                    // TODO java.lang.ClassCastException: java.lang.Integer cannot be cast to java.lang.Long
//                    .fallback(property("score"), 100)
                    .get();

            userDescription.add(new StringBuilder()
                    .append(readOnlyUser.getId()).append("\t")
                    .append(readOnlyUser.getUserName()).append("\t")
                    .append(readOnlyUser.getLevel()).append("\t")
                    .append(readOnlyUser.getOptionalName()).append("\t")
                    .append(readOnlyUser.getScore()).append("\t").toString());
        }

        userDescription.forEach(System.out::println);
    }

    private static List<User> queryUsers() {
        User userOne = new User();
        userOne.setId(1L);
        userOne.setUserName("userOne");
        // null indicates that this field has no valid value.
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
