/**
 * @author Daniel Kral
 * @id 11908284
 */

import java.io.Serial;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.StringJoiner;

/**
 * DVD is the specialized entity of Article, which describes DVD-related properties for an article.
 */
public final class DVD extends Article {

    @Serial
    private static final long serialVersionUID = 1L;
    private final int length;
    private final AgeRating ageRating;

    /**
     * Creates a DVD instance with the specified properties.
     *
     * @param id          the DVD identifier
     * @param title       the DVD title
     * @param publisher   the DVD publisher name
     * @param releaseYear the DVD release year (thus, not later than the current year)
     * @param basePrice   the DVD base price (without discounts)
     * @param length      the DVD material length in minutes
     * @param minAge      the DVD minimum age to consume the media
     */
    DVD(int id, String title, String publisher, int releaseYear, BigDecimal basePrice, int length, int minAge) {
        super(id, title, publisher, releaseYear, basePrice);

        this.length = length;
        ageRating = AgeRating.getAgeRatingByMinAge(minAge);
    }

    /**
     * Returns the DVD material length in minutes.
     *
     * @return the length of the DVD material in minutes
     */
    public int getLength() {
        return length;
    }

    /**
     * Returns the DVD minimum age to consume the media.
     *
     * @return the minimum age for the DVD to be consumable
     */
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
        StringJoiner joiner = new StringJoiner(System.lineSeparator());

        joiner.add(MessageFormat.format("Length:     {0,number,#}", Integer.valueOf(length)));
        joiner.add(MessageFormat.format("Age rating: {0,number,#}", Integer.valueOf(ageRating.minAge())));

        return MessageFormat.format("{0}{1}{2}", super.toString(), joiner, System.lineSeparator());
    }

    private enum AgeRating {

        NO_AGE_RESTRICTION(0),
        AGES_SIX_AND_UP(6),
        AGES_TWELVE_AND_UP(12),
        AGES_SIXTEEN_AND_UP(16),
        AGES_EIGHTEEN_AND_UP(18);

        static final String ERR_INVALID_AGE_RATING = "Error: Invalid age rating.";
        final int minAge;

        AgeRating(int minAge) {
            this.minAge = minAge;
        }

        static AgeRating getAgeRatingByMinAge(int minAge) {
            return switch (minAge) {
                case 0 -> NO_AGE_RESTRICTION;
                case 6 -> AGES_SIX_AND_UP;
                case 12 -> AGES_TWELVE_AND_UP;
                case 16 -> AGES_SIXTEEN_AND_UP;
                case 18 -> AGES_EIGHTEEN_AND_UP;
                default -> throw new IllegalArgumentException(ERR_INVALID_AGE_RATING);
            };
        }

        int minAge() {
            return minAge;
        }
    }
}
