package com.pce.domain.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.Relation;

import java.io.Serializable;

/**
 * Created by Leonardo Tarjadi on 28/12/2016.
 */
@Relation(collectionRelation = "gDriveFileList")
public class GDriveFileDto extends ResourceSupport implements DomainObjectDTO, Serializable {

  @ReadOnlyProperty
  private long gDriveFileId;

  @ReadOnlyProperty
  private String creationDate;
  @ReadOnlyProperty
  private String updatedDate;

  private String fileId;

  private String fileName;

  private String webContentLink;

  private String webViewLink;

  private String thumbnailLink;

  private String iconLink;

  private String mimeType;

  private String parentFolderIds;

  public GDriveFileDto() {
  }

  @JsonCreator
  public GDriveFileDto(@JsonProperty("gDriveFileId") long gDriveFileId,
                       @JsonProperty("fileId") String fileId,
                       @JsonProperty("fileName") String fileName,
                       @JsonProperty("creationDate") String creationDate,
                       @JsonProperty("updatedDate") String updatedDate,
                       @JsonProperty("webContentLink") String webContentLink,
                       @JsonProperty("thumbnailLink") String thumbnailLink,
                       @JsonProperty("iconLink") String iconLink,
                       @JsonProperty("mimeType") String mimeType,
                       @JsonProperty("webViewLink") String webViewLink,
                       @JsonProperty("parentFolderIds") String parentFolderIds) {
    this.gDriveFileId = gDriveFileId;
    this.fileId = fileId;
    this.fileName = fileName;
    this.creationDate = creationDate;
    this.updatedDate = updatedDate;
    this.webContentLink = webContentLink;
    this.thumbnailLink = thumbnailLink;
    this.iconLink = iconLink;
    this.mimeType = mimeType;
    this.webViewLink = webViewLink;
    this.parentFolderIds = parentFolderIds;
  }

  public String getFileId() {
    return fileId;
  }

  public void setFileId(String fileId) {
    this.fileId = fileId;
  }

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public String getWebContentLink() {
    return webContentLink;
  }

  public void setWebContentLink(String webContentLink) {
    this.webContentLink = webContentLink;
  }

  public String getThumbnailLink() {
    return thumbnailLink;
  }

  public void setThumbnailLink(String thumbnailLink) {
    this.thumbnailLink = thumbnailLink;
  }

  public long getgDriveFileId() {
    return gDriveFileId;
  }

  public void setgDriveFileId(long gDriveFileId) {
    this.gDriveFileId = gDriveFileId;
  }

  public String getIconLink() {
    return iconLink;
  }

  public void setIconLink(String iconLink) {
    this.iconLink = iconLink;
  }

  public String getMimeType() {
    return mimeType;
  }

  public void setMimeType(String mimeType) {
    this.mimeType = mimeType;
  }

  public String getWebViewLink() {
    return webViewLink;
  }

  public void setWebViewLink(String webViewLink) {
    this.webViewLink = webViewLink;
  }

  public String getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(String creationDate) {
    this.creationDate = creationDate;
  }

  public String getUpdatedDate() {
    return updatedDate;
  }

  public void setUpdatedDate(String updatedDate) {
    this.updatedDate = updatedDate;
  }

  public String getParentFolderIds() {
    return parentFolderIds;
  }

  public void setParentFolderIds(String parentFolderIds) {
    this.parentFolderIds = parentFolderIds;
  }
}
