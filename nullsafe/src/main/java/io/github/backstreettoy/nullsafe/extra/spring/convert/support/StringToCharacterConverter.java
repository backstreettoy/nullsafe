/*
 * Copyright 2002-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.backstreettoy.nullsafe.extra.spring.convert.support;

import io.github.backstreettoy.nullsafe.extra.spring.convert.converter.Converter;
import org.springframework.lang.Nullable;

/**
 * Converts a String to a Character.
 *
 * @author Keith Donald
 * @since 3.0
 */
final class StringToCharacterConverter implements Converter<String, Character> {

	@Override
	@Nullable
	public Character convert(String source) {
		if (source.isEmpty()) {
			return null;
		}
		if (source.length() > 1) {
			throw new IllegalArgumentException(
					"Can only convert a [String] with length of 1 to a [Character]; string value '" + source + "'  has length of " + source.length());
		}
		return source.charAt(0);
	}

}