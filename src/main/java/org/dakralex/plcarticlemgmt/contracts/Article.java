package org.dakralex.plcarticlemgmt.contracts;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
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
        this.setId(id);
        this.setTitle(title);
        this.setReleaseYear(releaseYear);
        this.setPublisher(publisher);
        this.setBasePrice(basePrice);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        if (id > 0) {
            this.id = id;
        } else {
            throw new IllegalArgumentException("The id must be positive.");
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if (!(title.isEmpty() && title.isBlank())) {
            this.title = title;
        } else {
            throw new IllegalArgumentException("The title must not be empty.");
        }
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        int yearNow = Year.now().getValue();

        if (releaseYear > 0 && releaseYear <= yearNow) {
            this.releaseYear = releaseYear;
        } else {
            throw new IllegalArgumentException("The release year is either in the future or otherwise invalid");
        }
    }

    public int getAge() {
        return Year.now().getValue() - releaseYear;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        if (!(publisher.isEmpty() && publisher.isBlank())) {
            this.publisher = publisher;
        } else {
            throw new IllegalArgumentException("The publisher must not be empty.");
        }
    }

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(BigDecimal basePrice) {
        if (basePrice.compareTo(BigDecimal.valueOf(0)) >= 0) {
            this.basePrice = basePrice;
        } else {
            throw new IllegalArgumentException("The base price must be positive");
        }
    }

    public abstract BigDecimal getDiscount();

    public BigDecimal getPrice() {
        return basePrice.subtract(this.getDiscount());
    }

    @Override
    public String toString() {
        return MessageFormat.format("Type:       {0}", TYPE) +
                MessageFormat.format("Id:         {0}", id) +
                MessageFormat.format("Title:      {0}", title) +
                MessageFormat.format("Year:       {0}", releaseYear) +
                MessageFormat.format("Publisher:  {0}", publisher) +
                MessageFormat.format("Base price: {0}", basePrice) +
                MessageFormat.format("Price:      {0}", getPrice());
    }
}
