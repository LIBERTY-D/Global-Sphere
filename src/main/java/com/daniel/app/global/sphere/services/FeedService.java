package com.daniel.app.global.sphere.services;


import com.daniel.app.global.sphere.models.Comment;
import com.daniel.app.global.sphere.models.FeedItem;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FeedService {


    public List<FeedItem> getFeeds(){
        List<FeedItem> feeds = List.of(
                FeedItem.builder()
                        .id(1L)
                        .author("Jonas Müller")
                        .avatar("https://i.pravatar.cc/96?img=32")
                        .role("Student")
                        .content("Can someone review my Controller for a layered architecture? I separated DTO ↔ Entity mapping and used a service facade. Feedback welcome 👇")
                        .codeSnippet("""
                            // CourseController.java (snippet)
                            @PostMapping("/courses")
                            public String create(@Valid CourseDto dtos, BindingResult result, Model model){
                              if(result.hasErrors()){ model.addAttribute("errors", result.getAllErrors()); return "course/form"; }
                              courseFacade.create(dtos); // handles mapping + business rules
                              return "redirect:/courses";
                            }
                            """)
                        .link("")
                        .filePath("")
                        .createdAt(LocalDateTime.now().minusHours(6))
                        .likes(42)
                        .comments(List.of(
                                new Comment("Sara Lee", "Looks clean! Maybe move validation to service layer.", null, LocalDateTime.now().minusHours(5)),
                                new Comment("Tom Becker", "Nice use of facade 👌", null, LocalDateTime.now().minusHours(4))
                        ))
                        .build(),

                FeedItem.builder()
                        .id(2L)
                        .author("Sara Lee")
                        .avatar("https://i.pravatar.cc/96?img=12")
                        .role("Instructor")
                        .content("New resources added for Spring MVC students! Check them out.")
                        .codeSnippet("")
                        .link("https://example.com/resources")
                        .filePath("/uploads/sample.pdf")
                        .createdAt(LocalDateTime.now().minusHours(2))
                        .likes(15)
                        .comments(List.of(
                                new Comment("Jonas Müller",
                                        "Thanks! Super helpful 🙏", null, LocalDateTime.now().minusHours(1))
                        ))
                        .build()
        );

        return  feeds;
    }
}
