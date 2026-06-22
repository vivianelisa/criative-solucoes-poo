import entidades.Chamado;
import entidades.CriticidadeChamado;
import entidades.Ocorrencia;
import entidades.StatusChamado;
import entidades.StatusOcorrencia;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class GerenciadorArquivo {

    private static final String ARQUIVO = "chamados.csv";

    public static void salvar(List<Chamado> listaChamados) {
        try (PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(ARQUIVO), StandardCharsets.UTF_8))) {
            for (Chamado c : listaChamados) {
                // Linha do chamado (formato original preservado)
                String anexosStr = String.join(";", c.getAnexos());
                pw.println(c.getId() + "|" +
                           c.getTitulo() + "|" +
                           c.getDescricao() + "|" +
                           c.getStatus() + "|" +
                           c.getCriticidade() + "|" +
                           c.getIdCliente() + "|" +
                           c.getNomeCliente() + "|" +
                           c.getTecnico() + "|" +
                           c.getObra() + "|" +
                           c.getDataAbertura() + "|" +
                           anexosStr);

                // Linhas das ocorrências do chamado
                for (Ocorrencia o : c.getOcorrencias()) {
                    String anexosOc = String.join(";", o.getAnexos());
                    String resposta   = o.getResposta()    != null ? o.getResposta()    : "";
                    String dataResp   = o.getDataResposta() != null ? o.getDataResposta().toString() : "";
                    pw.println("OC@@" +
                               c.getId()           + "@@" +
                               o.getId()           + "@@" +
                               o.getDescricao()    + "@@" +
                               o.getIdCliente()    + "@@" +
                               o.getNomeCliente()  + "@@" +
                               o.getStatus()       + "@@" +
                               o.getDataRegistro() + "@@" +
                               resposta            + "@@" +
                               dataResp            + "@@" +
                               anexosOc);
                }
            }
            System.out.println("Chamados salvos com sucesso!");
        } catch (IOException e) {
            System.out.println("Erro ao salvar: " + e.getMessage());
        }
    }

    public static List<Chamado> carregar() {
        List<Chamado> lista = new ArrayList<>();
        File arquivo = new File(ARQUIVO);
        if (!arquivo.exists()) return lista;

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(ARQUIVO), StandardCharsets.UTF_8))) {
            String linha;
            while ((linha = br.readLine()) != null) {

                if (linha.startsWith("OC@@")) {
                    // Linha de ocorrência — vincula ao chamado correspondente
                    String[] p = linha.split("@@", -1);
                    if (p.length >= 11) {
                        String idChamado = p[1];
                        for (Chamado c : lista) {
                            if (c.getId().equals(idChamado)) {
                                Ocorrencia o = new Ocorrencia(p[3], p[4], p[5]);
                                // Restaurar id e datas via reflexão seria invasivo;
                                // usamos os setters disponíveis na entidade
                                o.setStatus(StatusOcorrencia.valueOf(p[6]));
                                if (!p[8].isEmpty()) {
                                    o.setResposta(p[8]);
                                }
                                if (p[6].equals("VISTORIA_AGENDADA")) {
                                    o.agendarVistoria();
                                }
                                if (!p[10].isEmpty()) {
                                    for (String anx : p[10].split(";")) {
                                        o.getAnexos().add(anx);
                                    }
                                }
                                c.getOcorrencias().add(o);
                                break;
                            }
                        }
                    }

                } else {
                    // Linha de chamado (formato original)
                    String[] p = linha.split("\\|", -1);
                    if (p.length >= 10) {
                        Chamado c = new Chamado(p[1], p[2], p[5], p[6], p[8], CriticidadeChamado.valueOf(p[4]));
                        c.setId(p[0]);
                        c.setStatus(StatusChamado.valueOf(p[3]));
                        c.setTecnico(p[7]);
                        c.setDataAbertura(LocalDateTime.parse(p[9]));
                        if (p.length > 10 && !p[10].isEmpty()) {
                            for (String anexo : p[10].split(";")) {
                                c.getAnexos().add(anexo);
                            }
                        }
                        lista.add(c);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao carregar: " + e.getMessage());
        }
        return lista;
    }
}
