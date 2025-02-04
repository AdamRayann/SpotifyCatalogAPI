package com.example.catalog.services;

import com.example.catalog.model.Album;
import com.example.catalog.model.Artist;
import com.example.catalog.model.Song;
import com.example.catalog.model.Track;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;


import com.example.catalog.model.Album;
import com.example.catalog.model.Artist;
import com.example.catalog.model.Song;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class JSONDataSourceService implements DataSourceService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${JSONDataSourceService.baseData}")
    private String basePath;

    private static final String SONGS_FILE = "popular_songs.json";
    private static final String ALBUMS_FILE = "albums.json";
    private static final String ARTISTS_FILE = "popular_artists.json";

    public JSONDataSourceService() {}


    @Override
    public List<Artist> getAllArtists() throws IOException {
        JsonNode artistsNode = loadJsonData(ARTISTS_FILE);
        List<Artist> artists = new ArrayList<>();
        for (JsonNode artistNode : artistsNode) {
            artists.add(objectMapper.treeToValue(artistNode, Artist.class));
        }
        return artists;
    }

    @Override
    public Artist getArtistById(String id) throws IOException {
        JsonNode artists = loadJsonData(ARTISTS_FILE);
        JsonNode artistNode = artists.get(id);
        if (artistNode == null) {
            return null;
        }
        return objectMapper.treeToValue(artistNode, Artist.class);
    }

    @Override
    public Artist createArtist(Artist artist) throws IOException {
        JsonNode rootNode = loadJsonData(ARTISTS_FILE);
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Artist> artistsMap = mapper.convertValue(rootNode, new TypeReference<Map<String, Artist>>() {});

        artistsMap.put(artist.getId(), artist);
        saveJsonData(artistsMap, ARTISTS_FILE);
        return artist;
    }



    @Override
    public Artist updateArtist(String id, Artist artist) throws IOException {
        JsonNode rootNode = loadJsonData(ARTISTS_FILE);
        ObjectMapper mapper = new ObjectMapper();

        Map<String, Artist> artistsMap = mapper.convertValue(rootNode, new TypeReference<Map<String, Artist>>() {});

        if (artistsMap.containsKey(id)) {
            artistsMap.put(id, artist);
            saveJsonData(artistsMap, ARTISTS_FILE);
            return artist;
        }
        return null;
    }


    @Override
    public void deleteArtist(String id) throws IOException {
        JsonNode rootNode = loadJsonData(ARTISTS_FILE);
        ObjectMapper mapper = new ObjectMapper();

        Map<String, Artist> artistsMap = mapper.convertValue(rootNode, new TypeReference<Map<String, Artist>>() {});

        if (artistsMap.containsKey(id)) {
            artistsMap.remove(id);
            saveJsonData(artistsMap, ARTISTS_FILE);
        }
    }


    @Override
    public List<Album> getAlbumsByArtist(String artistId) throws IOException {
        List<Song> songs = getAllSongs();

        Set<String> songIdsByArtist = songs.stream()
                .filter(song -> song.getArtists() != null && song.getArtists().stream()
                        .anyMatch(artist -> artist.getId().equals(artistId)))
                .map(Song::getId)
                .collect(Collectors.toSet());

        List<Album> albums = getAllAlbums();
        List<Album> artistAlbums = new ArrayList<>();

        for (Album album : albums) {
            if (album.getTracks() != null && album.getTracks().stream()
                    .anyMatch(track -> songIdsByArtist.contains(track.getId()))) {
                artistAlbums.add(album);
            }
        }

        return artistAlbums;
    }


    @Override
    public List<Song> getPopularSongsByArtist(String artistId) throws IOException {
        List<Song> songs = getAllSongs();

        return songs.stream()
                .filter(song -> song.getArtists() != null && song.getArtists().stream()
                        .anyMatch(artist -> artistId != null && artistId.equals(artist.getId())))
                .collect(Collectors.toList());
    }



//    @Override
//    public List<Album> getAllAlbums() throws IOException {
//        JsonNode albumsNode = loadJsonData(ALBUMS_FILE);
//        List<Album> albums = new ArrayList<>();
//        for (JsonNode albumNode : albumsNode) {
//            albums.add(objectMapper.treeToValue(albumNode, Album.class));
//        }
//        return albums;
//    }
@Override
public List<Album> getAllAlbums() throws IOException {
    JsonNode rootNode = loadJsonData(ALBUMS_FILE);
    List<Album> albums = new ArrayList<>();

    for (Iterator<Map.Entry<String, JsonNode>> it = rootNode.fields(); it.hasNext(); ) {
        Map.Entry<String, JsonNode> entry = it.next();
        String artistId = entry.getKey();
        JsonNode albumNode = entry.getValue();

        Album album = objectMapper.treeToValue(albumNode, Album.class);

        album.setArtistId(artistId);

        albums.add(album);
    }
    return albums;
}



    @Override
    public Album getAlbumById(String id) throws IOException {
        JsonNode albums = loadJsonData(ALBUMS_FILE);
        JsonNode albumNode = albums.get(id);
        return albumNode != null ? objectMapper.treeToValue(albumNode, Album.class) : null;
    }

    @Override
    public Album createAlbum(Album album) throws IOException {
        JsonNode rootNode = loadJsonData(ALBUMS_FILE);
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Album> albumsMap = mapper.convertValue(rootNode, new TypeReference<Map<String, Album>>() {});
        albumsMap.put(album.getId(), album);
        saveJsonData(albumsMap, ALBUMS_FILE);

        return album;
    }



    @Override
    public Album updateAlbum(String id, Album album) throws IOException {
        JsonNode rootNode = loadJsonData(ALBUMS_FILE);
        ObjectMapper mapper = new ObjectMapper();

        Map<String, Album> albumsMap = mapper.convertValue(rootNode, new TypeReference<Map<String, Album>>() {});

        if (albumsMap.containsKey(id)) {
            albumsMap.put(id, album);
            saveJsonData(albumsMap, ALBUMS_FILE);
            return album;
        }

        return null;
    }


    @Override
    public void deleteAlbum(String id) throws IOException {

        JsonNode rootNode = loadJsonData(ALBUMS_FILE);
        ObjectMapper mapper = new ObjectMapper();

        // Convert the JSON to a Map
        Map<String, Album> albumsMap = mapper.convertValue(rootNode, new TypeReference<Map<String, Album>>() {});

        albumsMap.remove(id);

        saveJsonData(albumsMap, ALBUMS_FILE);
    }


    @Override
    public List<Track> getTracksByAlbum(String albumId) throws IOException {
        JsonNode albums = loadJsonData(ALBUMS_FILE);
        JsonNode albumNode = albums.get(albumId);
        if (albumNode == null || !albumNode.has("tracks")) return null;
        List<Track> tracks = new ArrayList<>();
        for (JsonNode trackNode : albumNode.get("tracks")) {
            tracks.add(objectMapper.treeToValue(trackNode, Track.class));
        }
        return tracks;
    }

    @Override
    public Track addTrackToAlbum(String albumId, Track track) throws IOException {
        JsonNode rootNode = loadJsonData(ALBUMS_FILE);
        ObjectMapper mapper = new ObjectMapper();

        Map<String, Album> albumsMap = mapper.convertValue(rootNode, new TypeReference<Map<String, Album>>() {});

        if (albumsMap.containsKey(albumId)) {
            Album album = albumsMap.get(albumId);
            if (album.getTracks() == null) {
                album.setTracks(new ArrayList<>());
            }
            album.getTracks().add(track);

            saveJsonData(albumsMap, ALBUMS_FILE);
            return track;
        }
        return null;
    }


    @Override
    public Track updateTrackInAlbum(String albumId, String trackId, Track track) throws IOException {
        JsonNode rootNode = loadJsonData(ALBUMS_FILE);
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Album> albumsMap = mapper.convertValue(rootNode, new TypeReference<Map<String, Album>>() {});

        if (albumsMap.containsKey(albumId)) {
            Album album = albumsMap.get(albumId);

            if (album.getTracks() != null) {
                for (int i = 0; i < album.getTracks().size(); i++) {
                    if (album.getTracks().get(i).getId().equals(trackId)) {
                        album.getTracks().set(i, track);

                        saveJsonData(albumsMap, ALBUMS_FILE);
                        return track;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public void deleteTrackFromAlbum(String albumId, String trackId) throws IOException {
        JsonNode rootNode = loadJsonData(ALBUMS_FILE);
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Album> albumsMap = mapper.convertValue(rootNode, new TypeReference<Map<String, Album>>() {});

        if (albumsMap.containsKey(albumId)) {
            Album album = albumsMap.get(albumId);

            if (album.getTracks() != null) {
                album.getTracks().removeIf(track -> track.getId().equals(trackId));
                saveJsonData(albumsMap, ALBUMS_FILE);
            }
        }
    }


    @Override
    public List<Song> getAllSongs() throws IOException {
        JsonNode songsNode = loadJsonData(SONGS_FILE);
        List<Song> songs = new ArrayList<>();
        for (JsonNode songNode : songsNode) {
            songs.add(objectMapper.treeToValue(songNode, Song.class));
        }
        return songs;
    }

    @Override
    public Song getSongById(String id) throws IOException {
        JsonNode songs = loadJsonData(SONGS_FILE);
        for (JsonNode songNode : songs) {
            if (songNode.get("id").asText().equals(id)) {
                return objectMapper.treeToValue(songNode, Song.class);
            }
        }
        return null;
    }

//    @Override
//    public Song createSong(Song song) throws IOException {
//        JsonNode rootNode = loadJsonData(SONGS_FILE);
//        ObjectMapper mapper = new ObjectMapper();
//        Map<String, Song> songsMap = mapper.convertValue(rootNode, new TypeReference<Map<String, Song>>() {});
//
//        songsMap.put(song.getId(), song);
//        saveJsonData(songsMap, SONGS_FILE);
//
//        return song;
//    }
    @Override
    public Song createSong(Song song) throws IOException {
        JsonNode rootNode = loadJsonData(SONGS_FILE);
        ObjectMapper mapper = new ObjectMapper();

        List<Song> songsList = mapper.convertValue(rootNode, new TypeReference<List<Song>>() {});

        songsList.add(song);
        saveJsonData(songsList, SONGS_FILE);

        return song;
    }


    @Override
    public Song updateSong(String id, Song song) throws IOException {
        JsonNode rootNode = loadJsonData(SONGS_FILE);
        ObjectMapper mapper = new ObjectMapper();

        List<Song> songs = mapper.convertValue(rootNode, new TypeReference<List<Song>>() {});

        for (int i = 0; i < songs.size(); i++) {
            Song existingSong = songs.get(i);
            if (existingSong != null && existingSong.getId() != null && existingSong.getId().equals(id)) {
                songs.set(i, song);
                saveJsonData(songs, SONGS_FILE);
                return song;
            }
        }
        return null;
    }




    @Override
    public void deleteSong(String id) throws IOException {
        JsonNode rootNode = loadJsonData(SONGS_FILE);
        ObjectMapper mapper = new ObjectMapper();

        List<Song> songs = mapper.convertValue(rootNode, new TypeReference<List<Song>>() {});
        songs.removeIf(song -> song.getId() != null && song.getId().equals(id));
        saveJsonData(songs, SONGS_FILE);
    }



//    private JsonNode loadJsonData(String path) throws IOException {
//        ClassPathResource resource = new ClassPathResource(path);
//        return objectMapper.readTree(resource.getFile());
//    }

//    private JsonNode loadJsonData(String path) throws IOException {
//        File file = new File("src/main/resources/" + path);
//        if (!file.exists()) {
//            throw new IOException("File not found: " + file.getAbsolutePath());
//        }
//        return objectMapper.readTree(file);
//    }
//
    private void saveJsonData(Object data, String path) throws IOException {
        File file = new File("src/main/resources/" +basePath +"/"+ path);

        ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter().writeValue(file, data);
    }
    private JsonNode loadJsonData(String fileName) throws IOException {
        String fullPath = basePath + "/" + fileName;
        ClassPathResource resource = new ClassPathResource(fullPath);

        try (InputStream inputStream = resource.getInputStream()) {
            return objectMapper.readTree(inputStream);
        } catch (IOException e) {
            throw new IOException("Failed to load JSON data from " + fullPath, e);
        }
    }


//    private void saveJsonData(Object data, String path) throws IOException {
//        Path filePath;
//
//        // Check if the resource exists in the target directory (for tests)
//        ClassPathResource resource = new ClassPathResource(path);
//        if (resource.exists()) {
//            filePath = resource.getFile().toPath();
//        } else {
//            // Fallback to src/main/resources for regular runs
//            filePath = Path.of("src/main/resources/" + path);
//        }
//
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.writerWithDefaultPrettyPrinter().writeValue(filePath.toFile(), data);
//    }



}
