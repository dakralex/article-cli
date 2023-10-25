package articlecli;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;

/**
 * @author Daniel Kral
 * @id 11908284
 */

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

    /**
     * Returns a list of the article prices without any identifier.
     *
     * @return list of raw article prices
     */
    public List<BigDecimal> getArticlePrices() {
        return articleDAO.getArticleList().stream().map(Article::getPrice).toList();
    }

    /**
     * Returns the sum of the article prices.
     *
     * @return sum of the article prices
     */
    public BigDecimal getArticlePriceSum() {
        return getArticlePrices().stream().map(Objects::requireNonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Returns the average mean of the article prices.
     *
     * @return average mean of article prices
     */
    public BigDecimal getArticlesPriceMean() {
        return getArticlePriceSum().divide(new BigDecimal(getArticlesTotalAmount()), RoundingMode.HALF_UP);
    }

    /**
     * Returns the id(s) of the oldest article(s).
     *
     * @return array of oldest article ids
     */
    public int[] getOldestArticleIds() {
        // Find the earliest release year in the articles list
        int minReleaseYear = articleDAO.getArticleList().stream().mapToInt(Article::getReleaseYear).min().orElseThrow(NoSuchFieldError::new);

        // Return an array of all ids that have the earliest release year in the articles list
        return articleDAO.getArticleList().stream().filter(article -> article.getReleaseYear() == minReleaseYear).mapToInt(Article::getId).toArray();
    }

}
