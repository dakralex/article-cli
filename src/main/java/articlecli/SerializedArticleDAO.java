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

    protected File file;
    protected List<Article> articleList = new ArrayList<>();

    public SerializedArticleDAO(File file) {
        this.file = file;

        if (!file.exists()) return;

        try (
                FileInputStream fis = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(fis)
        ) {
            articleList = (List<Article>) ois.readObject();
        } catch (IOException | RuntimeException | ClassNotFoundException e) {
            throw new RuntimeException("Error during deserialization.");
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
        try (
                FileOutputStream fos = new FileOutputStream(file);
                ObjectOutputStream oos = new ObjectOutputStream(fos)
        ) {
            int articleId = article.getId();

            // Check whether the article already exists
            if (getArticle(articleId) != null)
                throw new IllegalArgumentException(MessageFormat.format("Error: Article already exists. (id={0,number,#})", articleId));

            // Add article to the list
            articleList.add(article);

            oos.writeObject(articleList);
        } catch (IOException | RuntimeException e) {
            throw new RuntimeException("Error during serialization.");
        }
    }

    @Override
    public void deleteArticle(int id) {
        try (
                FileOutputStream fos = new FileOutputStream(file);
                ObjectOutputStream oos = new ObjectOutputStream(fos)
        ) {
            Article article = getArticle(id);

            // Check whether the article was found
            if (article == null)
                throw new IllegalArgumentException(MessageFormat.format("Error: Article not found. (id={0,number,#})", id));

            // Remove all instances of the given article
            do {
                articleList.remove(article);
                article = getArticle(id);
            } while (article != null);

            // Write the article list to the file
            oos.writeObject(articleList);
        } catch (IOException | RuntimeException e) {
            throw new RuntimeException("Error during serialization.");
        }
    }

}
