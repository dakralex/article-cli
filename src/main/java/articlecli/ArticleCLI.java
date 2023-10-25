package articlecli;

import java.io.File;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

/**
 * @author Daniel Kral
 * @id 11908284
 */

public class ArticleCLI {

    public static final String ERR_MSG_INVALID_PARAMETER = "Error: Invalid parameter.";
    public static final String ERR_MSG_NO_ARTICLES_FOUND = "Error: No articles found.";
    public static final String ERR_MSG_FMT_ARTICLE_NOT_FOUND = "Error: Article not found. (id={0,number,#})";
    public static final String ERR_MSG_FMT_ARTICLE_ALREADY_EXISTS = "Error: Article already exists. (id={0,number,#})";

    public static void main(String[] args) {
        try {
            File input = new File(args[0]);
            String command = args[1];
            List<String> arguments = Collections.emptyList();

            if (args.length > 2) {
                arguments = Arrays.asList(args).subList(2, args.length);
            }

            SerializedArticleDAO articleDAO = new SerializedArticleDAO(input);
            ArticleManagement articleMgmt = new ArticleManagement(articleDAO);

            switch (command) {
                case "add" -> addCommand(articleMgmt, arguments);
                case "list" -> listCommand(articleMgmt, arguments);
                case "delete" -> deleteCommand(articleMgmt, arguments);
                case "count" -> countCommand(articleMgmt, arguments);
                case "meanprice" -> meanpriceCommand(articleMgmt);
                case "oldest" -> oldestCommand(articleMgmt);
                default -> throw new IllegalArgumentException(ERR_MSG_INVALID_PARAMETER);
            }
        } catch (IndexOutOfBoundsException | NullPointerException e) {
            System.err.println(ERR_MSG_INVALID_PARAMETER);
        } catch (Throwable th) {
            System.err.println(th.getMessage());
        }
    }

    private static void addCommand(ArticleManagement articleMgmt, List<String> arguments) {
        Article article = ArticleFactory.getArticleFromArgs(arguments);

        articleMgmt.saveArticle(article);

        System.out.println(MessageFormat.format("Info: Article {0,number,#} added.", article.getId()));
    }

    private static void listCommand(ArticleManagement articleMgmt, List<String> arguments) {
        List<Article> articleList = articleMgmt.getArticleList();

        if (articleList.isEmpty()) throw new RuntimeException(ERR_MSG_NO_ARTICLES_FOUND);

        if (!arguments.isEmpty()) {
            try {
                int id = Integer.parseInt(arguments.get(0), 10);
                Article article = articleMgmt.getArticle(id);

                if (article == null)
                    throw new IllegalArgumentException(MessageFormat.format(ERR_MSG_FMT_ARTICLE_NOT_FOUND, id));

                System.out.println(article);
            } catch (NumberFormatException ex) {
                throw new IllegalArgumentException(ERR_MSG_INVALID_PARAMETER);
            }

            return;
        }

        System.out.println(articleList.stream().map(Article::toString).collect(Collectors.joining("\n\n")));
    }

    private static void deleteCommand(ArticleManagement articleMgmt, List<String> arguments) {
        int id = Integer.parseInt(arguments.get(0), 10);

        articleMgmt.deleteArticle(id);

        System.out.println(MessageFormat.format("Info: Article {0} deleted.", id));
    }

    private static void countCommand(ArticleManagement articleMgmt, List<String> arguments) {
        String type = arguments.isEmpty() ? "articles" : arguments.get(0);

        String output = switch (type) {
            case "book" -> String.valueOf(articleMgmt.getBooksTotalAmount());
            case "dvd" -> String.valueOf(articleMgmt.getDVDsTotalAmount());
            default -> String.valueOf(articleMgmt.getArticlesTotalAmount());
        };

        System.out.println(output);
    }

    private static void meanpriceCommand(ArticleManagement articleMgmt) {
        System.out.println(articleMgmt.getArticlesPriceMean());
    }

    private static void oldestCommand(ArticleManagement articleMgmt) {
        StringJoiner joiner = new StringJoiner("\n");

        Arrays.stream(articleMgmt.getOldestArticleIds()).forEach(id -> joiner.add(MessageFormat.format("Id: {0,number,#}", id)));

        System.out.println(joiner);
    }

    static class ArticleFactory {

        /**
         * Create Article specified by a sequence of command line arguments.
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
                        DVD.AgeRating ageRating = DVD.AgeRating.getAgeRatingByMinAge(minAge);
                        return new DVD(id, title, releaseYear, publisher, basePrice, length, ageRating);
                    }
                    default -> throw new IllegalArgumentException(ERR_MSG_INVALID_PARAMETER);
                }
            } catch (IndexOutOfBoundsException | NumberFormatException ex) {
                // Catch exceptions from the arguments parsing and throw a custom exception to reflect the error message
                throw new IllegalArgumentException(ERR_MSG_INVALID_PARAMETER);
            }
        }

    }
}
