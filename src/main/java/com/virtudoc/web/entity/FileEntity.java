package com.virtudoc.web.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Database object that tracks user-generated files that are uploaded to the abstract storage layer. DO NOT create
 * FileEntity objects directly. Instead use the FileService bean to generate a FileEntity after the data is successfully
 * uploaded. Likewise, do not directly delete the FileEntity objects. This operation should be handled by the FileService
 * as well.
 *
 * @see com.virtudoc.web.service.FileService
 * @author ARMmaster17
 */
@Entity
public class FileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotNull
    @NotEmpty
    private String filePath;

    @NotNull
    @NotEmpty
    private String storageType;

    private Date uploadDate;

    /**
     * For use by Hibernate framework serialize. Do not use.
     */
    public FileEntity() {

    }

    /**
     * For internal use inside the FileService class only. Do not use directly.
     * @param filePath .
     * @param storageType .
     * @param uploadDate .
     */
    public FileEntity(@NotNull @NotEmpty String filePath, @NotNull @NotEmpty String storageType, Date uploadDate) {
        this.filePath = filePath;
        this.storageType = storageType;
        this.uploadDate = uploadDate;
    }

    /**
     * Gets the file URI within the current storage layer instance.
     * @return File URI.
     */
    public String getFilePath() {
        return filePath;
    }

    public Integer getId() {
        return id;
    }

    /**
     * Gets the type of storage used by the underlying abstract storage layer. Examples include 'ephemeral' and 'block'.
     * @return Storage type.
     */
    public String getStorageType() {
        return storageType;
    }

    /**
     * Gets the date in which the file was successfully saved in the abstract storage layer.
     * @return Upload date.
     */
    public Date getUploadDate() {
        return uploadDate;
    }
}
