package com.example.catalog.APITests;

import com.example.catalog.controller.SongController;
import com.example.catalog.model.Song;
import com.example.catalog.services.DataSourceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class SongControllerTest {
    private final String VALID_ID="5bnF93Rx87YqUBLSgjiMU8";

    @Mock
    private DataSourceService dataSourceService;

    @InjectMocks
    private SongController songController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllSongs() throws IOException {
        List<Song> songs = Arrays.asList(new Song(VALID_ID, "Song1"), new Song(VALID_ID+1, "Song2"));
        when(dataSourceService.getAllSongs()).thenReturn(songs);

        ResponseEntity<List<Song>> response = songController.getAllSongs();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(songs, response.getBody());
    }

    @Test
    void getSongById() throws IOException {
        Song song = new Song(VALID_ID, "Song1");
        when(dataSourceService.getSongById(VALID_ID)).thenReturn(song);

        ResponseEntity<Song> response = songController.getSongById(VALID_ID);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(song, response.getBody());
    }

    @Test
    void getSongById_Invalid() {
        ResponseEntity<Song> response = songController.getSongById("invalid-id");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void createSong() throws IOException {
        Song song = new Song(VALID_ID, "New Song");
        when(dataSourceService.createSong(song)).thenReturn(song);

        ResponseEntity<Song> response = songController.createSong(song);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(song, response.getBody());
    }

    @Test
    void createSong_Invalid() {
        ResponseEntity<Song> response = songController.createSong(null);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void updateSong() throws IOException {
        Song updatedSong = new Song(VALID_ID, "Updated Song");
        when(dataSourceService.updateSong(VALID_ID, updatedSong)).thenReturn(updatedSong);

        ResponseEntity<Song> response = songController.updateSong(VALID_ID, updatedSong);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedSong, response.getBody());
    }

    @Test
    void updateSong_Invalid() {
        Song updatedSong = new Song(VALID_ID, "Updated Song");
        ResponseEntity<Song> response = songController.updateSong("invalid-id", updatedSong);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void deleteSong() throws IOException {
        doNothing().when(dataSourceService).deleteSong(VALID_ID);

        ResponseEntity<Void> response = songController.deleteSong(VALID_ID);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void deleteSong_Invalid() {
        ResponseEntity<Void> response = songController.deleteSong("invalid-id");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
