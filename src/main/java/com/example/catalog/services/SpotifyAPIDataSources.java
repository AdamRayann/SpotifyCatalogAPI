package com.example.catalog.services;

import com.example.catalog.model.Album;
import com.example.catalog.model.Artist;
import com.example.catalog.model.Song;
import com.example.catalog.model.Track;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class SpotifyAPIDataSources implements DataSourceService {

    private static final String BASE_URL = "https://api.spotify.com/v1";
    private static final String ACCESS_TOKEN = "BQCWUmHhEZekB9VSa7EUixDrcu1RK2u_aw9DC1FbVYt4-xFMgqVtvtUVeGaaBOxYuMijoCZUtXsTcDK1XrTMIEmI0xs8DEuiTk6wZYWClq4y3M0eB7KOFQVcdws1uOR-mJjcPZ5QNis"; // Replace with your access token

    private JsonNode sendGetRequest(String endpoint) throws IOException {
        URL url = new URL(BASE_URL + endpoint);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Authorization", "Bearer " + ACCESS_TOKEN);

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        conn.disconnect();

        ObjectMapper mapper = new ObjectMapper();
        return mapper.readTree(content.toString());
    }

    @Override
    public List<Artist> getAllArtists() throws IOException {
        return new ArrayList<>();
    }

    @Override
    public Artist getArtistById(String id) throws IOException {
        JsonNode artistNode = sendGetRequest("/artists/" + id);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.treeToValue(artistNode, Artist.class);
    }

    @Override
    public List<Album> getAlbumsByArtist(String artistId) throws IOException {
        JsonNode albumsNode = sendGetRequest("/artists/" + artistId + "/albums");
        ObjectMapper mapper = new ObjectMapper();
        List<Album> albums = new ArrayList<>();
        for (JsonNode node : albumsNode.get("items")) {
            albums.add(mapper.treeToValue(node, Album.class));
        }
        return albums;
    }

    @Override
    public List<Song> getPopularSongsByArtist(String artistId) throws IOException {
        JsonNode tracksNode = sendGetRequest("/artists/" + artistId + "/top-tracks?market=US");
        ObjectMapper mapper = new ObjectMapper();
        List<Song> songs = new ArrayList<>();
        for (JsonNode node : tracksNode.get("tracks")) {
            songs.add(mapper.treeToValue(node, Song.class));
        }
        return songs;
    }

    @Override
    public List<Track> getTracksByAlbum(String albumId) throws IOException {
        JsonNode tracksNode = sendGetRequest("/albums/" + albumId + "/tracks");
        ObjectMapper mapper = new ObjectMapper();
        List<Track> tracks = new ArrayList<>();
        for (JsonNode node : tracksNode.get("items")) {
            tracks.add(mapper.treeToValue(node, Track.class));
        }
        return tracks;
    }



    @Override
    public Artist createArtist(Artist artist) { throw new UnsupportedOperationException("Not supported by Spotify API"); }
    @Override
    public Artist updateArtist(String id, Artist artist) { throw new UnsupportedOperationException("Not supported by Spotify API"); }
    @Override
    public void deleteArtist(String id) { throw new UnsupportedOperationException("Not supported by Spotify API"); }
    @Override
    public Album createAlbum(Album album) { throw new UnsupportedOperationException("Not supported by Spotify API"); }
    @Override
    public Album updateAlbum(String id, Album album) { throw new UnsupportedOperationException("Not supported by Spotify API"); }
    @Override
    public void deleteAlbum(String id) { throw new UnsupportedOperationException("Not supported by Spotify API"); }
    @Override
    public Track addTrackToAlbum(String albumId, Track track) { throw new UnsupportedOperationException("Not supported by Spotify API"); }
    @Override
    public Track updateTrackInAlbum(String albumId, String trackId, Track track) { throw new UnsupportedOperationException("Not supported by Spotify API"); }
    @Override
    public void deleteTrackFromAlbum(String albumId, String trackId) { throw new UnsupportedOperationException("Not supported by Spotify API"); }
    @Override
    public Song createSong(Song song) { throw new UnsupportedOperationException("Not supported by Spotify API"); }
    @Override
    public Song updateSong(String id, Song song) { throw new UnsupportedOperationException("Not supported by Spotify API"); }
    @Override
    public void deleteSong(String id) { throw new UnsupportedOperationException("Not supported by Spotify API"); }

    @Override
    public List<Album> getAllAlbums() { throw new UnsupportedOperationException("Not supported by Spotify API"); }

    @Override
    public Album getAlbumById(String id) throws IOException {
        JsonNode albumNode = sendGetRequest("/albums/" + id);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.treeToValue(albumNode, Album.class);
    }
    @Override
    public List<Song> getAllSongs() { throw new UnsupportedOperationException("Not supported by Spotify API"); }
    @Override
    public Song getSongById(String id) throws IOException {
        JsonNode songNode = sendGetRequest("/tracks/" + id);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.treeToValue(songNode, Song.class);
    }
}
