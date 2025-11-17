package com.fibofx.spring6restmvc.entities;

import com.fibofx.spring6restmvc.model.BeerStyle;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;


@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Beer {

    @Id
    @GeneratedValue(generator = "UUID")
    @UuidGenerator
    @Column(length =36,columnDefinition = "varchar",updatable = false,nullable = false)
    private UUID id ;


    @Version
    private Integer version;

    @NotNull
    @NotBlank


    //we are using size beside the length because we want to handle errors with validation before reaching the
    //database constrains.
    //size is a validation constrain
    @Size(max=50)
    // the length is database schema  constrain .
    @Column(length = 50)
    private String beerName;

    @NotNull
    private BeerStyle beerStyle;

    @NotBlank
    @NotNull
    @Size(max=255)
    private String upc;

    private Integer quantityOnHand;

    @NotNull
    private BigDecimal price ;
    private LocalDateTime createdDate;
    private LocalDateTime updateDate;
}
