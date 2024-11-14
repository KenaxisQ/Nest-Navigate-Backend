package com.arista.nestnavigator.property.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "SUB_PROPERTY")
//@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
//@DiscriminatorColumn(name = "TYPE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public  class SubProperty {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @GenericGenerator(name="uuid", strategy = "uuid2")
    private String id;
}