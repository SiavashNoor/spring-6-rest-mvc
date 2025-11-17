package com.fibofx.spring6restmvc.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;


@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Customer {


    @Id
    @GeneratedValue(generator = "UUID")
    @UuidGenerator
    @Column(length = 36,columnDefinition = "varchar",updatable = false,nullable = false)
    private UUID id;


    @Version
    private Integer version;

    private LocalDateTime createdDate;
    private LocalDateTime updateDate ;

    @NotBlank
    @NotNull
    @Size(max=50)
    @Column(length = 50)
    private String name;

}


