package articlecli.articles;

public enum AgeRating {

    NO_AGE_RESTRICTION(0), AGES_SIX_AND_UP(6), AGES_TWELVE_AND_UP(12), AGES_SIXTEEN_AND_UP(16), AGES_EIGHTEEN_AND_UP(18);

    private final int minAge;

    AgeRating(int minAge) {
        this.minAge = minAge;
    }

    public static AgeRating getAgeRatingByMinAge(int minAge) {
        return switch (minAge) {
            case 0 -> NO_AGE_RESTRICTION;
            case 6 -> AGES_SIX_AND_UP;
            case 12 -> AGES_TWELVE_AND_UP;
            case 16 -> AGES_SIXTEEN_AND_UP;
            case 18 -> AGES_EIGHTEEN_AND_UP;
            default -> throw new IllegalArgumentException("Error: Invalid age rating.");
        };
    }

    public int getMinAge() {
        return minAge;
    }
}
