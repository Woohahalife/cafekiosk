package sample.cafekiosk.unit.order;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import sample.cafekiosk.unit.beverage.Beverage;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 주문일시, 음료리스트
 */
@Getter
@RequiredArgsConstructor
public class Order {

    private final LocalDateTime orderDateTime;
    private final List<Beverage> beverages;
}
