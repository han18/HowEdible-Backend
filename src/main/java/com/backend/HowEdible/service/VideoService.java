package com.backend.HowEdible.service;

import com.backend.HowEdible.model.Video;
import com.backend.HowEdible.dto.VideoDTO;
import com.backend.HowEdible.model.User;
import com.backend.HowEdible.repository.VideoRepository;
import com.backend.HowEdible.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;
import java.io.IOException;

@Service
public class VideoService {

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private UserRepository userRepository;

    public Video uploadVideo(Long userId, MultipartFile file) throws IOException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (file.getSize() > 100 * 1024 * 1024) {
            throw new IllegalArgumentException("File size exceeds the 100MB limit");
        }

        Video video = new Video();
        video.setUser(user);
        video.setFileName(file.getOriginalFilename());
        video.setContent(file.getBytes());
        video.setAspectRatio("16:9");
        video.setResolution("1920x1080");
        video.setUploadDate(new Timestamp(System.currentTimeMillis()));

        // 🛑 **Set a temporary URL to avoid the SQLIntegrityConstraintViolationException**
        video.setUrl("PENDING");

        // ✅ First save the video to generate an ID
        Video savedVideo = videoRepository.save(video);

        // ✅ Now update the URL with the actual video ID
        String generatedUrl = "http://localhost:8080/api/videos/stream/" + savedVideo.getId();
        savedVideo.setUrl(generatedUrl);

        // ✅ Save again with the correct URL
        return videoRepository.save(savedVideo);
    }



    public List<VideoDTO> getAllVideos() {
        return videoRepository.findAll().stream()
                .map(VideoDTO::new)
                .collect(Collectors.toList());
    }

    public Video findById(Long videoId) {
        return videoRepository.findById(videoId).orElse(null);
    }

    public List<VideoDTO> getPaginatedVideos(Timestamp cursor, int limit) {
        Pageable pageable = PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "uploadDate"));

        List<Video> videos;
        if (cursor == null) {
            videos = videoRepository.findTopByOrderByUploadDateDesc(pageable);
        } else {
            videos = videoRepository.findByUploadDateBeforeOrderByUploadDateDesc(cursor, pageable);
        }

        return videos.stream().map(VideoDTO::new).collect(Collectors.toList());
    }
}
