package com.example.catalog.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Track {
    private String id;
    private String name;

    @JsonProperty("duration_ms")
    private int durationMs;

    private boolean explicit;
    private String uri;
    private Album album;

    // Constructors
    public Track() {}

    public Track(String id, String name, int durationMs, boolean explicit, String uri, Album album) {
        this.id = id;
        this.name = name;
        this.durationMs = durationMs;
        this.explicit = explicit;
        this.uri = uri;
        this.album = album;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getDurationMs() { return durationMs; }
    public void setDurationMs(int durationMs) { this.durationMs = durationMs; }

    public boolean isExplicit() { return explicit; }
    public void setExplicit(boolean explicit) { this.explicit = explicit; }

    public String getUri() { return uri; }
    public void setUri(String uri) { this.uri = uri; }

    public Album getAlbum() { return album; }
    public void setAlbum(Album album) { this.album = album; }

    @Override
    public String toString() {
        return "Track{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", durationMs=" + durationMs +
                ", explicit=" + explicit +
                ", uri='" + uri + '\'' +
                ", album=" + (album != null ? album.getId() : "null") +
                '}';
    }
}
