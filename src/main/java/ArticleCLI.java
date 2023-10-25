/**
 * @author Daniel Kral
 * @id 11908284
 */

import java.io.File;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public class ArticleCLI {

    public static final String ERR_MSG_INVALID_PARAMETER = "Error: Invalid parameter.";
    public static final String ERR_MSG_NO_ARTICLES_FOUND = "Error: No articles found.";
    public static final String ERR_MSG_FMT_ARTICLE_NOT_FOUND = "Error: Article not found. (id={0,number,#})";
    public static final String INFO_MSG_FMT_ARTICLE_ID = "Id: {0,number,#}";
    public static final String INFO_MSG_FMT_ARTICLE_DELETED = "Info: Article {0} deleted.";
    public static final String INFO_MSG_FMT_ARTICLE_ADDED = "Info: Article {0,number,#} added.";
    private static File file;
    private static String commandName;
    private static List<String> commandArguments;

    public static void main(String[] args) {
        try {
            initializeArticleCLI(args);

            SerializedArticleDAO articleDAO = new SerializedArticleDAO(file);
            ArticleManagement articleMgmt = new ArticleManagement(articleDAO);

            switch (commandName) {
                case "add" -> addCommand(articleMgmt, commandArguments);
                case "list" -> listCommand(articleMgmt, commandArguments);
                case "delete" -> deleteCommand(articleMgmt, commandArguments);
                case "count" -> countCommand(articleMgmt, commandArguments);
                case "meanprice" -> meanpriceCommand(articleMgmt);
                case "oldest" -> oldestCommand(articleMgmt);
                default -> throw new IllegalArgumentException(ERR_MSG_INVALID_PARAMETER);
            }
        } catch (Throwable th) {
            System.out.println(th.getMessage());
        }
    }

    /**
     * Initializes ArticleCLI's class members.
     *
     * @param args arguments from main method
     */
    private static void initializeArticleCLI(String[] args) {
        try {
            file = new File(args[0]);
            commandName = String.valueOf(args[1]);
            commandArguments = Collections.emptyList();

            // Store arguments after the command, if there are any
            if (args.length > 2) {
                commandArguments = Arrays.asList(args).subList(2, args.length);
            }
        } catch (Throwable th) {
            throw new IllegalArgumentException(ERR_MSG_INVALID_PARAMETER);
        }
    }

    private static void addCommand(ArticleManagement articleMgmt, List<String> arguments) {
        final Article article = ArticleFactory.getArticleFromArgs(arguments);

        articleMgmt.saveArticle(article);

        System.out.println(MessageFormat.format(INFO_MSG_FMT_ARTICLE_ADDED, article.getId()));
    }

    private static void listCommand(ArticleManagement articleMgmt, List<String> arguments) {
        List<Article> articleList = articleMgmt.getArticleList();

        // If there is more than one argument, throw an exception
        if (arguments.size() > 1) {
            throw new IllegalArgumentException(ERR_MSG_INVALID_PARAMETER);
        }

        // If there is an argument, reduce the article list to the specified article id
        if (arguments.size() == 1) {
            int id = ArticleFactory.parseIntFromArgs(arguments, 0);
            Article article = articleMgmt.getArticle(id);

            // If the specified article was not found, throw an exception
            if (article == null) {
                throw new RuntimeException(MessageFormat.format(ERR_MSG_FMT_ARTICLE_NOT_FOUND, id));
            }

            articleList = Collections.singletonList(article);
        }

        // If there are no articles, throw an exception
        if (articleList.isEmpty()) {
            throw new RuntimeException(ERR_MSG_NO_ARTICLES_FOUND);
        }

        // Join the article description(s) together with new lines
        System.out.println(articleList.stream().map(Article::toString).collect(Collectors.joining("\n")));
    }

    private static void deleteCommand(ArticleManagement articleMgmt, List<String> arguments) {
        final int id = ArticleFactory.parseIntFromArgs(arguments, 0);

        articleMgmt.deleteArticle(id);

        System.out.println(MessageFormat.format(INFO_MSG_FMT_ARTICLE_DELETED, id));
    }

    private static void countCommand(ArticleManagement articleMgmt, List<String> arguments) {
        final String type = arguments.isEmpty() ? "articles" : ArticleFactory.parseStringFromArgs(arguments, 0);

        final String output = switch (type) {
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
        final StringJoiner joiner = new StringJoiner("\n");

        // Join the id(s) of the oldest article(s) together with new lines
        articleMgmt.getOldestArticleIds().forEach(id -> joiner.add(MessageFormat.format(INFO_MSG_FMT_ARTICLE_ID, id)));

        System.out.println(joiner);
    }

    static class ArticleFactory {

        protected static int parseInt(String intStr) {
            try {
                return Integer.parseUnsignedInt(intStr);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException(ArticleCLI.ERR_MSG_INVALID_PARAMETER);
            }
        }

        protected static int parseIntFromArgs(List<String> arguments, int index) {
            try {
                return parseInt(arguments.get(index));
            } catch (Exception e) {
                throw new IllegalArgumentException(ERR_MSG_INVALID_PARAMETER);
            }
        }

        protected static String parseStringFromArgs(List<String> arguments, int index) {
            try {
                return String.valueOf(arguments.get(index));
            } catch (IndexOutOfBoundsException e) {
                throw new IllegalArgumentException(ERR_MSG_INVALID_PARAMETER);
            }
        }

        @SuppressWarnings("SameParameterValue")
        protected static BigDecimal parseBigDecimalFromArgs(List<String> arguments, int index) {
            try {
                final BigDecimal value = new BigDecimal(arguments.get(index));

                // If the value is below zero, throw an exception
                if (value.compareTo(BigDecimal.ZERO) < 0) throw new NumberFormatException();

                return value.setScale(Article.PRICE_DECIMAL_COUNT, Article.PRICE_DECIMAL_ROUNDING);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException(ERR_MSG_INVALID_PARAMETER);
            }
        }

        /**
         * Create Article specified by a sequence of command line arguments.
         *
         * @param arguments list of descriptors for specified article
         * @return Article with the specified descriptors
         */
        public static Article getArticleFromArgs(List<String> arguments) {
            // Try to parse the universal article descriptors
            final String type = parseStringFromArgs(arguments, 0);
            final int id = parseIntFromArgs(arguments, 1);
            final String title = parseStringFromArgs(arguments, 2);
            final String publisher = parseStringFromArgs(arguments, 3);
            final int releaseYear = parseIntFromArgs(arguments, 4);
            final BigDecimal basePrice = parseBigDecimalFromArgs(arguments, 5);

            // Parse the specific article descriptors depending on type and return article instance
            switch (type) {
                case "book" -> {
                    final int pages = parseIntFromArgs(arguments, 6);

                    return new Book(id, title, publisher, releaseYear, basePrice, pages);
                }
                case "dvd" -> {
                    final int length = parseIntFromArgs(arguments, 6);
                    final int minAge = parseIntFromArgs(arguments, 7);

                    return new DVD(id, title, publisher, releaseYear, basePrice, length, minAge);
                }
                default -> throw new IllegalArgumentException(ERR_MSG_INVALID_PARAMETER);
            }
        }

    }
}
