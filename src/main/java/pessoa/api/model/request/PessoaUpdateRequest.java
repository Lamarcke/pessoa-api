package pessoa.api.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import pessoa.api.model.entity.Endereco;
import pessoa.api.model.entity.Pessoa;

import java.util.List;

@Data
public class PessoaUpdateRequest {
    private String nome;
    private String dataNascimento;
    private List<Endereco> enderecos;
}
