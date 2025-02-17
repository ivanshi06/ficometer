package com.model;


public enum TypeOfCredit {
    HOME_LOAN(1),
    PERSONAL_LOAN(2),
    CREDIT_LOAN(3),
    CAR_LOAN(4),
    EDUCATION_LOAN(5),
    BUSINESS_LOAN(6);

    private final int id;

    TypeOfCredit(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static TypeOfCredit fromId(int id) {
        for (TypeOfCredit type : TypeOfCredit.values()) {
            if (type.getId() == id) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid TypeOfCredit id: " + id);
    }
}

