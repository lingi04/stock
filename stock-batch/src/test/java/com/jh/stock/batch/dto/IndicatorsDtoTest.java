package com.jh.stock.batch.dto;

import com.jh.stock.domain.Indicators;
import org.javaunit.autoparams.AutoSource;
import org.javaunit.autoparams.customization.Customization;
import org.javaunit.autoparams.lombok.BuilderCustomizer;
import org.junit.jupiter.params.ParameterizedTest;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class IndicatorsDtoTest {
    @ParameterizedTest
    @AutoSource
    @Customization(BuilderCustomizer.class)
    void of_test(IndicatorsDto dto) {
        Indicators indicators = dto.toConfirmedIndicators();

        List<String> fieldNameList = Arrays.stream(IndicatorsDto.class.getDeclaredFields()).map(Field::getName).filter(fieldName -> !"indicatorType".contains(fieldName)).collect(Collectors.toList());
        fieldNameList.forEach(fieldName -> {
            try {
                Field field1 = Indicators.class.getDeclaredField(fieldName);
                field1.setAccessible(true);
                Field field2 = IndicatorsDto.class.getDeclaredField(fieldName);
                field2.setAccessible(true);

                assertThat(field1.get(indicators)).isEqualTo(field2.get(dto)).describedAs("field(" + fieldName + ") 이 같아야 한다.").descriptionText();
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException();
            }
        });
    }
}
