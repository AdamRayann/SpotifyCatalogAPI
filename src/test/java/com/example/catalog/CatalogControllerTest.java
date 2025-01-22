package com.example.catalog;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@SpringBootTest
@AutoConfigureMockMvc
public class CatalogControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testPopularSongs() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/popularSongs"))
                .andExpect(status().isOk());
    }
    @Test
    public void testPopularSongsFuncs() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/popularSongs?minPopularity=80&offset=3&limit=2"))
                .andExpect(MockMvcResultMatchers.content().string("[{\"duration_ms\":167303,\"id\":\"4Dvkj6JhhA12EX05fT7y2e\",\"name\":\"As It Was\",\"popularity\":87,\"uri\":\"spotify:track:4Dvkj6JhhA12EX05fT7y2e\",\"album\":{\"id\":\"5r36AJ6VOJtp00oxSkBZ5h\",\"name\":\"Harry's House\",\"uri\":\"spotify:album:5r36AJ6VOJtp00oxSkBZ5h\",\"release_date\":\"2022-05-20\",\"total_tracks\":13,\"images\":[{\"width\":640,\"height\":640,\"url\":null},{\"url\":\"ab67616d00001e022e8ed79e177ff6011076f5f0.jpeg\",\"width\":300,\"height\":300},{\"url\":\"ab67616d000048512e8ed79e177ff6011076f5f0.jpeg\",\"width\":64,\"height\":64}]},\"artists\":[{\"id\":\"6KImCVD70vtIoJWnq6nGn3\",\"name\":\"Harry Styles\",\"uri\":\"spotify:artist:6KImCVD70vtIoJWnq6nGn3\"}]},{\"duration_ms\":240400,\"id\":\"2QjOHCTQ1Jl3zawyYOpxh6\",\"name\":\"Sweater Weather\",\"popularity\":90,\"uri\":\"spotify:track:2QjOHCTQ1Jl3zawyYOpxh6\",\"album\":{\"id\":\"4xkM0BwLM9H2IUcbYzpcBI\",\"name\":\"I Love You.\",\"uri\":\"spotify:album:4xkM0BwLM9H2IUcbYzpcBI\",\"release_date\":\"2013-04-22\",\"total_tracks\":11,\"images\":[{\"width\":640,\"height\":640,\"url\":null},{\"url\":\"ab67616d00001e028265a736a1eb838ad5a0b921.jpeg\",\"width\":300,\"height\":300},{\"url\":\"ab67616d000048518265a736a1eb838ad5a0b921.jpeg\",\"width\":64,\"height\":64}]},\"artists\":[{\"id\":\"77SW9BnxLY8rJ0RciFqkHh\",\"name\":\"The Neighbourhood\",\"uri\":\"spotify:artist:77SW9BnxLY8rJ0RciFqkHh\"}]}]"));
    }


}
