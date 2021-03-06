package es.thalesalv.cartservice.cart.domain.model;

import java.util.ArrayList;
import java.util.List;

import javax.money.MonetaryAmount;
import javax.persistence.Convert;

import es.thalesalv.cartservice.cart.application.bean.MonetaryAmountConverter;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Cart {

    private Integer id;
    private List<LineItem> lineItems = new ArrayList<>();
    private String customerId;

    @Convert(converter = MonetaryAmountConverter.class)
    private MonetaryAmount total;

    public void addLineItem(LineItem lineItem) {
        this.lineItems.add(lineItem);
    }
}
