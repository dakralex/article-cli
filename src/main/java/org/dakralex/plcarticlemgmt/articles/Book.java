package org.dakralex.plcarticlemgmt.articles;

import org.dakralex.plcarticlemgmt.contracts.Article;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.MessageFormat;

public class Book extends Article {

    private static final String TYPE = "Book";
    protected int pages;

    public Book(int id, String title, int releaseYear, String publisher, BigDecimal basePrice, int pages) {
        super(id, title, releaseYear, publisher, basePrice);

        setPages(pages);
    }

    @Override
    public BigDecimal getDiscount() {
        // For every year of age add a 5% discount with 30% being the highest discount from this
        int ageDiscountPercentage = Math.max(5 * getAge(), 30);
        // When the book has more than 1000 pages add a 3% discount
        int pagesDiscountPercentage = pages > 1000 ? 3 : 0;

        BigDecimal discountPercentage = (new BigDecimal(ageDiscountPercentage + pagesDiscountPercentage)).divide(new BigDecimal(100), RoundingMode.HALF_UP);

        return getBasePrice().multiply(discountPercentage).setScale(2, RoundingMode.HALF_UP);
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        if (pages <= 0) {
            throw new IllegalArgumentException("Error: Invalid parameter.");
        }

        this.pages = pages;
    }

    @Override
    public String toString() {
        return super.toString() +
                MessageFormat.format("Pages:      {0}\n", pages);
    }
}
