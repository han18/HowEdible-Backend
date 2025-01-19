package com.backend.HowEdible.model;

import jakarta.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "videos")
public class Video {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "filename", nullable = false)
    private String fileName;

    @Column(name = "url")
    private String url;

    @Column(name = "resolution")
    private String resolution;

    @Column(name = "aspect_ratio")
    private String aspectRatio;

    @Lob
    @Column(name = "content", columnDefinition = "LONGBLOB")
    private byte[] content;

    @Column(name = "upload_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp uploadDate;

    public Video() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getUrl() {
        if (this.url == null || this.url.isEmpty()) {
            return "http://localhost:8080/api/videos/stream/" + this.id;
        }
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public String getAspectRatio() {
        return aspectRatio;
    }

    public void setAspectRatio(String aspectRatio) {
        this.aspectRatio = aspectRatio;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public Timestamp getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Timestamp uploadDate) {
        this.uploadDate = uploadDate;
    }
}
