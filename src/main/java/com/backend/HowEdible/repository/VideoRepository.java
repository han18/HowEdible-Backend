package com.backend.HowEdible.repository;

import com.backend.HowEdible.model.Video;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

public interface VideoRepository extends JpaRepository<Video, Long> {

    List<Video> findByUserId(Long userId);

    @Query("SELECT v FROM Video v WHERE (:cursor IS NULL OR v.uploadDate < :cursor) ORDER BY v.uploadDate DESC")
    List<Video> findByUploadDateBeforeOrderByUploadDateDesc(@Param("cursor") Timestamp cursor, Pageable pageable);

    List<Video> findTopByOrderByUploadDateDesc(Pageable pageable);

    Page<Video> findByIdLessThanOrderByIdDesc(Long lastVideoId, Pageable pageable);
    
    @Query("SELECT v FROM Video v WHERE v.uploadDate < :cursor ORDER BY v.uploadDate DESC")
    List<Video> findVideos(@Param("cursor") LocalDateTime cursor, Pageable pageable);
    
    @Query("SELECT v FROM Video v WHERE v.uploadDate < :cursor ORDER BY v.uploadDate DESC")
    List<Video> findTopNByUploadDateBeforeOrderByUploadDateDesc(@Param("cursor") Timestamp cursor, Pageable pageable);

    @Query("SELECT v FROM Video v WHERE v.uploadDate < :cursor ORDER BY v.uploadDate DESC")
    List<Video> findPaginatedVideos(@Param("cursor") Timestamp cursor, Pageable pageable);


}
