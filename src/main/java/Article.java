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
import java.util.List;
import java.util.StringJoiner;

/**
 * Article is the entity we are using for specifying items in the article catalogue.
 *
 * @author Daniel Kral
 * @id 11908284
 */
public abstract sealed class Article implements Serializable permits Book, DVD {

    @Serial
    private static final long serialVersionUID = 1L;
    private static final int PRICE_DECIMAL_COUNT = 2;
    private static final RoundingMode PRICE_DECIMAL_ROUNDING = RoundingMode.HALF_UP;
    private static final String ERR_MSG_INVALID_RELEASE_YEAR = "Error: Invalid release year.";
    private final int id;
    private final String title;
    private final String publisher;
    private final int releaseYear;
    private final BigDecimal basePrice;

    Article(int id, String title, String publisher, int releaseYear, BigDecimal basePrice) {
        this.id = id;
        this.title = title;

        // If the release year is in the future, throw an exception
        if (releaseYear > Year.now().getValue()) throw new IllegalArgumentException(ERR_MSG_INVALID_RELEASE_YEAR);
        this.releaseYear = releaseYear;

        this.publisher = publisher;
        this.basePrice = basePrice;
    }

    /**
     * Creates an Article instance from a sequence of command line arguments.
     *
     * @param arguments list of descriptors for specified article
     * @return Article with the specified descriptors
     * @throws IllegalArgumentException if the arguments could not be parsed successfully
     */
    static Article newFromArgs(List<String> arguments) {
        // Try to parse the universal article descriptors
        String type = parseStringFromArgs(arguments, 0);
        int id = parseIntFromArgs(arguments, 1);
        String title = parseStringFromArgs(arguments, 2);
        String publisher = parseStringFromArgs(arguments, 3);
        int releaseYear = parseIntFromArgs(arguments, 4);
        BigDecimal basePrice = parseBigDecimalFromArgs(arguments, 5);

        // Parse the specific article descriptors depending on type and return article instance
        switch (type) {
            case "book" -> {
                int pages = parseIntFromArgs(arguments, 6);

                return new Book(id, title, publisher, releaseYear, basePrice, pages);
            }
            case "dvd" -> {
                int length = parseIntFromArgs(arguments, 6);
                int minAge = parseIntFromArgs(arguments, 7);

                return new DVD(id, title, publisher, releaseYear, basePrice, length, minAge);
            }
            default -> throw new IllegalArgumentException(ArticleCLI.ERR_MSG_INVALID_PARAMETER);
        }
    }

    private static int parseIntArg(String intStr) {
        try {
            return Integer.parseUnsignedInt(intStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(ArticleCLI.ERR_MSG_INVALID_PARAMETER, e);
        }
    }

    static int parseIntFromArgs(List<String> arguments, int index) {
        try {
            return parseIntArg(arguments.get(index));
        } catch (IndexOutOfBoundsException e) {
            throw new IllegalArgumentException(ArticleCLI.ERR_MSG_INVALID_PARAMETER, e);
        }
    }

    static String parseStringFromArgs(List<String> arguments, int index) {
        try {
            return String.valueOf(arguments.get(index));
        } catch (IndexOutOfBoundsException e) {
            throw new IllegalArgumentException(ArticleCLI.ERR_MSG_INVALID_PARAMETER, e);
        }
    }

    static BigDecimal parseBigDecimalFromArgs(List<String> arguments, int index) {
        try {
            BigDecimal value = new BigDecimal(arguments.get(index));

            // If the value is below zero, throw an exception
            if (value.compareTo(BigDecimal.ZERO) < 0) throw new NumberFormatException("BigDecimal is negative");

            return value.setScale(PRICE_DECIMAL_COUNT, PRICE_DECIMAL_ROUNDING);
        } catch (IndexOutOfBoundsException | NumberFormatException e) {
            throw new IllegalArgumentException(ArticleCLI.ERR_MSG_INVALID_PARAMETER, e);
        }
    }

    /**
     * Returns the article's identifier.
     *
     * @return identifier of the article
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the article's title.
     *
     * @return title of the article
     */
    public String getTitle() {
        return title;
    }

    /**
     * Returns the article's publisher name.
     *
     * @return name of the publisher of the article
     */
    public String getPublisher() {
        return publisher;
    }

    /**
     * Returns the article's release year.
     * <p>
     * This is the year in which the article was released, thus it cannot be later than the current year.
     *
     * @return release year of the article
     */
    public int getReleaseYear() {
        return releaseYear;
    }

    /**
     * Returns the article's age.
     * <p>
     * This is the amount of years between when the article was released and the current year.
     *
     * @return age of the article
     */
    public int getAge() {
        return Year.now().getValue() - releaseYear;
    }

    /**
     * Returns the article's base price.
     * <p>
     * This is the price of the article without any discounts.
     *
     * @return base price of the article
     */
    public BigDecimal getBasePrice() {
        return basePrice;
    }

    /**
     * Returns the article's discount percentage.
     * <p>
     * This is the integer that represents the percentage that can be taken off the base price.
     *
     * @return discount percentage for the article
     */
    protected abstract int getDiscountPercentage();

    /**
     * Returns the article's discount.
     * <p>
     * This is the price value that is taken off the base price.
     *
     * @return discount for the article
     */
    private BigDecimal getDiscount() {
        // Convert the integer discount percentage to a decimal
        BigDecimal percentage = BigDecimal.valueOf(getDiscountPercentage()).divide(BigDecimal.valueOf(100), PRICE_DECIMAL_COUNT, PRICE_DECIMAL_ROUNDING);

        // Return the discount as the price that is cut off the base price
        return basePrice.multiply(percentage).setScale(PRICE_DECIMAL_COUNT, PRICE_DECIMAL_ROUNDING);
    }

    /**
     * Returns the article's price.
     * <p>
     * This is the price of the article, which takes discounts into account.
     *
     * @return price of the article
     */
    public BigDecimal getPrice() {
        return basePrice.subtract(getDiscount()).setScale(PRICE_DECIMAL_COUNT, PRICE_DECIMAL_ROUNDING);
    }

    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner(System.lineSeparator());

        joiner.add(MessageFormat.format("Type:       {0}", getClass().getSimpleName()));
        joiner.add(MessageFormat.format("Id:         {0,number,#}", Integer.valueOf(id)));
        joiner.add(MessageFormat.format("Title:      {0}", title));
        joiner.add(MessageFormat.format("Year:       {0,number,#}", Integer.valueOf(releaseYear)));
        joiner.add(MessageFormat.format("Publisher:  {0}", publisher));
        joiner.add(MessageFormat.format("Base price: {0}", basePrice));
        joiner.add(MessageFormat.format("Price:      {0}", getPrice()));

        return MessageFormat.format("{0}{1}", joiner, System.lineSeparator());
    }
}
