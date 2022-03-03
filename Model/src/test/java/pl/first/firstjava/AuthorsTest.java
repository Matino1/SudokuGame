package pl.first.firstjava;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class AuthorsTest {

    @Test
    void getContents() {
        Authors authors = new Authors();
        assertEquals(authors.getString("author1"), "Mateusz Strzelecki");
    }
}