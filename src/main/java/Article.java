/**
 * @author Daniel Kral
 * @id 11908284
 */

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.MessageFormat;
import java.time.Year;
import java.util.StringJoiner;

public abstract sealed class Article implements Serializable permits Book, DVD {

    public static final int PRICE_DECIMAL_COUNT = 2;
    public static final RoundingMode PRICE_DECIMAL_ROUNDING = RoundingMode.HALF_UP;
    public static final String ERR_MSG_INVALID_RELEASE_YEAR = "Error: Invalid release year.";
    @Serial
    private static final long serialVersionUID = 1L;
    protected final int id;
    protected final String title;
    protected final int releaseYear;
    protected final String publisher;
    protected final BigDecimal basePrice;

    public Article(int id, String title, String publisher, int releaseYear, BigDecimal basePrice) {
        this.id = id;
        this.title = title;

        // If the release year is in the future, throw an exception
        if (releaseYear > Year.now().getValue()) throw new IllegalArgumentException(ERR_MSG_INVALID_RELEASE_YEAR);
        this.releaseYear = releaseYear;

        this.publisher = publisher;
        this.basePrice = basePrice;
    }

    public int getId() {
        return id;
    }

    @SuppressWarnings("unused")
    public String getTitle() {
        return title;
    }

    @SuppressWarnings("unused")
    public String getPublisher() {
        return publisher;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public int getAge() {
        return Year.now().getValue() - releaseYear;
    }

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    /**
     * Returns the discount percentage for the article as an integer.
     *
     * @return discount percentage as an integer
     */
    protected abstract int getDiscountPercentage();

    /**
     * Returns the discount for the article.
     *
     * @return discount for the article
     */
    protected BigDecimal getDiscount() {
        // Convert the integer discount percentage to a decimal
        BigDecimal percentage = BigDecimal.valueOf(getDiscountPercentage()).divide(BigDecimal.valueOf(100), PRICE_DECIMAL_COUNT, PRICE_DECIMAL_ROUNDING);

        // Return the discount as the price that is cut off the base price
        return getBasePrice().multiply(percentage).setScale(PRICE_DECIMAL_COUNT, PRICE_DECIMAL_ROUNDING);
    }

    /**
     * Returns the actual price of the article by taking discounts into account.
     *
     * @return actual price of the article
     */
    public BigDecimal getPrice() {
        return basePrice.subtract(getDiscount()).setScale(Article.PRICE_DECIMAL_COUNT, Article.PRICE_DECIMAL_ROUNDING);
    }

    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner("\n");

        joiner.add(MessageFormat.format("Type:       {0}", getClass().getSimpleName()));
        joiner.add(MessageFormat.format("Id:         {0,number,#}", id));
        joiner.add(MessageFormat.format("Title:      {0}", title));
        joiner.add(MessageFormat.format("Year:       {0,number,#}", releaseYear));
        joiner.add(MessageFormat.format("Publisher:  {0}", publisher));
        joiner.add(MessageFormat.format("Base price: {0}", basePrice));
        joiner.add(MessageFormat.format("Price:      {0}", getPrice()));

        return joiner + "\n";
    }
}
