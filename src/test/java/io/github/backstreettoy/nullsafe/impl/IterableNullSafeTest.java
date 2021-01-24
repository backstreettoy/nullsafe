package io.github.backstreettoy.nullsafe.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.mock;

/**
 *
 * @author backstreettoy
 */
public class IterableNullSafeTest {
    private static final IterableNullSafe ITERABLE_ASSERTION = IterableNullSafe.getInstance();

    @Test
    public void testNotNullElementsThen() {
        Float value = new Float(Math.random());
        List<Float> list = new ArrayList<>();
        list.add(value);
        list.add(null);

        Consumer mockConsumer = mock(Consumer.class);

        ITERABLE_ASSERTION.notNullElementsThen(list, mockConsumer);

    }
}
