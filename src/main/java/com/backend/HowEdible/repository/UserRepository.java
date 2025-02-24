package com.backend.HowEdible.repository;

import com.backend.HowEdible.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {
    
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.videos WHERE u.id = :userId")
    User findByIdWithVideos(@Param("userId") Long userId);
    
    // retrieve user by username
    User findByUsername(String username);  
}



// old code 
//package com.backend.HowEdible.repository;
//
//
//import com.backend.HowEdible.model.User;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//
//import java.util.Optional;
//
////public interface UserRepository extends JpaRepository<User, Long> {
////    Optional<User> findByUsername(String username);
////}
//
//@Repository
//public interface UserRepository extends JpaRepository<User, Long> {
//    User findByUsername(String username);
//    
//    
////    this is to fetch a user along with the their video **video related**    
////    @Query("SELECT u FROM User u LEFT JOIN FETCH u.videos WHERE u.id = :id")
////    User findByIdWithVideos(Long id); // fetch user and their videos
//    
//    @Query("SELECT u FROM User u LEFT JOIN FETCH u.videos WHERE u.id = :userId")
//    User findByIdWithVideos(@Param("userId") Long userId);
//
//    
//}
//
