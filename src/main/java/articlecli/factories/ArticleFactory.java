package articlecli.factories;

import articlecli.articles.AgeRating;
import articlecli.articles.Book;
import articlecli.articles.DVD;
import articlecli.contracts.Article;

import java.math.BigDecimal;
import java.util.List;

public class ArticleFactory {

    public static Article getArticleFromArgs(List<String> arguments) {
        String type = arguments.get(0);

        String title = arguments.get(2);
        String publisher = arguments.get(3);

        int id;
        int releaseYear;
        BigDecimal basePrice;

        try {
            id = Integer.parseInt(arguments.get(1));
            releaseYear = Integer.parseInt(arguments.get(4));
            basePrice = new BigDecimal(arguments.get(5));

            switch (type) {
                case "book" -> {
                    int pages = Integer.parseInt(arguments.get(6));
                    return new Book(id, title, releaseYear, publisher, basePrice, pages);
                }
                case "dvd" -> {
                    int length = Integer.parseInt(arguments.get(6));
                    AgeRating ageRating = AgeRating.getAgeRatingByMinAge(Integer.parseInt(arguments.get(7)));

                    return new DVD(id, title, releaseYear, publisher, basePrice, length, ageRating);
                }
                default -> throw new IllegalArgumentException("Error: Invalid parameter.");
            }
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Error: Invalid parameter.");
        }
    }

}
