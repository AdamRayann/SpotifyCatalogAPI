package com.example.catalog.APITests;

import com.example.catalog.controller.ArtistController;
import com.example.catalog.model.Album;
import com.example.catalog.model.Artist;
import com.example.catalog.model.Song;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ArtistControllerTest {
    private final String VALID_ID="5bnF93Rx87YqUBLSgjiMU8";
    @Mock
    private DataSourceService dataSourceService;

    @InjectMocks
    private ArtistController artistController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllArtists() throws IOException {
        Artist artist = new Artist();
        artist.setId(VALID_ID);
        when(dataSourceService.getAllArtists()).thenReturn(Collections.singletonList(artist));

        ResponseEntity<List<Artist>> response = artistController.getAllArtists();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void testGetArtistById() throws IOException {
        Artist artist = new Artist();
        artist.setId(VALID_ID);
        when(dataSourceService.getArtistById(VALID_ID)).thenReturn(artist);

        ResponseEntity<Artist> response = artistController.getArtistById(VALID_ID);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(artist, response.getBody());
    }

    @Test
    void testGetArtistById_NotFound() throws IOException {
        when(dataSourceService.getArtistById(VALID_ID)).thenReturn(null);

        ResponseEntity<Artist> response = artistController.getArtistById(VALID_ID);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testCreateArtist() throws IOException {
        Artist artist = new Artist();
        artist.setId(VALID_ID);
        when(dataSourceService.createArtist(artist)).thenReturn(artist);

        ResponseEntity<Artist> response = artistController.createArtist(artist);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(artist, response.getBody());
    }

    @Test
    void testUpdateArtist() throws IOException {
        Artist artist = new Artist();
        artist.setId(VALID_ID);
        when(dataSourceService.getArtistById(VALID_ID)).thenReturn(artist);
        when(dataSourceService.updateArtist(VALID_ID, artist)).thenReturn(artist);

        ResponseEntity<Artist> response = artistController.updateArtist(VALID_ID, artist);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(artist, response.getBody());
    }

    @Test
    void testDeleteArtist() throws IOException {
        Artist artist = new Artist();
        artist.setId(VALID_ID);
        when(dataSourceService.getArtistById(VALID_ID)).thenReturn(artist);

        ResponseEntity<Void> response = artistController.deleteArtist(VALID_ID);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void testGetAlbumsByArtist() throws IOException {
        Artist artist = new Artist();
        artist.setId(VALID_ID);
        Album album = new Album();
        album.setId(VALID_ID+"1");
        when(dataSourceService.getArtistById(VALID_ID)).thenReturn(artist);
        when(dataSourceService.getAlbumsByArtist(VALID_ID)).thenReturn(Collections.singletonList(album));

        ResponseEntity<List<Album>> response = artistController.getAlbumsByArtist(VALID_ID);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void testGetPopularSongsByArtist() throws IOException {
        Artist artist = new Artist();
        artist.setId(VALID_ID);
        Song song = new Song();
        song.setId(VALID_ID+"201");
        when(dataSourceService.getArtistById(VALID_ID)).thenReturn(artist);
        when(dataSourceService.getPopularSongsByArtist(VALID_ID)).thenReturn(Collections.singletonList(song));

        ResponseEntity<List<Song>> response = artistController.getPopularSongsByArtist(VALID_ID);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void testGetArtistById_Invalid() {
        ResponseEntity<Artist> response = artistController.getArtistById("invalid_id");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
