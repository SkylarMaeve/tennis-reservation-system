package skylarmaeve.tennisreservationsystem.dto;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class PriceResponseDto {
    private final BigDecimal price;

    public PriceResponseDto(BigDecimal price) {
        this.price = price;
    }
}
