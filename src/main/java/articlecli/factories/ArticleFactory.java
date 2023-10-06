package articlecli.factories;

import articlecli.articles.AgeRating;
import articlecli.articles.Book;
import articlecli.articles.DVD;
import articlecli.contracts.Article;

import java.math.BigDecimal;
import java.util.List;

public class ArticleFactory {

    /**
     * Create Article specified by a sequence of command line arguments.
     * <p>
     * The command line arguments are accepted in the following order:
     * [type] [id] [title] [publisher] [release_year] [base_price] [...additional arguments]
     * <p>
     * The additional arguments depend on the type of the article:
     * * type = book: [pages]
     * * type = dvd: [length] [age_rating]
     * <p>
     * [type]               enum            either "book" or "dvd"
     * [id]                 integer         unique identifier as integer greater than zero
     * [title]              string          non-null, non-blank string for the article title
     * [publisher]          string          non-null, non-blank string for the article's publisher
     * [release_year]       integer         release year between 1436 and the current year
     * [base_price]         decimal         base price at which the book is sold; cannot be negative
     * [pages]              integer         amount of pages the book has; must be at least 1
     * [length]             integer         amount of minutes of the dvd material; must be at least 1
     * [age_rating]         enum            minimum age with specific classes: 0 (none), 6, 12, 16, 18
     *
     * @param arguments list of descriptors for specified article
     * @return Article with the specified descriptors
     */
    public static Article getArticleFromArgs(List<String> arguments) {
        try {
            // Try to parse the universal article descriptors
            String type = arguments.get(0);
            int id = Integer.parseInt(arguments.get(1));
            String title = arguments.get(2);
            String publisher = arguments.get(3);
            int releaseYear = Integer.parseInt(arguments.get(4));
            BigDecimal basePrice = new BigDecimal(arguments.get(5));

            // Parse the specific article descriptors depending on type and return object
            switch (type) {
                case "book" -> {
                    int pages = Integer.parseInt(arguments.get(6));
                    return new Book(id, title, releaseYear, publisher, basePrice, pages);
                }
                case "dvd" -> {
                    int length = Integer.parseInt(arguments.get(6));
                    int minAge = Integer.parseInt(arguments.get(7));
                    AgeRating ageRating = AgeRating.getAgeRatingByMinAge(minAge);
                    return new DVD(id, title, releaseYear, publisher, basePrice, length, ageRating);
                }
                default -> throw new IllegalArgumentException("Error: Invalid parameter.");
            }
        } catch (IndexOutOfBoundsException | NumberFormatException ex) {
            throw new IllegalArgumentException("Error: Invalid parameter.");
        }
    }

}
