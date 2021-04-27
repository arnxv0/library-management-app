package com.arnav.library.models;

import java.util.HashMap;
import java.util.Map;

public class Library {

    private final String libraryCode, name, address;

    public Library(String libraryCode, String name, String address) {
        this.libraryCode = libraryCode;
        this.address = address;
        this.name = name;
    }

    public String getLibraryCode() {
        return libraryCode;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public Map<String, Object> getObjectMap() {

        Map<String, Object> map = new HashMap<>();
        map.put("libraryCode", this.libraryCode);
        map.put("address", this.address);
        map.put("name", this.name);

        return map;

    }
}
