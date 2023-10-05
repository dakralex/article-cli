package articlecli.storage;

import articlecli.contracts.Article;

import java.io.*;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SerializedArticleDAO implements ArticleDAO {

    protected File file;
    protected List<Article> articleList;

    public SerializedArticleDAO(File file) {
        this.file = file;
        articleList = new ArrayList<>();

        if (file.exists()) {
            try {
                FileInputStream fis = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(fis);

                articleList = (List<Article>) ois.readObject();

                ois.close();
                fis.close();
            } catch (IOException | ClassNotFoundException e) {
                throw new IllegalArgumentException("Error during deserialization.");
            }
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
        try {
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            int articleId = article.getId();

            if (getArticle(articleId) != null) {
                oos.flush();
                oos.close();

                throw new IllegalArgumentException(MessageFormat.format("Error: Article already exists. (id={0})", articleId));
            }

            articleList.add(article);

            oos.writeObject(articleList);
            oos.flush();
            oos.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteArticle(int id) {
        try {
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            Article article = getArticle(id);

            if (article == null) {
                oos.flush();
                oos.close();

                throw new IllegalArgumentException(MessageFormat.format("Error: Article not found. (id={0})", id));
            }

            articleList.removeAll(Collections.singletonList(article));

            oos.writeObject(articleList);
            oos.flush();
            oos.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
