/**
 * @author Daniel Kral
 * @id 11908284
 */

import java.io.Serial;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.StringJoiner;

/**
 * Book is the specialized entity of Article, which describes book-related properties for an article.
 */
public final class Book extends Article {

    @Serial
    private static final long serialVersionUID = 1L;
    private static final int AGE_DISCOUNT_LIMIT = 30;
    private static final int AGE_DISCOUNT_PER_YEAR = 5;
    private static final int PAGES_DISCOUNT_QUALIFIER = 1000;
    private static final int PAGE_DISCOUNT = 3;
    private final int pages;

    /**
     * Creates a Book instance with the specified properties.
     *
     * @param id          the book's identifier
     * @param title       the book's title
     * @param publisher   the book's publisher name
     * @param releaseYear the book's release year (thus, not later than the current year)
     * @param basePrice   the book's base price (without discounts)
     * @param pages       the book's page count
     */
    Book(int id, String title, String publisher, int releaseYear, BigDecimal basePrice, int pages) {
        super(id, title, publisher, releaseYear, basePrice);

        this.pages = pages;
    }

    /**
     * Returns the book's page count.
     *
     * @return page count of the book
     */
    public int getPages() {
        return pages;
    }

    @Override
    protected int getDiscountPercentage() {
        // Calculate the age discount as 5% for every passed year not passing 30%
        int ageDiscount = Math.min(AGE_DISCOUNT_PER_YEAR * getAge(), AGE_DISCOUNT_LIMIT);
        // Calculate the pages discount as 3% if there are more than 1000 pages
        int pagesDiscount = pages > PAGES_DISCOUNT_QUALIFIER ? PAGE_DISCOUNT : 0;

        return ageDiscount + pagesDiscount;
    }

    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner(System.lineSeparator());

        joiner.add(MessageFormat.format("Pages:      {0}", Integer.valueOf(pages)));

        return MessageFormat.format("{0}{1}{2}", super.toString(), joiner, System.lineSeparator());
    }
}
