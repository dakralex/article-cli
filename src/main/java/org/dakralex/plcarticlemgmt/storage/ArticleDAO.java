package org.dakralex.plcarticlemgmt.storage;

import org.dakralex.plcarticlemgmt.contracts.Article;

import java.util.List;

public interface ArticleDAO {
    List<Article> getArticleList();

    Article getArticle(int id);

    void saveArticle(Article article);

    void deleteArticle(int id);
}
