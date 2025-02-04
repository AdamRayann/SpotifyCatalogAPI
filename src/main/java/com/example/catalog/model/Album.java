package com.example.catalog.model;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Album {
    private String artistId;
    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("uri")
    private String uri;

    @JsonProperty("release_date")
    private String releaseDate;

    @JsonProperty("total_tracks")
    private int totalTracks;

    @JsonProperty("tracks")
    private List<Track> tracks;

    @JsonProperty("images")
    private List<Image> images; // Add this field to store images



    // Getters and Setters

    public String getArtistId() { return artistId; }
    public void setArtistId(String artistId) { this.artistId = artistId; }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getUri() { return uri; }
    public void setUri(String uri) { this.uri = uri; }

    public String getReleaseDate() { return releaseDate; }
    public void setReleaseDate(String releaseDate) { this.releaseDate = releaseDate; }

    public int getTotalTracks() { return totalTracks; }
    public void setTotalTracks(int totalTracks) { this.totalTracks = totalTracks; }

    public List<Track> getTracks() { return tracks; }
    public void setTracks(List<Track> tracks) { this.tracks = tracks; }

    public List<Image> getImages() { return images; }
    public void setImages(List<Image> images) { this.images = images; }


    @Override
    public String toString() {
        return "Album{" +
                "artistId='" + artistId + '\'' +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", uri='" + uri + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                ", totalTracks=" + totalTracks +
                ", tracks=" + (tracks != null ? tracks.size() : "null") +
                ", images=" + (images != null ? images.size() : "null") +
                '}';
    }
}
