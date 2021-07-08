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
        boolean notNull = singleNullSafe.notNullThen(obj, mockConsumer);
        assertThat(notNull).isTrue();
        ArgumentCaptor<Object> captor = ArgumentCaptor.forClass(Object.class);
        verify(mockConsumer, times(1)).accept(captor.capture());

        Object captureValue = captor.getValue();
        assertThat(captureValue).isEqualTo(obj);
    }

    @Test
    public void testNotNullThenWithNullAction() {
        Action whenNullAction = mock(Action.class);
        Consumer notNullConsumer = mock(Consumer.class);
        boolean notNull = singleNullSafe.notNullThenOrElse(null, notNullConsumer, whenNullAction);
        assertThat(notNull).isFalse();
        verify(whenNullAction, times(1)).act();
    }

    @Test
    public void testNotNullThenNullActionIsNull() {
        Consumer mockConsumer = mock(Consumer.class);
        try {
            singleNullSafe.notNullThenOrElse(null, mockConsumer, null);
        } catch (NullPointerException e) {
            Assertions.fail("shoud allow nullAction is null");
        }
    }

    @Test(expected = NullPointerException.class)
    public void testNotNullThenNoConsumer() {
        singleNullSafe.notNullThen(new Object(), null);
    }

    @Test
    public void testNotNullThenByOptional() {
        Consumer notNullAction = mock(Consumer.class);
        Object obj = new Float(Math.random());
        boolean notNull = singleNullSafe.notNullThenByOptional(Optional.of(obj), notNullAction);
        assertThat(notNull).isTrue();
        ArgumentCaptor<Object> captor = ArgumentCaptor.forClass(Object.class);
        verify(notNullAction, times(1)).accept(captor.capture());

        Object captureValue = captor.getValue();
        assertThat(captureValue).isEqualTo(obj);
    }

    @Test
    public void testNotNullThenByOptionalNullActionIsNull() {
        Consumer notNullConsumer = mock(Consumer.class);
        Action nullAction = mock(Action.class);
        try {
            boolean notNull = singleNullSafe.notNullThenByOptional(Optional.empty(), notNullConsumer, null);
            assertThat(notNull).isFalse();
            notNull = singleNullSafe.notNullThenByOptional(Optional.of(1), null, nullAction);
            assertThat(notNull).isTrue();
        } catch (NullPointerException e) {
            Assertions.fail("shoud allow null action is null");
        }
    }

    @Test
    public void testMapNotNull() {
        Function mockFunction = mock(Function.class);
        Float mockResult = new Float(Math.random());
        when(mockFunction.apply(anyObject())).thenReturn(Optional.of(mockResult));
        Optional<Float> result = singleNullSafe.mapNotNull(new Object(), mockFunction);
        assertThat(result).isNotNull();
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get()).isEqualTo(mockResult);
    }

    @Test
    public void testMapNotNullMapAsEmpty() {
        Function mockFunction = mock(Function.class);
        when(mockFunction.apply(anyObject())).thenReturn(Optional.empty());
        Optional<Object> result = singleNullSafe.mapNotNull(new Object(), mockFunction);
        assertThat(result).isNotNull();
        assertThat(result.isPresent()).isFalse();
    }

    @Test(expected = NullPointerException.class)
    public void testMapNotNullNoFunction() {
        singleNullSafe.mapNotNull(new Object(), null);
    }

}
