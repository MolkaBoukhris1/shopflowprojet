package com.shopflow.shpflow.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
//nom de tableau fi base donner
@Table(name = "categories")
// data : generer automatique hashcode getter setter.... |@NoArgsConstructor @AllArgsConstructor= Constructeurs -vide /avec tous les champs:
@Data @NoArgsConstructor @AllArgsConstructor @Builder //builder pour creer object comme ca : Category c = Category.builder().nom("Electronics").build();
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})  // ← AJOUTER
public class Category {
//generer attribut auto genere comme cle primerer  @GeneratedValue(strategy = GenerationType.IDENTITY) =auto incrimentation
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    private String description;
    //partie louta categorie  
// @JsonIgnore évite boucle infinie JSON:Category → parent → Category → parent → ...
    @JsonIgnore
    
    //“Je charge les données SEULEMENT quand j’en ai besoin”
    @ManyToOne(fetch = FetchType.LAZY)
    
    @JoinColumn(name = "parent_id")
    private Category parent;

    @JsonIgnore                                                  // ← AJOUTER @JsonIgnore
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    @ToString.Exclude @EqualsAndHashCode.Exclude
    private List<Category> sousCategories;

    @Builder.Default
    private Boolean actif = true;
}