package articlecli.contracts;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.MessageFormat;
import java.time.Year;

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
        if (id <= 0) {
            throw new IllegalArgumentException("Error: Invalid parameter.");
        }

        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if (title.isEmpty() || title.isBlank()) {
            throw new IllegalArgumentException("Error: Invalid parameter.");
        }

        this.title = title;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        int yearNow = Year.now().getValue();

        if (releaseYear <= 0 || releaseYear > yearNow) {
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
        if (publisher.isEmpty() || publisher.isBlank()) {
            throw new IllegalArgumentException("Error: Invalid parameter.");
        }

        this.publisher = publisher;
    }

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(BigDecimal basePrice) {
        if (basePrice.compareTo(new BigDecimal(0)) < 0) {
            throw new IllegalArgumentException("Error: Invalid parameter.");
        }

        this.basePrice = basePrice.setScale(2, RoundingMode.HALF_UP);
    }

    public abstract BigDecimal getDiscount();

    public BigDecimal getPrice() {
        return basePrice.subtract(getDiscount()).setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public String toString() {
        return MessageFormat.format("Type:       {0}\n", TYPE) +
                MessageFormat.format("Id:         {0}\n", id) +
                MessageFormat.format("Title:      {0}\n", title) +
                MessageFormat.format("Year:       {0}\n", releaseYear) +
                MessageFormat.format("Publisher:  {0}\n", publisher) +
                MessageFormat.format("Base price: {0}\n", basePrice) +
                MessageFormat.format("Price:      {0}\n", getPrice());
    }
}
