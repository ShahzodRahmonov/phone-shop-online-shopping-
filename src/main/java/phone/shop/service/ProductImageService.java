package phone.shop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import phone.shop.dto.ImageDTO;
import phone.shop.entity.ImageEntity;
import phone.shop.entity.ProductImageEntity;
import phone.shop.repository.ProductImageRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductImageService {
    @Autowired
    private ProductImageRepository productImageRepository;
    @Autowired
    private ImageService imageService;

    public void create(Integer imageId, Integer productId) {
        ProductImageEntity productImage = new ProductImageEntity();
        productImage.setProductId(productId);
        productImage.setImageId(imageId);

        this.productImageRepository.save(productImage);
    }

    public void create(List<Integer> imageIdList, Integer productId) {
        imageIdList.forEach(integer -> create(integer, productId));
    }

    public List<ImageDTO> getProductImageDTOList(Integer productId) {
        List<ProductImageEntity> productImageList = productImageRepository.findAllByProductId(productId);
        return productImageList.stream().map(pImage -> imageService.getImage(pImage.getImageId())).collect(Collectors.toList());
    }

    public ImageDTO getProductImageDTO(Integer productId) {
        List<ProductImageEntity> productImageList = productImageRepository.findAllByProductId(productId);
        return productImageList.stream().findFirst()
                .map(pImage -> imageService.getImage(pImage.getImageId())).orElse(null);
    }

    public List<ImageEntity> getProductImageList(Integer productId) {
        return productImageRepository.findAllByProductId(productId).stream().map(e -> e.getImage()).collect(Collectors.toList());
    }
}
