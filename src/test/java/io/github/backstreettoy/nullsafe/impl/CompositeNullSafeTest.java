package io.github.backstreettoy.nullsafe.impl;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 *
 * @author backstreettoy
 */
public class CompositeNullSafeTest {

    private CompositeNullSafe INSTANCE = CompositeNullSafe.getInstance();

    @Test
    public void testCoalesce() {
        Optional<? super Object> optional = INSTANCE.coalesce();
        assertThat(optional.isPresent()).isFalse();

        optional = INSTANCE.coalesce(1);
        assertThat(optional.isPresent()).isTrue();
        assertThat(optional.get()).isEqualTo(1);

        optional = INSTANCE.coalesce(null);
        assertThat(optional.isPresent()).isFalse();

        optional = INSTANCE.coalesce(null, 1);
        assertThat(optional.isPresent()).isTrue();
        assertThat(optional.get()).isEqualTo(1);

        optional = INSTANCE.coalesce(null, 1, null, 2, null);
        assertThat(optional.isPresent()).isTrue();
        assertThat(optional.get()).isEqualTo(1);
    }
}
