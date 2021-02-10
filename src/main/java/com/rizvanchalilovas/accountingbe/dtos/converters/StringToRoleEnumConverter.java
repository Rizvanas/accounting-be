package com.rizvanchalilovas.accountingbe.dtos.converters;

import com.rizvanchalilovas.accountingbe.models.RoleEnum;
import org.springframework.core.convert.converter.Converter;

public class StringToRoleEnumConverter implements Converter<String, RoleEnum> {
    @Override
    public RoleEnum convert(String source) {
        try {
            return RoleEnum.valueOf(source.toUpperCase());
        } catch (IllegalStateException e) {
            return null;
        }
    }
}
