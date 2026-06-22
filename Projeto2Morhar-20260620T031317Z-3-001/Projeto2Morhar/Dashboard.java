import entidades.Chamado;
import entidades.CriticidadeChamado;
import entidades.StatusChamado;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class Dashboard {

    private static final int DIAS_ATRASO = 3;

    private final List<Chamado> listaChamados;

    public Dashboard(List<Chamado> listaChamados) {
        this.listaChamados = listaChamados;
    }

    public void exibir() {
        if (listaChamados.isEmpty()) {
            System.out.println("\nNenhum chamado registrado. Dashboard indisponível.\n");
            return;
        }

        exibirCabecalho();
        exibirVolumeChamados();
        exibirTempoMedioAtendimento();
        exibirProdutividadeTecnicos();
        exibirChamadosEmAtraso();
        exibirRodape();
    }

    private void exibirCabecalho() {
        System.out.println("\n╔════════════════════════════════════════════════════════╗");
        System.out.println("║                                                        ║");
        System.out.println("║            DASHBOARD - INDICADORES DE ESTADO           ║");
        System.out.println("║                   CRIATIVE SOLUÇÕES                    ║");
        System.out.println("║                                                        ║");
        System.out.println("╚════════════════════════════════════════════════════════╝");
        System.out.printf("  Gerado em: %s%n", LocalDateTime.now()
                .format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        System.out.println("  Total de chamados no sistema: " + listaChamados.size());
        System.out.println();
    }

    // ==================== VOLUME DE CHAMADOS ====================
    private void exibirVolumeChamados() {
        long novo        = contar(StatusChamado.NOVO);
        long emAndamento = contar(StatusChamado.EM_ANDAMENTO);
        long pausado     = contar(StatusChamado.PAUSADO);
        long encerrado   = contar(StatusChamado.ENCERRADO);

        long baixa = contarCriticidade(CriticidadeChamado.BAIXA);
        long media = contarCriticidade(CriticidadeChamado.MEDIA);
        long alta  = contarCriticidade(CriticidadeChamado.ALTA);

        int total = listaChamados.size();

        System.out.println("┌──────────────────────────────────────────────────────────────────────┐");
        System.out.println("│  PAINEL DE VOLUME DE CHAMADOS                                        │");
        System.out.println("├────────────────────────────────┬─────────┬───────────────────────────┤");
        System.out.println("│  Por Status                    │ Qtd     │ Proporção                 │");
        System.out.println("├────────────────────────────────┼─────────┼───────────────────────────┤");
        System.out.printf( "│  Novo                       │ %-7d │ %s%n", novo,        barra(novo,        total));
        System.out.printf( "│  Em Andamento               │ %-7d │ %s%n", emAndamento, barra(emAndamento, total));
        System.out.printf( "│  Pausado                    │ %-7d │ %s%n", pausado,     barra(pausado,     total));
        System.out.printf( "│  Encerrado                  │ %-7d │ %s%n", encerrado,   barra(encerrado,   total));
        System.out.println("├────────────────────────────────┼─────────┼───────────────────────────┤");
        System.out.println("│  Por Criticidade               │ Qtd     │ Proporção                 │");
        System.out.println("├────────────────────────────────┼─────────┼───────────────────────────┤");
        System.out.printf( "│  Baixa                      │ %-7d │ %s%n", baixa, barra(baixa, total));
        System.out.printf( "│  Média                      │ %-7d │ %s%n", media, barra(media, total));
        System.out.printf( "│  Alta                       │ %-7d │ %s%n", alta,  barra(alta,  total));
        System.out.println("└────────────────────────────────┴─────────┴───────────────────────────┘");
        System.out.println();
    }

    // ==================== TEMPO MÉDIO DE ATENDIMENTO ====================
    private void exibirTempoMedioAtendimento() {
        List<Chamado> encerrados = listaChamados.stream()
                .filter(c -> c.getStatus() == StatusChamado.ENCERRADO)
                .collect(Collectors.toList());

        System.out.println("┌──────────────────────────────────────────────────────────────────────┐");
        System.out.println("│       TEMPO MÉDIO DE ATENDIMENTO                                     │");
        System.out.println("├──────────────────────────────────────────────────────────────────────┤");

        if (encerrados.isEmpty()) {
            System.out.println("│  Nenhum chamado encerrado para calcular.                             │");
        } else {
            double mediaDias = encerrados.stream()
                    .mapToLong(c -> Duration.between(c.getDataAbertura(), LocalDateTime.now()).toDays())
                    .average()
                    .orElse(0);

            long menorDias = encerrados.stream()
                    .mapToLong(c -> Duration.between(c.getDataAbertura(), LocalDateTime.now()).toDays())
                    .min().orElse(0);

            long maiorDias = encerrados.stream()
                    .mapToLong(c -> Duration.between(c.getDataAbertura(), LocalDateTime.now()).toDays())
                    .max().orElse(0);

            System.out.printf( "│  Chamados encerrados analisados : %-5d                                │%n", encerrados.size());
            System.out.printf( "│  Tempo médio (abertura→encerr.) : %.1f dias                           │%n", mediaDias);
            System.out.printf( "│  Menor tempo registrado         : %d dia(s)                           │%n", menorDias);
            System.out.printf( "│  Maior tempo registrado         : %d dia(s)                           │%n", maiorDias);
        }

        System.out.println("└──────────────────────────────────────────────────────────────────────┘");
        System.out.println();
    }

    // ==================== PRODUTIVIDADE POR TÉCNICO ====================
    private void exibirProdutividadeTecnicos() {
        Map<String, List<Chamado>> porTecnico = listaChamados.stream()
                .filter(c -> !c.getTecnico().equals("Nao atribuido"))
                .collect(Collectors.groupingBy(Chamado::getTecnico));

        System.out.println("┌──────────────────────────────────────────────────────────────────────┐");
        System.out.println("│      PRODUTIVIDADE POR TÉCNICO                                       │");
        System.out.println("├──────────────────────┬────────┬────────────┬────────────┬────────────┤");
        System.out.println("│  Técnico             │ Total  │ Encerrados │ Em aberto  │ Taxa       │");
        System.out.println("├──────────────────────┼────────┼────────────┼────────────┼────────────┤");

        if (porTecnico.isEmpty()) {
            System.out.println("│  Nenhum técnico atribuído ainda.                                     │");
        } else {
            porTecnico.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .forEach(entry -> {
                        String tecnico = entry.getKey();
                        List<Chamado> chamados = entry.getValue();
                        long total      = chamados.size();
                        long encerrados = chamados.stream().filter(c -> c.getStatus() == StatusChamado.ENCERRADO).count();
                        long abertos    = total - encerrados;
                        double taxa     = total > 0 ? (encerrados * 100.0 / total) : 0;

                        System.out.printf("│  %-20s│ %-6d │ %-10d │ %-10d │ %5.1f%%     │%n",
                                truncar(tecnico, 20), total, encerrados, abertos, taxa);
                    });

            long semTecnico = listaChamados.stream()
                    .filter(c -> c.getTecnico().equals("Nao atribuido")).count();
            if (semTecnico > 0) {
                System.out.printf("│  %-20s│ %-6d │ %-10s │ %-10s │ %-10s │%n",
                        "Sem técnico", semTecnico, "-", "-", "-");
            }
        }

        System.out.println("└──────────────────────┴────────┴────────────┴────────────┴────────────┘");
        System.out.println();
    }

    // ==================== CHAMADOS EM ATRASO ====================
    private void exibirChamadosEmAtraso() {
        LocalDateTime agora = LocalDateTime.now();

        List<Chamado> atrasados = listaChamados.stream()
                .filter(c -> c.getStatus() == StatusChamado.NOVO
                          || c.getStatus() == StatusChamado.EM_ANDAMENTO)
                .filter(c -> Duration.between(c.getDataAbertura(), agora).toDays() >= DIAS_ATRASO)
                .sorted(Comparator.comparing(Chamado::getDataAbertura))
                .collect(Collectors.toList());

        System.out.println("┌──────────────────────────────────────────────────────────────────────┐");
        System.out.printf( "│      CHAMADOS EM ATRASO (Abertos há +%d dias sem atualização)        │%n", DIAS_ATRASO);
        System.out.println("├──────────┬──────────────────┬────────────────┬───────────┬───────────┤");
        System.out.println("│  ID      │ Cliente          │ Status         │ Dias      │ Crítico   │");
        System.out.println("├──────────┼──────────────────┼────────────────┼───────────┼───────────┤");

        if (atrasados.isEmpty()) {
            System.out.println("│  Nenhum chamado em atraso no momento.                             │");
        } else {
            for (Chamado c : atrasados) {
                long dias = Duration.between(c.getDataAbertura(), agora).toDays();
                String alerta = c.getCriticidade() == CriticidadeChamado.ALTA ? "ALTA" : 
                                c.getCriticidade() == CriticidadeChamado.MEDIA ? "MÉDIA" : "BAIXA";
                System.out.printf("│  %-8s│ %-16s │ %-14s │ %-9d │ %-9s │%n",
                        c.getId(),
                        truncar(c.getNomeCliente(), 16),
                        c.getStatus(),
                        dias,
                        alerta);
            }
            System.out.printf("%n    Total em atraso: %d chamado(s)%n", atrasados.size());
        }

        System.out.println("└──────────┴──────────────────┴────────────────┴───────────┴───────────┘");
        System.out.println();
    }

    // ==================== RODAPÉ ====================
    private void exibirRodape() {
        System.out.println("══════════════════════════════════════════════════════════════════════");
        System.out.println("  Fim do Dashboard - pressione Enter para voltar ao menu principal.");
        System.out.println("══════════════════════════════════════════════════════════════════════");
    }

    // ==================== UTILITÁRIOS ====================
    private long contar(StatusChamado status) {
        return listaChamados.stream().filter(c -> c.getStatus() == status).count();
    }

    private long contarCriticidade(CriticidadeChamado crit) {
        return listaChamados.stream().filter(c -> c.getCriticidade() == crit).count();
    }

    /** Gera uma barra proporcional de até 20 chars + percentual. */
    private String barra(long valor, int total) {
        int largura = 20;
        int preenchido = total > 0 ? (int) Math.round((valor * largura) / (double) total) : 0;
        String barra = repetir("█", preenchido) + repetir("░", largura - preenchido);
        double pct = total > 0 ? (valor * 100.0 / total) : 0;
        return String.format("%s %5.1f%% │", barra, pct);
    }

    private String repetir(String caractere, int vezes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < vezes; i++) sb.append(caractere);
        return sb.toString();
    }

    private String truncar(String texto, int limite) {
        if (texto == null) return "";
        return texto.length() > limite ? texto.substring(0, limite - 1) + "…" : texto;
    }
}
