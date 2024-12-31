package com.example.catalog.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

import com.example.catalog.utils.SpotifyUtils;

@RestController
public class CatalogController {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping("/popularSongs")
    public ResponseEntity<JsonNode> getPopularSongs() throws IOException {
        try {
            ClassPathResource resource = new ClassPathResource("data/popular_songs.json");
            return new ResponseEntity<>(objectMapper.readTree(resource.getFile()), HttpStatus.OK);

        }catch (Exception e){
            ObjectNode errorResponse = objectMapper.createObjectNode();
            errorResponse.put("message", "unable to read the JSON files");
            errorResponse.put("status", 500);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);

        }

    }
    @GetMapping("/internal")
    public ResponseEntity<String> internalEndpoint() {
        return ResponseEntity.ok("Internal request successful. No rate limiting applied.");
    }
    @GetMapping("/popularArtists")
    public ResponseEntity<JsonNode> getPopularArtists() throws IOException {
        try {
            ClassPathResource resource = new ClassPathResource("data/popular_artists.json");
            ObjectMapper objectMapper = new ObjectMapper();
            // Read the JSON
            JsonNode jsonNode = objectMapper.readTree(resource.getFile());
            if(jsonNode.isEmpty())
            {
                ObjectNode errorResponse = objectMapper.createObjectNode();
                errorResponse.put("message", "JSON files are empty");
                errorResponse.put("status", 503);
                return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(objectMapper.readTree(resource.getFile()), HttpStatus.OK);

        }
        catch (Exception e){
            ObjectNode errorResponse = objectMapper.createObjectNode();
            errorResponse.put("message", "unable to read the JSON files");
            errorResponse.put("status", 500);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/albums/{id}")
    public ResponseEntity<JsonNode> getAlbumById(@PathVariable String id) throws IOException {
        // Validate the ID
        if (!SpotifyUtils.isValidId(id)) {
            ObjectNode errorResponse = objectMapper.createObjectNode();
            errorResponse.put("message", "Resource not found");
            errorResponse.put("status", 400);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        // Check if the ID consists entirely of '0's
        int count = (int) id.chars()
                .filter(ch -> ch == '0')
                .count();
        if (count == id.length()) {
            ObjectNode errorResponse = objectMapper.createObjectNode();
            errorResponse.put("message", "Album not supported");
            errorResponse.put("status", 403);
            return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
        }

        try {
            // Load JSON data
            ClassPathResource resource = new ClassPathResource("data/albums.json");
            JsonNode albums = objectMapper.readTree(resource.getFile());

            // Check if the JSON is empty
            if (albums == null || albums.isEmpty()) {
                ObjectNode errorResponse = objectMapper.createObjectNode();
                errorResponse.put("message", "JSON files are empty");
                errorResponse.put("status", 503);
                return new ResponseEntity<>(errorResponse, HttpStatus.SERVICE_UNAVAILABLE);
            }

            // Retrieve the specific album
            JsonNode album = albums.get(id);

            // Check if the album exists
            if (album == null) {
                ObjectNode errorResponse = objectMapper.createObjectNode();
                errorResponse.put("message", "Album not found");
                errorResponse.put("status", 404);
                return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
            }

            // Return the album if found
            return new ResponseEntity<>(album, HttpStatus.OK);

        } catch (Exception e) {
            // Handle exceptions while reading JSON
            ObjectNode errorResponse = objectMapper.createObjectNode();
            errorResponse.put("message", "Unable to read the JSON files");
            errorResponse.put("status", 500);
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}