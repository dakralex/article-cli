/**
 * @author Daniel Kral
 * @id 11908284
 */

import java.util.List;

interface ArticleDAO {

    /**
     * Return all stored articles as a List.
     *
     * @return all stored articles as a List
     */
    List<Article> getArticleList();

    /**
     * Return the article with the given id number. If the article could not be found, it will return null.
     *
     * @param id article id number
     * @return found article or null
     */
    Article getArticle(int id);

    /**
     * Store the article object persistently.
     *
     * @param article article to store
     * @throws IllegalArgumentException if the article's id is already taken
     * @throws RuntimeException         if the article list could not be serialized afterward
     */
    void saveArticle(Article article);

    /**
     * Delete an article from the persistent storage.
     *
     * @param id article id number to delete
     * @throws IllegalArgumentException if the article's id could not be found
     * @throws RuntimeException         if the article list could not be serialized afterward
     */
    void deleteArticle(int id);
}
