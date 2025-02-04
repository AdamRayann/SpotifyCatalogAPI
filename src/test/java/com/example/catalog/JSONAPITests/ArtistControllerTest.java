package com.example.catalog.JSONAPITests;

import com.example.catalog.Application;
import com.example.catalog.model.Album;
import com.example.catalog.model.Artist;
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

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class ArtistControllerTest {

    @Value("${server.port}")
    private int port;

    private final TestRestTemplate restTemplate = new TestRestTemplate();

    @Value("${JSONDataSourceService.baseData}")
    private String basePath;

    private String testArtistsPath;
    private String originalContent;

    private final String VALID_ID = "1Xyo4u8uXC1ZmMpatF05PJ";
    private final String DELETE_VALID_ID = "3TVXtAsR1Inumwj472S9r4";

    private String getBaseUrl() {
        return "http://localhost:" + port + "/artists";
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
        if (testArtistsPath != null && originalContent != null) {
            Files.writeString(Path.of(testArtistsPath), originalContent);
        }
    }

    @Test
    void testGetAllArtists() {
        ResponseEntity<Artist[]> response = restTemplate.getForEntity(getBaseUrl(), Artist[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testGetArtistById() {
        ResponseEntity<Artist> response = restTemplate.getForEntity(getBaseUrl() + "/" + VALID_ID, Artist.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testCreateArtist() {
        Artist newArtist = new Artist();
        newArtist.setId("2bnF93Rx87YqUBLSgjiMU0");
        newArtist.setName("New Test Artist");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Artist> request = new HttpEntity<>(newArtist, headers);

        ResponseEntity<Artist> response = restTemplate.postForEntity(getBaseUrl(), request, Artist.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("New Test Artist", response.getBody().getName());
    }

    @Test
    void testUpdateArtist() {
        Artist updatedArtist = new Artist();
        updatedArtist.setId(VALID_ID);
        updatedArtist.setName("Updated Artist Name");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Artist> request = new HttpEntity<>(updatedArtist, headers);

        ResponseEntity<Artist> response = restTemplate.exchange(
                getBaseUrl() + "/" + VALID_ID,
                HttpMethod.PUT,
                request,
                Artist.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Updated Artist Name", response.getBody().getName());
    }

    @Test
    void testDeleteArtist() {
        ResponseEntity<Void> response = restTemplate.exchange(
                getBaseUrl() + "/" + DELETE_VALID_ID,
                HttpMethod.DELETE,
                null,
                Void.class
        );

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void testGetAlbumsByArtist() {
        ResponseEntity<Album[]> response = restTemplate.getForEntity(getBaseUrl() + "/" + VALID_ID + "/albums", Album[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().length >= 1);
    }

    @Test
    void testGetPopularSongsByArtist() {
        ResponseEntity<Song[]> response = restTemplate.getForEntity(getBaseUrl() + "/" + VALID_ID + "/songs", Song[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().length >= 1);
    }

    @Test
    void testGetArtistById_NotFound() {
        ResponseEntity<Artist> response = restTemplate.getForEntity(getBaseUrl() + "/invalid_id", Artist.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
