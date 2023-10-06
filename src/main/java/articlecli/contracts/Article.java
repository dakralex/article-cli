package articlecli.contracts;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.MessageFormat;
import java.time.Year;
import java.util.StringJoiner;

public abstract class Article implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    private static final String TYPE = "Article";
    protected int id;
    protected String title;
    protected int releaseYear;
    protected String publisher;
    /**
     * The base price of the article in Euros
     **/
    protected BigDecimal basePrice;

    public Article(int id, String title, int releaseYear, String publisher, BigDecimal basePrice) {
        setId(id);
        setTitle(title);
        setReleaseYear(releaseYear);
        setPublisher(publisher);
        setBasePrice(basePrice);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        if (id <= 0) throw new IllegalArgumentException("Error: Invalid parameter.");

        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if (title == null || title.isBlank()) throw new IllegalArgumentException("Error: Invalid parameter.");

        this.title = title;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        int currentYear = Year.now().getValue();

        if (releaseYear < 1436 || releaseYear > currentYear) {
            throw new IllegalArgumentException("Error: Invalid release year.");
        }

        this.releaseYear = releaseYear;
    }

    public int getAge() {
        return Year.now().getValue() - releaseYear;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        if (publisher == null || publisher.isBlank()) throw new IllegalArgumentException("Error: Invalid parameter.");

        this.publisher = publisher;
    }

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(BigDecimal basePrice) {
        // Validate only if the base price is zero or positive
        if (basePrice == null || basePrice.compareTo(new BigDecimal(0)) < 0) {
            throw new IllegalArgumentException("Error: Invalid parameter.");
        }

        try {
            this.basePrice = basePrice.setScale(2, RoundingMode.HALF_UP);
        } catch (ArithmeticException ex) {
            throw new IllegalArgumentException("Error: Invalid parameter.");
        }
    }

    public abstract BigDecimal getDiscount();

    public BigDecimal getPrice() throws ArithmeticException {
        return basePrice.subtract(getDiscount()).setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner("\n");

        joiner.add(MessageFormat.format("Type:       {0}", TYPE));
        joiner.add(MessageFormat.format("Id:         {0,number,#}", id));
        joiner.add(MessageFormat.format("Title:      {0}", title));
        joiner.add(MessageFormat.format("Year:       {0,number,#}", releaseYear));
        joiner.add(MessageFormat.format("Publisher:  {0}", publisher));
        joiner.add(MessageFormat.format("Base price: {0}", basePrice));
        joiner.add(MessageFormat.format("Price:      {0}", getPrice()));

        return joiner + "\n";
    }
}
