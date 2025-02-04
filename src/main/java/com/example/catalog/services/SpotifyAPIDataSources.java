package com.example.catalog.services;

import com.example.catalog.model.Album;
import com.example.catalog.model.Artist;
import com.example.catalog.model.Song;
import com.example.catalog.model.Track;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
public class SpotifyAPIDataSources implements DataSourceService {
    private static final String USER_ID = "38b99bfdafda489ba94b089b9101918d";
    private static final String USER_SECRET = "1e921ef348304cf4b5bc0f2ce68b8dc3";

    private static final String BASE_URL = "https://api.spotify.com/v1";
    private static final String ACCESS_TOKEN;

    static {
        try {
            ACCESS_TOKEN = generateAccessToken();
            System.out.println(ACCESS_TOKEN);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String generateAccessToken() throws IOException {
        String auth = USER_ID + ":" + USER_SECRET;
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("https://accounts.spotify.com/api/token");
        httpPost.setHeader("Authorization", "Basic " + encodedAuth);
        httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");

        StringEntity params = new StringEntity("grant_type=client_credentials");
        httpPost.setEntity(params);

        try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
            String jsonResponse = EntityUtils.toString(response.getEntity());
            JSONObject jsonObject = new JSONObject(jsonResponse);
            return jsonObject.getString("access_token");
        } finally {
            httpClient.close();
        }
    }

    public JsonNode sendGetRequest(String endpoint) throws IOException {
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

//    @Override
//    public Artist getArtistById(String id) throws IOException {
//        JsonNode artistNode = sendGetRequest("/artists/" + id);
//        ObjectMapper mapper = new ObjectMapper();
//        return mapper.treeToValue(artistNode, Artist.class);
//    }
    @Override
    public Artist getArtistById(String id) throws IOException {
        try {
            JsonNode artistNode = sendGetRequest("/artists/" + id);
            ObjectMapper mapper = new ObjectMapper();
            return mapper.treeToValue(artistNode, Artist.class);
        } catch (IOException e) {
            System.err.println("Error fetching artist: " + e.getMessage());
            throw e;
        }
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

//    @Override
//    public List<Track> getTracksByAlbum(String albumId) throws IOException {
//        JsonNode tracksNode = sendGetRequest("/albums/" + albumId + "/tracks");
//        ObjectMapper mapper = new ObjectMapper();
//        List<Track> tracks = new ArrayList<>();
//        for (JsonNode node : tracksNode.get("items")) {
//            tracks.add(mapper.treeToValue(node, Track.class));
//        }
//        return tracks;
//    }
    @Override
    public List<Track> getTracksByAlbum(String albumId) throws IOException {
        JsonNode tracksNode = sendGetRequest("/albums/" + albumId + "/tracks");
        ObjectMapper mapper = new ObjectMapper();
        List<Track> tracks = new ArrayList<>();

        JsonNode itemsNode = tracksNode.get("items");
        if (itemsNode != null && itemsNode.isArray()) {
            for (JsonNode node : itemsNode) {
                tracks.add(mapper.treeToValue(node, Track.class));
            }
        } else {
            System.err.println("Warning: 'items' node is missing or not an array for albumId: " + albumId);
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
