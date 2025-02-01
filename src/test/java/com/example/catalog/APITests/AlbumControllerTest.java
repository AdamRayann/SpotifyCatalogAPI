package com.example.catalog.APITests;

import com.example.catalog.controller.AlbumController;
import com.example.catalog.model.Album;
import com.example.catalog.model.Track;
import com.example.catalog.services.DataSourceService;
import com.example.catalog.utils.SpotifyUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class AlbumControllerTest {
    private final String VALID_ID="5bnF93Rx87YqUBLSgjiMU8";


    @Mock
    private DataSourceService dataSourceService;

    @InjectMocks
    private AlbumController albumController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllAlbums() throws IOException {
        List<Album> albums = Arrays.asList(new Album(), new Album());
        when(dataSourceService.getAllAlbums()).thenReturn(albums);

        ResponseEntity<List<Album>> response = albumController.getAllAlbums();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void testGetAlbumById() throws IOException {
        Album album = new Album();
        when(dataSourceService.getAlbumById(VALID_ID)).thenReturn(album);

        ResponseEntity<Album> response = albumController.getAlbumById(VALID_ID);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(album, response.getBody());
    }

    @Test
    void testGetAlbumById_Invalid() {
        ResponseEntity<Album> response = albumController.getAlbumById("invalidId");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testCreateAlbum() throws IOException {
        Album album = new Album();
        when(dataSourceService.createAlbum(any(Album.class))).thenReturn(album);

        ResponseEntity<Album> response = albumController.createAlbum(album);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(album, response.getBody());
    }

    @Test
    void testUpdateAlbum() throws IOException {
        Album updatedAlbum = new Album();
        when(dataSourceService.updateAlbum(anyString(), any(Album.class))).thenReturn(updatedAlbum);

        ResponseEntity<Album> response = albumController.updateAlbum(VALID_ID, updatedAlbum);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedAlbum, response.getBody());
    }

    @Test
    void testUpdateAlbum_Invalid() {
        Album updatedAlbum = new Album();
        ResponseEntity<Album> response = albumController.updateAlbum("invalidId", updatedAlbum);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testDeleteAlbum() throws IOException {
        ResponseEntity<Void> response = albumController.deleteAlbum(VALID_ID);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(dataSourceService, times(1)).deleteAlbum(VALID_ID);
    }

    @Test
    void testDeleteAlbum_Invalid() {
        ResponseEntity<Void> response = albumController.deleteAlbum("invalidId");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testGetTracksByAlbum() throws IOException {
        List<Track> tracks = Collections.singletonList(new Track());
        when(dataSourceService.getTracksByAlbum(VALID_ID)).thenReturn(tracks);

        ResponseEntity<List<Track>> response = albumController.getTracksByAlbum(VALID_ID);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void testAddTrackToAlbum() throws IOException {
        Track track = new Track();
        when(dataSourceService.addTrackToAlbum(anyString(), any(Track.class))).thenReturn(track);

        ResponseEntity<Track> response = albumController.addTrackToAlbum(VALID_ID, track);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(track, response.getBody());
    }
}
