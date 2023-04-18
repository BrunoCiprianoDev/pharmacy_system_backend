package com.bcipriano.pharmacysystem.model.entity.enums;

import com.bcipriano.pharmacysystem.exception.BusinessRuleException;

public enum Stripe {

    RED("Tarja vermelha"),
    YELLOW("Tarja amarela"),

    BLACK("Tarja preta"),

    NO_STRIPE("Sem tarja");

    private final String value;

    Stripe(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static Stripe fromString(String stripeString) {
        try {
            return Stripe.valueOf(stripeString);
        } catch (IllegalArgumentException e) {
            throw new BusinessRuleException("Tarja inválida.");
        }
    }

}
