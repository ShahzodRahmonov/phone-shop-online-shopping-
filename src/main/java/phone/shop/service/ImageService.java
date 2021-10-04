package phone.shop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import phone.shop.dto.ImageDTO;
import phone.shop.entity.ImageEntity;
import phone.shop.exp.ItemNotFoundException;
import phone.shop.repository.ImageRepository;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Optional;
import java.util.UUID;

@Service
public class ImageService {
    @Autowired
    private ImageRepository imageRepository;
    @Value("${image.folder.url}")
    private String imageFolderUrl;
    @Value("${image.url}")
    private String imageUrl;

    public ImageDTO saveToSystem(MultipartFile file) {
        try {
            String filePath = getYmDString(); // 2021/07/13
            String fileType = file.getContentType().split("/")[1]; // png, jpg, jpeg
            String fileToken = UUID.randomUUID().toString();
            String fileUrl = filePath + "/" + fileToken + "." + fileType; // sdasdasdasdasdas.png
            // 2021/07/13/ + adsadasdasdasda + . + png
            File folder = new File(imageFolderUrl + filePath); //  uploads/2021/07/29
            if (!folder.exists()) {
                folder.mkdirs();
            }

            // save to system
            Path path = Paths.get(imageFolderUrl + fileUrl);

            Files.copy(file.getInputStream(), path);

            ImageDTO dto = this.createImage(file, filePath, fileType, fileToken);

            return dto;
        } catch (Exception e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
    }

    public Resource load(String token) {
        try {
            ImageEntity image = this.getImage(token);
            Path file = Paths.get(imageFolderUrl + image.getPath() + "/" + image.getToken() + "." + image.getType()); //
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    public byte[] getImg(String token) {
        try {
            ImageEntity image = this.getImage(token);
            String path = imageFolderUrl + image.getPath() + "/" + image.getToken() + "." + image.getType();

            byte[] imageInByte;

            BufferedImage originalImage;
            try {
                originalImage = ImageIO.read(new File(path));
            } catch (Exception e) {
                return new byte[0];
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            ImageIO.write(originalImage, "png", baos);

            baos.flush();
            imageInByte = baos.toByteArray();
            baos.close();
            return imageInByte;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    public static String getYmDString() {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
        int day = Calendar.getInstance().get(Calendar.DATE);

        return year + "/" + month + "/" + day + "/";
    }

    public void deleteAttach(Integer id) {
//        if (id == null) return;
//
//        Attach attach = this.getById(id);
//
//        String attachName = attach.getName();
//        String path = attach.getPath();
//
//        File file = new File(attachFolder + path + attachName);
//
//        if (file.exists()) {
//            file.delete();
//        }
    }

    private ImageDTO createImage(MultipartFile file, String filePath, String fileType, String fileToken) {
        long size = file.getSize();
        ImageEntity imageEntity = new ImageEntity();
        imageEntity.setType(fileType);
        imageEntity.setSize(size);
        imageEntity.setPath(filePath);
        imageEntity.setToken(fileToken);
        imageEntity.setCreatedDate(LocalDateTime.now());
//        try {
//            imageEntity.setContent(file.getBytes());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        this.imageRepository.save(imageEntity);

        ImageDTO dto = new ImageDTO();
        dto.setPath(filePath);
        dto.setType(fileType);
        dto.setSize(size);
        dto.setToken(fileToken);
        dto.setUrl(imageUrl + fileToken);
        dto.setId(imageEntity.getId());
        return dto;

    }

    public ImageEntity getImage(String token) {
        Optional<ImageEntity> optional = this.imageRepository.findByToken(token);
        return optional.orElseThrow(() -> new ItemNotFoundException("Item Not Found"));

    }

    public ImageEntity get(Integer id) {
        return imageRepository.findById(id).orElseThrow(() -> new ItemNotFoundException("Image Not fFound"));
    }

    public ImageDTO getImage(Integer id) {
        ImageEntity entity = get(id);
        ImageDTO dto = new ImageDTO();
        dto.setId(entity.getId());
        dto.setUrl(imageUrl + entity.getToken());
        return dto;
    }

    public ImageDTO getImage(ImageEntity entity) {
        ImageDTO dto = new ImageDTO();
        dto.setId(entity.getId());
        dto.setUrl(imageUrl + entity.getToken());
        return dto;
    }

    public void deleteImage(Integer id) {
        ImageEntity image = get(id);

        File file = new File(imageFolderUrl + image.getPath() + "/" + image.getToken() + "." + image.getType());

        if (file.exists()) {
            boolean result = file.delete();
            if (result)
                imageRepository.delete(image);
        }
    }

    public void deleteImage(ImageEntity image) {
        File file = new File(imageFolderUrl + image.getPath() + "/" + image.getToken() + "." + image.getType());

        if (file.exists()) {
            boolean result = file.delete();
            if (result)
                imageRepository.delete(image);
        }
    }

}
