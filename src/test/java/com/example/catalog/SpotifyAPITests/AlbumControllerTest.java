package com.example.catalog.SpotifyAPITests;

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
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class AlbumControllerTest {
    private final String song_valid_id = "3KkXRkHbMCARz0aVfEt68P";
    private final String album_valid_id = "35s58BRTGAEWztPo9WqCIs";

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
    void testGetTracksByAlbum() throws IOException {

        Track mockTrack = new Track();
        mockTrack.setId(song_valid_id);
        mockTrack.setName("Mock Track");


        when(mockRootNode.get("items")).thenReturn(mockItemsNode);
        Iterator<JsonNode> mockIterator = Collections.singletonList(mockTrackNode).iterator();
        when(mockItemsNode.elements()).thenReturn(mockIterator);

        when(objectMapper.treeToValue(eq(mockTrackNode), eq(Track.class))).thenReturn(mockTrack);

        SpotifyAPIDataSources spyService = spy(spotifyAPIDataSources);
        doReturn(mockRootNode).when(spyService).sendGetRequest(anyString());

        doReturn(Collections.singletonList(mockTrack)).when(spyService).getTracksByAlbum(album_valid_id);

        List<Track> tracks = spyService.getTracksByAlbum(album_valid_id);

        assertNotNull(tracks);
        assertFalse(tracks.isEmpty());
        assertEquals(song_valid_id, tracks.get(0).getId());
        assertEquals("Mock Track", tracks.get(0).getName());
    }
}
