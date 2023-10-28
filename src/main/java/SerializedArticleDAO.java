/**
 * @author Daniel Kral
 * @id 11908284
 */

import java.io.*;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * SerializedArticleDAO is the data access object specialization for storing a list of articles in file on the local
 * filesystem by serializing the ArticleList object with Java Object Serialization.
 */
public class SerializedArticleDAO implements ArticleDAO {

    private static final String ERR_MSG_SERIALIZATION = "Error during serialization.";
    private static final String ERR_MSG_DESERIALIZATION = "Error during deserialization.";
    private static final String ERR_MSG_FMT_ARTICLE_ALREADY_EXISTS = "Error: Article already exists. (id={0,number,#})";
    private final File file;
    private List<Article> articleList = new ArrayList<>(1);

    /**
     * Creates an instance of SerializedArticleDAO.
     *
     * @param file the file used for serialization
     * @throws RuntimeException if something goes wrong while deserialization of an existent file
     */
    SerializedArticleDAO(File file) {
        this.file = file;

        // Deserialize the specified file and store it in article list, if the file exists
        if (file.exists()) {
            deserializeArticleList();
        }
    }

    @Override
    public List<Article> getArticleList() {
        return Collections.unmodifiableList(articleList);
    }

    @Override
    public Article getArticle(int id) {
        return articleList.stream().filter(article -> article.getId() == id).findFirst().orElse(null);
    }

    @Override
    public void saveArticle(Article article) {
        int articleId = article.getId();

        // Throw an exception if the article already exists
        if (getArticle(articleId) != null) {
            throw new IllegalArgumentException(MessageFormat.format(ERR_MSG_FMT_ARTICLE_ALREADY_EXISTS, Integer.valueOf(articleId)));
        }

        articleList.add(article);

        serializeArticleList();
    }

    @Override
    public void deleteArticle(int id) {
        Article article = getArticle(id);

        // Throw an exception if the article could not be found
        if (article == null) {
            throw new IllegalArgumentException(MessageFormat.format(ArticleCLI.ERR_MSG_FMT_ARTICLE_NOT_FOUND, Integer.valueOf(id)));
        }

        articleList.remove(article);

        serializeArticleList();
    }

    /**
     * Deserialize the content stored in the specified file and store it in articleList.
     *
     * @throws RuntimeException if something goes wrong while reading the file or serializing the article list
     */
    @SuppressWarnings("unchecked")
    private void deserializeArticleList() {
        try (
                FileInputStream fis = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(fis)
        ) {
            articleList = (List<Article>) ois.readObject();
        } catch (IOException | SecurityException | ClassNotFoundException e) {
            throw new RuntimeException(ERR_MSG_DESERIALIZATION, e);
        }
    }

    /**
     * Serialize the content stored in articleList to the file specified in file.
     *
     * @throws RuntimeException if something goes wrong while serializing the article list or writing the file
     */
    private void serializeArticleList() {
        try (
                FileOutputStream fos = new FileOutputStream(file);
                ObjectOutput oos = new ObjectOutputStream(fos)
        ) {
            oos.writeObject(articleList);
        } catch (IOException | SecurityException | ClassCastException | UnsupportedOperationException e) {
            throw new RuntimeException(ERR_MSG_SERIALIZATION, e);
        }
    }

}
