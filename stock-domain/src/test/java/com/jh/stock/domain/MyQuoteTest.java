package com.jh.stock.domain;

import com.jh.stock.domain.dto.MyQuoteDto;
import org.javaunit.autoparams.AutoSource;
import org.javaunit.autoparams.customization.Customization;
import org.javaunit.autoparams.lombok.BuilderCustomizer;
import org.junit.jupiter.params.ParameterizedTest;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class MyQuoteTest {

    @ParameterizedTest
    @AutoSource
    @Customization(BuilderCustomizer.class)
    void of_test(MyQuoteDto dto) {
        MyQuote quote = MyQuote.of(dto);

        List<String> fieldNameList = Arrays.stream(MyQuoteDto.class.getDeclaredFields()).map(Field::getName).collect(Collectors.toList());
        fieldNameList.forEach(fieldName -> {
            try {
                Field field1 = MyQuote.class.getDeclaredField(fieldName);
                field1.setAccessible(true);
                Field field2 = MyQuoteDto.class.getDeclaredField(fieldName);
                field2.setAccessible(true);

                assertThat(field1.get(quote)).isEqualTo(field2.get(dto)).describedAs("field(" + fieldName + ") 이 같아야 한다.");
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException();
            }
        });
    }
}
