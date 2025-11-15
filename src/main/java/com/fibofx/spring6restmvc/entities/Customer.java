package com.fibofx.spring6restmvc.entities;

import jakarta.persistence.*;
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
    private String name;
}


