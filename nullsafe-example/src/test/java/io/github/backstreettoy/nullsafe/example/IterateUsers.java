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
                    .fallback(property("password"), "defaultPassword")
                    .get();

            userDescription.add(new StringBuilder()
                    .append(readOnlyUser.getId()).append("\t")
                    .append(readOnlyUser.getUserName()).append("\t")
                    .append(readOnlyUser.getPassword()).append("\t")
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

        User userTwo = new User();
        userTwo.setId(2L);
        userTwo.setUserName("userTwo");

        return Arrays.asList(userOne, userTwo);
    }
}
