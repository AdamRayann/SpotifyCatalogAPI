package com.example.catalog.JSONAPITests;

import com.example.catalog.Application;
import com.example.catalog.model.Album;
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

@ActiveProfiles("test")
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class AlbumControllerTest {

    @Value("${server.port}")
    private int port;

    private final TestRestTemplate restTemplate = new TestRestTemplate();
    private Path testAlbumsPath;
    private String originalContent;
    private final String validId = "5658aM19fA3JVwTK6eQX70";
    private final String deleteValidId = "4yP0hdKOZPNshxUOjY0cZj";

    private String getBaseUrl() {
        return "http://localhost:" + port + "/albums";
    }

    @BeforeEach
    void setUp() throws IOException {
        Path path = Paths.get("src/main/resources/test_data/albums.json");
        if (!Files.exists(path)) {
            throw new IOException("File not found: " + path);
        }
        originalContent = Files.readString(path);

    }


    @AfterEach
    void tearDown() throws IOException {
        if (testAlbumsPath != null && originalContent != null) {
            Files.writeString(testAlbumsPath, originalContent);
        }
    }

    @Test
    void testGetAllAlbums() {
        ResponseEntity<Album[]> response = restTemplate.getForEntity(getBaseUrl(), Album[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testGetAlbumById() {
        ResponseEntity<Album> response = restTemplate.getForEntity(getBaseUrl() + "/" + validId, Album.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testCreateAlbum() {
        Album newAlbum = new Album();
        newAlbum.setId("2bnF93Rx87YqUBLSgjiMU0");
        newAlbum.setName("New Test Album");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Album> request = new HttpEntity<>(newAlbum, headers);

        ResponseEntity<Album> response = restTemplate.postForEntity(getBaseUrl(), request, Album.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("New Test Album", response.getBody().getName());
    }

    @Test
    void testUpdateAlbum() {
        Album updatedAlbum = new Album();
        updatedAlbum.setId(validId);
        updatedAlbum.setName("Updated Album Name");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Album> request = new HttpEntity<>(updatedAlbum, headers);

        ResponseEntity<Album> response = restTemplate.exchange(
                getBaseUrl() + "/" + validId,
                HttpMethod.PUT,
                request,
                Album.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Updated Album Name", response.getBody().getName());
    }

    @Test
    void testDeleteAlbum() {
        ResponseEntity<Void> response = restTemplate.exchange(
                getBaseUrl() + "/" + deleteValidId,
                HttpMethod.DELETE,
                null,
                Void.class
        );

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}
