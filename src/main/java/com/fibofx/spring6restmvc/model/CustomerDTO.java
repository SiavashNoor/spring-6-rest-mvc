package com.fibofx.spring6restmvc.model;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class CustomerDTO {


    @NotNull
    @NotBlank
    private String name;

    private UUID id;
    private Integer version;
    private LocalDateTime createdDate;
    private LocalDateTime updateDate ;

}
