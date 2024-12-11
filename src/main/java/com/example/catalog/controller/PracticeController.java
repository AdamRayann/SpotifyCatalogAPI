package com.example.catalog.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.*;

@RestController
public class PracticeController {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping("/capitalize")
    public String capitalizeName(String name) {
        return StringUtils.capitalize(name);
    }


    /**
     * @return sample sorted songs list
     */
    @GetMapping("/sampleSongsNames")
    public List<String> getSampleSongs() {
        List<String> songNames = Arrays.asList(
                "Kill Bill", "Daydreaming", "Havana (feat. Young Thug)"
        );

        // TODO sort songs by names
        songNames.sort(String::compareTo);
        return songNames;
    }


    /**
     * @return the song object with the highest popularity
     * @throws IOException
     */
    @GetMapping("/mostPopularSongs")
    public Map<String, Object> getMostPopularSongs() throws IOException {
        // Load the JSON file
        ClassPathResource resource = new ClassPathResource("data/popular_songs.json");
        JsonNode songsNode = objectMapper.readTree(resource.getFile());
        // Convert JSON to a list of maps
        List<Map<String, Object>> songsList = objectMapper.convertValue(songsNode, List.class);

        // Find the song with the highest popularity
        Map<String, Object> mostPopularSong = Collections.max(songsList, Comparator.comparing(song ->
                (Integer) song.get("popularity")  // Assuming "popularity" is an Integer
        ));

        // Return the most popular song
        return mostPopularSong;
    }





}