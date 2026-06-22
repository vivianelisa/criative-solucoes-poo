import entidades.Chamado;
import entidades.Cliente;
import entidades.CriticidadeChamado;
import entidades.Ocorrencia;
import entidades.StatusChamado;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Utilitário de testes — popula a lista de chamados com dados fictícios.
 * Para ativar/desativar, basta alterar MODO_TESTE em MenuPrincipal.
 */
public class DadosTeste {

    public static void popularChamados(List<Chamado> listaChamados) {

        // ── Chamado 1 — NOVO, sem técnico, criticidade ALTA ──────────────────
        Chamado c1 = new Chamado(
            "Infiltração na cobertura",
            "Cliente relata goteiras no teto do escritório após chuva forte.",
            "CLI001", "Construtora Horizonte",
            "Edifício Horizonte - Bloco A",
            CriticidadeChamado.ALTA
        );
        c1.setId("TST001");
        c1.setDataAbertura(LocalDateTime.now().minusDays(5));
        listaChamados.add(c1);

        // ── Chamado 2 — EM_ANDAMENTO, técnico atribuído, criticidade MEDIA ───
        Chamado c2 = new Chamado(
            "Revisão de projeto elétrico",
            "Solicitação de revisão do projeto elétrico para adequação à norma NBR 5410.",
            "CLI002", "Studio Arq&Design",
            "Sede Studio Arq&Design - Sala 204",
            CriticidadeChamado.MEDIA
        );
        c2.setId("TST002");
        c2.setTecnico("tecnico01");
        c2.setStatus(StatusChamado.EM_ANDAMENTO);
        c2.setDataAbertura(LocalDateTime.now().minusDays(2));
        Ocorrencia oc2 = new Ocorrencia(
            "Planta elétrica original não foi localizada no arquivo.",
            "CLI002", "Studio Arq&Design"
        );
        c2.getOcorrencias().add(oc2);
        listaChamados.add(c2);

        // ── Chamado 3 — PAUSADO, com anexo, criticidade BAIXA ────────────────
        Chamado c3 = new Chamado(
            "Ajuste de layout sala de reuniões",
            "Reposicionamento de divisórias e revisão de iluminação.",
            "CLI003", "Grupo Vértice",
            "Torre Vértice - 10º andar",
            CriticidadeChamado.BAIXA
        );
        c3.setId("TST003");
        c3.setTecnico("maria");
        c3.setStatus(StatusChamado.PAUSADO);
        c3.setDataAbertura(LocalDateTime.now().minusDays(7));
        c3.getAnexos().add("[Planta baixa] plantas/vertice_sala_reunioes.pdf");
        listaChamados.add(c3);

        // ── Chamado 4 — ENCERRADO, com anexo, criticidade MEDIA ──────────────
        Chamado c4 = new Chamado(
            "Vistoria pós-obra apartamento 302",
            "Verificação de acabamentos e entrega das chaves ao cliente.",
            "CLI004", "Residencial Alameda",
            "Residencial Alameda - Apto 302",
            CriticidadeChamado.MEDIA
        );
        c4.setId("TST004");
        c4.setTecnico("carlos");
        c4.setStatus(StatusChamado.ENCERRADO);
        c4.setDataAbertura(LocalDateTime.now().minusDays(10));
        c4.getAnexos().add("[Relatório final] relatorios/alameda_302_final.pdf");
        listaChamados.add(c4);

        System.out.println("[MODO TESTE] 4 chamados de teste carregados.\n");
    }

    public static void popularClientes(List<Cliente> listaClientes) {

        // ── Cliente 1 ─────────────────────────────────────────────────────────
        Cliente cli1 = new Cliente(
            "Construtora Horizonte Ltda",
            "12.345.678/0001-90",
            "Juridico",
            35,
            "Av. Boa Viagem, 1500",
            "Sala 301 - Recife/PE",
            "81911110001"
        );
        cli1.setId("CLI001");
        listaClientes.add(cli1);

        // ── Cliente 2 ─────────────────────────────────────────────────────────
        Cliente cli2 = new Cliente(
            "Studio Arq e Design",
            "98.765.432/0001-11",
            "Juridico",
            28,
            "Rua do Riachuelo, 220",
            "2 andar - Recife/PE",
            "81922220002"
        );
        cli2.setId("CLI002");
        listaClientes.add(cli2);

        // ── Cliente 3 ─────────────────────────────────────────────────────────
        Cliente cli3 = new Cliente(
            "Grupo Vertice Empreendimentos",
            "55.443.221/0001-33",
            "Juridico",
            42,
            "Rua Jeronimo Nogueira, 80",
            "Bloco B - Recife/PE",
            "81933330003"
        );
        cli3.setId("CLI003");
        listaClientes.add(cli3);

        // ── Cliente 4 ─────────────────────────────────────────────────────────
        Cliente cli4 = new Cliente(
            "Residencial Alameda",
            "77.888.999/0001-44",
            "Juridico",
            50,
            "Estrada do Arraial, 3000",
            "Torre A - Recife/PE",
            "81944440004"
        );
        cli4.setId("CLI004");
        listaClientes.add(cli4);

        System.out.println("[MODO TESTE] 4 clientes de teste carregados.\n");
    }
}
