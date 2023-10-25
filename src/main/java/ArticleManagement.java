/**
 * @author Daniel Kral
 * @id 11908284
 */

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Predicate;

public class ArticleManagement {

    private final ArticleDAO articleDAO;

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
     * Returns the oldest release year among the articles.
     *
     * @return oldest release year
     * @throws NoSuchElementException if there are no articles or the oldest year could not be determined
     */
    public int getOldestReleaseYear() throws NoSuchElementException {
        // Find the earliest release year in the article list
        return articleDAO.getArticleList().stream().mapToInt(Article::getReleaseYear).reduce(Integer::min).orElseThrow();
    }

    /**
     * Returns a list of the id(s) of the oldest article(s).
     *
     * @return list of the oldest article id(s)
     */
    public List<Integer> getOldestArticleIds() {
        int oldestReleaseYear = getOldestReleaseYear();
        Predicate<Article> isOldArticle = article -> article.getReleaseYear() == oldestReleaseYear;

        // Returns a list of the ids of the oldest article
        return articleDAO.getArticleList().stream().filter(isOldArticle).mapToInt(Article::getId).boxed().toList();
    }

}
