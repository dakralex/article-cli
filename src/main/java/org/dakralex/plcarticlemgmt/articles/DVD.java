package org.dakralex.plcarticlemgmt.articles;

import org.dakralex.plcarticlemgmt.contracts.Article;

import java.math.BigDecimal;
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

        this.setLength(length);
        this.setAgeRating(ageRating);
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        if (length > 0) {
            this.length = length;
        } else {
            throw new IllegalArgumentException("The length of the DVD must be more than 0 minutes.");
        }
    }

    public AgeRating getAgeRating() {
        return ageRating;
    }

    public void setAgeRating(AgeRating ageRating) {
        this.ageRating = ageRating;
    }

    @Override
    public BigDecimal getDiscount() {
        int ageRatingDiscountPercentage = switch (this.ageRating) {
            case AGES_SIXTEEN_AND_UP -> 5;
            case AGES_TWELVE_AND_UP -> 10;
            case AGES_SIX_AND_UP -> 15;
            case NO_AGE_RESTRICTION -> 20;
            default -> 0;
        };

        BigDecimal discountPercentage = (new BigDecimal(ageRatingDiscountPercentage)).divide(new BigDecimal(100));

        return getBasePrice().multiply(discountPercentage);
    }

    @Override
    public String toString() {
        return super.toString() +
                MessageFormat.format("Length:     {0}", length) +
                MessageFormat.format("Age rating: {0}", ageRating.getMinAge());
    }
}
