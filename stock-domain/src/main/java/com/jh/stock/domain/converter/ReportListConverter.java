package com.jh.stock.domain.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jh.stock.domain.IndicatorReportType;

import javax.persistence.AttributeConverter;
import java.io.IOException;
import java.util.List;

public class ReportListConverter implements AttributeConverter<List<IndicatorReportType>, String> {
    private static final ObjectMapper mapper = new ObjectMapper()
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        .configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);

    @Override
    public String convertToDatabaseColumn(List<IndicatorReportType> attribute) {
        try {
            return mapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public List<IndicatorReportType> convertToEntityAttribute(String dbData) {
        try {
            return mapper.readValue(dbData, new TypeReference<>() {
            });
        } catch (IOException e) {
            throw new IllegalArgumentException();
        }
    }
}
