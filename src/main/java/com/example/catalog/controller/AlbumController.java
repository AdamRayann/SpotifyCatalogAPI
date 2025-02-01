package com.example.catalog.controller;

import com.example.catalog.model.Album;
import com.example.catalog.model.Track;
import com.example.catalog.services.DataSourceService;
import com.example.catalog.utils.SpotifyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/albums")
public class AlbumController {

    private final DataSourceService dataSourceService;

    @Autowired
    public AlbumController(DataSourceService dataSourceService) {
        this.dataSourceService = dataSourceService;
    }

    @GetMapping
    public ResponseEntity<List<Album>> getAllAlbums() throws IOException {
        return ResponseEntity.ok(dataSourceService.getAllAlbums());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Album> getAlbumById(@PathVariable String id) throws IOException {
        if (!SpotifyUtils.isValidId(id)) {
            return ResponseEntity.badRequest().build();
        }
        Album album = dataSourceService.getAlbumById(id);
        return album != null ? ResponseEntity.ok(album) : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PostMapping
    public ResponseEntity<Album> createAlbum(@RequestBody Album album) throws IOException {
        return ResponseEntity.status(HttpStatus.CREATED).body(dataSourceService.createAlbum(album));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Album> updateAlbum(@PathVariable String id, @RequestBody Album updatedAlbum) throws IOException {
        if (!SpotifyUtils.isValidId(id)) {
            return ResponseEntity.badRequest().build();
        }
        if (updatedAlbum.getId() != null && !updatedAlbum.getId().equals(id)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        Album updated = dataSourceService.updateAlbum(id, updatedAlbum);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlbum(@PathVariable String id) throws IOException {
        if (!SpotifyUtils.isValidId(id)) {
            return ResponseEntity.badRequest().build();
        }
        dataSourceService.deleteAlbum(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/tracks")
    public ResponseEntity<List<Track>> getTracksByAlbum(@PathVariable String id) throws IOException {
        if (!SpotifyUtils.isValidId(id)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(dataSourceService.getTracksByAlbum(id));
    }

    @PostMapping("/{id}/tracks")
    public ResponseEntity<Track> addTrackToAlbum(@PathVariable String id, @RequestBody Track track) throws IOException {
        if (!SpotifyUtils.isValidId(id)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(dataSourceService.addTrackToAlbum(id, track));
    }

    @PutMapping("/{id}/tracks/{track_id}")
    public ResponseEntity<Track> updateTrackInAlbum(@PathVariable String id, @PathVariable String track_id, @RequestBody Track track) throws IOException {
        if (!SpotifyUtils.isValidId(id) || !SpotifyUtils.isValidId(track_id)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(dataSourceService.updateTrackInAlbum(id, track_id, track));
    }

    @DeleteMapping("/{id}/tracks/{track_id}")
    public ResponseEntity<Void> deleteTrackFromAlbum(@PathVariable String id, @PathVariable String track_id) throws IOException {
        if (!SpotifyUtils.isValidId(id) || !SpotifyUtils.isValidId(track_id)) {
            return ResponseEntity.badRequest().build();
        }
        dataSourceService.deleteTrackFromAlbum(id, track_id);
        return ResponseEntity.noContent().build();
    }

}