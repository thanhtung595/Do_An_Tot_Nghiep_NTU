package nguyenthanhtung.datn.dto.services;

import lombok.Data;

@Data
public class UpdateServiceRequestDTO {
    int id;
    String name;
    String description;
    double price;
}
