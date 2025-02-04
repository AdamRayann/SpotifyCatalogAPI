package com.example.catalog.SpotifyAPITests;

import com.example.catalog.model.Album;
import com.example.catalog.model.Artist;
import com.example.catalog.model.Song;
import com.example.catalog.services.SpotifyAPIDataSources;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class ArtistControllerTest {

    private final String valid_album_id = "35s58BRTGAEWztPo9WqCIs";
    private final String valid_artist_id = "246dkjvS1zLTtiykXe5h60";
    private final String valid_song_id = "3KkXRkHbMCARz0aVfEt68P";

    @InjectMocks
    private SpotifyAPIDataSources spotifyAPIDataSources;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private JsonNode mockRootNode;

    @Mock
    private JsonNode mockItemsNode;

    @Mock
    private JsonNode mockTrackNode;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetArtistById() throws IOException {
        Artist mockArtist = new Artist();
        mockArtist.setId(valid_artist_id);
        mockArtist.setName("Test Artist");

        when(objectMapper.treeToValue(any(JsonNode.class), eq(Artist.class))).thenReturn(mockArtist);

        SpotifyAPIDataSources spyService = spy(spotifyAPIDataSources);
        doReturn(mockRootNode).when(spyService).sendGetRequest(anyString());
        doReturn(mockArtist).when(spyService).getArtistById(anyString());

        Artist artist = spyService.getArtistById(valid_artist_id);
        assertNotNull(artist);
        assertEquals(valid_artist_id, artist.getId());
        assertEquals("Test Artist", artist.getName());
    }

    @Test
    void testGetAlbumsByArtist() throws IOException {
        Album mockAlbum = new Album();
        mockAlbum.setId(valid_album_id);
        mockAlbum.setName("Mock Album");

        when(mockRootNode.get("items")).thenReturn(mockItemsNode);
        Iterator<JsonNode> mockIterator = Collections.singletonList(mockTrackNode).iterator();
        when(mockItemsNode.elements()).thenReturn(mockIterator);

        when(objectMapper.treeToValue(any(JsonNode.class), eq(Album.class))).thenReturn(mockAlbum);

        SpotifyAPIDataSources spyService = spy(spotifyAPIDataSources);
        doReturn(mockRootNode).when(spyService).sendGetRequest(anyString());
        doReturn(Collections.singletonList(mockAlbum)).when(spyService).getAlbumsByArtist(anyString());

        List<Album> albums = spyService.getAlbumsByArtist(valid_artist_id);
        assertNotNull(albums);
        assertFalse(albums.isEmpty());
        assertEquals(valid_album_id, albums.get(0).getId());
    }

    @Test
    void testGetPopularSongsByArtist() throws IOException {
        Song mockSong = new Song();
        mockSong.setId(valid_song_id);
        mockSong.setName("Mock Song");

        when(mockRootNode.get("tracks")).thenReturn(mockItemsNode);
        Iterator<JsonNode> mockIterator = Collections.singletonList(mockTrackNode).iterator();
        when(mockItemsNode.elements()).thenReturn(mockIterator);

        when(objectMapper.treeToValue(any(JsonNode.class), eq(Song.class))).thenReturn(mockSong);

        SpotifyAPIDataSources spyService = spy(spotifyAPIDataSources);
        doReturn(mockRootNode).when(spyService).sendGetRequest(anyString());
        doReturn(Collections.singletonList(mockSong)).when(spyService).getPopularSongsByArtist(anyString());

        List<Song> songs = spyService.getPopularSongsByArtist(valid_artist_id);
        assertNotNull(songs);
        assertFalse(songs.isEmpty());
        assertEquals(valid_song_id, songs.get(0).getId());
    }
}
