package rbtips.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import rbtips.domain.Article;

public class ArticleDao implements ArticleDaoApi {

    private Database db;
    private String tableName;
    private TagDao tagDao;

    public ArticleDao(Database db, String tableName) {
        this.db = db;
        this.tableName = tableName;
        this.tagDao = new TagDao(db, "Tag");
    }

    @Override
    public void create(Article article) throws SQLException {
        Connection conn = db.getConnection();
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO " + tableName + "(headline, author, url) VALUES (?, ?, ?)");
        stmt.setString(1, article.getHeadline());
        stmt.setString(2, article.getAuthor());
        stmt.setString(3, article.getUrl());
        stmt.executeUpdate();
        stmt.close();
        conn.close();
    }

    @Override
    public ArrayList<Article> getAll() throws SQLException {
        ArrayList<Article> articles = new ArrayList<>();

        Connection conn = db.getConnection();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM " + tableName);

        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            Article article = new Article(rs.getString("headline"), rs.getString("author"), rs.getString("url"));
            article.setTags(String.join(",", tagDao.findArticleTags(article)));
            articles.add(article);
        }

        stmt.close();
        rs.close();
        conn.close();

        return articles;
    }

    public int getIdByHeadline(String headline) throws SQLException {
        Connection conn = db.getConnection();
        PreparedStatement stmt = conn.prepareStatement("SELECT *  FROM " + tableName + " WHERE headline = (?)");
        stmt.setString(1, headline);
        ResultSet rs = stmt.executeQuery();

        int id = rs.getInt("id");

        stmt.close();
        rs.close();
        conn.close();
        return id;
    }

    @Override
    public ArrayList<Article> searchHeadline(String headline, boolean StricSearch) throws SQLException {
        ArrayList<Article> articles = new ArrayList<>();
        Connection conn = db.getConnection();
        PreparedStatement stmt;
        String queryString;
        if (StricSearch) {
            stmt = conn.prepareStatement("SELECT * FROM " + tableName + " WHERE headline = (?) ");
            queryString = headline;
        } else {
            stmt = conn.prepareStatement("SELECT * FROM " + tableName + " WHERE headline LIKE (?) ");
            queryString = "%" + headline + "%";
        }

        stmt.setString(1, queryString);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            Article article = new Article(rs.getString("headline"), rs.getString("author"), rs.getString("url"));
            articles.add(article);
        }

        stmt.close();
        rs.close();
        conn.close();

        return articles;
    }

    public ArrayList<Article> searchArticleByTags(String tag) throws SQLException {
        ArrayList<Article> articles = new ArrayList<>();
        Connection conn = db.getConnection();
        PreparedStatement stmt = conn.prepareStatement("select articles.headline from articles,tag,articletag" +
                " where tag.name = (?) and tag.id = articletag.tag_id and articletag.article_id = articles.id");
        stmt.setString(1, tag);

        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            String headline = rs.getString(1);
            articles.add(searchHeadline(headline, true).get(0));
        }
        return articles;
    }

}
