package com.bcipriano.pharmacysystem.model.entity.enums;

import com.bcipriano.pharmacysystem.exception.BusinessRuleException;

public enum Position {

    MANAGER("Gerente"),

    SELLER("Vendedor");

    private final String value;

    Position(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static Position fromString(String positionString) {
        try {
            return Position.valueOf(positionString);
        } catch (IllegalArgumentException e) {
            throw new BusinessRuleException("Função inválida.");
        }
    }

}
