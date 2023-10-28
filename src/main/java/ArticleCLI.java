/**
 * @author Daniel Kral
 * @id 11908284
 */

import java.io.File;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

final class ArticleCLI {

    static final String ERR_MSG_INVALID_PARAMETER = "Error: Invalid parameter.";
    static final String ERR_MSG_FMT_ARTICLE_NOT_FOUND = "Error: Article not found. (id={0,number,#})";
    private static final String ERR_MSG_NO_ARTICLES_FOUND = "Error: No articles found.";
    private static final String INFO_MSG_FMT_ARTICLE_ID = "Id: {0,number,#}";
    private static final String INFO_MSG_FMT_ARTICLE_DELETED = "Info: Article {0} deleted.";
    private static final String INFO_MSG_FMT_ARTICLE_ADDED = "Info: Article {0,number,#} added.";
    private static File file;
    private static String commandName;
    private static List<String> commandArguments = Collections.emptyList();

    private ArticleCLI() {
    }

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
     * @throws IllegalArgumentException if some or all of the parameters could not be parsed
     */
    private static void initializeArticleCLI(String[] args) {
        try {
            file = new File(args[0]);
            commandName = String.valueOf(args[1]);

            // Store arguments after the command, if there are any
            if (args.length > 2) {
                commandArguments = Arrays.asList(args).subList(2, args.length);
            }
        } catch (Throwable th) {
            throw new IllegalArgumentException(ERR_MSG_INVALID_PARAMETER, th);
        }
    }

    private static void addCommand(ArticleManagement articleMgmt, List<String> arguments) {
        Article article = Article.newFromArgs(arguments);

        articleMgmt.saveArticle(article);

        System.out.println(MessageFormat.format(INFO_MSG_FMT_ARTICLE_ADDED, Integer.valueOf(article.getId())));
    }

    private static void listCommand(ArticleManagement articleMgmt, List<String> arguments) {
        List<Article> articleList = articleMgmt.getArticleList();

        // If there is more than one argument, throw an exception
        if (arguments.size() > 1) {
            throw new IllegalArgumentException(ERR_MSG_INVALID_PARAMETER);
        }

        // If there is an argument, reduce the article list to the specified article id
        if (arguments.size() == 1) {
            int id = Article.parseIntFromArgs(arguments, 0);
            Article article = articleMgmt.getArticle(id);

            // If the specified article was not found, throw an exception
            if (article == null) {
                throw new NoSuchElementException(MessageFormat.format(ERR_MSG_FMT_ARTICLE_NOT_FOUND, Integer.valueOf(id)));
            }

            articleList = Collections.singletonList(article);
        }

        // If there are no articles, throw an exception
        if (articleList.isEmpty()) {
            throw new NoSuchElementException(ERR_MSG_NO_ARTICLES_FOUND);
        }

        // Join the article description(s) together with new lines
        System.out.println(articleList.stream().map(Article::toString).collect(Collectors.joining(System.lineSeparator())));
    }

    private static void deleteCommand(ArticleManagement articleMgmt, List<String> arguments) {
        int id = Article.parseIntFromArgs(arguments, 0);

        articleMgmt.deleteArticle(id);

        System.out.println(MessageFormat.format(INFO_MSG_FMT_ARTICLE_DELETED, Integer.valueOf(id)));
    }

    private static void countCommand(ArticleManagement articleMgmt, List<String> arguments) {
        String type = arguments.isEmpty() ? "articles" : Article.parseStringFromArgs(arguments, 0);

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
        StringJoiner joiner = new StringJoiner(System.lineSeparator());

        // Join the id(s) of the oldest article(s) together with new lines
        articleMgmt.getOldestArticleIds().forEach(id -> joiner.add(MessageFormat.format(INFO_MSG_FMT_ARTICLE_ID, id)));

        System.out.println(joiner);
    }
}
