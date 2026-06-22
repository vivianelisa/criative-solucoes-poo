package entidades;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Chamado extends Registro {

    private String titulo;
    private String descricao;
    private StatusChamado status;
    private CriticidadeChamado criticidade;
    private String idCliente;
    private String nomeCliente;
    private String tecnico;
    private String obra;
    private List<String> anexos;
    private List<Ocorrencia> ocorrencias;

    public Chamado(String titulo, String descricao, String idCliente, String nomeCliente, String obra, CriticidadeChamado criticidade) {
        super();
        this.titulo = titulo;
        this.descricao = descricao;
        this.idCliente = idCliente;
        this.nomeCliente = nomeCliente;
        this.obra = obra;
        this.criticidade = criticidade;
        this.tecnico = "Nao atribuido";
        this.status = StatusChamado.NOVO;
        this.anexos = new ArrayList<>();
        this.ocorrencias = new ArrayList<>();
    }

    public String getTitulo() { 
        return titulo; 
    }

    public void setTitulo(String titulo) { 
        this.titulo = titulo; 
    }

    public String getDescricao() { 
        return descricao; 
    }

    public void setDescricao(String descricao) { 
        this.descricao = descricao; 
    }

    public StatusChamado getStatus() { 
        return status; 
    }

    public void setStatus(StatusChamado status) { 
        this.status = status; 
    }

    public CriticidadeChamado getCriticidade() { 
        return criticidade; 
    }

    public void setCriticidade(CriticidadeChamado criticidade) { 
        this.criticidade = criticidade; 
    }

    public String getIdCliente() { 
        return idCliente; 
    }

    public void setIdCliente(String idCliente) { 
        this.idCliente = idCliente; 
    }

    public String getNomeCliente() { 
        return nomeCliente; 
    }

    public void setNomeCliente(String nomeCliente) { 
        this.nomeCliente = nomeCliente; 
    }

    public String getTecnico() { 
        return tecnico; 
    }

    public void setTecnico(String tecnico) { 
        this.tecnico = tecnico; 
    }

    public String getObra() { 
        return obra; 
    }

    public void setObra(String obra) { 
        this.obra = obra; 
    }

    public List<String> getAnexos() { 
        return anexos; 
    }

    public void setAnexos(List<String> anexos) { 
        this.anexos = anexos; 
    }

    public List<Ocorrencia> getOcorrencias() { 
        return ocorrencias; 
    }

    public void setOcorrencias(List<Ocorrencia> ocorrencias) { 
        this.ocorrencias = ocorrencias; 
    }

    public void avancarEtapa() {
        switch (this.status) {
            case NOVO:
                if (this.tecnico.equals("Nao atribuido")) {
                    System.out.println("ERRO: Atribua um tecnico antes de avancar o chamado!");
                    return;
                }
                this.status = StatusChamado.EM_ANDAMENTO;
                break;
            case EM_ANDAMENTO:
                this.status = StatusChamado.PAUSADO;
                break;
            case PAUSADO:
                this.status = StatusChamado.ENCERRADO;
                break;
            case ENCERRADO:
                System.out.println("Este chamado ja esta encerrado!");
                break;
        }
    }

    public boolean podeEncerrar() {
        if (this.tecnico.equals("Nao atribuido")) {
            System.out.println("ERRO: Chamado sem tecnico atribuido!");
            return false;
        }
        return true;
    }

    public void exibirOcorrencias() {
        if (ocorrencias.isEmpty()) {
            System.out.println("Nenhuma ocorrencia registrada.");
            return;
        }
        System.out.println("\n--- Ocorrencias do Chamado #" + getId() + " ---");
        for (Ocorrencia o : ocorrencias) {
            System.out.println(o);
        }
    }

    @Override
    public String exibirDetalhes() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String listaAnexos = anexos.isEmpty() ? "Nenhum anexo" : String.join(", ", anexos);
        String totalOcorrencias = ocorrencias.isEmpty() ? "Nenhuma" : ocorrencias.size() + " ocorrencia(s)";
        return String.format(
            "Chamado #%s | Cliente: %s | Obra: %s | Tecnico: %s | Titulo: %s | Status: %s | Criticidade: %s | Aberto em: %s | Anexos: %s | Ocorrencias: %s",
            getId(), nomeCliente, obra, tecnico, titulo, status, criticidade,
            fmt.format(getDataAbertura()), listaAnexos, totalOcorrencias);
    }

    @Override
    public String toString() {
        return exibirDetalhes();
    }
}