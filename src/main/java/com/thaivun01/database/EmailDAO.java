package com.thaivun01.database;

import com.thaivun01.beans.BoostedEmail;
import com.thaivun01.beans.ConfigurationBean;
import com.thaivun01.beans.EmailPreview;
import com.thaivun01.beans.FolderBean;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.collections.ObservableList;

/**
 * DAO Interface
 * 
 * @author Thai-vu Nguyen
 */
public interface EmailDAO {
    
    /** Folder **/
    
     /**
     * Get an observable list of all FolderBeans
     * @return Observable list of FolderBean objects
     * @throws SQLException 
     */
    public ObservableList<FolderBean> getAllFoldersTree() throws SQLException;
    
    /**
     * Creates a folder into the database
     * 
     * @param folder
     * @return number of rows affected
     * @throws SQLException 
     */
    public int createFolder(String folder) throws SQLException;
    
    /**
     * Removes a folder from the database
     * 
     * @param folder_id
     * @return number of rows affected
     * @throws SQLException 
     */
    public int removeFolder(int folder_id) throws SQLException;
    
    /**
     * Retrieves all the folders in the database in the form of FolderBean inside of an ArrayList
     * @return ArrayList of FolderBean
     * @throws SQLException 
     */
    public ArrayList<FolderBean> getAllFolders() throws SQLException;
    
     /**
     * Updates the Email's Folder
     * 
     * @param be BoostedEmail
     * @return number of rows affected
     * @throws SQLException 
     */
    public int updateEmailFolder(BoostedEmail be) throws SQLException;
    
    
    /**
     * Updates the Email's Folder
     * 
     * @param email_id int
     * @param folder_id int
     * @return number of rows affects
     * @throws SQLException 
     */
    public int updateEmailFolder(int email_id, int folder_id) throws SQLException;
    
    
    
    /** Email **/
    
     /**
     * Saves a BoostedEmail into the database
     * @param be BoostedEmail
     * @return number of rows affected
     * @throws SQLException 
     */
    public int saveEmail(BoostedEmail be) throws SQLException;
    
    /**
     * Retrives all BoostedEmail by folder_id from the database
     * @param folder_id int
     * @return ArrayList of BoostedEmail
     * @throws SQLException 
     */
    public ArrayList<BoostedEmail> getAllEmailsByFolder(int folder_id) throws SQLException;
    
    /**
     * Deletes an email from the database
     * @param id int
     * @return rows affected
     * @throws SQLException 
     */
    public int deleteEmail(int id) throws SQLException;
    
    public ObservableList<EmailPreview> getEmailPreviewByFolder(int folder_id) throws SQLException;
    public BoostedEmail getEmailById(int email_id) throws SQLException;
   
}
