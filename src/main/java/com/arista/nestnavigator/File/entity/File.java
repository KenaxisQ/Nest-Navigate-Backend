package com.arista.nestnavigator.File.entity;

import com.arista.nestnavigator.property.entity.Property;
import jakarta.persistence.*;

@Entity
@Table(name = "MEDIA")
public class File {

    @Id
    private String id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String type;
    @Column(nullable = false)
    private String url;
    @Column(nullable = false)
    private String size;
//    @ManyToOne
//    @JoinColumn(name = "property_id", nullable = false)
//    private Property property;
}
