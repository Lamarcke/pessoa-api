package pessoa.api.model.entity;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@Setter
@Table(name = "endereco")
public class Endereco {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer id;

    @Column(name = "endereço_principal")
    @ColumnDefault("false")
    private boolean enderecoPrincipal = false;

    @Column(name = "logradouro", nullable = false)
    @NotNull(message = "O campo logradouro é obrigatório.")
    @NotBlank(message = "O campo logradouro não pode estar vazio.")
    private String logradouro;

    @Column(name = "cep", nullable = false)
    @NotNull(message = "O campo cep é obrigatório.")
    @Pattern(regexp = "[0-9]{5}-[0-9]{3}", message = "cep deve estar no formato 99999-999")
    private String cep;

    @Column(name = "numero")
    // Numero e Complemento são parâmetros separados para garantir que numero seja sempre um inteiro.
    // Isso nos permite especificar numeros como "39A" usando "A" como complemento.
    // Utilizar o método "setNumero" com um valor de 0 ou nulo automaticamente transforma o complemento em
    // "S/N" (sem número).
    private String numero;

    @NotNull(message = "O campo cidade é obrigatório.")
    @NotBlank(message = "O campo cidade não pode estar vazio.")
    @Column(name = "cidade", nullable = false)
    private String cidade;


    public void setNumero(String numero) {
        if (numero == null || numero.isBlank()) {
            this.numero = "S/N";
        } else {
            this.numero = numero;
        }

    }


}