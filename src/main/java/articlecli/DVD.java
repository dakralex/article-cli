package articlecli;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.MessageFormat;
import java.util.StringJoiner;

public class DVD extends Article {

    private static final String TYPE = "DVD";
    /**
     * Length of the video or audio material on the DVD in minutes
     **/
    protected int length;
    protected AgeRating ageRating;

    public DVD(int id, String title, int releaseYear, String publisher, BigDecimal basePrice, int length, AgeRating ageRating) {
        super(id, title, releaseYear, publisher, basePrice);

        setLength(length);
        setAgeRating(ageRating);
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        if (length <= 0) throw new IllegalArgumentException("Error: Invalid parameter.");

        this.length = length;
    }

    public AgeRating getAgeRating() {
        return ageRating;
    }

    public void setAgeRating(AgeRating ageRating) {
        this.ageRating = ageRating;
    }

    public void setAgeRating(int minAge) {
        this.ageRating = AgeRating.getAgeRatingByMinAge(minAge);
    }

    @Override
    public BigDecimal getDiscount() {
        int ageRatingDiscountPercentage = switch (ageRating) {
            case NO_AGE_RESTRICTION -> 20;
            case AGES_SIX_AND_UP -> 15;
            case AGES_TWELVE_AND_UP -> 10;
            case AGES_SIXTEEN_AND_UP -> 5;
            default -> 0;
        };

        // Calculate percentage that can be discounted from the DVD
        BigDecimal discountPercentage = (new BigDecimal(ageRatingDiscountPercentage)).divide(new BigDecimal(100), RoundingMode.HALF_UP);

        // Return the price that can be discounted from the base price
        return getBasePrice().multiply(discountPercentage).setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner("\n");

        joiner.add(MessageFormat.format("Length:     {0,number,#}", length));
        joiner.add(MessageFormat.format("Age rating: {0,number,#}", ageRating.getMinAge()));

        return super.toString() + joiner;
    }

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
}
