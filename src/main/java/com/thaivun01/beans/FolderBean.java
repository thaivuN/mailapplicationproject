package com.thaivun01.beans;

import java.util.Objects;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Bean containing folder name and id
 * 
 * @author Thai-vu nguyen
 * @version  10/14/2016
 */
public class FolderBean {
    private StringProperty folderName;
    private IntegerProperty folderID;
    
    /**
     * Default Constructor
     */
    public FolderBean(){
        this("", -1);
    }
    
    /**
     * Constructor taking in a folderName
     * @param folderName 
     */
    public FolderBean(String folderName){
        this(folderName, -1);
    }
    
    /**
     * Constructor taking in a folderName and an ID
     * @param folderName
     * @param folderID 
     */
    public FolderBean (String folderName, int folderID){
        this.folderID = new SimpleIntegerProperty(folderID);
        this.folderName = new SimpleStringProperty(folderName);
    }

    /**
     * Getter for the ID
     * @return ID of the Folder
     */
    public int getFolderID() {
        return folderID.get();
    }
    
    public IntegerProperty getFolderIDProperty(){
        return folderID;
    }

    /**
     * Mutator for the ID of the Folder
     * @param folderID int
     */
    public void setFolderID(int folderID) {
        this.folderID.set(folderID);
    }

    
    /**
     * Getter for the Folder name
     * 
     * @return Name of the folder
     */
    public String getFolderName() {
        return folderName.get();
    }

    public StringProperty getFolderNameProperty(){
        return folderName;
    }
    
    /**
     * Mutator for the Folder name
     * @param folderName String
     */
    public void setFolderName(String folderName) {
        this.folderName.set(folderName);
    }

    /**
     * Hashcode method
     * 
     * @return 
     */
    @Override
    public int hashCode() {
        int hash = 7;
        
        hash = 69 * hash + Objects.hash(this.folderName.get());
        hash =  hash + Objects.hash(this.folderID.get());
        return hash;
    }

    /**
     * Equals method of the FolderBean class
     * 
     * 
     * @param obj Object
     * @return true or false
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final FolderBean other = (FolderBean) obj;
        if (this.folderID.get() != other.folderID.get()) {
            return false;
        }
        
        return this.folderName.get().equals(other.folderName.get());
    }
    
    
}
