package com.backend.HowEdible.service;

import com.backend.HowEdible.model.Video;
import com.backend.HowEdible.model.User;
import com.backend.HowEdible.repository.VideoRepository;
import com.backend.HowEdible.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.sql.Timestamp;

// these are the new impo
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import java.util.List;


import java.io.IOException;

@Service
public class VideoService {

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private UserRepository userRepository;

    public Video uploadVideo(Long userId, MultipartFile file) throws IOException {
        // Retrieve the user from the repository
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Validate file size
        if (file.getSize() > 100 * 1024 * 1024) { // Limit file size to 100MB
            throw new IllegalArgumentException("File size exceeds the 100MB limit");
        }

        // Create a new Video object and populate its fields
        Video video = new Video();
        video.setUser(user);
        video.setFileName(file.getOriginalFilename());
        video.setContent(file.getBytes());
        video.setAspectRatio(calculateAspectRatio(file)); // Placeholder method
        video.setResolution("1920x1080"); // Default resolution

        // Set the URL for the video (you can customize the path logic here)
        String generatedUrl = "/videos/" + System.currentTimeMillis() + "_" + file.getOriginalFilename();
        video.setUrl(generatedUrl);

        // Save the video to the repository
        return videoRepository.save(video);
    }




    public List<Video> getAllVideos(Long userId) {
        return videoRepository.findByUserId(userId);
    }

    public Video findById(Long videoId) {
        return videoRepository.findById(videoId)
                .orElse(null); // Return null if the video doesn't exist
    }

    private String calculateAspectRatio(MultipartFile file) {
        // Placeholder: Implement video aspect ratio calculation logic if needed.
        return "16:9";
    }
    
    // this is implementing pagination 
//    Calls the repository method to retrieve videos with cursor-based pagination.
//    	public List<Video> getPaginatedVideos(Timestamp cursor, int limit) {
//        return videoRepository.findVideosWithPagination(cursor, limit);
//    }
//    	
//    	// new method cursor
//    	public List<Video> getVideosWithPagination(Long lastVideoId, int pageSize) {
//    	    Pageable pageable = PageRequest.of(0, pageSize, Sort.by(Sort.Direction.DESC, "id"));
//
//    	    if (lastVideoId == null || lastVideoId <= 0) {
//    	        return videoRepository.findAll(pageable).getContent();
//    	    } else {
//    	        return videoRepository.findByIdLessThanOrderByIdDesc(lastVideoId, pageable);
//    	    }
//    	}
    	  
}
