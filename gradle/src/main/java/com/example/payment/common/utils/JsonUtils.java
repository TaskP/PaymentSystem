package com.example.payment.common.utils;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;

public final class JsonUtils {

    private JsonUtils() {
        super();
    }

    public static String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            return mapper.writeValueAsString(obj);

        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static class DoublelJsonSerializer extends JsonSerializer<Double> {
        @Override
        public void serialize(final Double value, final JsonGenerator jgen, final SerializerProvider provider) throws IOException, JsonProcessingException {
            jgen.writeNumber(String.format("%.8f", value));
        }
    }
}
