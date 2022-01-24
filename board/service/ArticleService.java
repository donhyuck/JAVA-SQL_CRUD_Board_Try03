package board.service;

import java.sql.Connection;
import java.util.List;

import board.dao.ArticleDao;
import board.dto.Article;

public class ArticleService {

	ArticleDao articleDao;

	public ArticleService(Connection conn) {
		articleDao = new ArticleDao(conn);
	}

	public List<Article> getArticles(int page, int itemsInAPage) {
		int limitFrom = (page - 1) * itemsInAPage; // ?번 게시물부터
		int limitTake = itemsInAPage; // ?개를 한번에 보인다.
		return articleDao.getArticles(limitFrom, limitTake);
	}

	public Article getArticle(int id) {
		return articleDao.getArticle(id);
	}

	public int getArticlesCntById(int id) {
		return articleDao.getArticlesCntById(id);
	}

	public int doWrite(String title, String body, int loginedMemberId) {
		return articleDao.doWrite(title, body, loginedMemberId);
	}

	public void doModify(String title, String body, int id) {
		articleDao.doModify(title, body, id);
	}

	public void doDelete(int id) {
		articleDao.doDelete(id);
	}

	public void increaseHit(int id) {
		articleDao.increaseHit(id);
	}

	public List<Article> getArticlesByKeyword(int page, int itemsInAPage, String searchKeyword) {
		int limitFrom = (page - 1) * itemsInAPage; // ?번 게시물부터
		int limitTake = itemsInAPage; // ?개를 한번에 보인다.
		return articleDao.getArticlesByKeyword(limitFrom, limitTake, searchKeyword);
	}

	public int getArticlesCnt(String searchKeyword) {
		return articleDao.getArticlesCnt(searchKeyword);
	}

	public void insertLike(int id, int likeType, int loginedMemberId) {
		articleDao.insertLike(id, likeType, loginedMemberId);
	}

	public int likeCheck(int id, int loginedMemberId) {
		return articleDao.likeCheck(id, loginedMemberId);
	}

	public void deleteLike(int id, int loginedMemberId) {
		articleDao.deleteLike(id, loginedMemberId);
	}

	public void modifyLike(int id, int likeType, int loginedMemberId) {
		articleDao.modifyLike(id, likeType, loginedMemberId);
	}

	public int getLikeVal(int id, int likeType) {
		return articleDao.getLikeVal(id, likeType);
	}
}
