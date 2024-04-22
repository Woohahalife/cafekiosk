package sample.cafekiosk.spring.api.service.product;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sample.cafekiosk.spring.api.service.product.request.ProductCreateServiceRequest;
import sample.cafekiosk.spring.api.service.product.response.ProductResponse;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;

import java.util.List;
import java.util.stream.Collectors;

import static sample.cafekiosk.spring.domain.product.ProductSellingType.forDisplay;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<ProductResponse> getSellingProducts() {

        List<Product> products = productRepository.findAllBySellingTypeIn(forDisplay());

        return products.stream()
                .map(ProductResponse::of)
                .collect(Collectors.toList());
    }

    /**
     * 동시성 이슈 고민
     */
    @Transactional
    public ProductResponse createProduct(ProductCreateServiceRequest request) {

        String latestProductNumber = createNextProductNumber();
        Product product = request.toEntity(latestProductNumber);

        Product savedProduct = productRepository.save(product);

        return ProductResponse.of(savedProduct);
    }

    private String createNextProductNumber() { // 가장 최근의 productNumber를 조회해 +1 처리 후 양식에 맞게 반환

        String latestProductNumber = productRepository.findLatestProduct();
        if(latestProductNumber == null) {
            return "001";
        }

        Integer nextProductNumberInt = Integer.parseInt(latestProductNumber) + 1;

        return String.format("%03d", nextProductNumberInt);
    }

}
