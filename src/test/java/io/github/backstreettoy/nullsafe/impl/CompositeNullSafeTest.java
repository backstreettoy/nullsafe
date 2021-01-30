package io.github.backstreettoy.nullsafe.impl;

import java.util.Collections;
import java.util.Optional;

import io.github.backstreettoy.nullsafe.functions.Action;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
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
    public void testIfAllExistThenAllExist() {
        Action mockAction = mock(Action.class);

        boolean allExist = INSTANCE.ifAllExistThen(mockAction, 1, 2, 3);
        assertThat(allExist).isTrue();
        verify(mockAction, Mockito.timeout(1)).act();
    }

    @Test(expected = NullPointerException.class)
    public void testIfAllExistThenActionIsNull() {
        boolean allExist = INSTANCE.ifAllExistThen(null, 1, 2, 3);
        Assertions.fail("Should not execute here");
    }

    @Test
    public void testIfAllExistThenNotAllExist() {
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
    public void testIfAllExistThenDifferentType() {
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
    public void testIfAllExistThenOrElseKeyValue() {

    }
}
