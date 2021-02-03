package io.github.backstreettoy.nullsafe.impl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import io.github.backstreettoy.nullsafe.functions.Action;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 *
 * @author backstreettoy
 */
public class CompositeNullSafeTest {

    private CompositeNullSafe INSTANCE = CompositeNullSafe.getInstance();

    @Test
    public void testIfAllExistThen_AllExist() {
        Action mockAction = mock(Action.class);

        boolean allExist = INSTANCE.ifAllExistThen(mockAction, 1, 2, 3);
        assertThat(allExist).isTrue();
        verify(mockAction, Mockito.timeout(1)).act();
    }

    @Test(expected = NullPointerException.class)
    public void testIfAllExistThen_ActionIsNull() {
        boolean allExist = INSTANCE.ifAllExistThen(null, 1, 2, 3);
        Assertions.fail("Should not execute here");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIfAllExistThen_NoneOfParam() {
        Action mockAction = mock(Action.class);

        boolean allExist = INSTANCE.ifAllExistThen(mockAction);
        Assertions.fail("Shoud not execute to here!");
    }

    @Test
    public void testIfAllExistThen_NotAllExist() {
        Action mockAction = mock(Action.class);

        boolean allExist = INSTANCE.ifAllExistThen(mockAction, null);
        assertThat(allExist).isFalse();
        verify(mockAction, never()).act();

        reset(mockAction);
        allExist = INSTANCE.ifAllExistThen(mockAction, 1, null);
        assertThat(allExist).isFalse();
        verify(mockAction, never()).act();

        reset(mockAction);
        allExist = INSTANCE.ifAllExistThen(mockAction, null, null);
        assertThat(allExist).isFalse();
        verify(mockAction, never()).act();
    }

    @Test
    public void testIfAllExistThen_DifferentType() {
        Action mockAction = mock(Action.class);

        boolean allExist = INSTANCE.ifAllExistThen(mockAction, new Object(), 1, 2f, Collections.EMPTY_LIST);
        assertThat(allExist).isTrue();
        verify(mockAction, times(1)).act();

        reset(mockAction);
        allExist = INSTANCE.ifAllExistThen(mockAction, 1, new Object(), null, Collections.EMPTY_LIST);
        assertThat(allExist).isFalse();
        verify(mockAction, never()).act();
    }

    @Test
    public void testIfAllExistThenOrElse() {
        Action mockNoneIsNullAction = mock(Action.class);
        Action mockAnyIsNullAction = mock(Action.class);

        boolean allExist = INSTANCE.ifAllExistThenOrElse(
                mockNoneIsNullAction, mockAnyIsNullAction, null, null, 1);
        assertThat(allExist).isFalse();
        verify(mockNoneIsNullAction, never()).act();
        verify(mockAnyIsNullAction, times(1)).act();

        reset(mockNoneIsNullAction);
        reset(mockAnyIsNullAction);
        allExist = INSTANCE.ifAllExistThenOrElse(
                mockNoneIsNullAction, mockAnyIsNullAction, 1f, 2d, new Object(), Collections.EMPTY_LIST, new Object[]{});
        assertThat(allExist).isTrue();
        verify(mockNoneIsNullAction, times(1)).act();
        verify(mockAnyIsNullAction, never()).act();

        reset(mockNoneIsNullAction);
        reset(mockAnyIsNullAction);
        allExist = INSTANCE.ifAllExistThenOrElse(
                null, null, 1f, 2d, new Object(), Collections.EMPTY_LIST, new Object[]{});
        assertThat(allExist).isTrue();
        allExist = INSTANCE.ifAllExistThenOrElse(
                null, null, 1f, 2d, null, Collections.EMPTY_LIST, new Object[]{});
        assertThat(allExist).isFalse();
        verify(mockNoneIsNullAction, never()).act();
        verify(mockAnyIsNullAction, never()).act();
    }

    @Test
    public void testNamedIfAllExistThenOrElse() {
        Action mockNoneIsNullAction = mock(Action.class);
        Consumer mockAnyIsNullConsumer = mock(Consumer.class);

        // null value exist
        boolean allExist = INSTANCE.namedIfAllExistThenOrElse(
                mockNoneIsNullAction, mockAnyIsNullConsumer,
                Pair.of("A", 1), null, Pair.of("X", null),
                Pair.of("C", new Object()), Pair.of("D", null));
        assertThat(allExist).isFalse();
        verify(mockNoneIsNullAction, never()).act();
        ArgumentCaptor<List> anyIsNullParamCaptor = ArgumentCaptor.forClass(List.class);
        verify(mockAnyIsNullConsumer, times(1)).accept(anyIsNullParamCaptor.capture());
        List<String> nullValueKeys = anyIsNullParamCaptor.getValue();
        assertThat(nullValueKeys).containsExactly("X", "D");

        // only one null
        reset(mockNoneIsNullAction);
        reset(mockAnyIsNullConsumer);
        allExist = INSTANCE.namedIfAllExistThenOrElse(
                mockNoneIsNullAction, mockAnyIsNullConsumer,
                null);
        assertThat(allExist).isFalse();
        verify(mockNoneIsNullAction, never()).act();
        verify(mockAnyIsNullConsumer, times(1)).accept(anyObject());

        reset(mockNoneIsNullAction);
        reset(mockAnyIsNullConsumer);
        allExist = INSTANCE.namedIfAllExistThenOrElse(
                mockNoneIsNullAction, mockAnyIsNullConsumer,
                Pair.of("A", 1f), Pair.of("B", new Object()),
                Pair.of("C", new Object[]{}), Pair.of("D", Collections.EMPTY_LIST));
        assertThat(allExist).isTrue();
        verify(mockNoneIsNullAction, times(1)).act();
        verify(mockAnyIsNullConsumer, never()).accept(anyObject());

        reset(mockNoneIsNullAction);
        reset(mockAnyIsNullConsumer);
        allExist = INSTANCE.namedIfAllExistThenOrElse(
                null, null, Pair.of("A", 1), Pair.of("B", 2d));
        assertThat(allExist).isTrue();
        allExist = INSTANCE.namedIfAllExistThenOrElse(
                null, null, Pair.of("A", 1), Pair.of("B", null));
        assertThat(allExist).isFalse();
        verify(mockNoneIsNullAction, never()).act();
        verify(mockAnyIsNullConsumer, never()).accept(anyObject());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNamedIfAllExistThenOrElse_NoneOfParam() {
        Action mockNoneIsNullAction = mock(Action.class);
        Consumer mockAnyIsNullConsumer = mock(Consumer.class);
        mockNoneIsNullAction = mock(Action.class);
        mockAnyIsNullConsumer = mock(Consumer.class);
        INSTANCE.namedIfAllExistThenOrElse(
                mockNoneIsNullAction, mockAnyIsNullConsumer);
        Assertions.fail("Should not execute to here");
    }

    @Test
    public void testNamedIfAllExistThenOrElseByOptional_HasNullOrEmpty() {
        Action mockNoneIsNullAction = mock(Action.class);
        Consumer mockAnyIsNullConsumer = mock(Consumer.class);

        // null value exist
        boolean allExist = INSTANCE.namedIfAllExistThenOrElseByOptional(
                mockNoneIsNullAction, mockAnyIsNullConsumer,
                OptionalValuePair.of("A", Optional.of(1)), null, OptionalValuePair.of("X", Optional.empty()),
                OptionalValuePair.of("C", Optional.of(new Object())), OptionalValuePair.of("D", (Optional)null));
        assertThat(allExist).isFalse();
        verify(mockNoneIsNullAction, never()).act();
        ArgumentCaptor<List> anyIsNullParamCaptor = ArgumentCaptor.forClass(List.class);
        verify(mockAnyIsNullConsumer, times(1)).accept(anyIsNullParamCaptor.capture());
        List<String> nullValueKeys = anyIsNullParamCaptor.getValue();
        assertThat(nullValueKeys).containsExactly("X", "D");

        // only one null
        reset(mockNoneIsNullAction);
        reset(mockAnyIsNullConsumer);
        allExist = INSTANCE.namedIfAllExistThenOrElseByOptional(
                mockNoneIsNullAction, mockAnyIsNullConsumer,
                null);
        assertThat(allExist).isFalse();
        verify(mockNoneIsNullAction, never()).act();
        verify(mockAnyIsNullConsumer, times(1)).accept(anyObject());

        // two null
        reset(mockNoneIsNullAction);
        reset(mockAnyIsNullConsumer);
        allExist = INSTANCE.namedIfAllExistThenOrElseByOptional(
                mockNoneIsNullAction, mockAnyIsNullConsumer,
                null, null);
        assertThat(allExist).isFalse();
        verify(mockNoneIsNullAction, never()).act();
        verify(mockAnyIsNullConsumer, times(1)).accept(anyObject());

        // empty value
        reset(mockNoneIsNullAction);
        reset(mockAnyIsNullConsumer);
        allExist = INSTANCE.namedIfAllExistThenOrElseByOptional(
                mockNoneIsNullAction, mockAnyIsNullConsumer,
                OptionalValuePair.of("A", Optional.empty()));
        assertThat(allExist).isFalse();
        verify(mockNoneIsNullAction, never()).act();
        anyIsNullParamCaptor = ArgumentCaptor.forClass(List.class);
        verify(mockAnyIsNullConsumer, times(1)).accept(anyIsNullParamCaptor.capture());
        nullValueKeys = anyIsNullParamCaptor.getValue();
        assertThat(nullValueKeys).containsExactly("A");

        // empty value and null value
        reset(mockNoneIsNullAction);
        reset(mockAnyIsNullConsumer);
        allExist = INSTANCE.namedIfAllExistThenOrElseByOptional(
                mockNoneIsNullAction, mockAnyIsNullConsumer,
                OptionalValuePair.of("A", Optional.empty()), OptionalValuePair.of("B", null), null);
        assertThat(allExist).isFalse();
        verify(mockNoneIsNullAction, never()).act();
        anyIsNullParamCaptor = ArgumentCaptor.forClass(List.class);
        verify(mockAnyIsNullConsumer, times(1)).accept(anyIsNullParamCaptor.capture());
        nullValueKeys = anyIsNullParamCaptor.getValue();
        assertThat(nullValueKeys).containsExactly("A", "B");
    }

    @Test(expected = NullPointerException.class)
    public void testNamedIfAllExistThenOrElseByOptional_KeyIsNull() {
        Action mockNoneIsNullAction = mock(Action.class);
        Consumer mockAnyIsNullConsumer = mock(Consumer.class);
        boolean allExist = INSTANCE.namedIfAllExistThenOrElseByOptional(
                mockNoneIsNullAction, mockAnyIsNullConsumer,
                OptionalValuePair.of(null, Optional.of(1f)));
        Assertions.fail("should not execute to here!");
    }

    @Test
    public void testNamedIfAllExistThenOrElseByOptional_AllExist() {
        Action mockNoneIsNullAction = mock(Action.class);
        Consumer mockAnyIsNullConsumer = mock(Consumer.class);
        // many value with different types
        boolean allExist = INSTANCE.namedIfAllExistThenOrElseByOptional(
                mockNoneIsNullAction, mockAnyIsNullConsumer,
                OptionalValuePair.of("A", Optional.of(1f)),
                OptionalValuePair.of("B", Optional.of(new Object())),
                OptionalValuePair.of("C", Optional.of(new Object[]{})),
                OptionalValuePair.of("D", Optional.of(Collections.EMPTY_LIST)));
        assertThat(allExist).isTrue();
        verify(mockNoneIsNullAction, times(1)).act();
        verify(mockAnyIsNullConsumer, never()).accept(anyObject());

        // just one value
        reset(mockNoneIsNullAction);
        reset(mockAnyIsNullConsumer);
        allExist = INSTANCE.namedIfAllExistThenOrElseByOptional(
                mockNoneIsNullAction, mockAnyIsNullConsumer,
                OptionalValuePair.of("A", Optional.of(1f)));
        assertThat(allExist).isTrue();
        verify(mockNoneIsNullAction, times(1)).act();
        verify(mockAnyIsNullConsumer, never()).accept(anyObject());
    }

    @Test
    public void testNamedIfAllExistThenOrElseByOptional_CallbackIsNull() {
        Action mockNoneIsNullAction = mock(Action.class);
        Consumer mockAnyIsNullConsumer = mock(Consumer.class);

        boolean allExist = INSTANCE.namedIfAllExistThenOrElseByOptional(
                null, mockAnyIsNullConsumer,
                OptionalValuePair.of("A", Optional.of(1f)),
                OptionalValuePair.of("B", Optional.of(new Object())),
                OptionalValuePair.of("C", Optional.of(new Object[]{})),
                OptionalValuePair.of("D", Optional.of(Collections.EMPTY_LIST)));
        assertThat(allExist).isTrue();

        allExist = INSTANCE.namedIfAllExistThenOrElseByOptional(
                mockNoneIsNullAction, null,
                OptionalValuePair.of("A", Optional.of(1f)),
                OptionalValuePair.of("B", Optional.of(new Object())),
                OptionalValuePair.of("C", Optional.of(new Object[]{})),
                OptionalValuePair.of("D", Optional.of(Collections.EMPTY_LIST)));
        assertThat(allExist).isTrue();

        allExist = INSTANCE.namedIfAllExistThenOrElseByOptional(
                null, null,
                OptionalValuePair.of("A", Optional.of(1f)),
                OptionalValuePair.of("B", Optional.of(new Object())),
                OptionalValuePair.of("C", Optional.of(new Object[]{})),
                OptionalValuePair.of("D", Optional.of(Collections.EMPTY_LIST)));
        assertThat(allExist).isTrue();
    }

    @Test
    public void testIfAllExistThenByOptional_AllExist() {
        Action mockAction = mock(Action.class);

        boolean allExist = INSTANCE.ifAllExistThenByOptional(mockAction,
                Optional.of(1), Optional.of(2), Optional.of(3));
        assertThat(allExist).isTrue();
        verify(mockAction, Mockito.timeout(1)).act();
    }

    @Test(expected = NullPointerException.class)
    public void testIfAllExistThenByOptional_ActionIsNull() {
        boolean allExist = INSTANCE.ifAllExistThenByOptional(null,
                Optional.of(1), Optional.of(2), Optional.of(3));
        Assertions.fail("Should not execute here");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIfAllExistThenByOptional_NoneOfParam() {
        Action mockAction = mock(Action.class);

        boolean allExist = INSTANCE.ifAllExistThen(mockAction);
        Assertions.fail("Shoud not execute to here!");
    }

    @Test
    public void testIfAllExistThenByOptional_NotAllExist() {
        Action mockAction = mock(Action.class);

        boolean allExist = INSTANCE.ifAllExistThenByOptional(mockAction, null);
        assertThat(allExist).isFalse();
        verify(mockAction, never()).act();

        reset(mockAction);
        allExist = INSTANCE.ifAllExistThenByOptional(mockAction, Optional.of(1), null);
        assertThat(allExist).isFalse();
        verify(mockAction, never()).act();

        reset(mockAction);
        allExist = INSTANCE.ifAllExistThenByOptional(mockAction, null, null);
        assertThat(allExist).isFalse();
        verify(mockAction, never()).act();

        reset(mockAction);
        allExist = INSTANCE.ifAllExistThenByOptional(mockAction, Optional.of(1), Optional.empty());
        assertThat(allExist).isFalse();
        verify(mockAction, never()).act();
    }

    @Test
    public void testIfAllExistThenByOptional_DifferentType() {
        Action mockAction = mock(Action.class);

        boolean allExist = INSTANCE.ifAllExistThenByOptional(mockAction,
                Optional.of(new Object()), Optional.of(1), Optional.of(2f), Optional.of(Collections.EMPTY_LIST));
        assertThat(allExist).isTrue();
        verify(mockAction, times(1)).act();

        reset(mockAction);
        allExist = INSTANCE.ifAllExistThenByOptional(mockAction,
                Optional.of(1), Optional.of(new Object()), null, Optional.of(Collections.EMPTY_LIST));
        assertThat(allExist).isFalse();
        verify(mockAction, never()).act();
    }

    @Test
    public void testIfAllExistThenOrElseByOptional() {
        Action mockNoneIsNullAction = mock(Action.class);
        Action mockAnyIsNullAction = mock(Action.class);

        boolean allExist = INSTANCE.ifAllExistThenOrElseByOptional(
                mockNoneIsNullAction, mockAnyIsNullAction,
                Optional.empty(), null, Optional.of(1));
        assertThat(allExist).isFalse();
        verify(mockNoneIsNullAction, never()).act();
        verify(mockAnyIsNullAction, times(1)).act();

        reset(mockNoneIsNullAction);
        reset(mockAnyIsNullAction);
        allExist = INSTANCE.ifAllExistThenOrElseByOptional(
                mockNoneIsNullAction, mockAnyIsNullAction,
                Optional.of(1f), Optional.of(2d), Optional.of(new Object()),
                Optional.of(Collections.EMPTY_LIST), Optional.of(new Object[]{}));
        assertThat(allExist).isTrue();
        verify(mockNoneIsNullAction, times(1)).act();
        verify(mockAnyIsNullAction, never()).act();

        reset(mockNoneIsNullAction);
        reset(mockAnyIsNullAction);
        allExist = INSTANCE.ifAllExistThenOrElseByOptional(
                null, null,
                Optional.of(1f), Optional.of(2d), Optional.of(new Object()),
                Optional.of(Collections.EMPTY_LIST), Optional.of(new Object[]{}));
        assertThat(allExist).isTrue();
        allExist = INSTANCE.ifAllExistThenOrElseByOptional(
                null, null,
                Optional.of(1f), Optional.of(2d), Optional.empty(),
                Optional.of(Collections.EMPTY_LIST), Optional.of(new Object[]{}));
        assertThat(allExist).isFalse();
        verify(mockNoneIsNullAction, never()).act();
        verify(mockAnyIsNullAction, never()).act();
    }

}
