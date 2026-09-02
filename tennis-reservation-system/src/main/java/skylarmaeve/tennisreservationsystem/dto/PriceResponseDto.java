package skylarmaeve.tennisreservationsystem.dto;

import lombok.Getter;

@Getter
public class PriceResponseDto {
    private Integer price;
    public PriceResponseDto(Integer price) {
        this.price = price;
    }
}
