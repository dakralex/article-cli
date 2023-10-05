package org.dakralex.plcarticlemgmt.articles;

import org.dakralex.plcarticlemgmt.contracts.Article;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.MessageFormat;

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
        if (length <= 0) {
            throw new IllegalArgumentException("Error: Invalid parameter.");
        }

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

        BigDecimal discountPercentage = (new BigDecimal(ageRatingDiscountPercentage)).divide(new BigDecimal(100), RoundingMode.HALF_UP);

        return getBasePrice().multiply(discountPercentage).setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public String toString() {
        return super.toString() +
                MessageFormat.format("Length:     {0}\n", length) +
                MessageFormat.format("Age rating: {0}\n", ageRating.getMinAge());
    }
}
