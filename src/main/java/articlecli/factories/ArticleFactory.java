package articlecli.factories;

import articlecli.articles.AgeRating;
import articlecli.contracts.Article;
import articlecli.articles.Book;
import articlecli.articles.DVD;

import java.math.BigDecimal;
import java.util.List;

public class ArticleFactory {

    public static Article getArticleFromArgs(List<String> arguments) {
        String type = arguments.get(0);

        int id = Integer.parseInt(arguments.get(1));
        String title = arguments.get(2);
        String publisher = arguments.get(3);
        int releaseYear = Integer.parseInt(arguments.get(4));
        BigDecimal basePrice = new BigDecimal(arguments.get(5));

        return switch (type) {
            case "book" -> new Book(id, title, releaseYear, publisher, basePrice, Integer.parseInt(arguments.get(6)));
            case "dvd" ->
                    new DVD(id, title, releaseYear, publisher, basePrice, Integer.parseInt(arguments.get(6)), AgeRating.getAgeRatingByMinAge(Integer.parseInt(arguments.get(7))));
            default -> throw new IllegalArgumentException("Error: Invalid parameter.");
        };
    }

}
