package com.example.catalog.services;

import com.example.catalog.model.Album;
import com.example.catalog.model.Artist;
import com.example.catalog.model.Song;
import com.example.catalog.model.Track;

import java.io.IOException;
import java.util.List;

public interface DataSourceService {


    // Artist-related methods
    List<Artist> getAllArtists() throws IOException;
    Artist getArtistById(String id) throws IOException;
    Artist createArtist(Artist artist) throws IOException;

    Artist updateArtist(String id, Artist artist) throws IOException;
    void deleteArtist(String id) throws IOException;
    List<Album> getAlbumsByArtist(String artistId) throws IOException;
    List<Song> getPopularSongsByArtist(String artistId) throws IOException;


    // Album-related methods
    List<Album> getAllAlbums() throws IOException;
    Album getAlbumById(String id) throws IOException;
    Album createAlbum(Album album) throws IOException;
    Album updateAlbum(String id, Album album) throws IOException;
    void deleteAlbum(String id) throws IOException;
    List<Track> getTracksByAlbum(String albumId) throws IOException;
    Track addTrackToAlbum(String albumId, Track track) throws IOException;
    Track updateTrackInAlbum(String albumId, String trackId, Track track) throws IOException;
    void deleteTrackFromAlbum(String albumId, String trackId) throws IOException;


    // Song-related methods
    List<Song> getAllSongs() throws IOException;
    Song getSongById(String id) throws IOException;
    Song createSong(Song song) throws IOException;
    Song updateSong(String id, Song song) throws IOException;
    void deleteSong(String id) throws IOException;
}