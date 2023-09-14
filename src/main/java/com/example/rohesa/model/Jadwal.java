package com.example.rohesa.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;
import java.util.Date;
import javax.persistence.Id;

@Entity
@Table(name = "Jadwal")
@Getter
@Setter
@NoArgsConstructor
public class Jadwal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "uid", nullable = false, unique = true)
    private String uid = UUID.randomUUID().toString();

    @Column(name = "req_description")
    private String reqDescription;

    @Column(name = "description")
    private String description;

    @Column(name = "status")
    private String status;

    @Column(name = "execution_type")
    private String executionType;

    @Column(name = "execution_at")
    private Date executionAt;

    @Column(name = "alasan_gagal")
    private String alasanGagal;
}
