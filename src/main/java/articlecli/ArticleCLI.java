package articlecli;

import articlecli.contracts.Article;
import articlecli.factories.ArticleFactory;
import articlecli.storage.SerializedArticleDAO;

import java.io.File;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public class ArticleCLI {

    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Usage: java ArticleCLI [FILE] [COMMAND]");
            System.err.println("Commands include: add, list, delete, count, meanprice, oldest");

            System.exit(1);
        }

        File input = new File(args[0]);
        String command = args[1];
        List<String> arguments = Collections.emptyList();

        if (args.length > 2) {
            arguments = Arrays.asList(args).subList(2, args.length);
        }

        try {
            SerializedArticleDAO articleDAO = new SerializedArticleDAO(input);
            ArticleManagement articleMgmt = new ArticleManagement(articleDAO);

            switch (command) {
                case "add" -> addCommand(articleMgmt, arguments);
                case "list" -> listCommand(articleMgmt, arguments);
                case "delete" -> deleteCommand(articleMgmt, arguments);
                case "count" -> countCommand(articleMgmt, arguments);
                case "meanprice" -> meanpriceCommand(articleMgmt);
                case "oldest" -> oldestCommand(articleMgmt);
                default -> throw new IllegalArgumentException("Error: Invalid parameter.");
            }
        } catch (Throwable th) {
            System.err.println(th.getMessage());
            System.exit(1);
        }
    }

    private static void addCommand(ArticleManagement articleMgmt, List<String> arguments) {
        Article article = ArticleFactory.getArticleFromArgs(arguments);

        articleMgmt.saveArticle(article);

        System.out.println(MessageFormat.format("Info: Article {0,number,#} added.", article.getId()));
    }

    private static void listCommand(ArticleManagement articleMgmt, List<String> arguments) {
        List<Article> articleList = articleMgmt.getArticleList();

        if (articleList.isEmpty()) throw new RuntimeException("Error: No articles found.");

        if (!arguments.isEmpty()) {
            try {
                int id = Integer.parseInt(arguments.get(0), 10);
                Article article = articleMgmt.getArticle(id);

                if (article == null)
                    throw new IllegalArgumentException(MessageFormat.format("Error: Article not found. (id={0,number,#})", id));

                System.out.println(article);
            } catch (NumberFormatException ex) {
                throw new IllegalArgumentException("Error: Invalid parameter.");
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

}
