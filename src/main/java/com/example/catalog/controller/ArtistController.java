package com.example.catalog.controller;

import com.example.catalog.model.Album;
import com.example.catalog.model.Artist;
import com.example.catalog.model.Song;
import com.example.catalog.services.DataSourceService;
import com.example.catalog.utils.SpotifyUtils;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/artists")
public class ArtistController {

    private final DataSourceService dataSourceService;

    @Autowired
    public ArtistController(DataSourceService dataSourceService) {
        this.dataSourceService = dataSourceService;
    }


    @GetMapping
    public ResponseEntity<List<Artist>> getAllArtists() throws IOException {
        return ResponseEntity.ok(dataSourceService.getAllArtists());
    }

    @PostMapping
    public ResponseEntity<Artist> createArtist(@RequestBody Artist artist) throws IOException {

        if (artist == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        Artist createdArtist = dataSourceService.createArtist(artist);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdArtist);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Artist> getArtistById(@PathVariable String id) throws IOException {
        if (!SpotifyUtils.isValidId(id)) {
            return ResponseEntity.badRequest().build();
        }

        Artist artist = dataSourceService.getArtistById(id);
        if (artist == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok(artist);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Artist> updateArtist(@PathVariable String id, @RequestBody Artist updatedArtist) throws IOException {
        if (!SpotifyUtils.isValidId(id)) {
            return ResponseEntity.badRequest().build();
        }
        Artist artist = dataSourceService.getArtistById(id);
        if (artist == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(dataSourceService.updateArtist(id, updatedArtist));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArtist(@PathVariable String id) throws IOException {
        if (!SpotifyUtils.isValidId(id)) {
            return ResponseEntity.badRequest().build();
        }
        Artist artist = dataSourceService.getArtistById(id);
        if (artist == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        dataSourceService.deleteArtist(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/albums")
    public ResponseEntity<List<Album>> getAlbumsByArtist(@PathVariable String id) throws IOException {
        if (!SpotifyUtils.isValidId(id)) {
            return ResponseEntity.badRequest().build();
        }
        Artist artist = dataSourceService.getArtistById(id);
        if (artist == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(dataSourceService.getAlbumsByArtist(id));
    }

    @GetMapping("/{id}/songs")
    public ResponseEntity<List<Song>> getPopularSongsByArtist(@PathVariable String id) throws IOException {
        if (!SpotifyUtils.isValidId(id)) {
            return ResponseEntity.badRequest().build();
        }
        Artist artist = dataSourceService.getArtistById(id);
        if (artist == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(dataSourceService.getPopularSongsByArtist(id));
    }
}