package com.example.catalog.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Song {

    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("duration_ms")
    private int durationMs;

    @JsonProperty("popularity")
    private int popularity;

    @JsonProperty("uri")
    private String uri;

    @JsonProperty("artists")
    private List<Artist> artists;

    // Constructors

    public Song() {
    }
    public Song(String id, String name, int durationMs, int popularity, String uri, List<Artist> artists) {
        this.id = id;
        this.name = name;
        this.durationMs = durationMs;
        this.popularity = popularity;
        this.uri = uri;
        this.artists = artists;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getDurationMs() { return durationMs; }
    public void setDurationMs(int durationMs) { this.durationMs = durationMs; }

    public int getPopularity() { return popularity; }
    public void setPopularity(int popularity) { this.popularity = popularity; }

    public String getUri() { return uri; }
    public void setUri(String uri) { this.uri = uri; }

    public List<Artist> getArtists() { return artists; }
    public void setArtists(List<Artist> artists) { this.artists = artists; }

    public String getArtistId() {
        return (artists != null && !artists.isEmpty()) ? artists.get(0).getId() : null;
    }

    @Override
    public String toString() {
        return "Song{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", durationMs=" + durationMs +
                ", popularity=" + popularity +
                ", uri='" + uri + '\'' +
                ", artistId=" + getArtistId() +
                '}';
    }
}
