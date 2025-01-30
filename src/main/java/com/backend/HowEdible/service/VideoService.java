package com.backend.HowEdible.service;

import com.backend.HowEdible.model.Video;
import com.backend.HowEdible.dto.VideoDTO;
import com.backend.HowEdible.model.User;
import com.backend.HowEdible.repository.VideoRepository;
import com.backend.HowEdible.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;
import java.io.IOException;

@Service
public class VideoService {

    private final VideoRepository videoRepository;
    private final UserRepository userRepository;

    // ✅ Constructor-based injection instead of field injection
    public VideoService(VideoRepository videoRepository, UserRepository userRepository) {
        this.videoRepository = videoRepository;
        this.userRepository = userRepository;
    }

    public Video uploadVideo(Long userId, String title, MultipartFile file) throws IOException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (file.getSize() > 100 * 1024 * 1024) {
            throw new IllegalArgumentException("File size exceeds the 100MB limit");
        }

        Video video = new Video();
        video.setUser(user);
        video.setTitle(title); // ✅ Store the video title
        video.setFileName(file.getOriginalFilename());
        video.setContent(file.getBytes());
        video.setAspectRatio("16:9");
        video.setResolution("1920x1080");
        video.setUploadDate(new Timestamp(System.currentTimeMillis()));

        // 🛑 **Set a temporary URL to avoid SQLIntegrityConstraintViolationException**
        video.setUrl("PENDING");

        // ✅ First save the video to generate an ID
        Video savedVideo = videoRepository.save(video);

        // ✅ Now update the URL with the actual video ID
        String generatedUrl = "http://localhost:8080/api/videos/stream/" + savedVideo.getId();
        savedVideo.setUrl(generatedUrl);

        // ✅ Save again with the correct URL
        return videoRepository.save(savedVideo);
    }

    // I added the updated one in the bottom
//    public List<VideoDTO> getAllVideos() {
//        return videoRepository.findAll().stream()
//                .map(VideoDTO::new)
//                .collect(Collectors.toList());
//    }

    public Video findById(Long videoId) {
        return videoRepository.findById(videoId).orElse(null);
    }

    public List<VideoDTO> getPaginatedVideos(Timestamp cursor, int limit) {
        Pageable pageable = PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "uploadDate"));

        List<Video> videos;
        if (cursor != null) {
            videos = videoRepository.findPaginatedVideos(cursor, pageable);
        } else {
            videos = videoRepository.findAll(pageable).getContent();
        }

        return videos.stream().map(VideoDTO::new).collect(Collectors.toList());
    }
    
    // this is the old code in 10/30/2025 to add
//    public List<VideoDTO> getAllVideos() {
//        List<Video> videos = videoRepository.findAllVideosWithUser(); // ✅ Fetch videos with users
//        return videos.stream().map(VideoDTO::new).collect(Collectors.toList());
//    }
    
    public List<VideoDTO> getAllVideos() {
        List<Video> videos = videoRepository.findAll();
        return videos.stream()
                     .map(VideoDTO::new) // ✅ Convert to DTO with username
                     .collect(Collectors.toList());
    }
}
