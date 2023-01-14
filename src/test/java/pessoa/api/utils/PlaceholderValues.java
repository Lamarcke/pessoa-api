package pessoa.api.utils;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import pessoa.api.model.entity.Endereco;
import pessoa.api.model.entity.Pessoa;

import java.util.List;

public class PlaceholderValues {

    public static Endereco getPlaceholderEndereco() {
        Endereco placeholderEndereco = new Endereco();
        placeholderEndereco.setCep("12345-678");
        placeholderEndereco.setCidade("São Paulo");
        placeholderEndereco.setLogradouro("Rua da Amizade");
        placeholderEndereco.setNumero("12");
        placeholderEndereco.setEnderecoPrincipal(true);
        return placeholderEndereco;
    }

    public static Pessoa getPlaceholderPessoa() {
        Pessoa placeholderPessoa = new Pessoa();
        placeholderPessoa.setNome("João Pedro");
        placeholderPessoa.setDataNascimento("18/05/1999");
        Endereco placeholderEndereco = getPlaceholderEndereco();
        List<Endereco> listaDeEnderecos = List.of(placeholderEndereco);
        placeholderPessoa.setEnderecos(listaDeEnderecos);

        return placeholderPessoa;
    }

    public static String getPlaceholderPessoaJson() throws JsonProcessingException {
        Pessoa pessoa = getPlaceholderPessoa();
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter ow = objectMapper.writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(pessoa);
    }
}
