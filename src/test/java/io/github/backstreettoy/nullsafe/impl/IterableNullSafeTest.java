package io.github.backstreettoy.nullsafe.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
        ArgumentCaptor<Float> captor = ArgumentCaptor.forClass(Float.class);
        verify(mockConsumer, times(1)).accept(captor.capture());
        List<Float> capturedValues = captor.getAllValues();
        assertThat(capturedValues).hasSize(1).containsExactly(value);
    }

    @Test(expected = NullPointerException.class)
    public void testNotNullElementsThenNoIterable() {
        ITERABLE_ASSERTION.notNullElementsThen(null, mock(Consumer.class));
    }

    @Test(expected = NullPointerException.class)
    public void testNotNullElementsThenNoConsumer() {
        ITERABLE_ASSERTION.notNullElementsThen(Collections.singletonList(1), null);
    }

    @Test
    public void testMapNotNullElements() {
        List<Float> list = new ArrayList<>();
        list.add(new Float(Math.random()));
        list.add(null);

        Function mockFunction = mock(Function.class);
        when(mockFunction.apply(anyObject())).thenReturn("RESULT");

        Stream<String> stream = ITERABLE_ASSERTION.mapExistElements(list.stream(), mockFunction);
        List<String> resultList = stream.collect(Collectors.toList());
        verify(mockFunction, times(1)).apply(anyObject());
        assertThat(resultList).hasSize(1).containsExactly("RESULT");
    }

    @Test(expected = NullPointerException.class)
    public void testMapNotNullElementsNoStream() {
        ITERABLE_ASSERTION.mapExistElements(null, mock(Function.class));
    }

    @Test(expected = NullPointerException.class)
    public void testNotNullElementsThenNoFunction() {
        ITERABLE_ASSERTION.mapExistElements(Collections.singletonList(1).stream(), null);
    }
}
