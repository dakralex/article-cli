/**
 * @author Daniel Kral
 * @id 11908284
 */

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.StringJoiner;

public final class Book extends Article {

    private final int pages;

    public Book(int id, String title, String publisher, int releaseYear, BigDecimal basePrice, int pages) {
        super(id, title, publisher, releaseYear, basePrice);

        this.pages = pages;
    }

    @Override
    protected int getDiscountPercentage() {
        // Calculate the age discount as 5% for every passed year not passing 30%
        int ageDiscount = Math.min(5 * getAge(), 30);
        // Calculate the pages discount as 3% if there are more than 1000 pages
        int pagesDiscount = pages > 1000 ? 3 : 0;

        return ageDiscount + pagesDiscount;
    }

    @SuppressWarnings("unused")
    public int getPages() {
        return pages;
    }

    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner("\n");

        joiner.add(MessageFormat.format("Pages:      {0}", pages));

        return super.toString() + joiner + "\n";
    }
}
