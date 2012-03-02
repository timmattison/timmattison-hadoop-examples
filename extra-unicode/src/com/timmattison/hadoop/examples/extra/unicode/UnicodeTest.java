package com.timmattison.hadoop.examples.extra.unicode;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.UnsupportedEncodingException;

import org.apache.hadoop.io.Text;
import org.junit.Test;

public class UnicodeTest {
	/**
	 * The "A" character
	 */
	private static final Character CHARACTER_1 = '\u0041';
	private static final int CHARACTER_1_INT_VALUE = CHARACTER_1.charValue();

	/**
	 * The "ß" character
	 */
	private static final Character CHARACTER_2 = '\u00DF';
	private static final int CHARACTER_2_INT_VALUE = CHARACTER_2.charValue();

	/**
	 * The "東" character
	 */
	private static final Character CHARACTER_3 = '\u6771';
	private static final int CHARACTER_3_INT_VALUE = CHARACTER_3.charValue();

	/**
	 * The "А" character. Since this starts with 0xD8 it is a high (or leading)
	 * surrogate (http://unicode.org/faq/utf_bom.html#utf16-2).
	 */
	private static final Character CHARACTER_4 = '\uD801';
	private static final int CHARACTER_4_INT_VALUE = CHARACTER_4.charValue();

	/**
	 * The "" character. Since this starts with 0xDC it is a low (or trailing)
	 * surrogate (http://unicode.org/faq/utf_bom.html#utf16-2).
	 */
	private static final Character CHARACTER_5 = '\uDC00';
	private static final int CHARACTER_5_INT_VALUE = CHARACTER_5.charValue();

	/**
	 * The nonsense Unicode string "А" composed of characters 4 and 5. This is
	 * a surrogate pair (http://unicode.org/faq/utf_bom.html#utf16-2).
	 */
	private static final String CHARACTERS_4_AND_5 = CHARACTER_4.toString()
			+ CHARACTER_5.toString();

	/**
	 * The surrogate pair of characters 4 and 5 converted into their integer
	 * representation. This information was gathered from the "UTF-16 decoder"
	 * chart found here:
	 * http://en.wikipedia.org/wiki/UTF-16#Code_points_U.2B10000_to_U.2B10FFFF
	 */

	/**
	 * Use only the low byte of each character since the high bytes (0xD8 for
	 * character 4, and 0xDC for character 5) are just used to indicate to the
	 * decoder that this is a surrogate pair.
	 */
	private static final int CHARACTER_4_SHORT_INT_VALUE = CHARACTER_4_INT_VALUE & 0xFF;
	private static final int CHARACTER_5_SHORT_INT_VALUE = CHARACTER_5_INT_VALUE & 0xFF;

	/**
	 * Surrogate pair values start at 0x10000 and go up to 0x10FFFF
	 */
	private static final int SURROGATE_START = 0x10000;

	/**
	 * The surrogate pair value calculated by starting at the surrogate start
	 * value, adding character 5's value, and then adding character 4's value
	 * shifted left by 10 bits
	 */
	private static final int CHARACTERS_4_AND_5_SURROGATE_INT_VALUE = SURROGATE_START
			+ CHARACTER_5_SHORT_INT_VALUE + (CHARACTER_4_SHORT_INT_VALUE << 10);

	/**
	 * The nonsense Unicode string "Aß東А" composed of all of the characters
	 * together
	 */
	private static final String CANNED_TEXT = CHARACTER_1.toString()
			+ CHARACTER_2.toString() + CHARACTER_3.toString()
			+ CHARACTERS_4_AND_5;

	@Test
	public void string() throws UnsupportedEncodingException {
		// Use the canned text to do our tests
		String s = CANNED_TEXT;

		/**
		 * The String object's length method indicates that this string contains
		 * 5 characters
		 */
		assertThat(s.length(), is(5));

		/**
		 * If we get the raw UTF-8 bytes and check the length of that we'll see
		 * that the 5 characters we've used actually occupy 10 bytes
		 */
		assertThat(s.getBytes("UTF-8").length, is(10));

		/**
		 * The first character is at character offset 0
		 */
		assertThat(s.indexOf(CHARACTER_1), is(0));

		/**
		 * The second character is at character offset 1
		 */
		assertThat(s.indexOf(CHARACTER_2), is(1));

		/**
		 * The third character is at character offset 2
		 */
		assertThat(s.indexOf(CHARACTER_3), is(2));

		/**
		 * The fourth and fifth characters together are at character offset 3
		 */
		assertThat(s.indexOf(CHARACTERS_4_AND_5), is(3));

		/**
		 * The first character is at character offset 0
		 */
		assertThat(s.charAt(0), is(CHARACTER_1));

		/**
		 * The second character is at character offset 1
		 */
		assertThat(s.charAt(1), is(CHARACTER_2));

		/**
		 * The third character is at character offset 2
		 */
		assertThat(s.charAt(2), is(CHARACTER_3));

		/**
		 * The fourth character is at character offset 3
		 */
		assertThat(s.charAt(3), is(CHARACTER_4));

		/**
		 * The fifth character is at character offset 4
		 */
		assertThat(s.charAt(4), is(CHARACTER_5));

		/**
		 * The first character is at code point 0
		 */
		assertThat(s.codePointAt(0), is(CHARACTER_1_INT_VALUE));

		/**
		 * The second character is at code point 1
		 */
		assertThat(s.codePointAt(1), is(CHARACTER_2_INT_VALUE));

		/**
		 * The third character is at code point 2
		 */
		assertThat(s.codePointAt(2), is(CHARACTER_3_INT_VALUE));

		/**
		 * The surrogate pair of characters 4 and 5 is at code point 3
		 */
		assertThat(s.codePointAt(3), is(CHARACTERS_4_AND_5_SURROGATE_INT_VALUE));
	}

	@Test
	public void text() {
		// Use the canned text to do our tests
		Text t = new Text(CANNED_TEXT);

		/**
		 * The Text object's getLength method indicates that the text is 10
		 * bytes long
		 */
		assertThat(t.getLength(), is(10));

		/**
		 * The first character is at index 0
		 */
		assertThat(t.find(CHARACTER_1.toString()), is(0));

		/**
		 * The second character is at index 1
		 */
		assertThat(t.find(CHARACTER_2.toString()), is(1));

		/**
		 * The third character is at index 3, not index 2. We are looking at
		 * byte indexes here.
		 */
		assertThat(t.find(CHARACTER_3.toString()), is(3));

		/**
		 * The surrogate pair of characters 4 and 5 is at index 6, not index 4.
		 * We are looking at byte indexes here.
		 */
		assertThat(t.find(CHARACTERS_4_AND_5), is(6));

		/**
		 * The first character is at index 0
		 */
		assertThat(t.charAt(0), is(CHARACTER_1_INT_VALUE));

		/**
		 * The second character is at index 1
		 */
		assertThat(t.charAt(1), is(CHARACTER_2_INT_VALUE));

		/**
		 * The third character is at index 3, not index 2. We are looking at
		 * byte indexes here.
		 */
		assertThat(t.charAt(3), is(CHARACTER_3_INT_VALUE));

		/**
		 * The surrogate pair of characters 4 and 5 is at index 6, not index 4.
		 * We are looking at byte indexes here.
		 */
		assertThat(t.charAt(6), is(CHARACTERS_4_AND_5_SURROGATE_INT_VALUE));
	}
}