package com.sj.ecommerce.utils;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {


    @Autowired
    private Cloudinary cloudinary;

//    public String uploadImage(MultipartFile image) {
//        try {
//            // Upload the image
//            Map<String, Object> uploadResult = cloudinary.uploader().upload(image.getBytes(), ObjectUtils.emptyMap());
//            return (String) uploadResult.get("url");  // Return the URL of the uploaded image
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }

    // compressed the size of image
    public String uploadImage(MultipartFile image) {
        try {
            // Upload the image to Cloudinary
            Map uploadResult = cloudinary.uploader().upload(image.getBytes(), ObjectUtils.emptyMap());

            // Extract the public_id from the uploadResult
            String publicId = (String) uploadResult.get("public_id");

            // Use the Cloudinary URL generation method with transformation
            String imageUrl = cloudinary.url()
                    .transformation(new Transformation()
                            .width(1000)     // Resize the image to width 1000px
                            .height(1000)    // Optionally set a height
                            .crop("fill")    // Ensure the image fills the target size
                            .quality("auto") // Automatically adjust the quality for compression
                            .fetchFormat("auto")  // Automatically select the best format (e.g., WebP, JPEG)
                    )
                    .generate(publicId);  // Use the public_id to generate the URL

            return imageUrl;  // Return the transformed image URL

        } catch (IOException e) {
            e.printStackTrace();
            return null;  // In case of error, return null
        }
    }

}
