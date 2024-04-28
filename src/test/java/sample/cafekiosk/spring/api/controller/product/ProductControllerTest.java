package sample.cafekiosk.spring.api.controller.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import sample.cafekiosk.spring.ControllerTestSupport;
import sample.cafekiosk.spring.api.controller.product.dto.request.ProductCreateRequest;
import sample.cafekiosk.spring.api.service.product.ProductService;
import sample.cafekiosk.spring.api.service.product.response.ProductResponse;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static sample.cafekiosk.spring.domain.product.ProductSellingType.SELLING;
import static sample.cafekiosk.spring.domain.product.ProductType.HANDMADE;

class ProductControllerTest extends ControllerTestSupport {

    @Test
    @DisplayName("신규 상품을 등록한다.")
    void createProduct() throws Exception {
        // given
        ProductCreateRequest request = ProductCreateRequest.builder()
                .type(HANDMADE)
                .sellingType(SELLING)
                .name("아메리카노")
                .price(4000)
                .build();

        // when & then
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/v1/products/new")
                                .content(objectMapper.writeValueAsString(request)) // json으로 역직렬화 ( 역직렬화 대상에 기본 생성자 필요 )
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print()) // 행동 결과에 대한 로그를 보여줌
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("신규 상품을 등록할 때 상품 타입은 필수값이다.")
    void createProductWithoutType() throws Exception {
        // given
        ProductCreateRequest request = ProductCreateRequest.builder()
                .sellingType(SELLING)
                .name("아메리카노")
                .price(4000)
                .build();

        // when & then
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/v1/products/new")
                                .content(objectMapper.writeValueAsString(request)) // json으로 역직렬화 ( 역직렬화 대상에 기본 생성자 필요 )
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print()) // 행동 결과에 대한 로그를 보여줌
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("상품 타입은 필수입니다."))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @DisplayName("신규 상품을 등록할 때 상품 이름은 필수값이다.")
    void createProductWithoutName() throws Exception {
        // given
        ProductCreateRequest request = ProductCreateRequest.builder()
                .type(HANDMADE)
                .sellingType(SELLING)
                .price(4000)
                .build();

        // when & then
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/v1/products/new")
                                .content(objectMapper.writeValueAsString(request)) // json으로 역직렬화 ( 역직렬화 대상에 기본 생성자 필요 )
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print()) // 행동 결과에 대한 로그를 보여줌
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("상품 이름은 필수입니다."))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @DisplayName("신규 상품을 등록할 때 상품 가격은 양수이다.")
    void createProductWithZeroPrice() throws Exception {
        // given
        ProductCreateRequest request = ProductCreateRequest.builder()
                .type(HANDMADE)
                .sellingType(SELLING)
                .name("아메리카노")
                .price(0)
                .build();

        // when & then
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/v1/products/new")
                                .content(objectMapper.writeValueAsString(request)) // json으로 역직렬화 ( 역직렬화 대상에 기본 생성자 필요 )
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print()) // 행동 결과에 대한 로그를 보여줌
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("상품 가격은 양수여야 합니다."))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @DisplayName("판매 상품을 조회한다.")
    void getSellingProducts() throws Exception {
        // given
        List<ProductResponse> result = List.of();

        when(productService.getSellingProducts()).thenReturn(result);
        // when & then
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/v1/products/selling")
                )
                .andDo(MockMvcResultHandlers.print()) // 행동 결과에 대한 로그를 보여줌
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data").isArray()); // 값의 구체적인 전달내용은 Business Layer에서 검증했기 때문에 Presentation Layer에서는 데이터의 형태 전달 성공 여부만 검증함
    }


}