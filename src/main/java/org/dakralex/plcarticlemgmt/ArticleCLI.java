package org.dakralex.plcarticlemgmt;

import org.dakralex.plcarticlemgmt.contracts.Article;
import org.dakralex.plcarticlemgmt.factories.ArticleFactory;
import org.dakralex.plcarticlemgmt.storage.SerializedArticleDAO;

import java.io.File;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.StringJoiner;

public class ArticleCLI {

    public static void main(String[] args) {
        SerializedArticleDAO articleDAO = new SerializedArticleDAO();
        ArticleManagement articleMgmt = new ArticleManagement(articleDAO);

        if (args.length < 2) {
            System.out.println("Usage: java ArticleCLI [FILE] [COMMAND]\nCommands include: add, list, delete, count, meanprice, oldest");
            System.exit(1);
        }

        File inputfile = new File(args[0]);
        String command = args[1];
        List<String> arguments = Arrays.asList(args).subList(2, args.length - 1);

        try {
            StringJoiner output = new StringJoiner("\n");

            if (!inputfile.exists() || !inputfile.isFile() || !inputfile.canRead()) {
                throw new IllegalArgumentException("Error: Invalid parameter.");
            }

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
                        articleList = Collections.singletonList(articleMgmt.getArticle(id));
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
            System.exit(1);
        }
    }

}
