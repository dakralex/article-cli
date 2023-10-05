package org.dakralex.plcarticlemgmt.articles;

import org.dakralex.plcarticlemgmt.contracts.Article;

import java.math.BigDecimal;
import java.text.MessageFormat;

public class Book extends Article {

    private static final String TYPE = "Book";
    protected int pages;

    public Book(int id, String title, int releaseYear, String publisher, BigDecimal basePrice, int pages) {
        super(id, title, releaseYear, publisher, basePrice);

        this.setPages(pages);
    }

    @Override
    public BigDecimal getDiscount() {
        int ageDiscountPercentage = Math.max(5 * this.getAge(), 30);
        int pagesDiscountPercentage = pages > 1000 ? 3 : 0;

        BigDecimal discountPercentage = (new BigDecimal(ageDiscountPercentage + pagesDiscountPercentage)).divide(new BigDecimal(100));

        return getBasePrice().multiply(discountPercentage);
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        if (pages > 0) {
            this.pages = pages;
        } else {
            throw new IllegalArgumentException("There must be more than zero pages.");
        }
    }

    @Override
    public String toString() {
        return super.toString() +
                MessageFormat.format("Pages:      {0}", pages);
    }
}
