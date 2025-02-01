package com.example.catalog.controller;

import com.example.catalog.model.Song;
import com.example.catalog.services.DataSourceService;
import com.example.catalog.utils.SpotifyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/songs")
public class SongController {

    private final DataSourceService dataSourceService;

    @Autowired
    public SongController(DataSourceService dataSourceService) {
        this.dataSourceService = dataSourceService;
    }

    @GetMapping
    public ResponseEntity<List<Song>> getAllSongs() throws IOException {
        return ResponseEntity.ok(dataSourceService.getAllSongs());
    }
    @GetMapping("/{id}")
    public ResponseEntity<Song> getSongById(@PathVariable String id) throws IOException {
        if (!SpotifyUtils.isValidId(id)) {
            return ResponseEntity.badRequest().build();
        }
        Song song = dataSourceService.getSongById(id);
        return song != null ? ResponseEntity.ok(song) : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PostMapping
    public ResponseEntity<Song> createSong(@RequestBody Song song) throws IOException {
        return ResponseEntity.status(HttpStatus.CREATED).body(dataSourceService.createSong(song));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Song> updateSong(@PathVariable String id, @RequestBody Song updatedSong) throws IOException {
        if (!SpotifyUtils.isValidId(id)) {
            return ResponseEntity.badRequest().build();
        }
        if (updatedSong.getId() != null && !updatedSong.getId().equals(id)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        Song updated = dataSourceService.updateSong(id, updatedSong);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSong(@PathVariable String id) throws IOException {
        if (!SpotifyUtils.isValidId(id)) {
            return ResponseEntity.badRequest().build();
        }
        dataSourceService.deleteSong(id);
        return ResponseEntity.noContent().build();
    }

}