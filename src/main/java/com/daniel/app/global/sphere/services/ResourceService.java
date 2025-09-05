package com.daniel.app.global.sphere.services;


import com.daniel.app.global.sphere.Utils.CreatedAtComparator;
import com.daniel.app.global.sphere.annotation.LogAspectAnnotation;
import com.daniel.app.global.sphere.dtos.CreateResourceDto;
import com.daniel.app.global.sphere.models.Resource;
import com.daniel.app.global.sphere.models.User;
import com.daniel.app.global.sphere.repository.ResourceRepository;
import com.daniel.app.global.sphere.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ResourceService {

    private final UserService userService;
    private final UserRepository userRepository;
    private final ResourceRepository repository;

    public List<Resource> getResources() {
        return repository.findAll().stream().sorted(new CreatedAtComparator<>()).collect(Collectors.toList());

    }

    public List<Resource> getFeaturedResources() {
        return repository.findAll().stream().
                sorted(new CreatedAtComparator<>()).limit(5).
                collect(Collectors.toList());


    }

    @Transactional
    @LogAspectAnnotation
    public boolean createResource(CreateResourceDto createResourceDto) {
        log.info("CREATING RESOURCE");
        User user = userService.getAuthenticatedUser();
        Resource newResource = getResource(createResourceDto, user);
        user.getResources().add(newResource);
        user.incrementPostsCount();
        userRepository.save(user);
        return true;
    }


    private static Resource getResource(CreateResourceDto createResourceDto, User user) {
        Resource createResource = new Resource();
        createResource.setAuthor(user.getName());
        createResource.setAuthorId(user.getId());
        createResource.setTitle(createResourceDto.getTitle());
        createResource.setExternalUrl(createResourceDto.getExternalUrl());
        createResource.setContent(createResourceDto.getContent());
        createResource.setImageUrl(createResourceDto.getImageUrl().getName());
        createResource.setDescription(createResourceDto.getDescription());
        createResource.setUser(user);
        return createResource;
    }

}
