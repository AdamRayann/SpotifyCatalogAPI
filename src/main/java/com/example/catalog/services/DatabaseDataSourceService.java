package com.example.catalog.services;

import com.example.catalog.model.Album;
import com.example.catalog.model.Artist;
import com.example.catalog.model.Song;
import com.example.catalog.model.Track;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class DatabaseDataSourceService implements DataSourceService {

    @Override
    public List<Artist> getAllArtists() throws IOException {
        return null;
    }

    @Override
    public Artist getArtistById(String id) {

        //return db.findById(id).orElse(null);
        return null;
    }

    @Override
    public Artist createArtist(Artist artist) throws IOException {
        return null;
    }

    @Override
    public Artist updateArtist(String id, Artist artist) throws IOException {
        return null;
    }

    @Override
    public void deleteArtist(String id) throws IOException {

    }

    @Override
    public List<Album> getAlbumsByArtist(String artistId) throws IOException {
        return null;
    }

    @Override
    public List<Song> getPopularSongsByArtist(String artistId) throws IOException {
        return null;
    }

    @Override
    public List<Album> getAllAlbums() throws IOException {
        return null;
    }

    @Override
    public Album getAlbumById(String id) throws IOException {
        return null;
    }

    @Override
    public Album createAlbum(Album album) throws IOException {
        return null;
    }

    @Override
    public Album updateAlbum(String id, Album album) throws IOException {
        return null;
    }

    @Override
    public void deleteAlbum(String id) throws IOException {

    }

    @Override
    public List<Track> getTracksByAlbum(String albumId) throws IOException {
        return null;
    }

    @Override
    public Track addTrackToAlbum(String albumId, Track track) throws IOException {
        return null;
    }

    @Override
    public Track updateTrackInAlbum(String albumId, String trackId, Track track) throws IOException {
        return null;
    }

    @Override
    public void deleteTrackFromAlbum(String albumId, String trackId) throws IOException {

    }

    @Override
    public List<Song> getAllSongs() throws IOException {
        return null;
    }

    @Override
    public Song getSongById(String id) throws IOException {
        return null;
    }

    @Override
    public Song createSong(Song song) throws IOException {
        return null;
    }

    @Override
    public Song updateSong(String id, Song song) throws IOException {
        return null;
    }

    @Override
    public void deleteSong(String id) throws IOException {

    }
}
