package com.example.diploma;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class DiplomaApplicationTests {

    @Test
    void applicationClassIsInstantiable() {
        assertDoesNotThrow(DiplomaApplication::new);
    }

}
