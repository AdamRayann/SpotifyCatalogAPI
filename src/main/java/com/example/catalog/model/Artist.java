package com.example.catalog.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.core.io.ClassPathResource;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Artist {
    private String id;
    private String name;
    private int followers;
    private List<String> genres;
    private List<Image> images;
    private int popularity;
    private String uri;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFollowers() {
        return followers;
    }

//    @JsonProperty("followers")
//    public void setFollowers(Map<String, Object> followers) {
//        if (followers != null && followers.get("total") != null) {
//            this.followers = (int) followers.get("total");
//        } else {
//            this.followers = 0;
//        }
//    }


    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public int getPopularity() {
        return popularity;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public boolean addNewArtist(JsonNode artist) throws IOException {
        try {
            ClassPathResource resource = new ClassPathResource("./data/popular_artists.json");
            ObjectMapper objectMapper = new ObjectMapper();
            File jsonFile = resource.getFile();

            JsonNode rootNode = objectMapper.readTree(jsonFile);
            System.out.println(rootNode);

            if (rootNode.isObject()) {

                // Cast to ArrayNode for modification
                ObjectNode artistsObject = (ObjectNode) rootNode;

                for (JsonNode art : artistsObject) {
                    if (art.get("name").asText().equalsIgnoreCase(artist.get("name").asText())) {
                        return false;
                    }
                }

                String newArtistId = artist.get("id").asText();
                artistsObject.set(newArtistId,artist);

                objectMapper.writerWithDefaultPrettyPrinter().writeValue(jsonFile, artistsObject);
                return true;
            }

            return false;

        } catch (IOException e) {
            e.printStackTrace(); // Log the error
            return false; // Indicate failure
        }
    }

}