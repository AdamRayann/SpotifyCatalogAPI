package com.example.catalog.SpotifyAPITests;

import com.example.catalog.model.Album;
import com.example.catalog.model.Artist;
import com.example.catalog.model.Song;
import com.example.catalog.model.Track;
import com.example.catalog.services.SpotifyAPIDataSources;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
        import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class SongControllerTest {
    private final String valid_album_id = "35s58BRTGAEWztPo9WqCIs";
    private final String valid_artist_id = "246dkjvS1zLTtiykXe5h60";
    private final String valid_song_id = "3KkXRkHbMCARz0aVfEt68P";
    @InjectMocks
    private SpotifyAPIDataSources spotifyAPIDataSources;

    @Mock
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }



    @Test
    void testGetSongById() throws IOException {
        Song mockSong = new Song();
        mockSong.setId(valid_song_id);
        mockSong.setName("Test Song");

        SpotifyAPIDataSources spyService = spy(spotifyAPIDataSources);
        doReturn(mockSong).when(spyService).getSongById(anyString());

        Song song = spyService.getSongById(valid_song_id);
        assertNotNull(song);
        assertEquals(valid_song_id, song.getId());
        assertEquals("Test Song", song.getName());
    }
}

