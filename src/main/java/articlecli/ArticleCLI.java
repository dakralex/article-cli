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

public class ArticleCLI {

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: java ArticleCLI [FILE] [COMMAND]\nCommands include: add, list, delete, count, meanprice, oldest");
            System.exit(1);
        }

        File inputFile = new File(args[0]);
        String command = args[1];
        List<String> arguments = Collections.emptyList();

        if (args.length > 2) {
            arguments = Arrays.asList(args).subList(2, args.length);
        }

        try {
            StringJoiner output = new StringJoiner("\n");

            SerializedArticleDAO articleDAO = new SerializedArticleDAO(inputFile);
            ArticleManagement articleMgmt = new ArticleManagement(articleDAO);

            switch (command) {
                case "add" -> {
                    Article article = ArticleFactory.getArticleFromArgs(arguments);

                    articleMgmt.saveArticle(article);

                    output.add(MessageFormat.format("Info: Article {0} added.", article.getId()));
                }
                case "list" -> {
                    List<Article> articleList = articleMgmt.getArticleList();

                    if (!arguments.isEmpty()) {
                        int id = Integer.parseInt(arguments.get(0), 10);
                        Article article = articleMgmt.getArticle(id);

                        if (article == null)
                            throw new IllegalArgumentException(MessageFormat.format("Error: Article not found. (id={0,number,#})", id));

                        articleList = Collections.singletonList(article);
                    }

                    if (articleList.isEmpty()) {
                        throw new IllegalArgumentException("Error: No articles found.");
                    }

                    articleList.forEach(article -> output.add(article.toString()));
                }
                case "delete" -> {
                    int id = Integer.parseInt(arguments.get(0), 10);

                    articleMgmt.deleteArticle(id);

                    output.add(MessageFormat.format("Info: Article {0} deleted.", id));
                }
                case "count" -> {
                    String type = arguments.isEmpty() ? "articles" : arguments.get(0);

                    switch (type) {
                        case "book" -> output.add(String.valueOf(articleMgmt.getBooksTotalAmount()));
                        case "dvd" -> output.add(String.valueOf(articleMgmt.getDVDsTotalAmount()));
                        default -> output.add(String.valueOf(articleMgmt.getArticlesTotalAmount()));
                    }
                }
                case "meanprice" -> {
                    output.add(String.valueOf(articleMgmt.getArticlesPriceMean()));
                }
                case "oldest" -> {
                    Arrays.stream(articleMgmt.getOldestArticleIds()).forEach(id -> output.add("Id: " + id));
                }
                default -> throw new IllegalArgumentException("Error: Invalid parameter.");
            }

            System.out.println(output);
        } catch (Throwable th) {
            System.err.println(th.getMessage());
        }
    }

}
