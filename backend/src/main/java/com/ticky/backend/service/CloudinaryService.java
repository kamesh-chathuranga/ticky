package com.ticky.backend.service;

import com.cloudinary.Cloudinary;
import com.ticky.backend.exception.ImageUploadException;
import com.ticky.backend.util.ImageUploadUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryService {
    private final Cloudinary cloudinary;

    @Transactional
    public String uploadImage(MultipartFile file) {
        try {
            final String fileName = ImageUploadUtil.getFileName(file.getOriginalFilename());
            ImageUploadUtil.assertAllowed(file, ImageUploadUtil.IMAGE_PATTERN);
            final Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(), Map.of("public_id", fileName));
            return (String) uploadResult.get("secure_url");
        } catch (IOException e) {
            throw new ImageUploadException("Failed to upload image");
        }
    }
}
