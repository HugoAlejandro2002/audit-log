package org.hugo.common.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class JsonUtils {

    private final ObjectMapper mapper;

    @Inject
    public JsonUtils(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public String toJson(Object obj) {
        if (obj == null) return null;
        try {
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to serialize object to JSON", e);
        }
    }
}
