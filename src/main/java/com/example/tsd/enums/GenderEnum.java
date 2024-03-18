package com.example.tsd.enums;

import java.util.stream.Stream;

public enum GenderEnum {
    UNKNOWN(0, "未知"),
    MALE(1, "男"),
    FEMALE(2, "女");

    private final int value;
    private final String description;

    GenderEnum(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public Integer getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public static GenderEnum convert(String description) {
        for (GenderEnum gender : GenderEnum.values()) {
            if (gender.getDescription().equals(description)) {
                return gender;
            }
        }
        throw new IllegalArgumentException("Invalid gender description: " + description);
    }
    public static GenderEnum convertByStream(String description) {
        return Stream.of(values())
                .filter(bean -> bean.description.equals(description))
                .findAny()
                .orElse(UNKNOWN);
    }




    public static GenderEnum convert(Integer value) {
        for (GenderEnum gender : GenderEnum.values()) {
            if (gender.getValue() == value) {
                return gender;
            }
        }
        throw new IllegalArgumentException("Invalid gender value: " + value);
    }

    public static GenderEnum convertByStream(Integer value) {
        return Stream.of(GenderEnum.values())
                .filter(bean -> bean.getValue().equals((value)))
                .findAny()
                .orElse(UNKNOWN);
    }
}