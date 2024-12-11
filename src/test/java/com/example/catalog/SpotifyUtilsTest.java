package com.example.catalog;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static com.example.catalog.utils.SpotifyUtils.*;
import static org.junit.jupiter.api.Assertions.*;


//@Disabled("Should be enabled for Junit exercises")
public class SpotifyUtilsTest {

    @Test
    public void testValidId() {
        assertTrue(isValidId("6rqhFgbbKwnb9MLmUQDhG6")); // valid Spotify ID
        assertTrue(isValidId("1a2B3c4D5e6F7g8H9iJkL0mN")); // valid 22 character ID
        assertTrue(isValidId("a1b2C3d4E5f6G7h8I9jK0L1m2N")); // valid 30 character ID
    }

    @Test
    public void testInvalidId() {
        assertFalse(isValidId(null)); // null ID
        assertFalse(isValidId("")); // empty ID
        assertFalse(isValidId("shortID")); // too short ID (less than 15 characters)
        assertFalse(isValidId("thisIDiswaytoolongtobevaqqliqqqd")); // too long ID (more than 30 characters)
        assertFalse(isValidId("!@#$$%^&*()_+")); // invalid characters
        assertFalse(isValidId("1234567890abcdefGHIJKLMNO!@#")); // includes invalid characters
    }

    @Test
    public void testValidURI() {
        assertTrue(isValidURI("spotify:artist:1vCWHaC5f2uS3yhpwWbIA6"));
        assertTrue(isValidURI("spotify:album:4aawyAB9vmqN3uQ7FjRGTy"));
        assertTrue(isValidURI("spotify:track:6rqhFgbbKwnb9MLmUQDhG6"));
        assertTrue(isValidURI("spotify:playlist:37i9dQZF1DXcBWIGoYBM5M"));
        assertTrue(isValidURI("spotify:playlist:5AvwZVawapvyhJUIx71pdJ"));
    }
    @Test
    public void testInvalidURI() {
        assertFalse(isValidURI("spotify::6rqhFgbbKwnb9MLmUQDhG6"));
        assertFalse(isValidURI("spotify:wrongtype:6rqhFgbbKwnb9MLmUQDhG6"));
        assertFalse(isValidURI(null));
        assertFalse(isValidURI(""));
        assertFalse(isValidURI("spotify:track:"));
        assertFalse(isValidURI("spotify-track-6rqhFgbbKwnb9MLmUQDhG6"));
        assertFalse(isValidURI("spotify:track:!!@@##$$%%^^"));


    }


    @Test
    public void testGetSpotifyClient() {
        assertThrows(IllegalArgumentException.class,()-> getSpotifyClient("","1234"));
        assertThrows(IllegalArgumentException.class,()-> getSpotifyClient("1234",""));
        assertThrows(IllegalArgumentException.class,()-> getSpotifyClient("",""));

    }


}
