package com.example.catalog.controller;

import com.example.catalog.model.Artist;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.example.catalog.utils.SpotifyUtils;

@RestController
public class CatalogController {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping("/popularSongs")
    public JsonNode getPopularSongs(
            @RequestParam(value = "name", required = false) String songName,
            @RequestParam(value = "minPopularity", required = false) Integer minPopularity
            ,@RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "-1") int limit ) throws IOException {
        // Load JSON data
        ClassPathResource resource = new ClassPathResource("data/popular_songs.json");
        JsonNode allSongs = objectMapper.readTree(resource.getFile());

        // Filter using Streams
        List<JsonNode> filteredSongs = StreamSupport.stream(allSongs.spliterator(), false)
                .filter(song -> minPopularity == null || song.get("popularity").asInt() >= minPopularity) // Apply minPopularity filter
                .filter(song -> songName == null || song.get("name").asText().equalsIgnoreCase(songName)) // Apply songName filter
                .collect(Collectors.toList());
        if(limit!=-1)
            filteredSongs=filteredSongs.subList(offset,offset+limit);
        else
            filteredSongs=filteredSongs.subList(offset,filteredSongs.size());
        // Return filtered results as JSON
        return objectMapper.valueToTree(filteredSongs);
    }

//    @GetMapping("/popularSongs/filter?name={songName}&minPopularity={minPopularity}")
//    public JsonNode getFilteredPopularSongs(@PathVariable String songName,@PathVariable int minPopularity) throws IOException {
//        ClassPathResource resource = new ClassPathResource("data/popular_songs.json");
//        //JsonNode albums = objectMapper.readTree(resource.getFile());
//        //JsonNode album = albums.get(minPopularity);
//
//
//        return objectMapper.readTree(resource.getFile());
//    }

//    @GetMapping("/songs/mostRecent")
//    public JsonNode getMostRecentSong() throws IOException {
//        ClassPathResource resource = new ClassPathResource("data/popular_artists.json");
//        JsonNode allSongs = objectMapper.readTree(resource.getFile());
//        List<JsonNode> filteredSongs = StreamSupport.stream(allSongs.spliterator(), false)
//                .filter(song -> songName == null || song.get("name").asText().equalsIgnoreCase(songName)) // Apply songName filter
//                .collect(Collectors.toList());
//
//        return objectMapper.readTree(resource.getFile());
//    }
//
//    @GetMapping("/songs/longest")
//    public JsonNode getlongestSong() throws IOException {
//        ClassPathResource resource = new ClassPathResource("data/popular_artists.json");
//        return objectMapper.readTree(resource.getFile());
//    }


    @GetMapping("/internal")
    public ResponseEntity<String> internalEndpoint() {
        return ResponseEntity.ok("Internal request successful. No rate limiting applied.");
    }
    @GetMapping("/popularArtists")
    public JsonNode getPopularArtists() throws IOException {
        ClassPathResource resource = new ClassPathResource("data/popular_artists.json");
        return objectMapper.readTree(resource.getFile());
    }

//    @GetMapping("/albums/{id}")
//    public JsonNode getAlbumById(@PathVariable String id) throws IOException {
//        if (! SpotifyUtils.isValidId(id)) {
//            return objectMapper.createObjectNode().put("error", "Invalid Id");
//        }
//
//        ClassPathResource resource = new ClassPathResource("data/albums.json");
//        JsonNode albums = objectMapper.readTree(resource.getFile());
//        JsonNode album = albums.get(id);
//        if (album != null) {
//            return album;
//        } else {
//            return objectMapper.createObjectNode().put("error", "Album not found");
//        }
//    }
//
//    @PostMapping("/artists")
//    public void addArtist(@RequestBody Artist artist) throws IOException {
//        ObjectMapper objectMapper = new ObjectMapper();
//        JsonNode artistNode = objectMapper.valueToTree(artist);  // convert the Artists object to JsonNode
//
//        if(artist.addNewArtist(artistNode))
//            System.out.println("added successfully");
//        else
//            System.out.println("the artist was not added");
//
//    }

}