package irt.web.bean;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class EncoderTest {

	@Test
	void test() {

		String initial = "Oleksandr";
		final String encode = Encoder.encode(initial);
		assertNotEquals(initial, encode);
		assertEquals(initial, Encoder.decode(encode));
	}

}
