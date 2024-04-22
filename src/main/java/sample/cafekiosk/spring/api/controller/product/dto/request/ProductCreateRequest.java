package sample.cafekiosk.spring.api.controller.product.dto.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sample.cafekiosk.spring.api.service.product.request.ProductCreateServiceRequest;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductSellingType;
import sample.cafekiosk.spring.domain.product.ProductType;

@Getter
@NoArgsConstructor
public class ProductCreateRequest {

    @NotNull(message = "상품 타입은 필수입니다.")
    private ProductType type;

    @NotNull(message = "상품 판매상태는 필수입니다.") // Enum같은 경우 null 검증만 하면 됨
    private ProductSellingType sellingType;

    @NotBlank(message = "상품 이름은 필수입니다.") // 무조건 문자가 있어야함 (null, 빈문자, 공백 문자 X)
//    @Max(20) : 문자 개수 제한 -> BUT 과연 이 검증은 Controller에서 해야하는게 맞는지 고민해보자 -> 이런 특수한 형태의 검증은 조금 더 안쪽 레이어에서 하는게 맞다고 생각함
//    @NotNull : null만 아니면 됨(빈문자 허용)
//    @NotEmpty : null, 빈문자만 아니면 됨(공백 문자 허용)
    private String name;

    @Positive(message = "상품 가격은 양수여야 합니다.")
    private int price;

    @Builder
    private ProductCreateRequest(ProductType type, ProductSellingType sellingType, String name, int price) {
        this.type = type;
        this.sellingType = sellingType;
        this.name = name;
        this.price = price;
    }

    public ProductCreateServiceRequest toServiceRequest() {
        return ProductCreateServiceRequest.builder()
                .type(type)
                .sellingType(sellingType)
                .name(name)
                .price(price)
                .build();
    }
}
