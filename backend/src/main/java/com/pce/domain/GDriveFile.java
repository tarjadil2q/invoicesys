package com.pce.domain;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Calendar;

/**
 * Created by Leonardo Tarjadi on 29/12/2016.
 */

@Entity
@Table(name = "g_drive_file", schema = "ivs")
public class GDriveFile {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @Column(name = "id", nullable = false, updatable = false)
  private long gDriveFileId;

  @Column(name = "file_id", nullable = false)
  private String fileId;

  @Column(name = "file_name", nullable = false)
  private String fileName;

  @Column(name = "web_content_link")
  private String webContentLink;

  @Column(name = "web_view_link")
  private String webViewLink;

  @Column(name = "thumbnail_link")
  private String thumbnailLink;

  @Column(name = "icon_link")
  private String iconLink;

  @Column(name = "mime_type")
  private String mimeType;

  @ManyToOne(cascade = CascadeType.REFRESH)
  @JoinColumn(name = "pce_id", referencedColumnName = "id")
  private Pce pce;

  @CreationTimestamp
  @Column(name = "creation_date", insertable = true, updatable = false)
  private Calendar creationDate;

  @UpdateTimestamp
  private Calendar updatedDate;

  public GDriveFile() {
  }

  public GDriveFile(String fileId, String fileName, String webContentLink, String thumbnailLink,
                    String iconLink, String mimeType, String webViewLink, Pce pce) {
    this.fileId = fileId;
    this.fileName = fileName;
    this.webContentLink = webContentLink;
    this.thumbnailLink = thumbnailLink;
    this.iconLink = iconLink;
    this.pce = pce;
    this.mimeType = mimeType;
    this.webViewLink = webViewLink;
  }

  public long getgDriveFileId() {
    return gDriveFileId;
  }

  public void setgDriveFileId(long gDriveFileId) {
    this.gDriveFileId = gDriveFileId;
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

  public Pce getPce() {
    return pce;
  }

  public void setPce(Pce pce) {
    this.pce = pce;
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

  public Calendar getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(Calendar creationDate) {
    this.creationDate = creationDate;
  }

  public Calendar getUpdatedDate() {
    return updatedDate;
  }

  public void setUpdatedDate(Calendar updatedDate) {
    this.updatedDate = updatedDate;
  }
}
