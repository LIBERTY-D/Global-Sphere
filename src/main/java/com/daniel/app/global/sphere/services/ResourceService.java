package com.daniel.app.global.sphere.services;


import com.daniel.app.global.sphere.Utils.CommonUtil;
import com.daniel.app.global.sphere.Utils.CreatedAtComparator;
import com.daniel.app.global.sphere.annotation.LogAspectAnnotation;
import com.daniel.app.global.sphere.dtos.CreateResourceDto;
import com.daniel.app.global.sphere.dtos.EditResourceDto;
import com.daniel.app.global.sphere.exceptions.DataIntegrityException;
import com.daniel.app.global.sphere.exceptions.FileHandlerException;
import com.daniel.app.global.sphere.models.Resource;
import com.daniel.app.global.sphere.models.User;
import com.daniel.app.global.sphere.repository.ResourceRepository;
import com.daniel.app.global.sphere.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
        try {
            userRepository.save(user);
            userRepository.flush();
        } catch (DataIntegrityViolationException exp) {
            throw new DataIntegrityException("Resource field too long", createResourceDto);
        }

        return true;
    }


    private static Resource getResource(CreateResourceDto createResourceDto, User user) {
        Resource createResource = new Resource();
        createResource.setAuthor(user.getName());
        createResource.setAuthorId(user.getId());
        createResource.setTitle(createResourceDto.getTitle());

        createResource.setExternalUrl(CommonUtil.checkLinkValidation(createResourceDto.getExternalUrl(), createResourceDto));
        createResource.setContent(createResourceDto.getContent());
        MultipartFile file = createResourceDto.getImageUrl();
        if (file != null && !file.isEmpty()) {
            try {
                createResource.setImageUrl(file.getBytes());
            } catch (IOException ioException) {
                throw new FileHandlerException("imageUrl", ioException.getMessage());
            }
        }
        createResource.setDescription(createResourceDto.getDescription());
        createResource.setUser(user);
        return createResource;
    }

    private static Resource getResource(EditResourceDto editResourceDto,
                                        User user) {
        Resource createResource = new Resource();
        createResource.setAuthor(user.getName());
        createResource.setAuthorId(user.getId());
        createResource.setTitle(editResourceDto.getTitle());
        createResource.setExternalUrl(CommonUtil.checkLinkValidation(editResourceDto.getExternalUrl(), editResourceDto));
        createResource.setContent(editResourceDto.getContent());
        MultipartFile file = editResourceDto.getImage();
        if (file != null && !file.isEmpty()) {
            try {
                createResource.setImageUrl(file.getBytes());
            } catch (IOException ioException) {
                throw new FileHandlerException("imageUrl", ioException.getMessage());
            }
        }
        createResource.setDescription(editResourceDto.getDescription());
        createResource.setUser(user);
        return createResource;
    }

    public byte[] resourceImage(Long resourceId) {
        var resource = repository.findById(resourceId).orElse(new Resource());
        return resource.getImageUrl();
    }

    @Transactional
    public boolean updateResource(Long resourceId, EditResourceDto dto) {
        log.info("UPDATING RESOURCE");
        User user = userService.getAuthenticatedUser();
        Resource resource = repository.findById(resourceId).orElseThrow(() -> new IllegalArgumentException("Resource not found"));
        resource.setTitle(dto.getTitle());
        resource.setDescription(dto.getDescription());
        resource.setContent(dto.getContent());
        String newUrl = CommonUtil.checkLinkValidation(dto.getExternalUrl(), dto);
        resource.setExternalUrl(newUrl);
        MultipartFile file = dto.getImage();
        if (file != null && !file.isEmpty()) {
            try {
                resource.setImageUrl(file.getBytes());
            } catch (IOException e) {
                throw new FileHandlerException("imageUrl", e.getMessage());
            }
        }

        repository.save(resource);
        return true;
    }


    public EditResourceDto getResourceById(Long resourceId) {
        var resource = repository.findById(resourceId).orElse(new Resource());
        return new EditResourceDto(resource.getTitle(),
                resource.getDescription(), resource.getContent(),
                null,
                resource.getExternalUrl());
    }

    public void deleteResource(Long id) {
        repository.deleteById(id);
    }
}
