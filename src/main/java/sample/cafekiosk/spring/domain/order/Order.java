package sample.cafekiosk.spring.domain.order;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sample.cafekiosk.spring.domain.orderproduct.OrderProduct;
import sample.cafekiosk.spring.domain.BaseEntity;
import sample.cafekiosk.spring.domain.product.Product;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Entity
@Getter
@Table(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private int totalPrice;

    private LocalDateTime registeredDateTime;

    // mappedBy = "order" : 연관관계의 주인이 order (orderProduct에서 가지고 있는 order로 설정)
    // cascade = CascadeType.ALL : 연관관계 주인이 생성, 삭제, 변경시 같이 작업이 일어나도록 설정
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL) // order-orderProduct간 양방향 관계
    private List<OrderProduct> orderProducts = new ArrayList<>();

    private Order(List<Product> products, LocalDateTime registeredDateTime) {
        this.orderStatus = OrderStatus.INIT;
        this.totalPrice = calculateTotalPrice(products);
        this.registeredDateTime =  registeredDateTime;// 테스트가 어려운 관계로 외부에서 시간을 주입 받음 - 컨트롤러에서부터 가져옴
        this.orderProducts = products.stream()
                .map(product -> new OrderProduct(this, product))
                .collect(Collectors.toList());
    }

    public static Order create(List<Product> products, LocalDateTime registeredDateTime) {
        return new Order(products, registeredDateTime);
    }

    private int calculateTotalPrice(List<Product> products) {
        return products.stream() // 상품 리스트의 총 합계 계산
                .mapToInt(product -> product.getPrice())
                .sum();
    }
}
