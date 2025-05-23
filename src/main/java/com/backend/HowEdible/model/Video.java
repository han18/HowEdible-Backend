package com.backend.HowEdible.model;
import com.backend.HowEdible.model.Comment;

import jakarta.persistence.*;

import java.util.List;
import java.sql.Timestamp;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "videos")
public class Video {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonBackReference
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
    @JsonIgnore // this removes the content field from being included in api responses
    private byte[] content;

    @Column(name = "upload_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp uploadDate;
    
    @Column(nullable = false)
    private String title = "Untitled Video"; // the default title
    
    
    // this is the comment filed in the video entity and it's a one to many relationship added 02/02/2025
    // and also to delete comments when video is deleted 
    @OneToMany(mappedBy = "video", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();
    
    
    // =================================================================== //
    // from here down are the setters and getters
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

    public Timestamp getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Timestamp uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    // re added setContent method to fix compilation error
    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }
    
    // getters & setters for the comment filed
    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
