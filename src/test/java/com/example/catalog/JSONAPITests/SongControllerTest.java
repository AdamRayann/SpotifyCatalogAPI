package com.example.catalog.JSONAPITests;

import com.example.catalog.Application;
import com.example.catalog.model.Song;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ActiveProfiles("test")
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class SongControllerTest {

    @Value("${server.port}")
    private int port;
    private String originalContent;
    private String testSongsPath;

    private final TestRestTemplate restTemplate = new TestRestTemplate();
    private final String VALID_ID = "5aAx2yezTd8zXrkmtKl66Z";
    private final String VALID_ID_DELETE = "3KkXRkHbMCARz0aVfEt68P";


    private String getBaseUrl() {
        return "http://localhost:" + port + "/songs";
    }

    @BeforeEach
    void setUp() throws IOException {
        Path path = Paths.get("src/main/resources/test_data/popular_artists.json");
        if (!Files.exists(path)) {
            throw new IOException("File not found: " + path);
        }
        originalContent = Files.readString(path);

    }


    @AfterEach
    void tearDown() throws IOException {
        if (testSongsPath != null && originalContent != null) {
            Files.writeString(Path.of(testSongsPath), originalContent);
        }
    }

    @Test
    void getAllSongs() {
        ResponseEntity<Song[]> response = restTemplate.getForEntity(getBaseUrl(), Song[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void getSongById() {
        ResponseEntity<Song> response = restTemplate.getForEntity(getBaseUrl() + "/" + VALID_ID, Song.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void getSongById_Invalid() {
        ResponseEntity<Song> response = restTemplate.getForEntity(getBaseUrl() + "/invalid-id", Song.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void createSong() {
        Song newSong = new Song(VALID_ID + "1", "New Song");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Song> request = new HttpEntity<>(newSong, headers);

        ResponseEntity<Song> response = restTemplate.postForEntity(getBaseUrl(), request, Song.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("New Song", response.getBody().getName());
    }

    @Test
    void createSong_Invalid() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Song> request = new HttpEntity<>(null, headers);

        ResponseEntity<Song> response = restTemplate.postForEntity(getBaseUrl(), request, Song.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void updateSong() {
        Song updatedSong = new Song(VALID_ID, "Updated Song");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Song> request = new HttpEntity<>(updatedSong, headers);

        ResponseEntity<Song> response = restTemplate.exchange(
                getBaseUrl() + "/" + VALID_ID,
                HttpMethod.PUT,
                request,
                Song.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Updated Song", response.getBody().getName());
    }

    @Test
    void updateSong_Invalid() {
        Song updatedSong = new Song(VALID_ID, "Updated Song");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Song> request = new HttpEntity<>(updatedSong, headers);

        ResponseEntity<Song> response = restTemplate.exchange(
                getBaseUrl() + "/invalid-id",
                HttpMethod.PUT,
                request,
                Song.class
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void deleteSong() {
        ResponseEntity<Void> response = restTemplate.exchange(
                getBaseUrl() + "/" + VALID_ID_DELETE,
                HttpMethod.DELETE,
                null,
                Void.class
        );

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void deleteSong_Invalid() {
        ResponseEntity<Void> response = restTemplate.exchange(
                getBaseUrl() + "/invalid-id",
                HttpMethod.DELETE,
                null,
                Void.class
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
