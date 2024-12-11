package com.example.catalog;

import com.example.catalog.utils.CatalogUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

import static com.example.catalog.utils.SpotifyUtils.isValidId;
import static org.junit.jupiter.api.Assertions.*;

class CatalogUtilsTest {

    private CatalogUtils catalogUtils;
    private List<JsonNode> songs;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() throws Exception {
        catalogUtils = new CatalogUtils();
        objectMapper = new ObjectMapper();

        // Sample song data for testing. TODO - Add more songs
        String jsonData = """
                    [
                        {
                          "duration_ms": 200040,
                          "name": "J Blinding Lights",
                          "popularity": 87,
                          "album": {
                            "name": "After Hours",
                            "release_date": "2020-03-20",
                            "total_tracks": 14
                          },
                          "artists": [
                            {
                              "name": "The Weeknd"
                            }
                          ]
                        },
                        {
                          "duration_ms": 200040,
                          "name": "R Blinding Lights",
                          "popularity": 77,
                          "album": {
                            "name": "After Hours",
                            "release_date": "2020-03-20",
                            "total_tracks": 14
                          },
                          "artists": [
                            {
                              "name": "The Weeknd"
                            }
                          ]
                        },
                        {
                          "duration_ms": 200040,
                          "name": "H Blinding Lights",
                          "popularity": 67,
                          "album": {
                            "name": "After Hours",
                            "release_date": "2020-03-20",
                            "total_tracks": 14
                          },
                          "artists": [
                            {
                              "name": "The Weeknd"
                            }
                          ]
                        },
                        {
                          "duration_ms": 200040,
                          "name": "C Blinding Lights",
                          "popularity": 57,
                          "album": {
                            "name": "After Hours",
                            "release_date": "2020-03-20",
                            "total_tracks": 14
                          },
                          "artists": [
                            {
                              "name": "The Weeknd"
                            }
                          ]
                        },
                        {
                          "duration_ms": 200040,
                          "name": "L Blinding Lights",
                          "popularity": 47,
                          "album": {
                            "name": "After Hours",
                            "release_date": "2020-03-20",
                            "total_tracks": 14
                          },
                          "artists": [
                            {
                              "name": "The Weeknd"
                            }
                          ]
                        }
                    
                        
                        
                    ]
                """;
        songs = new ArrayList<>();
        objectMapper.readTree(jsonData).forEach(songs::add);
        //System.out.println(songs);

    }

    @Test
    public void testSortSongsByName() throws JsonProcessingException {
        String jsonData = """
                    [
                        {
                          "duration_ms": 200040,
                          "name": "C Blinding Lights",
                          "popularity": 57,
                          "album": {
                            "name": "After Hours",
                            "release_date": "2020-03-20",
                            "total_tracks": 14
                          },
                          "artists": [
                            {
                              "name": "The Weeknd"
                            }
                          ]
                        },
                        {
                          "duration_ms": 200040,
                          "name": "H Blinding Lights",
                          "popularity": 67,
                          "album": {
                            "name": "After Hours",
                            "release_date": "2020-03-20",
                            "total_tracks": 14
                          },
                          "artists": [
                            {
                              "name": "The Weeknd"
                            }
                          ]
                        },
                        {
                          "duration_ms": 200040,
                          "name": "J Blinding Lights",
                          "popularity": 87,
                          "album": {
                            "name": "After Hours",
                            "release_date": "2020-03-20",
                            "total_tracks": 14
                          },
                          "artists": [
                            {
                              "name": "The Weeknd"
                            }
                          ]
                        },
                        
                        {
                          "duration_ms": 200040,
                          "name": "L Blinding Lights",
                          "popularity": 47,
                          "album": {
                            "name": "After Hours",
                            "release_date": "2020-03-20",
                            "total_tracks": 14
                          },
                          "artists": [
                            {
                              "name": "The Weeknd"
                            }
                          ]
                        },
                        {
                          "duration_ms": 200040,
                          "name": "R Blinding Lights",
                          "popularity": 77,
                          "album": {
                            "name": "After Hours",
                            "release_date": "2020-03-20",
                            "total_tracks": 14
                          },
                          "artists": [
                            {
                              "name": "The Weeknd"
                            }
                          ]
                        }
                    
                        
                        
                    ]
                """;
        List<JsonNode> expected_songs = new ArrayList<>();
        objectMapper.readTree(jsonData).forEach(expected_songs::add);


        assertEquals(expected_songs,catalogUtils.sortSongsByName(songs)); // valid Spotify ID

    }




    @Test
    public void testFilterSongsByPopularity() throws JsonProcessingException {
        String jsonData0 = """
                [
                        {
                          "duration_ms": 200040,
                          "name": "J Blinding Lights",
                          "popularity": 87,
                          "album": {
                            "name": "After Hours",
                            "release_date": "2020-03-20",
                            "total_tracks": 14
                          },
                          "artists": [
                            {
                              "name": "The Weeknd"
                            }
                          ]
                        },
                        {
                          "duration_ms": 200040,
                          "name": "R Blinding Lights",
                          "popularity": 77,
                          "album": {
                            "name": "After Hours",
                            "release_date": "2020-03-20",
                            "total_tracks": 14
                          },
                          "artists": [
                            {
                              "name": "The Weeknd"
                            }
                          ]
                        },
                        {
                          "duration_ms": 200040,
                          "name": "H Blinding Lights",
                          "popularity": 67,
                          "album": {
                            "name": "After Hours",
                            "release_date": "2020-03-20",
                            "total_tracks": 14
                          },
                          "artists": [
                            {
                              "name": "The Weeknd"
                            }
                          ]
                        },
                        {
                          "duration_ms": 200040,
                          "name": "C Blinding Lights",
                          "popularity": 57,
                          "album": {
                            "name": "After Hours",
                            "release_date": "2020-03-20",
                            "total_tracks": 14
                          },
                          "artists": [
                            {
                              "name": "The Weeknd"
                            }
                          ]
                        },
                        {
                          "duration_ms": 200040,
                          "name": "L Blinding Lights",
                          "popularity": 47,
                          "album": {
                            "name": "After Hours",
                            "release_date": "2020-03-20",
                            "total_tracks": 14
                          },
                          "artists": [
                            {
                              "name": "The Weeknd"
                            }
                          ]
                        }



                    ]
            """;
        String jsonData60 = """
                [
                        {
                          "duration_ms": 200040,
                          "name": "J Blinding Lights",
                          "popularity": 87,
                          "album": {
                            "name": "After Hours",
                            "release_date": "2020-03-20",
                            "total_tracks": 14
                          },
                          "artists": [
                            {
                              "name": "The Weeknd"
                            }
                          ]
                        },
                        {
                          "duration_ms": 200040,
                          "name": "R Blinding Lights",
                          "popularity": 77,
                          "album": {
                            "name": "After Hours",
                            "release_date": "2020-03-20",
                            "total_tracks": 14
                          },
                          "artists": [
                            {
                              "name": "The Weeknd"
                            }
                          ]
                        },
                        {
                          "duration_ms": 200040,
                          "name": "H Blinding Lights",
                          "popularity": 67,
                          "album": {
                            "name": "After Hours",
                            "release_date": "2020-03-20",
                            "total_tracks": 14
                          },
                          "artists": [
                            {
                              "name": "The Weeknd"
                            }
                          ]
                        }
                    ]
            """;
        String jsonData80 = """
                [
                    {
                      "duration_ms": 200040,
                      "name": "J Blinding Lights",
                      "popularity": 87,
                      "album": {
                        "name": "After Hours",
                        "release_date": "2020-03-20",
                        "total_tracks": 14
                      },
                      "artists": [
                        {
                          "name": "The Weeknd"
                        }
                      ]
                    }
                    
                ]
                    """;
        List<JsonNode> expected_songs0 = new ArrayList<>();
        objectMapper.readTree(jsonData0).forEach(expected_songs0::add);

        List<JsonNode> expected_songs60 = new ArrayList<>();
        objectMapper.readTree(jsonData60).forEach(expected_songs60::add);

        List<JsonNode> expected_songs80 = new ArrayList<>();
        objectMapper.readTree(jsonData80).forEach(expected_songs80::add);

        assertEquals(expected_songs0,catalogUtils.filterSongsByPopularity(songs,0)); // valid Spotify ID
        assertEquals(expected_songs60,catalogUtils.filterSongsByPopularity(songs,60)); // valid Spotify ID
        assertEquals(expected_songs80,catalogUtils.filterSongsByPopularity(songs,80)); // valid Spotify ID
    }


    @Test
    public void testDoesSongExistByName() throws JsonProcessingException {

        assertTrue(catalogUtils.doesSongExistByName(songs,"J Blinding Lights"));
        assertTrue(catalogUtils.doesSongExistByName(songs,"H Blinding Lights"));
        assertTrue(catalogUtils.doesSongExistByName(songs,"R Blinding Lights"));
        assertTrue(catalogUtils.doesSongExistByName(songs,"C Blinding Lights"));

        assertFalse(catalogUtils.doesSongExistByName(songs,"Blinding Lights"));
        assertFalse(catalogUtils.doesSongExistByName(songs,"Q Blinding Lights"));
        assertFalse(catalogUtils.doesSongExistByName(songs,"J Blinding "));
        assertFalse(catalogUtils.doesSongExistByName(songs,"J Blinding"));

    }

    @Test
    public void testCountSongsByArtist() throws JsonProcessingException {

        assertEquals(5,catalogUtils.countSongsByArtist(songs,"The Weeknd")); // valid Spotify ID
        assertEquals(0,catalogUtils.countSongsByArtist(songs,"Adam")); // valid Spotify ID

    }


}