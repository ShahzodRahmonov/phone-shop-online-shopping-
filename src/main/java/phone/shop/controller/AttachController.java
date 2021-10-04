package phone.shop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import phone.shop.dto.ImageDTO;
import phone.shop.service.ImageService;

@RestController
@RequestMapping("/image")
public class AttachController {
    @Autowired
    private ImageService imageService;

    @GetMapping("/load/{fileToken:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable("fileToken") String fileToken) {

        Resource file = imageService.load(fileToken);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @PostMapping("/upload")
    public ResponseEntity<ImageDTO> handleFileUpload(@RequestParam("file") MultipartFile file) {
        ImageDTO result = imageService.saveToSystem(file);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping(value = "/get/{link:.+}", produces = MediaType.IMAGE_PNG_VALUE)
    public
    @ResponseBody
    byte[] getImage(@PathVariable("link") String link) {

        if (link != null && link.length() > 0) {
            try {
                return this.imageService.getImg(link);
            } catch (Exception e) {
                e.printStackTrace();
                return new byte[0];
            }
        }
        return null;
    }

}
