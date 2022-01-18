package ru.netology.cloudstorage.entity.db;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileInfoKey implements Serializable {

    @NotBlank
    @Column(nullable = false)
    private String fileName;

    @NotBlank
    @Column(nullable = false)
    private String ownerLogin;


}
