package articlecli;

import articlecli.articles.Book;
import articlecli.articles.DVD;
import articlecli.contracts.Article;
import articlecli.storage.ArticleDAO;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;

public class ArticleManagement {

    protected ArticleDAO articleDAO;

    public ArticleManagement(ArticleDAO articleDAO) {
        this.articleDAO = articleDAO;
    }

    public List<Article> getArticleList() {
        return articleDAO.getArticleList();
    }

    public Article getArticle(int id) {
        return articleDAO.getArticle(id);
    }

    public void saveArticle(Article article) {
        articleDAO.saveArticle(article);
    }

    public void deleteArticle(int id) {
        articleDAO.deleteArticle(id);
    }

    /**
     * Returns the total amount of articles.
     *
     * @return total amount of articles
     */
    public int getArticlesTotalAmount() {
        return articleDAO.getArticleList().size();
    }

    /**
     * Returns the total amount of books among the articles.
     *
     * @return total amount of books among the articles
     */
    public int getBooksTotalAmount() {
        return articleDAO.getArticleList().stream().filter(article -> article instanceof Book).toList().size();
    }

    /**
     * Returns the total amount of DVDs among the articles.
     *
     * @return total amount of DVDs among the articles
     */
    public int getDVDsTotalAmount() {
        return articleDAO.getArticleList().stream().filter(article -> article instanceof DVD).toList().size();
    }

    public BigDecimal getArticlesPriceMean() {
        // Collect all articles' prices and sum them together
        List<BigDecimal> prices = articleDAO.getArticleList().stream().map(Article::getPrice).toList();
        BigDecimal sumOfPrices = prices.stream().map(Objects::requireNonNull).reduce(BigDecimal.ZERO, BigDecimal::add);

        // Calculate the average mean of the articles' prices
        return sumOfPrices.divide(new BigDecimal(prices.size()), RoundingMode.HALF_UP);
    }

    public int[] getOldestArticleIds() {
        // Find the earliest release year in the articles list
        int minReleaseYear = articleDAO.getArticleList().stream().mapToInt(Article::getReleaseYear).min().orElseThrow(NoSuchFieldError::new);

        // Return an array of all ids that have the earliest release year in the articles list
        return articleDAO.getArticleList().stream().filter(article -> article.getReleaseYear() == minReleaseYear).mapToInt(Article::getId).toArray();
    }

}
