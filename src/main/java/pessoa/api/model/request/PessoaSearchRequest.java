package pessoa.api.model.request;

import lombok.Data;
import pessoa.api.utils.PessoaSearchOptions;

@Data
public class PessoaSearchRequest {
    private PessoaSearchOptions by;
    private String query;
}
