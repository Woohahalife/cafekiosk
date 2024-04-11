package sample.cafekiosk.unit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sample.cafekiosk.unit.beverage.Americano;
import sample.cafekiosk.unit.beverage.Latte;
import sample.cafekiosk.unit.order.Order;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CafeKioskTest {

    @Test
    void add_manual_test() {
        CafeKiosk kiosk = new CafeKiosk();
        kiosk.add(new Americano());

        System.out.println(">>> 담긴 음료 수 : " + kiosk.getBeverages().size());
        System.out.println(">>> 담긴 음료 : " + kiosk.getBeverages().get(0).getName());
    }

    @Test
//    @DisplayName("음료 1개 추가 테스트")
    @DisplayName("음료 1개를 추가하면 주문 목록에 담긴다.")
    void add() {
        CafeKiosk kiosk = new CafeKiosk();
        kiosk.add(new Americano());

//        assertThat(kiosk.getBeverages().size()).isEqualTo(1);
        assertThat(kiosk.getBeverages()).hasSize(1);
        assertThat(kiosk.getBeverages().get(0).getName()).isEqualTo("아메리카노");
    }

    @Test
    void addSeveralBeverages() {
        CafeKiosk kiosk = new CafeKiosk();
        Americano americano = new Americano();
        kiosk.add(americano, 2);

        assertThat(kiosk.getBeverages()).hasSize(2);
        assertThat(kiosk.getBeverages().get(0)).isEqualTo(americano);
        assertThat(kiosk.getBeverages().get(1)).isEqualTo(americano);
    }

    @Test
    void addZeroBeverages() {
        CafeKiosk kiosk = new CafeKiosk();
        Americano americano = new Americano();

        assertThatThrownBy(() -> kiosk.add(americano, 0))
                .isInstanceOf(IllegalArgumentException.class) // 발생하는 예외를 테스트
                .hasMessage("음료는 1잔 이상 주문하실 수 있습니다."); // 예외 메세지를 테스트
    }

    @Test
    void remove() {
        CafeKiosk kiosk = new CafeKiosk();
        Americano americano = new Americano();

        kiosk.add(americano);
        assertThat(kiosk.getBeverages()).hasSize(1);

        kiosk.remove(americano);
        assertThat(kiosk.getBeverages()).isEmpty();
    }

    @Test
    void clear() {
        CafeKiosk kiosk = new CafeKiosk();
        Americano americano = new Americano();
        Latte latte = new Latte();

        kiosk.add(americano);
        kiosk.add(latte);
        assertThat(kiosk.getBeverages()).hasSize(2);

        kiosk.clear();
        assertThat(kiosk.getBeverages()).isEmpty();
    }

    @Test
    @DisplayName("주문 목록에 담긴 상품들의 총 금액을 계산할 수 있다.")
    void calculateTotalPrice() {
        // given
        CafeKiosk kiosk = new CafeKiosk();
        Americano americano = new Americano();
        Latte latte = new Latte();

        kiosk.add(americano);
        kiosk.add(latte);

        // when
        int totalPrice = kiosk.calculateTotalPrice();

        // then
        assertThat(totalPrice).isEqualTo(8500);
    }

    @Test
    void createOrderWithCurrentTime() {
        CafeKiosk kiosk = new CafeKiosk();
        Americano americano = new Americano();

        kiosk.add(americano);

        Order order = kiosk.createOrder(LocalDateTime.of(2024, 4, 8, 14, 0));

        assertThat(order.getBeverages()).hasSize(1);
        assertThat(order.getBeverages().get(0).getName()).isEqualTo("아메리카노");
    }

    @Test
    void createOrderOutsideOpenTime() {
        CafeKiosk kiosk = new CafeKiosk();
        Americano americano = new Americano();

        kiosk.add(americano);

        assertThatThrownBy(() -> kiosk.createOrder(LocalDateTime.of(2024, 4, 8, 9, 59)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 시간이 아닙니다. 관리자에게 문의하세요");
    }
}