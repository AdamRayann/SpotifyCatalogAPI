package com.example.catalog.services;

import com.example.catalog.model.Artist;

import java.io.IOException;

public interface DataSourceService {
    Artist getArtistById(String id) throws IOException;

}
