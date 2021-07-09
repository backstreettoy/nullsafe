package io.github.backstreettoy.nullsafe.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
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
    private static final IterableNullSafe INSTANCE = IterableNullSafe.getInstance();

    @Test
    public void testNotNullElementsThen() {
        Float value = new Float(Math.random());
        List<Float> list = new ArrayList<>();
        list.add(value);
        list.add(null);

        Consumer mockConsumer = mock(Consumer.class);

        INSTANCE.notNullElementsThen(list, mockConsumer);
        ArgumentCaptor<Float> captor = ArgumentCaptor.forClass(Float.class);
        verify(mockConsumer, times(1)).accept(captor.capture());
        List<Float> capturedValues = captor.getAllValues();
        assertThat(capturedValues).hasSize(1).containsExactly(value);
    }

    @Test(expected = NullPointerException.class)
    public void testNotNullElementsThenNoIterable() {
        INSTANCE.notNullElementsThen(null, mock(Consumer.class));
    }

    @Test(expected = NullPointerException.class)
    public void testNotNullElementsThenNoConsumer() {
        INSTANCE.notNullElementsThen(Collections.singletonList(1), null);
    }

    @Test
    public void testMapNotNullElements() {
        List<Float> list = new ArrayList<>();
        list.add(new Float(Math.random()));
        list.add(null);

        Function mockFunction = mock(Function.class);
        when(mockFunction.apply(anyObject())).thenReturn("RESULT");

        Stream<String> stream = INSTANCE.mapExistElements(list.stream(), mockFunction);
        List<String> resultList = stream.collect(Collectors.toList());
        verify(mockFunction, times(1)).apply(anyObject());
        assertThat(resultList).hasSize(1).containsExactly("RESULT");
    }

    @Test(expected = NullPointerException.class)
    public void testMapNotNullElementsNoStream() {
        INSTANCE.mapExistElements(null, mock(Function.class));
    }

    @Test(expected = NullPointerException.class)
    public void testNotNullElementsThenNoFunction() {
        INSTANCE.mapExistElements(Collections.singletonList(1).stream(), null);
    }

    @Test
    public void testCoalesce() {
        Optional<? super Object> optional = INSTANCE.coalesce();
        assertThat(optional.isPresent()).isFalse();

        optional = INSTANCE.coalesce(1);
        assertThat(optional.isPresent()).isTrue();
        assertThat(optional.get()).isEqualTo(1);

        optional = INSTANCE.coalesce((Object)null);
        assertThat(optional.isPresent()).isFalse();

        optional = INSTANCE.coalesce(null, 1);
        assertThat(optional.isPresent()).isTrue();
        assertThat(optional.get()).isEqualTo(1);

        optional = INSTANCE.coalesce(null, 1, null, 2, null);
        assertThat(optional.isPresent()).isTrue();
        assertThat(optional.get()).isEqualTo(1);
    }
}
