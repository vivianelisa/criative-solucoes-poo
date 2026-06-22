package entidades;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Ocorrencia {

    private String id;
    private String descricao;
    private String idCliente;
    private String nomeCliente;
    private List<String> anexos;
    private String resposta;
    private StatusOcorrencia status;
    private LocalDateTime dataRegistro;
    private LocalDateTime dataResposta;

    public Ocorrencia(String descricao, String idCliente, String nomeCliente) {
        this.id = UUID.randomUUID().toString().substring(0, 6);
        this.descricao = descricao;
        this.idCliente = idCliente;
        this.nomeCliente = nomeCliente;
        this.anexos = new ArrayList<>();
        this.status = StatusOcorrencia.ABERTA;
        this.dataRegistro = LocalDateTime.now();
    }

    public String getId() { 
        return id; 
    }

    public String getDescricao() { 
        return descricao; 
    }

    public String getIdCliente() { 
        return idCliente; 
    }

    public String getNomeCliente() { 
        return nomeCliente; 
    }

    public List<String> getAnexos() { 
        return anexos; 
    }

    public String getResposta() { 
        return resposta; 
    }

    public StatusOcorrencia getStatus() { 
        return status; 
    }

    public LocalDateTime getDataRegistro() { 
        return dataRegistro; 
    }

    public LocalDateTime getDataResposta() { 
        return dataResposta; 
    }

    public void setResposta(String resposta) {
        this.resposta = resposta;
        this.dataResposta = LocalDateTime.now();
        this.status = StatusOcorrencia.RESPONDIDA;
    }

    public void setStatus(StatusOcorrencia status) {
        this.status = status;
    }

    public void agendarVistoria() {
        this.status = StatusOcorrencia.VISTORIA_AGENDADA;
    }

    @Override
    public String toString() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String listaAnexos = anexos.isEmpty() ? "Nenhum anexo" : String.join(", ", anexos);
        String respostaStr = resposta != null ? resposta : "Sem resposta";
        String dataRespostaStr = dataResposta != null ? fmt.format(dataResposta) : "Sem resposta";
        return String.format(
            "\nOcorrencia #%s | Cliente: %s | Status: %s | Registrada em: %s" +
            "\nDescricao: %s" +
            "\nAnexos: %s" +
            "\nResposta: %s | Data resposta: %s",
            id, nomeCliente, status, fmt.format(dataRegistro),
            descricao, listaAnexos, respostaStr, dataRespostaStr);
    }
}