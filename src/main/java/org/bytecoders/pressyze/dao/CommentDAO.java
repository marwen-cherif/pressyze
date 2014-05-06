package org.bytecoders.pressyze.dao;

import java.util.List;

import org.bytecoders.pressyze.common.Comment;
import org.bytecoders.pressyze.common.User;
import org.bytecoders.pressyze.exceptions.DAOException;

public interface CommentDAO {

	
	public void addComment(Comment comment) throws DAOException;
	
	public Comment findComment(String commentId) throws DAOException;
	
	public void removeComment(Comment comment) throws DAOException;
	
	public void updateComment(Comment comment) throws DAOException;
	
	public List<Comment> findCommentByFactId(String factId) throws DAOException;
	
	public List<Comment> findCommentByFactIdAndUserId(String factId, String userId) throws DAOException;
	
	public User findCommentUser(String commentId) throws DAOException;
}
