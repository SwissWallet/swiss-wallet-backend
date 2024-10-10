package com.swiss.wallet.utils;

import com.swiss.wallet.entity.StatusProduct;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

@Component
public class UtilsProduct {

    public StatusProduct checkAmount(Long amount){
        if (amount <= 0){
            return StatusProduct.OUT_OF_STOCK;
        }else if (amount >= 1 && amount <= 10){
            return StatusProduct.LOW_STOCK;
        }
        return StatusProduct.AVAILABLE;
    }

    public String encodeImageToBase64(MultipartFile file, int width, int height) throws IOException {
        BufferedImage originalImage = ImageIO.read(file.getInputStream());

        BufferedImage resizedImage = Thumbnails.of(originalImage)
                .size(width, height)
                .outputQuality(1.0)  // Maximum quality
                .asBufferedImage();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(resizedImage, "png", outputStream);
        byte[] imageBytes = outputStream.toByteArray();
        return Base64.getEncoder().encodeToString(imageBytes);
    }

}
