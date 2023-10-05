package org.dakralex.plcarticlemgmt.articles;

public enum AgeRating {
    NO_AGE_RESTRICTION(0),
    AGES_SIX_AND_UP(6),
    AGES_TWELVE_AND_UP(12),
    AGES_SIXTEEN_AND_UP(16),
    AGES_EIGHTEEN_AND_UP(18);

    private final int minAge;

    AgeRating(int minAge) {
        this.minAge = minAge;
    }

    public int getMinAge() {
        return minAge;
    }
}
