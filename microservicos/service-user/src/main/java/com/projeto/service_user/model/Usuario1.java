package com.projeto.service_user.model;

import java.util.UUID;

import jakarta.persistence.*;
import lombok.Data;

/**
 * Usuario
 */

@Entity 
@Data 
@Table(name = "usuarios")
public class Usuario1 {
    @Id 
    @GeneratedValue(strategy= GenerationType.UUID)
    private UUID id;
    
}