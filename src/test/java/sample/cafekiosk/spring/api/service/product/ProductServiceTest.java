package sample.cafekiosk.spring.api.service.product;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import sample.cafekiosk.spring.api.service.product.request.ProductCreateServiceRequest;
import sample.cafekiosk.spring.api.service.product.response.ProductResponse;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;
import sample.cafekiosk.spring.domain.product.ProductSellingType;
import sample.cafekiosk.spring.domain.product.ProductType;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static sample.cafekiosk.spring.domain.product.ProductSellingType.SELLING;
import static sample.cafekiosk.spring.domain.product.ProductType.HANDMADE;

@ActiveProfiles("test")
@SpringBootTest
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @BeforeAll
    static void beforeAll() {
        // before class
    }

    @BeforeEach
    static void setUp() {
        // before method
        // 각 테스트 입장에서 봤을 때 : 아얘 몰라도 테스트를 이해하는데 문제가 없는가?
        // 수정해도 모든 테스트에 영향을 주지 않는가?
        // ex) 연관관계를 가진 엔티티 생성
    }

    @AfterEach
    void tearDown() {
        productRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("신규 상품을 등록한다. 상품번호는 가장 최근 상품의 상품번호에서 1 증가한 값이다.")
    void createProduct() {
        // given
        Product product1 = createProduct("001", HANDMADE, SELLING, "아메리카노", 4000);
        productRepository.save(product1);

        ProductCreateServiceRequest request = ProductCreateServiceRequest.builder()
                .type(HANDMADE)
                .sellingType(SELLING)
                .name("카푸치노")
                .price(5000)
                .build();

        // when
        ProductResponse productResponse = productService.createProduct(request);

        // then
        assertThat(productResponse)
                .extracting("productNumber", "type", "sellingType", "name", "price")
                .contains("002", HANDMADE, SELLING, "카푸치노", 5000);

        List<Product> products = productRepository.findAll();
        assertThat(products).hasSize(2)
                .extracting("productNumber", "type", "sellingType", "name", "price")
                .containsExactlyInAnyOrder(
                        tuple("001", HANDMADE, SELLING, "아메리카노", 4000),
                        tuple("002", HANDMADE, SELLING, "카푸치노", 5000)
                );
    }

    @Test
    @DisplayName("상품이 하나도 없는 경우 신규 상품을 등록하면 상품번호는 001이다.")
    void createProductWhenProductsIsEmpty() {
        // given
        ProductCreateServiceRequest request = ProductCreateServiceRequest.builder()
                .type(HANDMADE)
                .sellingType(SELLING)
                .name("카푸치노")
                .price(5000)
                .build();

        // when
        ProductResponse productResponse = productService.createProduct(request);

        // then
        assertThat(productResponse)
                .extracting("productNumber", "type", "sellingType", "name", "price")
                .contains("001", HANDMADE, SELLING, "카푸치노", 5000);

        List<Product> products = productRepository.findAll();
        assertThat(products).hasSize(1)
                .extracting("productNumber", "type", "sellingType", "name", "price")
                .contains(
                        tuple("001", HANDMADE, SELLING, "카푸치노", 5000)
                );
    }

    // 테스트 픽스처 생성 메서드의 파라미터는 테스트 클래스 내에서 필요한 것만 남기기
    // 굳이 테스트에 필요하지 않은 파라미터는 제외하고 고정 값으로 설정하는 것도 관리하기 좋음
    // 다른 클래스에서 재사용시 주입받는 파라미터를 필요에 맞게 조정하면 됨
    private Product createProduct(String productNumber, ProductType type, ProductSellingType sellingStatus, String name, int price) {
        return Product.builder()
                .productNumber(productNumber)
                .type(type)
                .sellingType(sellingStatus)
                .name(name)
                .price(price)
                .build();
    }

}