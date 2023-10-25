/**
 * @author Daniel Kral
 * @id 11908284
 */

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.StringJoiner;

public final class DVD extends Article {

    public static final String ERR_INVALID_AGE_RATING = "Error: Invalid age rating.";
    private final int length;
    private final AgeRating ageRating;

    public DVD(int id, String title, String publisher, int releaseYear, BigDecimal basePrice, int length, int minAge) {
        super(id, title, publisher, releaseYear, basePrice);

        this.length = length;
        this.ageRating = AgeRating.getAgeRatingByMinAge(minAge);
    }

    @SuppressWarnings("unused")
    public int getLength() {
        return length;
    }

    @SuppressWarnings("unused")
    public int getMinAge() {
        return ageRating.minAge;
    }

    @Override
    protected int getDiscountPercentage() {
        // Calculate the discount based on the age rating
        return switch (ageRating) {
            case NO_AGE_RESTRICTION -> 20;
            case AGES_SIX_AND_UP -> 15;
            case AGES_TWELVE_AND_UP -> 10;
            case AGES_SIXTEEN_AND_UP -> 5;
            default -> 0;
        };
    }

    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner("\n");

        joiner.add(MessageFormat.format("Length:     {0,number,#}", length));
        joiner.add(MessageFormat.format("Age rating: {0,number,#}", ageRating.minAge()));

        return super.toString() + joiner + "\n";
    }

    enum AgeRating {

        NO_AGE_RESTRICTION(0),
        AGES_SIX_AND_UP(6),
        AGES_TWELVE_AND_UP(12),
        AGES_SIXTEEN_AND_UP(16),
        AGES_EIGHTEEN_AND_UP(18);

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
                default -> throw new IllegalArgumentException(ERR_INVALID_AGE_RATING);
            };
        }

        public int minAge() {
            return minAge;
        }
    }
}
