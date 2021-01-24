package io.github.backstreettoy.nullsafe.impl;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

import io.github.backstreettoy.nullsafe.functions.Action;
import org.assertj.core.api.Assertions;
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
public class SingleNullSafeTest {

    private SingleNullSafe singleNullSafe = SingleNullSafe.getInstance();

    @Test
    public void testIsNull() {
        assertThat(singleNullSafe.isNull(null)).isTrue();
        assertThat(singleNullSafe.isNull(new Object())).isFalse();
    }

    @Test
    public void testNotNullThen() {
        Consumer mockConsumer = mock(Consumer.class);
        Object obj = new Float(Math.random());
        singleNullSafe.notNullThen(obj, mockConsumer);
        ArgumentCaptor<Object> captor = ArgumentCaptor.forClass(Object.class);
        verify(mockConsumer, times(1)).accept(captor.capture());

        Object captureValue = captor.getValue();
        assertThat(captureValue).isEqualTo(obj);
    }

    @Test
    public void testNotNullThenWithNullAction() {
        Action mockAction = mock(Action.class);
        Consumer mockConsumer = mock(Consumer.class);
        singleNullSafe.notNullThen(null, mockConsumer, mockAction);
        verify(mockAction, times(1)).act();
    }

    @Test
    public void testNotNullThenNullActionIsNull() {
        Consumer mockConsumer = mock(Consumer.class);
        try {
            singleNullSafe.notNullThen(null, mockConsumer, null);
        } catch (NullPointerException e) {
            Assertions.fail("shoud allow null action is null");
        }
    }

    @Test(expected = NullPointerException.class)
    public void testNotNullThenNoConsumer() {
        singleNullSafe.notNullThen(new Object(), null);
    }

    @Test
    public void testNotNullThenByOptional() {
        Consumer mockConsumer = mock(Consumer.class);
        Object obj = new Float(Math.random());
        singleNullSafe.notNullThenByOptional(Optional.of(obj), mockConsumer);
        ArgumentCaptor<Object> captor = ArgumentCaptor.forClass(Object.class);
        verify(mockConsumer, times(1)).accept(captor.capture());

        Object captureValue = captor.getValue();
        assertThat(captureValue).isEqualTo(obj);
    }

    @Test
    public void testNotNullThenByOptionalNullActionIsNull() {
        Consumer mockConsumer = mock(Consumer.class);
        try {
            singleNullSafe.notNullThenByOptional(Optional.empty(), mockConsumer, null);
        } catch (NullPointerException e) {
            Assertions.fail("shoud allow null action is null");
        }
    }

    @Test
    public void testMapNotNull() {
        Function mockFunction = mock(Function.class);
        Float mockResult = new Float(Math.random());
        when(mockFunction.apply(anyObject())).thenReturn(mockResult);
        Optional result = singleNullSafe.mapNotNull(new Object(), mockFunction);
        assertThat(result).isNotNull();
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get()).isEqualTo(mockResult);
    }

    @Test
    public void testMapNotNullMapAsNull() {
        Function mockFunction = mock(Function.class);
        when(mockFunction.apply(anyObject())).thenReturn(null);
        Optional result = singleNullSafe.mapNotNull(new Object(), mockFunction);
        assertThat(result).isNotNull();
        assertThat(result.isPresent()).isFalse();
    }

    @Test(expected = NullPointerException.class)
    public void testMapNotNullNoFunction() {
        singleNullSafe.mapNotNull(new Object(), null);
    }

}
