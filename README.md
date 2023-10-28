ArticleCLI
---

ArticleCLI is an application developed for the university course "Programming Languages and Concepts" to test for basic 
understanding of Java's object-oriented features and how to apply those correctly.

## Specification

### Class `Article`

`Article` is the abstract class used to store information about articles.

It has the following member fields:

- `int id`, the article's identifier
- `String title`, the article's title
- `String publisher`, the article's publisher name
- `int releaseYear`, the article's release year (not later than the current year)
- `BigDecimal basePrice`, the article's base price (without discounts)

Each of those member fields should have access methods (`getId()`, `getTitle()`, ...).

In the case that the release year has an invalid value, an `IllegalArgumentException` with the message 
`Error: Invalid release year.` should be thrown. In the case that any other field has an invalid value (e.g. 
non-negative numbers, blank strings), an `IllegalArgumentException` with the message `Error: Invalid parameter.` should 
be thrown.

The method `getAge()` calculates the age of the article in whole years.

The method `getPrice()` calculates the price of an article based on the base price minus the discount.

The method `getDiscount()` calculates the discount as in the price that is taken off the base price.

#### Class `Book`

`Book` is the class implementing the abstract class `Article` and adding book-related information to it.

It has the following additional member fields:

- `int pages`, the book's page count

The book's discount is calculated by the age and the amount of pages:

- **age-related discount:** 5% of discount per year. Can be a maximum of 30% discount.
- **pages-related discount:** 3% of discount, if the book has more than 1000 pages.

#### Class `DVD`

`DVD` is the class implementing the abstract class `Article` and adding DVD-related information to it.

It has the following additional member fields:

- `int length`, the DVD material length in minutes
- `enum AgeRating`, the DVD age rating specified by the recommended minimum age for consuming the media

In the case that the age rating has an invalid value, an `IllegalArgumentException` with the message 
`Error: Invalid age rating.` should be thrown.

The DVD discount is calculated by the age rating:

- No age restriction offers 20% of discount
- For ages six and up offers 15% of discount
- For ages twelve and up offers 10% of discount
- For ages sixteen and up offers 5% of discount
- There is no discount for ages eighteen and up

##### Enum `AgeRating`

`AgeRating` is the enumeration for the age ratings a DVD can have. The minimum ages have the values and meaning:

- `0`, no age restriction
- `6`, for ages six and up
- `12`, for ages twelve and up
- `16`, for ages sixteen and up
- `18`, for ages eighteen and up

### Interface `ArticleDAO`

`ArticleDAO` is the interface that specifies abstract methods for storing, retrieving and deleting articles
independently of how persistent storage is implemented. This is called a data access object.

The method `List<Article> getArticleList()` returns all stored articles as a list.

The method `Article getArticle(int id)` returns the article specified by the article's identifier. If the article could 
not be found, it should return `null`.

The method `void saveArticle(Article article)` persistently stores an article object. It ensures that the article's
identifier are unique by checking whether an article with the same identifier already exists. If it does, it throws an 
`IllegalArgumentException` with the message `Error: Article already exists. (id=<id>)"`.

The method `void delete Article(int id)` persistently deletes an article object. It ensures that only existing articles 
can be deleted by checking whether the article existed in the persistent storage. If it doesn't, it throws an 
`IllegalArgumentException` with the message `Error: Article not found. (id=<id>)"`.

### Class `SerializedArticleDAO`

`SerializedArticleDAO` is the class that implements `ArticleDAO` by persistently storing the `List<Article>` object in 
a file using Java Object Serialization.

The constructor `SerializedArticleDAO(String file)` creates an instance of this class.

In the case that an exception is thrown during that process, throw a similar exception with the message 
`Error during serialization.` or `Error during deserialization.`. The program should then be terminated in some way.

If the specified file does not exist, it shall be created anew. If it does, then it should be deserialized right away.

### Class `ArticleManagement`

`ArticleManagement` is the class that implements the business logic between the application and the persistent storage.

The constructor `ArticleManagement(ArticleDAO articleDAO)` creates an instance of this class.

The method `List<Article> getArticleList()` returns the list of all articles.

The method `Article getArticle(int id)` returns the specified article.

The method `void saveArticle(Article article)` saves the specified article to the persistent storage.

The method `void deleteArticle(int id)` deletes the article with the specified identifier from the persistent storage.

The method `int getArticlesTotalAmount()` returns the total amount of articles.

The method `int getBooksTotalAmount()` returns the total amount of books.

The method `int getDVDsTotalAmount()` returns the total amount of DVDs.

The method `BigDecimal getArticlesPriceMean()` returns the average mean of the article's prices.

The method `List<Integer> getOldestArticleIds()` returns the id(s) of the oldest article(s).

### Class `ArticleCLI`

`ArticleCLI` is a runnable Java program that implements a command line interface to interact with the articles stored 
in a persistent file and retrieving information about them.

The command line interface is accessed with `java ArticleCLI <file> <command>`, where `<file>` and `<command>` are required.

The argument `<file>` specifies the name of the file used for persistent storage.

The argument `<command>` specifies the command on the storage, must match any of following commands.

The command `add <type>`, adds an article to the persistent storage's list. The type must match either `book` or `dvd`.
The following arguments must give meaningful value to all properties of their corresponding classes in the order of how 
they were listed in their specification. If the article was added successfully, it should print `Info: Article <id> added.`.

The command `list <id?>` prints a list of the articles' information separated with new-line characters. If `<id>` is 
given, then it will only show the information for the article with the specified identifier.

The command `delete <id>`, deletes the article with the specified identifier from the persistent storage's list. If the 
article was deleted successfully, it should print `Info: Article 1 deleted.`.

The command `count <type?>` prints the count of all books if type is `book`, of all dvds if type is `dvd`, or the total 
amount of books if no type was specified at all.

The command `meanprice` prints the average mean of the articles' prices.

The command `oldest` prints the oldest articles' identifiers in the format: `Id: <id>` separated with new-line 
characters.
