package com.bcipriano.pharmacysystem.model.entity.enums;

import com.bcipriano.pharmacysystem.exception.BusinessRuleException;

public enum StorageTemperature {
    BELOW_NEGATIVE_10("T < -10ºC"),
    BETWEEN_NEGATIVE_10_AND_0("-10ºC < T < 0ºC"),
    BETWEEN_0_AND_10("0ºC < T < 10ºC"),
    BETWEEN_10_AND_20("10ºC < T < 20ºC"),
    ABOVE_20("20ºC < T");

    private final String value;

    StorageTemperature(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static StorageTemperature fromString(String storageTemperatureString) {
        try {
            StorageTemperature storageTemperature = StorageTemperature.valueOf(storageTemperatureString);
            return storageTemperature;
        } catch (IllegalArgumentException e) {
            throw new BusinessRuleException("Temperatura de armazenamento inválida.");
        }
    }

}
