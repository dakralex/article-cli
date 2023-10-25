package articlecli;

import java.io.*;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Daniel Kral
 * @id 11908284
 */

public class SerializedArticleDAO implements ArticleDAO {

    public static final String ERR_MSG_SERIALIZATION = "Error during serialization.";
    public static final String ERR_MSG_DESERIALIZATION = "Error during deserialization.";
    protected File file;
    protected List<Article> articleList = new ArrayList<>();

    public SerializedArticleDAO(File file) {
        this.file = file;

        if (file.exists()) deserializeArticleList();
    }

    @SuppressWarnings("unchecked")
    private void deserializeArticleList() {
        try (
                FileInputStream fis = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(fis)
        ) {
            articleList = (List<Article>) ois.readObject();
        } catch (IOException | RuntimeException | ClassNotFoundException e) {
            throw new RuntimeException(ERR_MSG_DESERIALIZATION);
        }
    }

    private void serializeArticleList() {
        try (
                FileOutputStream fos = new FileOutputStream(file);
                ObjectOutputStream oos = new ObjectOutputStream(fos)
        ) {
            oos.writeObject(articleList);
        } catch (IOException | SecurityException | NullPointerException |
                 ClassCastException | UnsupportedOperationException e) {
            throw new RuntimeException(ERR_MSG_SERIALIZATION);
        }
    }

    @Override
    public List<Article> getArticleList() {
        return articleList;
    }

    @Override
    public Article getArticle(int id) {
        return articleList.stream().filter(article -> article.getId() == id).findFirst().orElse(null);
    }

    @Override
    public void saveArticle(Article article) throws IllegalArgumentException {
        int articleId = article.getId();

        // Check whether the article already exists
        if (getArticle(articleId) != null)
            throw new IllegalArgumentException(MessageFormat.format(ArticleCLI.ERR_MSG_FMT_ARTICLE_ALREADY_EXISTS, articleId));

        articleList.add(article);

        serializeArticleList();
    }

    @Override
    public void deleteArticle(int id) {
        Article article = getArticle(id);

        // Check whether the article was found
        if (article == null)
            throw new IllegalArgumentException(MessageFormat.format(ArticleCLI.ERR_MSG_FMT_ARTICLE_NOT_FOUND, id));

        // Remove all instances of the given article
        do {
            articleList.remove(article);
            article = getArticle(id);
        } while (article != null);

        serializeArticleList();
    }

}
