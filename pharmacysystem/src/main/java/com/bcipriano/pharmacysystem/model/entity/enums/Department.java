package com.bcipriano.pharmacysystem.model.entity.enums;

import com.bcipriano.pharmacysystem.exception.BusinessRuleException;

public enum Department {

    MEDICINES("Medicamento"),
    DERMATOLOGICAL("Dermatologia"),
    GERIATRICS("Geriatria"),
    BEAUTY("Beleza"),
    SUPPLEMENTS("Suplementos"),
    HYGIENE("Higiene"),
    CHILDREN("Infantil"),
    OTHERS("Outros");

    private final String value;

    Department(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static Department fromString(String departmentString) {
        try {
            return Department.valueOf(departmentString);
        } catch (IllegalArgumentException e) {
            throw new BusinessRuleException("Departamento inválido");
        }
    }

}
