package sample.cafekiosk.spring.api.service.product;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import sample.cafekiosk.spring.domain.product.ProductRepository;

@Component
@RequiredArgsConstructor
public class ProductNumberFactory {

    private final ProductRepository productRepository;

    public String createNextProductNumber() { // 가장 최근의 productNumber를 조회해 +1 처리 후 양식에 맞게 반환

        String latestProductNumber = productRepository.findLatestProduct();
        if(latestProductNumber == null) {
            return "001";
        }

        Integer nextProductNumberInt = Integer.parseInt(latestProductNumber) + 1;

        return String.format("%03d", nextProductNumberInt);
    }
}
