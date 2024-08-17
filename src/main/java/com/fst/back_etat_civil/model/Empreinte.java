package com.fst.back_etat_civil.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Arrays;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "empreinte")
public class Empreinte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column( name = "nom_doigt")
    private String nomDoigt;

    @Column(name = "empreinte_digitale", columnDefinition = "bytea")
    private byte[] empreinteDigitale;

    @Override
    public String toString() {
        return "Empreinte{" +
                "id=" + id +
                ", nomDoigt='" + nomDoigt + '\'' +
                ", empreinteDigitale=" + Arrays.toString(empreinteDigitale) +
                '}';
    }
}
